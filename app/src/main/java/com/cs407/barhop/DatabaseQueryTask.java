package com.cs407.barhop;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class DatabaseQueryTask extends AsyncTask<Void, Void, UsernameAndPassword> {

    private Context context;
    private String username;

    private String password;

    private LoginResultListener resultListener;

    public DatabaseQueryTask(Context context, String username, String password, LoginResultListener resultListener) {
        this.context = context.getApplicationContext();
        this.username = username;
        this.password = password;
        this.resultListener = resultListener;
    }

    @Override
    protected UsernameAndPassword doInBackground(Void... voids) {
        BarHopDatabase database = BarHopDatabase.getDatabase(context);
        UsersDao usersDao = database.usersDao();

        return new UsernameAndPassword(username, password, usersDao.getUser(username));
    }

    @Override
    protected void onPostExecute(UsernameAndPassword userInfo) {
        if (userInfo.getUser() == null) {
            Toast.makeText(context, "User not found!", Toast.LENGTH_SHORT).show();
        } else {
            if  (userInfo.getUser().getPassword() != userInfo.getPassword()) {
                Toast.makeText(context, "wrong password!", Toast.LENGTH_SHORT).show();
            } else {
                resultListener.onLoginSuccess();
            }
        }
    }
}

