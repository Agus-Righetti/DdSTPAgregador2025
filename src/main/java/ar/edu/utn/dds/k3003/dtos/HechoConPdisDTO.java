package ar.edu.utn.dds.k3003.dtos;

import java.util.List;

public record HechoConPdisDTO(
    HechoDTO hecho,
    List<PdIDTO> pdis
) {}


