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
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {

    private BarsDao barsDao;
    private UsersDao usersDao;

    private String username;
    private LinearLayout ll;
    private List<Bars> favoriteBars;

    private UsersFavoriteBarsDao favoriteBarsDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

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
                favoriteBarsDao = database.favoritesDao();
                usersDao = database.usersDao();
                barsDao = database.barsDao();

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
                List<UsersFavoriteBars> favorites = favoriteBarsDao.getFavoriteBars(currUser.getId());
                favoriteBars = new ArrayList<>();

                for (UsersFavoriteBars favorite : favorites) {
                    Bars bar = barsDao.getBarById(favorite.getBarId());
                    favoriteBars.add(bar);
                }
                int count = 0;
                for(Bars b: favoriteBars){
                    LinearLayout barLayout = new LinearLayout(getBaseContext());
                    barLayout.setOrientation(LinearLayout.VERTICAL);
                    TextView name = new TextView(getBaseContext());
                    name.setText(b.getName());
                    name.setTextSize(34);
                    name.setId(count);
                    name.setClickable(true);
                    name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewMore(v);
                        }
                    });
                    barLayout.addView(name);
                    LinearLayout horizontal = new LinearLayout(getBaseContext());
                    horizontal.setOrientation(LinearLayout.HORIZONTAL);
                    TextView friends = new TextView(getBaseContext());
                    friends.setText("# friends");
                    friends.setTextSize(28);
                    horizontal.addView(friends);
                    ImageButton heartButton = new ImageButton(getBaseContext());
                    UsersFavoriteBars thisBar = new UsersFavoriteBars(usersDao.getUser(username).getId(), b.getBarId());
                    if(favorites.contains(thisBar)) {
                        heartButton.setBackgroundResource(R.drawable.red_heart);
                    } else {
                        heartButton.setBackgroundResource(R.drawable.gray_heart);
                    }
                    heartButton.setTag(b);
                    heartButton.setOnClickListener(new View.OnClickListener() {
                        Bars clickedBar;
                        boolean isGray;
                        @Override
                        public void onClick(View v) {
                            if(favorites.contains(thisBar)) {
                                isGray = false;
                            } else {
                                isGray = true;
                            }
                            clickedBar = (Bars) v.getTag();

                            if (isGray) {
                                heartButton.setBackgroundResource(R.drawable.red_heart);
                                isGray = false;
                                addFavoriteBar(true, clickedBar.getName());
                            } else {
                                heartButton.setBackgroundResource(R.drawable.gray_heart);
                                isGray = true;
                                addFavoriteBar(false, clickedBar.getName());
                            }
                        }
                    });
                    horizontal.addView(heartButton);
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
        Bars currBar = favoriteBars.get(view.getId());
        intent.putExtra("title", currBar.getName());
        intent.putExtra("description", currBar.getDescription());
        intent.putExtra("latitude", currBar.getLatitude());
        intent.putExtra("longitude", currBar.getLongitude());
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public void addFavoriteBar(Boolean isAdd, String barName) {
        // add favorite to database
        if (isAdd) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    BarHopDatabase db = BarHopDatabase.getDatabase(getApplicationContext());
                    BarsDao barsDao = db.barsDao();
                    UsersDao usersDao = db.usersDao();
                    UsersFavoriteBarsDao favoriteBarsDao = db.favoritesDao();
                    Users currUser = usersDao.getUser(username);
                    Bars clickedBar = barsDao.getBar(barName);

                    if (currUser != null && clickedBar != null) {
                        UsersFavoriteBars newFav = new UsersFavoriteBars(currUser.getId(), clickedBar.getBarId());
                        favoriteBarsDao.insert(newFav);
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                }
            }.execute();
            // delete that ho
        } else {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    BarHopDatabase db = BarHopDatabase.getDatabase(getApplicationContext());
                    BarsDao barsDao = db.barsDao();
                    UsersDao usersDao = db.usersDao();
                    UsersFavoriteBarsDao favoriteBarsDao = db.favoritesDao();
                    Users currUser = usersDao.getUser(username);
                    Bars clickedBar = barsDao.getBar(barName);

                    if (currUser != null & clickedBar != null) {
                        favoriteBarsDao.delete(currUser.getId(), clickedBar.getBarId());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                }
            }.execute();
        }
    }
}