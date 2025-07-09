package com.java.wangyiding.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NewsSummary.class}, version = 1)
public abstract class SummaryDatabase extends RoomDatabase {
    public abstract SummaryDao summaryDao();
}