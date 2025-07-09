package com.java.wangyiding.ui.history;

import android.content.Intent;
import android.os.Bundle;

import com.java.wangyiding.ui.main.MainActivity;
import com.java.wangyiding.R;
import com.java.wangyiding.data.db.HistoryDatabase;
import com.java.wangyiding.ui.category.CategoryActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.room.Room;

public class InfoActivity extends AppCompatActivity {

    private LinearLayout historyPage;
    private LinearLayout favoritePage;

    private LinearLayout homePage;

    private LinearLayout categoryPage;

    private TextView historyCount;
    private TextView favoriteCount;

    private HistoryDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_info);
        db= Room.databaseBuilder(this,HistoryDatabase.class,"news-db").build();
        historyPage=findViewById(R.id.History);
        favoritePage=findViewById(R.id.Favorites);
        historyCount=findViewById(R.id.historyCount);
        favoriteCount=findViewById(R.id.favoriteCount);
        homePage=findViewById(R.id.HomePage);
        categoryPage=findViewById(R.id.CategoryPage);
        historyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(InfoActivity.this, HistoryActivity.class),1);

            }
        });
        favoritePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(InfoActivity.this, FavoritesActivity.class),1);
            }
        });
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this, MainActivity.class));
                finish();
            }
        });
        categoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this, CategoryActivity.class));
                finish();
            }
        });
        new Thread(() -> {
            int historySize = db.newsDao().getAllHistory().size();
            int favoriteSize = db.newsDao().getAllFavorites().size();

            runOnUiThread(() -> {
                historyCount.setText(String.valueOf(historySize));
                favoriteCount.setText(String.valueOf(favoriteSize));
            });
        }).start();
    }
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        new Thread(() -> {
            int historySize = db.newsDao().getAllHistory().size();
            int favoriteSize = db.newsDao().getAllFavorites().size();

            runOnUiThread(() -> {
                historyCount.setText(String.valueOf(historySize));
                favoriteCount.setText(String.valueOf(favoriteSize));
            });
        }).start();

    }
}