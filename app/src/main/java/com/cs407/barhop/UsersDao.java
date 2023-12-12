package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Users user);

    @Query("SELECT * FROM users")
    List<Users> getAllEntities();

    @Query("SELECT * FROM users WHERE username = :username")
    Users getUser(String username);

    @Query("SELECT * FROM users WHERE id = :id")
    Users getUserById(int id);

    @Update
    void updateUser(Users currUser);
}
