package com.example.term_project;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getClient(String apiKey) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + apiKey);
                        return chain.proceed(requestBuilder.build());
                    }
                }).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openai.com/") // GPT API URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
