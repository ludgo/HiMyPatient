package sk.stuba.fiit.mtaa.himypatient.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sk.stuba.fiit.mtaa.himypatient.util.Constants;

/**
 * Retrofit based REST web service tools
 */
class RestHelper {

    /**
     * Build Retrofit at the top of used API url
     * @return Retrofit HTTP client
     */
    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.SERVER_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // JSON
                .build();
    }

    /**
     * Build REST service on Retrofit client
     * @return Retrofit driven REST service
     */
    static RestService buildRestService() {
        return buildRetrofit().create(RestService.class);
    }
}
