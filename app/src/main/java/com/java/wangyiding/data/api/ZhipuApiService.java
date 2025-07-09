package com.java.wangyiding.data.api;

import com.java.wangyiding.data.model.ChatRequest;
import com.java.wangyiding.data.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ZhipuApiService {
    @Headers("Authorization: __api__key__")
    @POST("api/paas/v4/chat/completions")
    Call<ChatResponse> getCompletion(@Body ChatRequest request);
}
