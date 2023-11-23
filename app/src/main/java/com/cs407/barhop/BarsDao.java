package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BarsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Bars bar);

    @Query("SELECT * FROM bars")
    List<Bars> getAllEntities();

    @Query("DELETE FROM bars")
    void deleteAll();

    @Query("SELECT * FROM bars WHERE name = :barName")
    Bars getBar(String barName);
}
