package ar.edu.utn.dds.k3003.model;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Fuente")
public class Fuente {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    // Lista temporal de hechos obtenidos de la API para esta fuente.
    // Marcada como @Transient para NO persistir en la base de datos.
    @Transient
    private List<HechoDTO> hechos = new ArrayList<>();


    protected Fuente() {
    }

    public Fuente(String id, String nombre, String endpoint) {
        this.id = id;
        this.nombre = nombre;
        this.endpoint = endpoint;
    }

    public String getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public List<HechoDTO> getHechos() {
        return this.hechos;
    }

    public void setHechos(List<HechoDTO> hechos) {
        this.hechos = (hechos != null) ? hechos : new ArrayList<>();
    }
}
