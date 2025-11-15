package ar.edu.utn.dds.k3003.app.client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

public interface SolicitudesRetrofitClient
{
    @GET("api/solicitudes/tieneSolicitudes")
    Call<Boolean> tieneSolicitudes(@Query("hecho") String hecho);

    @POST("api/solicitudes/estados")
    Call<Map<String, Boolean>> tienenSolicitudes(@Body List<String> hechoIds);
    // Map<hechoId, tieneSolicitudActiva>

//    @POST("api/solicitudes/activos")
//    Call<Map<String, Boolean>> hechosActivos(@Body List<String> hechoIds);

    @GET("/api/solicitudes/activo")
    Call<Boolean> estaActivo(@Query("hecho") String hechoId);
}
