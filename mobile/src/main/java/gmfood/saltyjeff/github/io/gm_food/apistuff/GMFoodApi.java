package gmfood.saltyjeff.github.io.gm_food.apistuff;

import java.util.List;

import gmfood.saltyjeff.github.io.gm_food.MenuItem;
import gmfood.saltyjeff.github.io.gm_food.Vendor;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface GMFoodApi {
    @Multipart
    @POST("/options/")
    Call<List<Vendor>> getOptions(@Part MultipartBody.Part file);
    @GET("/menu/{vendorId}")
    Call<List<MenuItem>> getMenu(@Path("vendorId") String vendorId);
    @Multipart
    @POST("/quote/{vendorId}")
    Call<QuoteResponse> makeQuote(@Path("vendorId") String vendorId, @Part MultipartBody.Part file);
    @POST("/pay/{orderId}")
    Call<PayResponse> pay(@Path("orderId") String orderId);
}
