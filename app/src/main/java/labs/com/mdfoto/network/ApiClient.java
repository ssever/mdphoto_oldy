package labs.com.mdfoto.network;


import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by oktay on 27.05.2016.
 */
public class ApiClient {


    //private static final String URL = "http://webservisornek-oktayduman.rhcloud.com";
    private static final String URL = "http://client.mdphotoapp.com";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();



        if (retrofit==null) {
            retrofit = new Retrofit.Builder()

                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .client(client)

                    .build();

        }
        return retrofit;
    }
}
