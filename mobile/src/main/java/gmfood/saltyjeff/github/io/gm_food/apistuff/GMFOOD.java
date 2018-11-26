package gmfood.saltyjeff.github.io.gm_food.apistuff;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GMFOOD {
    public static GMFoodApi api;
    static {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(600, TimeUnit.SECONDS)
                .connectTimeout(600, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://a8887d96.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
				.client(okHttpClient)
                .build();
        api = retrofit.create(GMFoodApi.class);
    }
}
