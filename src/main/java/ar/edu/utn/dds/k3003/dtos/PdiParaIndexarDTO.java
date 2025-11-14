package ar.edu.utn.dds.k3003.dtos;

import java.util.List;

public record PdiParaIndexarDTO(
        String pdiId,
        String descripcionPDI,

        // Resultados del procesamiento asincrónico (Workers)
        String ocrResultado,
        String etiquetadorResultado,

        // Tags generados por el Etiquetador para el filtro AND
        List<String> tags)
{}