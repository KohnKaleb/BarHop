package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface UsersFriendsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UsersFriends usersFriends);
}
