package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface UsersDao {
    @Insert
    void insert(Users user);
}
