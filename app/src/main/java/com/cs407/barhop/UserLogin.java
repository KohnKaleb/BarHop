package com.cs407.barhop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserLogin extends AppCompatActivity implements LoginResultListener {

    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void login(View view) {
        validateLogin(username.getText().toString(), password.getText().toString());
    }

    public void signup(View view) {
        updateDatabase(username.getText().toString(), password.getText().toString());
    }
    public void goToActivity(String s) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", s);
        startActivity(intent);
    }

    private void validateLogin(String username, String password) {
        DatabaseQueryTask databaseQueryTask = new DatabaseQueryTask(this, username, password, this);
        databaseQueryTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void updateDatabase(String username, String password) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                BarHopDatabase db = BarHopDatabase.getDatabase(getApplicationContext());
                UsersDao usersDao = db.usersDao();

                Users existingUser = usersDao.getUser(username);
                if (existingUser == null) {

                    Users newUser = new Users();
                    newUser.setPassword(password);
                    newUser.setUserName(username);
                    usersDao.insert(newUser);

                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean isUpdated) {
                if (isUpdated) {
                    // Username was updated successfully
                    Toast.makeText(getApplicationContext(), "User updated!", Toast.LENGTH_SHORT).show();
                    goToActivity(username);
                } else {
                    // Username already exists
                    Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onLoginSuccess() {
        goToActivity(username.getText().toString());
    }

    @Override
    public void onLoginFailed(String errorMessage) {
    }
}