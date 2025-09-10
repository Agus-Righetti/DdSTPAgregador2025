package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FuenteProxy implements FachadaFuente
{
    private final FuenteRetroFitClient service;

    public FuenteProxy(ObjectMapper objectMapper, String baseUrl)
    {
        var retrofit =
            new Retrofit.Builder()
                .baseUrl(baseUrl.endsWith("/")? baseUrl : baseUrl+"/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.service = retrofit.create(FuenteRetroFitClient.class);
    }

    @Override
    public List<ColeccionDTO> colecciones()
    {
        try {
            var response = service.getCollecciones().execute();
            if(response.isSuccessful() && response.body() != null) {
                return response.body();
            }
            throw new RuntimeException("Error al obtener las colecciones , error " + response.code());

        }catch(Exception e) {
            throw new RuntimeException("Fallo la comunicacion con la fuente");
        }
    }
    @Override
    public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException
    {
        try {
            var response = service.getHechosPorColleccion(coleccionId).execute();
            if(response.isSuccessful() && response.body() != null) {
                return response.body();
            }
            throw new RuntimeException("Error al obtener los hechos por coleccion , error " + response.code());

        }catch(Exception e) {
            throw new RuntimeException("Fallo la comunicacion con la fuente");
        }
    }
    @Override
    public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
        return null;
    }

    @Override
    public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
    return null;
    }


    @Override
    public HechoDTO agregar(HechoDTO hechoDTO) {
    return null;
    }

    @Override
    public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
        return null;
    }

    @Override
    public void setProcesadorPdI(FachadaProcesadorPdI procesador) {

    }

    @Override
    public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
        return null;
    }
}