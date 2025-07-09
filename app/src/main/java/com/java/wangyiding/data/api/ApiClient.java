package com.java.wangyiding.data.api;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private  static Retrofit retrofit=null;
    public static Retrofit getClient() {
        if (retrofit == null){
            retrofit=new Retrofit.Builder().baseUrl("https://api2.newsminer.net").
                    addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient()).build();
        }
        return  retrofit;
    }
}
