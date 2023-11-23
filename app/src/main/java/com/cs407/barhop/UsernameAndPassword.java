package com.cs407.barhop;

public class UsernameAndPassword {
    private String username;
    private String password;

    private Users user;

    public UsernameAndPassword(String username, String password, Users user) {
        this.password = password;
        this.username = username;
        this.user = user;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
