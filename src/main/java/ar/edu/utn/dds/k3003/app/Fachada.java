package ar.edu.utn.dds.k3003.app;

import java.util.List;
import java.util.UUID;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

import ar.edu.utn.dds.k3003.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.enums.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.IFachadaAgregador;
import ar.edu.utn.dds.k3003.repository.IFuenteRepository;

@Service
public class Fachada implements IFachadaAgregador {

  private final IFuenteRepository fuenteRepository;

  public Fachada(IFuenteRepository fuenteRepository) {
    this.fuenteRepository = fuenteRepository;
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'buscarFuenteXId'");
  }

  @Override
  public List<HechoDTO> hechos(String coleccionId) throws NoSuchElementException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'hechos'");
  }

  @Override
  public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId) throws InvalidParameterException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setConsensoStrategy'");
  }

}