package com.java.wangyiding.ui.history;

import android.content.Intent;
import android.os.Bundle;

import com.java.wangyiding.data.db.HistoryDatabase;
import com.java.wangyiding.data.db.HistoryEntity;
import com.java.wangyiding.data.db.MyDatabase;
import com.java.wangyiding.data.model.News;
import com.java.wangyiding.ui.detail.DetailActivity;
import com.java.wangyiding.ui.main.NewsAdapter;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.java.wangyiding.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ImageView backButton;
    private NewsAdapter adapter;

    private HistoryDatabase db;

    private List<News> newsList=new ArrayList<News>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        backButton=findViewById(R.id.backButton);


        recyclerView = findViewById(R.id.recordRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db =  MyDatabase.getInstance(this);

        new Thread(() -> {
            List<HistoryEntity> historyList = db.newsDao().getAllHistory();

            for (HistoryEntity entry : historyList) {
                News news=new News();

                if (entry != null) {
                    news.setNewsId(entry.newsId);
                    news.setContent(entry.content);
                    news.setPublisher(entry.publisher);
                    news.setTitle(entry.title);
                    news.setPublishTime(entry.publishTime);
                    news.setImageJson(entry.imageJson);
                    news.setVideoUrl(entry.videoUrl);

                    newsList.add(news);
                }
            }

            runOnUiThread(() -> {
                adapter = new NewsAdapter(this,newsList, this::openDetail);
                recyclerView.setAdapter(adapter);
            });
        }).start();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void openDetail(News news) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("newsId", news.getNewsId());
        intent.putExtra("title", news.getTitle());
        intent.putExtra("publishTime", news.getPublishTime());
        intent.putExtra("publisher",news.getPublisher());
        intent.putExtra("content",news.getContent());
        intent.putExtra("videoPath",news.getVideoUrl());
        intent.putExtra("picPath",news.getImageJson());
        startActivity(intent);
    }
}