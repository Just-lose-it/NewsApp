package com.java.wangyiding.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = { HistoryEntity.class, FavoriteEntity.class}, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
}
