package ar.edu.utn.dds.k3003.model.consenso;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;

public class ConsensoTodosStrategy implements ConsensoStrategy {

    @Override
    public List<HechoDTO> aplicar(List<HechoDTO> hechosConDuplicados, int cantidadFuentes) {
        return hechosConDuplicados
            .stream()
            .collect(Collectors.toMap(HechoDTO::titulo, h -> h, (h1, h2) -> h1))
            .values()
            .stream()
            .toList();
    }
}


