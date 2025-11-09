package ar.edu.utn.dds.k3003.repository.jpa;

import java.util.Optional;

import ar.edu.utn.dds.k3003.model.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFuenteRepository extends JpaRepository<Fuente, String> {

  Optional<Fuente> findById(String id);
}
