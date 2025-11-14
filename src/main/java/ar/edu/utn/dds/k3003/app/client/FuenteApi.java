package ar.edu.utn.dds.k3003.app.client;

import java.util.List;

import ar.edu.utn.dds.k3003.dtos.HechoConPdisDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FuenteApi {

    @GET("api/colecciones/{nombre}/hechos")
    Call<List<HechoConPdisDTO>> listarHechosPorColeccion(@Path("nombre") String nombre);
}


