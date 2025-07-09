package com.java.wangyiding.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsSummary summary);

    @Query("SELECT * FROM summary WHERE newsId = :newsId LIMIT 1")
    NewsSummary getSummaryByNewsId(String newsId);
}
