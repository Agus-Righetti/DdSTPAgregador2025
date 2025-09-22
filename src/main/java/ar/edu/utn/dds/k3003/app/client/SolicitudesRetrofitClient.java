package ar.edu.utn.dds.k3003.app.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SolicitudesRetrofitClient
{
    @GET("api/solicitudes/activo")
    Call<Boolean> estaActivo(@Query("hecho") String hecho);
}
