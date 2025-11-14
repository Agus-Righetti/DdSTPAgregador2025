package ar.edu.utn.dds.k3003.app.client;

import ar.edu.utn.dds.k3003.dtos.HechoParaIndexarDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;

@Component
public class IndexadorProxy
{
    private final IndexadorRetrofitClient service;

    public IndexadorProxy(ObjectMapper objectMapper)
    {
        var env = System.getenv();
        String endpoint = env.getOrDefault("URL_BUSQUEDA", "http://localhost:8081/");

        this.service = RetrofitFactory.build(endpoint, objectMapper)
                .create(IndexadorRetrofitClient.class);
    }

    public void indexarHecho(HechoParaIndexarDTO hechoIndexar)
    {
        try {
            Response<Void> response = service.indexarHecho(hechoIndexar).execute();
            if (response.isSuccessful()) {
                System.out.println("[LOG INDEXADOR] Hecho " + hechoIndexar.hechoId() + " indexado. Cod: " + response.code());
            } else {
                System.err.println("[LOG INDEXADOR] ERROR al indexar hecho " + hechoIndexar.hechoId() + ". Cod: " + response.code() + ". Mensaje: " + response.errorBody().string());
            }
        } catch (IOException e) {
            System.err.println("Error de comunicación con el servicio de Indexación.");
        }
    }
}
