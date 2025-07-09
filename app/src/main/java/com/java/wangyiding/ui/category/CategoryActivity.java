package com.java.wangyiding.ui.category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.java.wangyiding.ui.history.InfoActivity;
import com.java.wangyiding.ui.main.MainActivity;
import com.java.wangyiding.R;
import com.java.wangyiding.data.api.ApiClient;
import com.java.wangyiding.data.api.NewsApiService;
import com.java.wangyiding.data.model.News;
import com.java.wangyiding.data.model.NewsBean;
import com.java.wangyiding.ui.detail.DetailActivity;
import com.java.wangyiding.ui.main.NewsAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private LinearLayout categoryBar;
    private String selectedCategory = "全部"; // 当前选中分类
    private String[] categories = {"全部", "科技", "体育", "娱乐", "教育", "军事", "财经", "汽车", "健康", "文化", "社会"};

    String startDate= "2010-07-01 13:12:45";
    String endDate;

    private List<News> newsList = new ArrayList<>();

    private long loadStartTime;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private NewsApiService apiService;

    private ImageView modifyButton;

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;

    private  LinearLayout homePage;

    private  LinearLayout infoPage;

    private Button selectedButton = null;
    private void setupCategoryBar() {
        categoryBar.removeAllViews();
        for (String category : categories) {
            Button button = new Button(this);

            button.setText(category);
            button.setTextSize(14);
            button.setTextColor(Color.parseColor("#1f1e33"));
            button.setPadding(30, 10, 30, 10);
            //button.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_button_selector)); // 选中/未选中状态
            button.setOnClickListener(v -> {
                selectedCategory = category;
                currentPage = 1;
                isLastPage = false;
                if(selectedButton!=null)
                {
                    selectedButton.setTextColor(Color.parseColor("#1f1e33"));
                }

                button.setTextColor(Color.parseColor("#cc6666"));
                selectedButton=button;
                loadNews(true); // 重新加载新闻
            });

            categoryBar.addView(button);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        SharedPreferences prefs = getSharedPreferences("category_prefs", MODE_PRIVATE);
        String json = prefs.getString("customCategories", null);

        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                categories = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    categories[i] = jsonArray.getString(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // 回退默认分类
                categories = new String[]{"全部", "科技", "体育", "娱乐", "教育", "军事", "财经", "汽车", "健康", "文化", "社会"};
            }
        } else {
            // 没有保存记录，默认分类
            categories = new String[]{"全部", "科技", "体育", "娱乐", "教育", "军事", "财经", "汽车", "健康", "文化", "社会"};
        }
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        adapter = new NewsAdapter(this,newsList, this::openNewsDetail);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        apiService = ApiClient.getClient().create(NewsApiService.class);
        modifyButton=findViewById(R.id.modify);
        categoryBar=findViewById(R.id.categoryBar);
        homePage=findViewById(R.id.HomePage);
        infoPage=findViewById(R.id.InfoPage);
        setupCategoryBar();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;

            Log.d("dd","Refresh!");
            SimpleDateFormat sdf=new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date=new Date();
            endDate = sdf.format(date);
            loadNews(true);
            //"八部门发文推动文化创意产品开发";
        });

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent=new Intent(CategoryActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });


        infoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this, InfoActivity.class);
                startActivity(intent);
                finish();
            }
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

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this, CategorySelectActivity.class);
                intent.putExtra("categories",categories);
                startActivityForResult(intent,2);
            }
        });

        currentPage = 1;
        isLastPage = false;

        Log.d("dd","Refresh!");
        SimpleDateFormat sdf=new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        endDate = sdf.format(date);
        loadNews(true);

    }
    private void loadNews(boolean isRefresh) {
        isLoading = true;
        if (isRefresh) swipeRefreshLayout.setRefreshing(true);

        int size = 15;


        //int currentPage=1;
        String realSelected=selectedCategory.equals("全部")?"":selectedCategory;
        apiService.getNewsList(size, startDate, endDate, realSelected ,"", currentPage)
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
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(@NonNull Call<NewsBean> call, @NonNull Throwable t) {
                        Toast.makeText(CategoryActivity.this, "加载失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("dd","Fail!");
                    }
                });
    }

    private void loadNews(boolean isRefresh,int delayMillis) {
        isLoading = true;
        if (isRefresh) swipeRefreshLayout.setRefreshing(true);

        int size = 15;


        //int currentPage=1;
        String realSelected=selectedCategory.equals("全部")?"":selectedCategory;
        apiService.getNewsList(size, startDate, endDate, realSelected ,"", currentPage)
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
                        Toast.makeText(CategoryActivity.this, "加载失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        isLoading = false;
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("dd","Fail!");
                    }
                });
    }
    private void openNewsDetail(News news) {
        Intent intent = new Intent(CategoryActivity.this, DetailActivity.class);
        Log.d("debug", "newsId = " + news.getNewsId());
        intent.putExtra("newsId", news.getNewsId());
        intent.putExtra("title", news.getTitle());
        intent.putExtra("publishTime", news.getPublishTime());
        intent.putExtra("publisher",news.getPublisher());
        intent.putExtra("content",news.getContent());
        intent.putExtra("videoPath",news.getVideoUrl());
        intent.putExtra("picPath",news.getImageJson());

        //startActivity(intent);
        startActivityForResult(intent,999);
        adapter.notifyDataSetChanged();
        //loadNews(false);
    }

    protected void  onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999)
        {
            adapter.notifyDataSetChanged();
            return;
        }
        if(requestCode!=2  || resultCode!=RESULT_OK)return;
        if( data == null)
        {
            selectedCategory="";
            currentPage = 1;
            isLastPage = false;
            loadNews(true);
            return;
        }

        categories=data.getStringArrayExtra("categories");
        selectedCategory="";
        currentPage = 1;
        isLastPage = false;
        setupCategoryBar();
        loadNews(true);

        SharedPreferences prefs = getSharedPreferences("category_prefs", MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (String category : categories) {
            jsonArray.put(category);
        }
        prefs.edit().putString("customCategories", jsonArray.toString()).apply();



    }
}