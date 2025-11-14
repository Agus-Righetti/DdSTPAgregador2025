package ar.edu.utn.dds.k3003.model.consenso;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.dtos.HechoConPdisDTO;
import ar.edu.utn.dds.k3003.model.Fuente;

public class ConsensoTodosStrategy implements ConsensoStrategy {

    @Override
    public List<HechoConPdisDTO> aplicar(List<Fuente> fuentes) {
        // Une todos los hechos de todas las fuentes evitando repetidos por titulo del Hecho
        return fuentes
            .stream()
            .flatMap(f -> f.getHechos().stream())
            .collect(Collectors.toMap(h -> h.hecho().titulo(), h -> h, (h1, h2) -> h1))
            .values()
            .stream()
            .toList();
    }
}


