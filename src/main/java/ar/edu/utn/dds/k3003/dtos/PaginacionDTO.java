package ar.edu.utn.dds.k3003.dtos;

import java.util.List;

public record PaginacionDTO(
    List<HechoConPdisDTO> hechos,
    int total_paginas,
    long total_elementos,
    int pagina_actual
)
{}
