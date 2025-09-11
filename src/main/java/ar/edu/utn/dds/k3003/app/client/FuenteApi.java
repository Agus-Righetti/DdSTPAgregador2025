package ar.edu.utn.dds.k3003.app.client;

import java.util.List;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FuenteApi {

    @GET("coleccion/{nombre}/hechos")
    Call<List<HechoDTO>> listarHechosPorColeccion(@Path("nombre") String nombre);
}


