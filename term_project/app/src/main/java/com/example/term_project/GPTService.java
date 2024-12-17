package com.example.term_project;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GPTService {
    @Headers({
            "Content-Type: application/json"
    })
    @POST("v1/chat/completions")
    Call<GPTResponse> getResponse(@Body GPTRequest request);
}


