package com.cs407.barhop;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "usersFriends",
        primaryKeys = {"userId", "friendId"},
        foreignKeys = {
            @ForeignKey(entity = Users.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Users.class,
                parentColumns = "id",
                childColumns = "friendId",
                onDelete = ForeignKey.CASCADE)
})
public class UsersFriends {
    public int userId;
    public int friendId;

    public UsersFriends(int userId, int friendId){
        this.userId = userId;
        this.friendId = friendId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int barId) {
        this.friendId = barId;
    }
}
