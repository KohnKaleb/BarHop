package com.cs407.barhop;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "usersFavoriteBars",
        primaryKeys = {"userId", "barId"},
        foreignKeys = {
                @ForeignKey(entity = Users.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Bars.class,
                        parentColumns = "barId",
                        childColumns = "barId",
                        onDelete = ForeignKey.CASCADE)
})
public class UsersFavoriteBars {
    public int userId;
    public int barId;

    public UsersFavoriteBars() {

    }
    public UsersFavoriteBars(int userId, int barId){
        this.userId = userId;
        this.barId = barId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    @Override
    public boolean equals(Object o) {
        UsersFavoriteBars b = (UsersFavoriteBars) o;
        if (b.getBarId() == this.getBarId() && b.getUserId() == this.getUserId()) {
            return true;
        } else {
            return false;
        }
    }
}
