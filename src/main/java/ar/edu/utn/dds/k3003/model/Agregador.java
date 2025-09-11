package ar.edu.utn.dds.k3003.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.utn.dds.k3003.app.client.FuenteApi;
import ar.edu.utn.dds.k3003.app.client.RetrofitFactory;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.consenso.ConsensoStrategy;
import retrofit2.Response;

@Component
public class Agregador {

    private ConsensoStrategy consensoStrategy;
    private final ObjectMapper objectMapper;

    public Agregador(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setConsensoStrategy(ConsensoStrategy consensoStrategy) {
        this.consensoStrategy = consensoStrategy;
    }

    public List<HechoDTO> findHechos(String nombreColeccion, List<Fuente> fuentes) {
        List<HechoDTO> todosLosHechos = new ArrayList<HechoDTO>();
        for (Fuente fuente : fuentes) {
            try {
                // System.out.println("Obteniendo hechos de la fuente " + fuente.getNombre());
                // System.out.println("Endpoint: " + fuente.getEndpoint());
                // System.out.println("Nombre Coleccion: " + nombreColeccion);
                FuenteApi api = RetrofitFactory.build(fuente.getEndpoint(), objectMapper).create(FuenteApi.class);
                Response<List<HechoDTO>> response = api.listarHechosPorColeccion(nombreColeccion).execute();
                System.out.println("Response: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    // System.out.println("Hechos: " + response.body());
                    todosLosHechos.addAll(response.body());
                    // System.out.println("Todos los hechos size: " + todosLosHechos.size());
                }
            } catch (Exception ex) {
                System.err.println("Error al obtener hechos de la fuente " + fuente.getNombre() + ": " + ex.getMessage());
            }
        }

        if (consensoStrategy == null) {
            return todosLosHechos;
        }
        return consensoStrategy.aplicar(todosLosHechos, fuentes.size());
    }
}