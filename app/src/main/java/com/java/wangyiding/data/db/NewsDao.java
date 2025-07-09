package com.java.wangyiding.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(HistoryEntity news);



    @Query("SELECT * FROM history WHERE newsId = :id LIMIT 1")
    HistoryEntity getHistoryById(String id);



    @Query("SELECT * FROM history ORDER BY viewTime DESC")
    List<HistoryEntity> getAllHistory();



    @Query("SELECT EXISTS(SELECT 1 FROM history WHERE newsId = :id)")
    boolean hasViewed(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavorite(FavoriteEntity news);

    @Query("SELECT * FROM favorites WHERE newsId = :id LIMIT 1")
    FavoriteEntity getFavoriteById(String id);

    @Query("SELECT * FROM favorites ORDER BY viewTime DESC")
    List<HistoryEntity> getAllFavorites();

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE newsId = :id)")
    boolean isFavorite(String id);

    @Query("DELETE FROM favorites WHERE newsId = :id")
    void removeFavorite(String id);
}
