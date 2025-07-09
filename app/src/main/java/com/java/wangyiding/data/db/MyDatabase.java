package com.java.wangyiding.data.db;

import android.content.Context;

import androidx.room.Room;

public class MyDatabase {
    private static HistoryDatabase instance;

    public static synchronized HistoryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            HistoryDatabase.class, "news-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
