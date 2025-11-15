package ar.edu.utn.dds.k3003.app.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SolicitudesProxy
{
    private final String endpoint;
    private final SolicitudesRetrofitClient service;

    public SolicitudesProxy(ObjectMapper objectMapper)
    {
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_SOLICITUDES", "https://ddstp2025.onrender.com/");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.endpoint)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.service = retrofit.create(SolicitudesRetrofitClient.class);
    }

    public boolean tieneSolicitudes(String hechoId)
    {
        try
        {
            Response<Boolean> response = service.tieneSolicitudes(hechoId).execute();
            if (response.isSuccessful() && response.body() != null)
            {
                System.out.println("[LOG][SolicitudesProxy] Body: " + response.body() + " para hechoId: " + hechoId);
                return response.body();
            } else {
                // Tiene solicitudes
                return true;
            }
        } catch (IOException e)
        {
            throw new RuntimeException("Error de comunicación con el servicio de solicitudes.", e);
        }
    }

    public Map<String, Boolean> tienenSolicitudes(List<String> hechoIds)
    {
        try
        {
            Response<Map<String, Boolean>> response = service.tienenSolicitudes(hechoIds).execute();
            if (response.isSuccessful() && response.body() != null)
            {
                System.out.println("[LOG][SolicitudesProxy] Body: " + response.body());
                return response.body();
            }
            else // TODO
            {
                return hechoIds.stream().collect(Collectors.toMap(id -> id, id -> true));
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error de comunicación con el servicio de solicitudes.", e);
        }
    }

//    public Map<String, Boolean> hechosActivos(List<String> hechoIds)
//    {
//        try
//        {
//            Response<Map<String, Boolean>> response = service.hechosActivos(hechoIds).execute();
//            if (response.isSuccessful() && response.body() != null)
//            {
//                System.out.println("[LOG][SolicitudesProxy] Body: " + response.body());
//                return response.body();
//            }
//            else // Manejo de error: por defecto se asume que están activos.
//            {
//                return hechoIds.stream().collect(Collectors.toMap(id -> id, id -> true));
//            }
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException("Error de comunicación con el servicio de solicitudes.", e);
//        }
//    }

    public boolean estaActivo(String hechoId)
    {
        try
        {
            // Llama al cliente Retrofit y ejecuta la solicitud HTTP
            Response<Boolean> response = service.estaActivo(hechoId).execute();

            if (response.isSuccessful() && response.body() != null)
            {
                // Retorna el booleano (true/false) devuelto por el microservicio Solicitudes
                return response.body();
            }
            else
            {
                // Si la respuesta HTTP no es 2xx, asumimos inactivo por seguridad.
                System.err.println("[Proxy] Error al obtener estaActivo para ID " + hechoId + ". Código: " + response.code());
                return false;
            }
        }
        catch (IOException e)
        {
            // Si hay un error de red o timeout, asumimos inactivo para evitar mostrar contenido no verificado.
            System.err.println("[Proxy] Error de IO al llamar a estaActivo para ID " + hechoId + ": " + e.getMessage());
            return false;
        }
    }
}