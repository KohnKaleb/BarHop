package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsersFriendsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UsersFriends usersFriends);

    @Query("DELETE FROM usersFriends WHERE userId = :userId AND friendId = :friendId")
    void delete(int userId, int friendId);

    @Query("SELECT * FROM usersFriends WHERE userId = :userId")
    List<UsersFriends>getUsersFriends(int userId);
}
