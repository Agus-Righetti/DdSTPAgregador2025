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
    // Criterio AND para tags: 'tags': {$all: ?1} y recibir List<String>
    @org.springframework.data.mongodb.repository.Query("{$and: [{$text: {$search: ?0}}, {'tags': {$all: ?1}}, {'esta_borrado': false}]}")
    Page<HechoDocument> buscarPorTextoYTags(String palabraClave, List<String> tags, Pageable pageable);

    // Solo por texto, palabra clave
    @org.springframework.data.mongodb.repository.Query("{$and: [{$text: {$search: ?0}}, {'esta_borrado': false}]}")
    Page<HechoDocument> buscarPorTexto(String palabraClave, Pageable pageable);

    // Solo por tags (para cuando no hay palabra clave)
    @org.springframework.data.mongodb.repository.Query("{$and: [{'tags': {$all: ?0}}, {'esta_borrado': false}]}")
    Page<HechoDocument> buscarPorTags(List<String> tags, Pageable pageable);

    HechoDocument findByHechoId(String hechoId);
    List<HechoDocument> findByTituloAndEstaBorradoIsFalse(String titulo);
}