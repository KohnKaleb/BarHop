package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface BarsDao {
    @Insert
    void insert(Bars bar);
}
