package ar.edu.utn.dds.k3003.model.consenso;

import java.util.List;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Fuente;

public interface ConsensoStrategy {
    /**
     * Aplica la estrategia de consenso sobre un conjunto de fuentes.
     * Cada {@link Fuente} trae sus hechos asociados en memoria (atributo transitorio).
     * Se espera que las estrategias consideren la unicidad por titulo.
     */
    List<HechoDTO> aplicar(List<Fuente> fuentes);
}


