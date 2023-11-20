package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsersDao {
    @Insert
    void insert(Users user);

    @Query("SELECT * FROM users")
    List<Users> getAllEntities();
}
