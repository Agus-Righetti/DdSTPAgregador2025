package ar.edu.utn.dds.k3003.model.consenso;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;

public class ConsensoAlMenos2Strategy implements ConsensoStrategy {

    @Override
    public List<HechoDTO> aplicar(List<HechoDTO> hechosConDuplicados, int cantidadFuentes) {
        if (cantidadFuentes <= 1) {
            return new ConsensoTodosStrategy().aplicar(hechosConDuplicados, cantidadFuentes);
        }

        Map<String, Long> ocurrenciasPorTitulo = hechosConDuplicados.stream()
          .collect(Collectors.groupingBy(HechoDTO::titulo, Collectors.counting()));

        return hechosConDuplicados
            .stream()
            .collect(Collectors.toMap(HechoDTO::titulo, h -> h, (h1, h2) -> h1))
            .values()
            .stream()
            .filter(h -> ocurrenciasPorTitulo.getOrDefault(h.titulo(), 0L) >= 2)
            .toList();
    }
}


