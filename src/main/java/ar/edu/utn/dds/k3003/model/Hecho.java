package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.CategoriaHechoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name ="Hecho")
public class Hecho {

	@Id
	@Column(name = "id", nullable = false, unique = true)
    private String id;
	@Column(name = "nombre_coleccion", nullable = false)
    private String nombreColeccion;
	@Column(name = "titulo", nullable = false)
    private String titulo;
	@Column(name = "ubicacion", nullable = false)

	private String ubicacion;
	@Column(name = "fecha", nullable = false)
	private LocalDateTime fecha;
	@Column(name = "origen", nullable = false)

    private String origen;
    public Hecho() {
        // No-argument constructor
    }

    @ManyToOne
    @JoinColumn(name = "Fuente_id", referencedColumnName="id")
    private Fuente fuente;
    
    public Hecho(String id, String nombreColeccion, String titulo,  String ubicacion, LocalDateTime fecha, String origen) {
        this.id = id;
        this.nombreColeccion = nombreColeccion;
        this.titulo = titulo;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.origen = origen;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreColeccion() {
        return nombreColeccion;
    }

    public void setNombreColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hecho hecho = (Hecho) o;

        return Objects.equals(id, hecho.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}