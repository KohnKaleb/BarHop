package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Users user);

    @Query("SELECT * FROM users")
    List<Users> getAllEntities();

    @Query("SELECT * FROM users WHERE username = :username")
    Users getUser(String username);
}
