package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsersFavoriteBarsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UsersFavoriteBars usersFavoriteBars);

    @Query("DELETE FROM usersfavoritebars WHERE userId = :userId AND barId = :barId")
    void delete(int userId, int barId);

    @Query("SELECT * FROM usersfavoritebars WHERE userId = :userId")
    List<UsersFavoriteBars> getFavoriteBars(int userId);
}
