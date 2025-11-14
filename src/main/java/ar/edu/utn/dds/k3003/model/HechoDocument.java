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

    @TextIndexed(weight = 3)
    private String contenidoTextoIndexable;

    @TextIndexed(weight = 3)
    private String ocrTextoIndexable;

    @Field("tags")
    private List<String> tags; // Para el filtro AND

    @Field("esta_borrado")
    private boolean estaBorrado = false; // El filtro principal de la búsqueda

    @Field("hecho_dto_data")
    private Object hechoDTOData;
}