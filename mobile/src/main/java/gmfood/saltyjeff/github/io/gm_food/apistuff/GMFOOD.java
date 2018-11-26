package gmfood.saltyjeff.github.io.gm_food.apistuff;

import retrofit2.Retrofit;

public class GMFOOD {
    public static GMFoodApi api;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .build();
        api = retrofit.create(GMFoodApi.class);
    }
}
