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

    public Fachada(IFuenteRepository fuenteRepository, IConsensoColeccionRepository consensoRepository, Agregador agregador, SolicitudesProxy solicitudesProxy, IBuscadorRepository buscadorRepository)
    {
        this.fuenteRepository = fuenteRepository;
        this.consensoRepository = consensoRepository;
        this.agregador = agregador;
        this.solicitudesProxy = solicitudesProxy;
        this.buscadorRepository = buscadorRepository;
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
    public List<HechoDTO> hechos(String nombreColeccion) throws NoSuchElementException
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
    public PaginacionDTO buscar(String palabraClave, List<String> tags, int pagina, int tamanoPagina)
    {
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

        Set<String> titulosYaVistos = new HashSet<>();
        List<HechoDTO> hechosDTO = resultados.getContent().stream()
                .filter(doc -> titulosYaVistos.add(doc.getTitulo())) // Solo acepta el primer documento con ese título
                .map(this::mapToHechoDTO)
                .collect(Collectors.toList());

        return new PaginacionDTO(
                hechosDTO,
                resultados.getTotalPages(), // Páginas totales de la query, NO del resultado filtrado
                resultados.getTotalElements(), // Elementos totales de la query, NO del resultado filtrado
                resultados.getNumber()
        );
    }

    private HechoDTO mapToHechoDTO(HechoDocument doc)
    {
        return new HechoDTO(
            doc.getNombreColeccion(),
            doc.getTitulo(),
            doc.getTags(),
            null, // CategoriaHechoEnum categoria
            null, // String ubicacion
            null, // LocalDate fecha
            null, // String origen
            doc.isEstaBorrado() ? EstadoHechoEnum.BORRADO : EstadoHechoEnum.PENDIENTE,
            doc.getHechoId()
        );
    }

}