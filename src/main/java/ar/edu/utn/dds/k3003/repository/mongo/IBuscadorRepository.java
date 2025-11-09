package ar.edu.utn.dds.k3003.repository.mongo;

import ar.edu.utn.dds.k3003.model.HechoDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBuscadorRepository extends MongoRepository<HechoDocument, String>
{
    @org.springframework.data.mongodb.repository.Query("{$text: {$search: ?0}, 'tags': {$in: [?1]}, 'esta_borrado': false}")
    Page<HechoDocument> buscarPorTextoYTag(String palabraClave, String tag, Pageable pageable);

    @org.springframework.data.mongodb.repository.Query("{$text: {$search: ?0}, 'esta_borrado': false}")
    Page<HechoDocument> buscarPorTexto(String palabraClave, Pageable pageable);

    HechoDocument findByHechoId(String hechoId);

    List<HechoDocument> findByTituloAndEstaBorradoIsFalse(String titulo);
}