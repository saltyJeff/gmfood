package gmfood.saltyjeff.github.io.gm_food.apistuff;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GMFOOD {
    public static GMFoodApi api;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://a8887d96.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(GMFoodApi.class);
    }
}
