package ar.edu.utn.dds.k3003.app.client;

import ar.edu.utn.dds.k3003.dtos.HechoParaIndexarDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IndexadorRetrofitClient
{
    @POST("api/indexacion/hechos")
    Call<Void> indexarHecho(@Body HechoParaIndexarDTO hechoIndexar);
}
