package com.cs407.barhop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Friends extends AppCompatActivity {

    private UsersDao usersDao;
    private UsersFriends usersFriends;
    private String username;
    private UsersFriendsDao usersFriendsDao;
    private LinearLayout ll;
    private List<Users> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

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
                View v = inflater.inflate(R.layout.activity_friends, null);
                ll = v.findViewById(R.id.parentLayout);
                Users currUser = usersDao.getUser(username);
                List<UsersFriends> favorites = usersFriendsDao.getUsersFriends(currUser.getId());
                friendsList = new ArrayList<>();
                Users clickedFriend = usersDao.getUser(username);

                for (UsersFriends favorite : favorites) {
                    Users friend = usersDao.getUserById(favorite.getFriendId());
                    friendsList.add(friend);
                }
                int count = 0;
                for(Users friend: friendsList){
                    LinearLayout barLayout = new LinearLayout(getBaseContext());
                    barLayout.setOrientation(LinearLayout.VERTICAL);
                    TextView name = new TextView(getBaseContext());
                    name.setText(friend.getUserName());
                    name.setTextSize(34);
                    barLayout.addView(name);
                    LinearLayout horizontal = new LinearLayout(getBaseContext());
                    horizontal.setOrientation(LinearLayout.HORIZONTAL);
                    TextView friends = new TextView(getBaseContext());
                    horizontal.addView(friends);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                for(int i = 2; i < ll.getChildCount(); i++) {
                    LinearLayout currLayout = (LinearLayout) ll.getChildAt(i);
                    TextView text = (TextView) currLayout.getChildAt(0);
                    if (!(text.getText().toString().toLowerCase().contains(newText.toLowerCase()))) {

                        ll.getChildAt(i).setVisibility(View.GONE);

                    } else {

                        ll.getChildAt(i).setVisibility(View.VISIBLE);

                    }
                }
                return false;
            }
        });
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    public void addFriend(Boolean isAdd, String friendName) {
        // add favorite to database
        if (isAdd) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    BarHopDatabase db = BarHopDatabase.getDatabase(getApplicationContext());
                    UsersDao usersDao = db.usersDao();
                    UsersFriendsDao friendsDao = db.friendsDao();
                    Users currUser = usersDao.getUser(username);
                    UsersFriends clickedFriend = usersFriendsDao.getFriend(friendName);

                    if (currUser != null && clickedFriend != null) {
                        UsersFriends newFav = new UsersFriends(currUser.getId(), clickedFriend.getFriendId());
                        friendsDao.insert(newFav);
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                }
            }.execute();

        } else {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    BarHopDatabase db = BarHopDatabase.getDatabase(getApplicationContext());
                    UsersDao usersDao = db.usersDao();
                    UsersFriendsDao friendsDao = db.friendsDao();
                    Users currUser = usersDao.getUser(username);
                    UsersFriends clickedFriend = usersFriendsDao.getFriend(friendName);

                    if (currUser != null & clickedFriend != null) {
                        friendsDao.delete(currUser.getId(), clickedFriend.getFriendId());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                }
            }.execute();
        }
    }

    public void switchToAddFriends(View view) {
        Log.e("addF", "ADD FRIES");
        Intent intent = new Intent(this, AddFriends.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}