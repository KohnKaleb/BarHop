package com.cs407.barhop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
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
                View v = inflater.inflate(R.layout.activity_add_friends, null);
                ll = v.findViewById(R.id.addFriendsParent);
                Users currUser = usersDao.getUser(username);
                List<Users> allUsers = usersDao.getAllEntities();
                List<UsersFriends> usersFriends = usersFriendsDao.getUsersFriends(currUser.getId());

                List<Integer> friendIds = new ArrayList<Integer>();
                for(UsersFriends friend: usersFriends){
                    friendIds.add(friend.getFriendId());
                }

                int count = 0;
                for(Users user: allUsers){
                    if(user.getId() != currUser.getId() && !friendIds.contains(user.getId())) {
                        Log.e("users", "user: " + user.getUserName());
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
                for(int i = 0; i < ll.getChildCount(); i++) {
                    LinearLayout currLayout = (LinearLayout) ll.getChildAt(i);
                    LinearLayout horizontal = (LinearLayout) currLayout.getChildAt(0);
                    TextView text = (TextView) horizontal.getChildAt(0);
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

    private void addFriend(View view, Users currUser,Users user) {
        UsersFriends friend = new UsersFriends(currUser.getId(), user.getId());
        usersFriendsDao.insert(friend);
        UsersFriends friend2 = new UsersFriends(user.getId(), currUser.getId());
        usersFriendsDao.insert(friend2);
    }
}
