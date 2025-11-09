package ar.edu.utn.dds.k3003.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.utn.dds.k3003.model.ConsensoColeccion;

public interface IConsensoColeccionRepository extends JpaRepository<ConsensoColeccion, String> {
    // No extra methods needed; PK is the collection name
}


