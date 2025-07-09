package com.java.wangyiding.data.api;

import com.java.wangyiding.data.model.ChatMessage;
import com.java.wangyiding.data.model.ChatRequest;
import com.java.wangyiding.data.model.ChatResponse;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZhipuApi {

    private static final String BASE_URL = "https://open.bigmodel.cn/";
    private static final String API_KEY = "Bearer d3ed522fa7214f0bb578bf6582b16822.qYPjvJLm80x158FA";

    public static void getSummary(String context, Callback<String> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ZhipuApiService service = retrofit.create(ZhipuApiService.class);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("user", "请根据以下新闻文本，生成一段100字以内的摘要。" + context));

        ChatRequest chatRequest = new ChatRequest("android-" + System.currentTimeMillis(), messages);

        Call<ChatResponse> call = service.getCompletion(chatRequest);

        call.enqueue(new retrofit2.Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, retrofit2.Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChatResponse.Choice> choices = response.body().choices;
                    if (choices != null && !choices.isEmpty()) {
                        callback.onSuccess(choices.get(0).message.content);
                    } else {
                        callback.onFailure("No content returned");
                    }
                } else {
                    callback.onFailure("HTTP error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure(String error);
    }
}

