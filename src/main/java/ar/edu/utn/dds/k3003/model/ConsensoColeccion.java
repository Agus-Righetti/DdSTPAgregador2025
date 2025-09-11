package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.enums.ConsensosEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ConsensoColeccion")
public class ConsensoColeccion {

    @Id
    @Column(name = "coleccion", nullable = false, unique = true)
    private String coleccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consenso", nullable = false)
    private ConsensosEnum tipoConsenso;

    protected ConsensoColeccion() {}

    public ConsensoColeccion(String coleccion, ConsensosEnum tipoConsenso) {
        this.coleccion = coleccion;
        this.tipoConsenso = tipoConsenso;
    }

    public String getColeccion() {
        return coleccion;
    }

    public ConsensosEnum getTipoConsenso() {
        return tipoConsenso;
    }

    public void setTipoConsenso(ConsensosEnum tipoConsenso) {
        this.tipoConsenso = tipoConsenso;
    }
}


