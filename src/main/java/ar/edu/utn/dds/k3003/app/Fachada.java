package ar.edu.utn.dds.k3003.app;

import java.util.List;
import java.util.UUID;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;
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
import ar.edu.utn.dds.k3003.repository.IFuenteRepository;
import ar.edu.utn.dds.k3003.repository.IConsensoColeccionRepository;

@Service
public class Fachada implements IFachadaAgregador {

  private final IFuenteRepository fuenteRepository;
  private final IConsensoColeccionRepository consensoRepository;
  private final Agregador agregador;

  public Fachada(IFuenteRepository fuenteRepository, IConsensoColeccionRepository consensoRepository, Agregador agregador) {
    this.fuenteRepository = fuenteRepository;
    this.consensoRepository = consensoRepository;
    this.agregador = agregador;
  }

  @Override
  public FuenteDTO agregar(FuenteDTO fuente) {
    String id = (fuente.id() == null || fuente.id().isBlank()) ? UUID.randomUUID().toString() : fuente.id();
    Fuente entity = new Fuente(id, fuente.nombre(), fuente.endpoint());
    Fuente saved = fuenteRepository.save(entity);
    return new FuenteDTO(saved.getId(), saved.getNombre(), saved.getEndpoint());
  }

  @Override
  public List<FuenteDTO> fuentes() {
    return fuenteRepository
        .findAll()
        .stream()
        .map(f -> new FuenteDTO(f.getId(), f.getNombre(), f.getEndpoint()))
        .toList();
  }

  @Override
  public FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException {
    return fuenteRepository
      .findById(fuenteId)
      .map(f -> new FuenteDTO(f.getId(), f.getNombre(), f.getEndpoint()))
      .orElseThrow(() -> new NoSuchElementException("Fuente no encontrada: " + fuenteId));
  }

  @Override
  public List<HechoDTO> hechos(String nombreColeccion) throws NoSuchElementException {
    List<Fuente> fuentes = fuenteRepository.findAll();
    if (fuentes.isEmpty()) {
      return List.of();
    }

    ConsensosEnum consenso = consensoRepository.findById(nombreColeccion)
      .map(ConsensoColeccion::getTipoConsenso)
      .orElse(ConsensosEnum.TODOS);

    agregador.setConsensoStrategy(getStrategyByConsenso(consenso));
    return agregador.findHechos(nombreColeccion, fuentes);
  }

  @Override
  public void setConsensoStrategy(ConsensosEnum tipoConsenso, String nombreColeccion) throws InvalidParameterException {
    if (tipoConsenso == null || nombreColeccion == null || nombreColeccion.isBlank()) {
      throw new InvalidParameterException("Parámetros inválidos para configurar consenso");
    }
    // Upsert utilizando la PK (nombreColeccion) como ID
    consensoRepository.save(new ConsensoColeccion(nombreColeccion, tipoConsenso));
  }

  private ConsensoStrategy getStrategyByConsenso(ConsensosEnum consenso) {
    return switch (consenso) {
      case AL_MENOS_2 -> new ConsensoAlMenos2Strategy();
      case TODOS -> new ConsensoTodosStrategy();
    };
  }

}