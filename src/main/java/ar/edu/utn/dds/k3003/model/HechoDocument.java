package ar.edu.utn.dds.k3003.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "hechos_busqueda")
@CompoundIndex(def = "{'hecho_id': 1, 'nombre_coleccion': 1}", unique = true)
public class HechoDocument {

    @Id
    private String mongoId; // Generado por MongoDB

    @Field("hecho_id")
    private String hechoId;

    @Field("nombre_coleccion")
    private String nombreColeccion;

    // @TextIndexed(weight = 5) // Mayor relevancia al título
    private String titulo;

    @TextIndexed(weight = 3) // Menor relevancia a la descripción
    private String contenidoTextoIndexable; // Descripción del hecho, descripciones de los PDIs, resultado del OCR y resultado del Etiquetador.

    @Field("tags")
    private List<String> tags; // Para el filtro AND

    @Field("esta_borrado")
    private boolean estaBorrado = false; // El filtro principal de la búsqueda

    @Field("hecho_dto_data")
    private Object hechoDTOData;
}