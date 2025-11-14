// ar.edu.utn.dds.k3003.service.IndexadorService.java

package ar.edu.utn.dds.k3003.app.client;

import ar.edu.utn.dds.k3003.model.HechoDocument;
import ar.edu.utn.dds.k3003.repository.mongo.IBuscadorRepository;
import ar.edu.utn.dds.k3003.dtos.HechoParaIndexarDTO; // DTO que trae toda la data
import ar.edu.utn.dds.k3003.dtos.PdiParaIndexarDTO; // DTO del PDI con resultados
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Usa la anotación adecuada para tu framework

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class IndexadorService {

    private final IBuscadorRepository buscadorRepository;

    public IndexadorService(IBuscadorRepository buscadorRepository)
    {
        this.buscadorRepository = buscadorRepository;
    }

    public void indexarHecho(HechoParaIndexarDTO hechoIndexar)
    {
        List<HechoDocument> hechosConMismoTitulo = buscadorRepository.findByTituloAndEstaBorradoIsFalse(hechoIndexar.titulo());

        if (!hechosConMismoTitulo.isEmpty() && !esActualizacionPropia(hechosConMismoTitulo, hechoIndexar.hechoId()))
        {
            System.out.println("[Indexador] Hecho ignorado: Título duplicado y ya existe un HechoDocument activo.");
            return;
        }

        HechoDocument doc = buscadorRepository.findByHechoId(hechoIndexar.hechoId());
        if (doc == null)
        {
            doc = new HechoDocument();
            doc.setHechoId(hechoIndexar.hechoId());
            doc.setNombreColeccion(hechoIndexar.nombreColeccion());
        }

        doc.setTitulo(hechoIndexar.titulo());
        doc.setEstaBorrado(false);

        StringBuilder contenido = new StringBuilder();
        Set<String> allTags = new HashSet<>();

        contenido.append(hechoIndexar.descripcionHecho()).append(" ");

        for (PdiParaIndexarDTO pdi : hechoIndexar.pdis())
        {
            contenido.append(pdi.descripcionPDI()).append(" ");
            contenido.append(pdi.ocrResultado()).append(" ");
            contenido.append(pdi.etiquetadorResultado()).append(" ");

            if (pdi.tags() != null)
            {
                allTags.addAll(pdi.tags());
            }
        }

        doc.setContenidoTextoIndexable(contenido.toString());
        doc.setTags(new ArrayList<>(allTags));
        doc.setHechoDTOData(hechoIndexar.hechoDTOCompleto());

        buscadorRepository.save(doc);
    }

    private boolean esActualizacionPropia(List<HechoDocument> documentos, String hechoId)
    {
        return documentos.stream()
                .anyMatch(doc -> doc.getHechoId().equals(hechoId));
    }

    public void marcarComoBorrado(String hechoId)
    {
        HechoDocument doc = buscadorRepository.findByHechoId(hechoId);

        if (doc != null)
        {
            doc.setEstaBorrado(true);
            buscadorRepository.save(doc);
            System.out.println("[Indexador] Hecho " + hechoId + " marcado como borrado exitosamente.");
        }
    }
}