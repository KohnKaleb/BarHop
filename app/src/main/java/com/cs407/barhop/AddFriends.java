package com.cs407.barhop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddFriends extends AppCompatActivity {
    private UsersDao usersDao;
    private String username;
    private UsersFriendsDao usersFriendsDao;
    private LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        username = getIntent().getStringExtra("username");
        populateAndRetrieveData();
    }

    @SuppressLint("StaticFieldLeak")
    private void populateAndRetrieveData() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Get a reference to your database instance
                BarHopDatabase database = BarHopDatabase.getDatabase(getApplicationContext());

                // Access the DAO
                usersFriendsDao = database.friendsDao();
                usersDao = database.usersDao();

                // Retrieve data after populating the database

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Handle any UI updates or further processing here
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.activity_main, null);
                ll = v.findViewById(R.id.parentLayout);
                Users currUser = usersDao.getUser(username);
                List<Users> allUsers = usersDao.getAllEntities();

                int count = 0;
                for(Users user: allUsers){
                    LinearLayout barLayout = new LinearLayout(getBaseContext());
                    barLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout horizontal = new LinearLayout(getBaseContext());
                    horizontal.setOrientation(LinearLayout.HORIZONTAL);
                    TextView name = new TextView(getBaseContext());
                    name.setText(user.getUserName());
                    name.setTextSize(34);
                    name.setId(count);
                    horizontal.addView(name);
                    Button add = new Button(getBaseContext());
                    add.setText("Add Friend");
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addFriend(v, currUser, user);
                        }
                    });
                    horizontal.addView(add);
                    barLayout.addView(horizontal);
                    Space space = new Space(getBaseContext());
                    space.setMinimumHeight(20);
                    barLayout.addView(space);
                    barLayout.setId(count);
                    count++;
                    ll.addView(barLayout);
                }
                setContentView(v);
            }
        }.execute();
    }
    private void addFriend(View view, Users currUser,Users user) {
        UsersFriends friend = new UsersFriends(currUser.getId(), user.getId());
        usersFriendsDao.insert(friend);
    }
}
