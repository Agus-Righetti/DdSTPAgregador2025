package ar.edu.utn.dds.k3003.dtos;

import java.time.LocalDate;
import java.util.List;

import ar.edu.utn.dds.k3003.enums.CategoriaHechoEnum;
import ar.edu.utn.dds.k3003.enums.EstadoHechoEnum;


public record HechoDTO(
    String nombre_coleccion,
    String titulo,
    List<String> etiquetas,
    CategoriaHechoEnum categoria,
    String ubicacion,
    LocalDate fecha,
    String origen,
    EstadoHechoEnum estado,
    String id) {

  public HechoDTO(String nombreColeccion, String titulo) {
    this(nombreColeccion, titulo, null, null, null, null, null, EstadoHechoEnum.PENDIENTE, null);
  }
}