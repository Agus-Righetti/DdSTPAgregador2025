package ar.edu.utn.dds.k3003.model.consenso;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Fuente;

public class ConsensoAlMenos2Strategy implements ConsensoStrategy {

    @Override
    public List<HechoDTO> aplicar(List<Fuente> fuentes) {
        // Si hay una sola fuente, el consenso es equivalente a TODOS
        if (fuentes == null || fuentes.size() <= 1) {
            return new ConsensoTodosStrategy().aplicar(fuentes == null ? List.of() : fuentes);
        }

        // Contamos en cuantas fuentes aparece cada titulo (unicidad por fuente)
        Map<String, Long> ocurrenciasPorTitulo = fuentes
            .stream()
            .map(f -> f.getHechos()
                .stream()
                .collect(Collectors.toMap(HechoDTO::titulo, h -> 1, (a, b) -> a)) // unique titles per fuente
                .keySet())
            .flatMap(Set -> Set.stream())
            .collect(Collectors.groupingBy(titulo -> titulo, Collectors.counting()));

        // Devolver un único HechoDTO por título para aquellos presentes en al menos 2 fuentes
        return fuentes
            .stream()
            .flatMap(f -> f.getHechos().stream())
            .collect(Collectors.toMap(HechoDTO::titulo, h -> h, (h1, h2) -> h1))
            .values()
            .stream()
            .filter(h -> ocurrenciasPorTitulo.getOrDefault(h.titulo(), 0L) >= 2)
            .toList();
    }
}


