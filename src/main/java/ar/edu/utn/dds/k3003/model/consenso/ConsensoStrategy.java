package ar.edu.utn.dds.k3003.model.consenso;

import java.util.List;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;

public interface ConsensoStrategy {

    List<HechoDTO> aplicar(List<HechoDTO> hechosConDuplicados, int cantidadFuentes);
}


