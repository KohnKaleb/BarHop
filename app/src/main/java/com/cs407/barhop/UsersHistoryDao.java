package com.cs407.barhop;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsersHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UsersHistory usersHistory);

    @Query("DELETE FROM usersHistory WHERE userId = :userId AND barId = :barId")
    void delete(int userId, int barId);

    @Query("SELECT * FROM usershistory WHERE userId = :userId")
    List<UsersHistory> getUsersHistory(int userId);
}
