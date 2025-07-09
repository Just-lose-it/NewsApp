package com.java.wangyiding.data.api;
import com.java.wangyiding.data.model.NewsBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("/svc/news/queryNewsList")
    Call<NewsBean> getNewsList(@Query("size") int size,
                                      @Query("startDate") String startDate,
                                      @Query("endDate") String endDate,
                                      @Query("categories") String categories,
                                      @Query("words") String words,
                                      @Query("page") int page);

}
