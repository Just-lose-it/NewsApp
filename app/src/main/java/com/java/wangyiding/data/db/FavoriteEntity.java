package com.java.wangyiding.data.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteEntity {

    @PrimaryKey
    @NonNull
    public String newsId;


    public String title;

    public String content;
    public String publisher;
    public String publishTime;
    public String videoUrl;
    public String imageJson;
    public String category;
    public String summary;

    public String viewTime; // 格式："yyyy-MM-dd HH:mm:ss"
}
