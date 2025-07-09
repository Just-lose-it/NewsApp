package com.java.wangyiding.ui.main;
import com.java.wangyiding.R;
import com.java.wangyiding.data.api.ApiClient;
import com.java.wangyiding.data.api.NewsApiService;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.wangyiding.data.model.News;
import com.java.wangyiding.data.model.NewsBean;
import com.java.wangyiding.ui.category.CategoryActivity;
import com.java.wangyiding.ui.detail.DetailActivity;
import com.java.wangyiding.ui.history.InfoActivity;
import com.java.wangyiding.ui.search.SearchActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;

    private ImageView searchIcon;

    private LinearLayout CategoryPage;
    private LinearLayout InfoPage;

    private List<News> newsList = new ArrayList<>();
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private long loadStartTime;

    private NewsApiService apiService;

    String startDate= "2010-07-01 13:12:45";
    String endDate;
    String keywords;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        keywords="";
        category="";
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        searchIcon=findViewById(R.id.toSearch);
        //this.deleteDatabase("news-db");
        adapter = new NewsAdapter(this,newsList, this::openNewsDetail);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        CategoryPage=findViewById(R.id.CategoryPage);
        InfoPage=findViewById(R.id.InfoPage);

        apiService = ApiClient.getClient().create(NewsApiService.class);

        searchIcon.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent,1);

            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;

            Log.d("dd","Refresh!");
            SimpleDateFormat sdf=new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date=new Date();
            endDate = sdf.format(date);
            //"八部门发文推动文化创意产品开发";       // 可以改为用户输入
            //keywords = "";
            // 可以改为用户选择
            //category = "";
            loadNews(true);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoading && !isLastPage) {
                    currentPage++;
                    loadStartTime = System.currentTimeMillis();
                    adapter.showFooter(true);
                    //recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    loadNews(false,1500);
                    Log.d("dd","Scroll!");

                }
            }
        });

        CategoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        InfoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SimpleDateFormat sdf=new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        endDate = sdf.format(date);
        //"八部门发文推动文化创意产品开发";       // 可以改为用户输入
        keywords = "";
        // 可以改为用户选择
        category = "";
        loadNews(true);
    }

    private void loadNews(boolean isRefresh,int delayMillis) {
        isLoading = true;
        if (isRefresh) swipeRefreshLayout.setRefreshing(true);

        int size = 15;


        //int currentPage=1;

        apiService.getNewsList(size, startDate, endDate,  category,keywords, currentPage)
                .enqueue(new Callback<NewsBean>(){
                    @Override
                    public void onResponse(@NonNull Call<NewsBean> call, @NonNull Response<NewsBean> response) {

                        long loadDuration = System.currentTimeMillis() - loadStartTime;
                        long delay = Math.max(0, delayMillis - loadDuration);
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if (response.isSuccessful() && response.body() != null) {
                                if (isRefresh) {
                                    newsList.clear();
                                }
                                List<News> fetched = response.body().getData();
                                Log.d("dd","News cnt:"+Integer.toString(fetched.size()));
                                newsList.addAll(fetched);
                                adapter.notifyDataSetChanged();
                                Log.d("dd","ResponseSuccess!");
                                isLastPage = fetched.size() < size;
                            }



                            adapter.showFooter(false);
                            swipeRefreshLayout.setRefreshing(false);
                            isLoading = false;
                        }, delay);

                    }

                    @Override
                    public void onFailure(@NonNull Call<NewsBean> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "加载失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("dd","Fail!");
                    }
                });
    }
    private void loadNews(boolean isRefresh) {
        isLoading = true;
        if (isRefresh) swipeRefreshLayout.setRefreshing(true);

        int size = 15;


        //int currentPage=1;

        apiService.getNewsList(size, startDate, endDate,  category,keywords, currentPage)
                .enqueue(new Callback<NewsBean>(){
                    @Override
                    public void onResponse(@NonNull Call<NewsBean> call, @NonNull Response<NewsBean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (isRefresh) {
                                newsList.clear();
                            }
                            List<News> fetched = response.body().getData();
                            Log.d("dd","News cnt:"+Integer.toString(fetched.size()));
                            newsList.addAll(fetched);
                            adapter.notifyDataSetChanged();
                            Log.d("dd","ResponseSuccess!");
                            isLastPage = fetched.size() < size;
                        }
                        isLoading = false;
                        adapter.showFooter(false);
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(@NonNull Call<NewsBean> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "加载失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("dd","Fail!");
                    }
                });
    }
    private void openNewsDetail(News news) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("newsId", news.getNewsId());
        intent.putExtra("title", news.getTitle());
        intent.putExtra("publishTime", news.getPublishTime());
        intent.putExtra("publisher",news.getPublisher());
        intent.putExtra("content",news.getContent());
        intent.putExtra("videoPath",news.getVideoUrl());
        intent.putExtra("picPath",news.getImageJson());

        startActivityForResult(intent,999);
        adapter.notifyDataSetChanged();
        //loadNews(false);
    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {


        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999)
        {
            adapter.notifyDataSetChanged();
            return;
        }
        if(resultCode!=RESULT_OK)return;
        if(data==null)return;
        startDate=data.getStringExtra("startTime");
        endDate=data.getStringExtra("endTime");
        keywords=data.getStringExtra("keyWord");
        category=data.getStringExtra("category");
        Log.d("Params", "start=" + startDate + ", end=" + endDate + ", keyword=" + keywords + ", cat=" + category);
        currentPage = 1;
        isLastPage = false;

        loadNews(true);

    }
}