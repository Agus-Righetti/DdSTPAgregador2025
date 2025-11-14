package ar.edu.utn.dds.k3003.dtos;

import java.util.List;

public record HechoParaIndexarDTO(
        String hechoId,
        String nombreColeccion,

        // Campos de Hecho para indexación
        String titulo,
        String descripcionHecho,

        // Lista de PDIs con sus resultados de procesamiento
        List<PdiParaIndexarDTO> pdis,

        HechoDTO hechoDTOCompleto)
{
}
