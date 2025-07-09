package com.java.wangyiding.data.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "summary")
public class NewsSummary {
    @PrimaryKey
    @NonNull
    public  String newsId;

    @ColumnInfo(name="time")
    public  String time;
    @ColumnInfo(name = "summary")
    public String summary;
}
