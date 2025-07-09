package com.java.wangyiding.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.java.wangyiding.R;
import com.java.wangyiding.data.api.ZhipuApi;
import com.java.wangyiding.data.db.FavoriteEntity;
import com.java.wangyiding.data.db.HistoryDatabase;
import com.java.wangyiding.data.db.HistoryEntity;
import com.java.wangyiding.data.db.MyDatabase;
import com.java.wangyiding.data.db.NewsSummary;
import com.java.wangyiding.data.db.SummaryDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {


    private TextView titleText;
    private TextView infoText;
    private PlayerView playerView;
    private Button buttonBack;
    private ImageView imageView;
    private TextView contentText;
    private TextView abstractText;

    private ImageView likeButton;
    private HistoryDatabase hisDb;

    private String newsId;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        hisDb = MyDatabase.getInstance(this);
        //this.deleteDatabase("news-db");
        SummaryDatabase db = Room.databaseBuilder(this, SummaryDatabase.class, "summary-db").build();
        setContentView(R.layout.activity_detail);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String time=intent.getStringExtra("publishTime");
        String publisher=intent.getStringExtra("publisher");
        titleText=findViewById(R.id.detailTitle);
        infoText=findViewById(R.id.detailInfo);
        playerView=findViewById(R.id.detailPlayer);
        buttonBack=findViewById(R.id.BackButton);
        imageView=findViewById(R.id.detailPic);
        abstractText=findViewById(R.id.detailAbstract);
        contentText=findViewById(R.id.detailContext);
        titleText.setText(title);
        likeButton=findViewById(R.id.likeButton);
        infoText.setText(publisher+"  "+time);
        String videoUrl=intent.getStringExtra("videoPath");
        String content=intent.getStringExtra("content");

        newsId = intent.getStringExtra("newsId");
        abstractText.setText("摘要生成中，请稍等……");
        contentText.setText(content+"\n\n\n\n\n\n\n\n\n\n\n");
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        //Log.d("player",videoUrl);
        if(videoUrl!=null &&!videoUrl.isEmpty())
        {
            Log.d("player",videoUrl);
            ExoPlayer player=new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
            player.setMediaItem(mediaItem);
            player.prepare();

        }
        String pic_path=intent.getStringExtra("picPath");
        Log.d("debug_history", "开始写入历史记录 newsId=" + newsId);
        if(pic_path!=null &&pic_path.length()>2)
        {
            String[] pic_paths=pic_path.substring(1,pic_path.length()-1).split(",");
            if (pic_paths.length > 0&&!pic_paths[0].isEmpty()) {
                Glide.with(this).load(pic_paths[0]).into(imageView);
            }
        }
        if(newsId!=null)
        {
            new Thread(()->{HistoryEntity history=new HistoryEntity();
                history.imageJson=pic_path;
                history.content=content;
                history.title=title;
                history.newsId=newsId;
                history.publisher=publisher;
                history.publishTime=time;
                history.videoUrl=videoUrl;
                history.viewTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                hisDb.newsDao().insertHistory(history);}).start();

        }




        new Thread(() -> {
            NewsSummary summary = db.summaryDao().getSummaryByNewsId(newsId);

                if (summary != null) {
                    runOnUiThread(() -> { abstractText.setText("摘要:"+summary.summary);});
                } else {
                    ZhipuApi.getSummary(content,new ZhipuApi.Callback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d("ZhipuResult", "摘要：" + result);
                            runOnUiThread(() -> abstractText.setText("摘要:"+result));
                            if(newsId==null)return;
                            NewsSummary insertSummary=new NewsSummary();

                            insertSummary.newsId=newsId;
                            insertSummary.time=time;
                            insertSummary.summary=result;
                            new Thread(()->db.summaryDao().insert(insertSummary)).start();

                        }

                        @Override
                        public void onFailure(String error) {
                            Log.e("ZhipuError", error);
                            runOnUiThread(() -> abstractText.setText("生成失败" + error));
                        }} );

                }
            ;
        }).start();

        new Thread(()->{if(hisDb.newsDao().isFavorite(newsId))
        {
            runOnUiThread(()->{likeButton.setImageResource(R.drawable.baseline_star_rate_24);});

        }
        else
        {
            runOnUiThread(()->{likeButton.setImageResource(R.drawable.baseline_star_border_24);});
        }}).start();

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(()->{

                    if(hisDb.newsDao().isFavorite(newsId))
                    {
                        runOnUiThread(()->likeButton.setImageResource(R.drawable.baseline_star_border_24));
                        hisDb.newsDao().removeFavorite(newsId);
                    }
                    else
                    {
                        runOnUiThread(()->likeButton.setImageResource(R.drawable.baseline_star_rate_24));
                        FavoriteEntity favorite=new FavoriteEntity();
                        favorite.imageJson=pic_path;
                        favorite.content=content;
                        favorite.title=title;
                        favorite.newsId=newsId;
                        favorite.publisher=publisher;
                        favorite.publishTime=time;
                        favorite.videoUrl=videoUrl;
                        favorite.viewTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        hisDb.newsDao().insertFavorite(favorite);
                    }

                }).start();

            }
        });


    }


}
