package ar.edu.utn.dds.k3003.app;

import java.util.*;
import java.security.InvalidParameterException;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.app.client.SolicitudesProxy;
import ar.edu.utn.dds.k3003.dtos.PaginacionDTO;
import ar.edu.utn.dds.k3003.enums.EstadoHechoEnum;
import ar.edu.utn.dds.k3003.model.HechoDocument;
import ar.edu.utn.dds.k3003.model.consenso.ConsensoEstrictoStrategy;
import ar.edu.utn.dds.k3003.repository.mongo.IBuscadorRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// no-op

import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.model.consenso.ConsensoStrategy;
import ar.edu.utn.dds.k3003.model.consenso.ConsensoTodosStrategy;
import ar.edu.utn.dds.k3003.model.consenso.ConsensoAlMenos2Strategy;
import ar.edu.utn.dds.k3003.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.model.ConsensoColeccion;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.dtos.HechoConPdisDTO;
import ar.edu.utn.dds.k3003.dtos.HechoConPdisDTO;
import ar.edu.utn.dds.k3003.enums.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.IFachadaAgregador;
import ar.edu.utn.dds.k3003.repository.jpa.IFuenteRepository;
import ar.edu.utn.dds.k3003.repository.jpa.IConsensoColeccionRepository;

@Service
public class Fachada implements IFachadaAgregador
{
    private final IFuenteRepository fuenteRepository;
    private final IConsensoColeccionRepository consensoRepository;
    private final Agregador agregador;
    private final SolicitudesProxy solicitudesProxy;
    private final IBuscadorRepository buscadorRepository;
    private final MeterRegistry meterRegistry;

    public Fachada(IFuenteRepository fuenteRepository, IConsensoColeccionRepository consensoRepository, Agregador agregador, SolicitudesProxy solicitudesProxy, IBuscadorRepository buscadorRepository, MeterRegistry meterRegistry)
    {
        this.fuenteRepository = fuenteRepository;
        this.consensoRepository = consensoRepository;
        this.agregador = agregador;
        this.solicitudesProxy = solicitudesProxy;
        this.buscadorRepository = buscadorRepository;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public FuenteDTO agregar(FuenteDTO fuente)
    {
        String id = (fuente.id() == null || fuente.id().isBlank()) ? UUID.randomUUID().toString() : fuente.id();
        Fuente entity = new Fuente(id, fuente.nombre(), fuente.endpoint());
        Fuente saved = fuenteRepository.save(entity);
        return new FuenteDTO(saved.getId(), saved.getNombre(), saved.getEndpoint());
    }

    @Override
    public List<FuenteDTO> fuentes()
    {
        return fuenteRepository
            .findAll()
            .stream()
            .map(f -> new FuenteDTO(f.getId(), f.getNombre(), f.getEndpoint()))
            .toList();
    }

    @Override
    public FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException
    {
        return fuenteRepository
            .findById(fuenteId)
            .map(f -> new FuenteDTO(f.getId(), f.getNombre(), f.getEndpoint()))
            .orElseThrow(() -> new NoSuchElementException("Fuente no encontrada: " + fuenteId));
    }

    @Override
    public List<HechoConPdisDTO> hechos(String nombreColeccion) throws NoSuchElementException
    {
        List<Fuente> fuentes = fuenteRepository.findAll();
        if (fuentes.isEmpty()) {
            return List.of();
        }

        ConsensosEnum consenso = consensoRepository.findById(nombreColeccion)
            .map(ConsensoColeccion::getTipoConsenso)
            .orElse(ConsensosEnum.TODOS);

        agregador.setConsensoStrategy(getStrategyByConsenso(consenso));
        return agregador.findHechos(nombreColeccion, fuentes)
                .stream()
                .distinct()
                .toList();
    }

    @Override
    public void setConsensoStrategy(ConsensosEnum tipoConsenso, String nombreColeccion) throws InvalidParameterException
    {
        if (tipoConsenso == null || nombreColeccion == null || nombreColeccion.isBlank()) {
            throw new InvalidParameterException("Parámetros inválidos para configurar consenso");
        }
        // Upsert utilizando la PK (nombreColeccion) como ID
        consensoRepository.save(new ConsensoColeccion(nombreColeccion, tipoConsenso));
    }

    private ConsensoStrategy getStrategyByConsenso(ConsensosEnum consenso)
    {
        return switch (consenso)
        {
            case AL_MENOS_2 -> new ConsensoAlMenos2Strategy();
            case TODOS -> new ConsensoTodosStrategy();
            case ESTRICTO -> new ConsensoEstrictoStrategy(solicitudesProxy);
        };
    }

    @Override
    public void borrarTodasLasFuentes()
    {
        this.fuenteRepository.deleteAll();
    }

    @Override
    public void indexarTodo()
    {
        // El índice de Mongo funciona como una vista materializada para búsqueda.
        // Para garantizar idempotencia, limpiamos todo y volvemos a poblarlo
        // en base a las fuentes externas y las colecciones configuradas.
        buscadorRepository.deleteAll();

        // Tomamos todas las colecciones conocidas (configuradas con consenso)
        List<ConsensoColeccion> colecciones = consensoRepository.findAll();
        if (colecciones.isEmpty())
        {
            return;
        }

        colecciones.stream()
                .map(ConsensoColeccion::getColeccion)
                .forEach(this::indexarColeccionInterna);
    }

    private void indexarColeccionInterna(String nombreColeccion)
    {
        if (nombreColeccion == null || nombreColeccion.isBlank())
        {
            throw new InvalidParameterException("El nombre de la colección no puede ser nulo ni vacío");
        }

        List<Fuente> fuentes = fuenteRepository.findAll();
        if (fuentes.isEmpty())
        {
            return;
        }

        // Para indexar usamos siempre el consenso "TODOS" (un Hecho por título)
        agregador.setConsensoStrategy(new ConsensoTodosStrategy());
        List<HechoConPdisDTO> hechosConPdis = agregador.findHechos(nombreColeccion, fuentes);

        hechosConPdis.stream()
                .filter(h -> h.hecho() != null && h.hecho().id() != null && !h.hecho().id().isBlank())
                .forEach(h -> {
                    HechoDTO hecho = h.hecho();

                    HechoDocument doc = new HechoDocument();
                    doc.setHechoId(hecho.id());
                    doc.setEstaBorrado(false);

                    // contenidoTextoIndexable:
                    // - título del hecho
                    // - descripción de cada PDI
                    // - contenido de cada PDI
                    StringBuilder contenido = new StringBuilder();
                    if (hecho.titulo() != null) {
                        contenido.append(hecho.titulo()).append(" ");
                    }

                    if (h.pdis() != null) {
                        h.pdis().forEach(pdi -> {
                            if (pdi.descripcion() != null) {
                                contenido.append(pdi.descripcion()).append(" ");
                            }
                            if (pdi.contenido() != null) {
                                contenido.append(pdi.contenido()).append(" ");
                            }
                        });
                    }
                    doc.setContenidoTextoIndexable(contenido.toString());

                    // ocrTextoIndexable: detalle de resultados tipo "OCR"
                    StringBuilder ocrContenido = new StringBuilder();
                    if (h.pdis() != null) {
                        h.pdis().forEach(pdi -> {
                            if (pdi.resultados() != null) {
                                pdi.resultados().stream()
                                        .filter(res -> "OCR".equalsIgnoreCase(res.tipo()) && res.detalle() != null)
                                        .forEach(res -> ocrContenido.append(res.detalle()).append(" "));
                            }
                        });
                    }
                    doc.setOcrTextoIndexable(ocrContenido.toString());

                    // tags: detalle del ETIQUETADOR, separado por comas y normalizado a lista
                    Set<String> tags = new HashSet<>();
                    if (h.pdis() != null) {
                        h.pdis().forEach(pdi -> {
                            if (pdi.resultados() != null) {
                                pdi.resultados().stream()
                                        .filter(res -> "ETIQUETADOR".equalsIgnoreCase(res.tipo()) && res.detalle() != null)
                                        .forEach(res -> {
                                            String[] partes = res.detalle().split(",");
                                            for (String parte : partes) {
                                                String tagNormalizado = parte.trim();
                                                if (!tagNormalizado.isEmpty()) {
                                                    tags.add(tagNormalizado);
                                                }
                                            }
                                        });
                            }
                        });
                    }
                    doc.setTags(new ArrayList<>(tags));

                    // Guardamos el HechoConPdisDTO completo para poder reconstruirlo en las búsquedas
                    doc.setHechoDTOData(h);

                    buscadorRepository.save(doc);
                });
    }

    @Override
    public PaginacionDTO buscar(String palabraClave, List<String> tags, int pagina, int tamanoPagina)
    {

      // AGREGAR:
      /**
       * No retornar hechos repetidos (hechos que tengan el mismo titulo)
       * No retornar hechos que esten censurados (chequear con solicitudes si estaActivo)
       * Si el hecho no esta activo, marcarlo en MONGO como estaBorrado = true y no retornarlo en la respuesta
       */
        meterRegistry.counter("busquedas_realizadas", "tipo", "hechos").increment();

        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        Page<HechoDocument> resultados;

        String textoBusqueda = palabraClave;

        boolean hayPalabraClave = palabraClave != null && !palabraClave.isBlank();
        boolean hayTags = tags != null && !tags.isEmpty();

        if (hayPalabraClave && hayTags)
        {
            // Texto Y Tags (AND)
            resultados = buscadorRepository.buscarPorTextoYTags(palabraClave, tags, pageable);
        } else if (hayTags) {
            // Solo Tags (AND)
            resultados = buscadorRepository.buscarPorTags(tags, pageable);
        } else if (hayPalabraClave) {
            // Solo Texto
            resultados = buscadorRepository.buscarPorTexto(palabraClave, pageable);
        } else {
            // Sin filtros
            return new PaginacionDTO(List.of(), 0, 0, pagina);
        }

        // Set<String> titulosYaVistos = new HashSet<>();
        List<HechoConPdisDTO> hechosDTO = resultados.getContent().stream()
                .map(doc -> (HechoConPdisDTO) doc.getHechoDTOData())
                .collect(Collectors.toList());

        return new PaginacionDTO(
                hechosDTO,
                resultados.getTotalPages(), // Páginas totales de la query, NO del resultado filtrado
                resultados.getTotalElements(), // Elementos totales de la query, NO del resultado filtrado
                resultados.getNumber()
        );
    }


}