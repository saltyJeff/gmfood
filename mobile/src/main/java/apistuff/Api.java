package apistuff;

import retrofit2.Retrofit;
import retrofit2.http.POST;

public class Api {
    static GMFoodApi api;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .build();
        api = retrofit.create(GMFoodApi.class);
    }
}
