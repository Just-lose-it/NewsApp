package com.java.wangyiding.data.api;

import com.java.wangyiding.data.model.ChatRequest;
import com.java.wangyiding.data.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ZhipuApiService {
    @Headers("Authorization: Bearer d3ed522fa7214f0bb578bf6582b16822.qYPjvJLm80x158FA")
    @POST("api/paas/v4/chat/completions")
    Call<ChatResponse> getCompletion(@Body ChatRequest request);
}