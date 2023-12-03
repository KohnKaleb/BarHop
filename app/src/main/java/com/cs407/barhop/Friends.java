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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private List<UsersFriends> friendsList;

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
                for(int i = 0; i < ll.getChildCount(); i++) {
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
    public void viewMore(View view) {
        Intent intent = new Intent(this, BarInfo.class);
        UsersFriends currFriends = friendsList.get(view.getId());
        intent.putExtra("userId", currFriends.getUserId());
        intent.putExtra("friendId", currFriends.getFriendId());
        intent.putExtra("username", username);
        startActivity(intent);
    }
}