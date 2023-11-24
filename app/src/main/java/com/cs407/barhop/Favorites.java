package com.cs407.barhop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

    ImageView xIcon;
    private BarsDao barsDao;
    private UsersDao usersDao;

    private String username;

    private UsersFavoriteBarsDao usersFavoriteBarsDao;
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
                usersFavoriteBarsDao = database.favoritesDao();
                usersDao = database.usersDao();
                barsDao = database.barsDao();

                // Retrieve data after populating the database

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Handle any UI updates or further processing here
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.activity_favorites, null);
                LinearLayout ll = v.findViewById(R.id.favoritesLayout);
                Users currUser = usersDao.getUser(username);
                List<UsersFavoriteBars> favorites = usersFavoriteBarsDao.getFavoriteBars(currUser.getId());
                List<Bars> favoriteBars = new ArrayList<>();

                for (UsersFavoriteBars favorite : favorites) {
                    Bars bar = barsDao.getBarById(favorite.getBarId());
                    favoriteBars.add(bar);
                }

                Log.e("bars", favorites.toString());
                int count = 0;
                for(Bars b: favoriteBars){
                    // TODO when favorites is implemented just change if case
                    if(b.getName() == b.getName()) {
                        TextView name = new TextView(getBaseContext());
                        name.setText(b.getName());
                        name.setTextSize(34);
                        ll.addView(name);
                        LinearLayout horizontal = new LinearLayout(getBaseContext());
                        horizontal.setOrientation(LinearLayout.HORIZONTAL);
                        TextView friends = new TextView(getBaseContext());
                        friends.setText("# friends");
                        friends.setTextSize(28);
                        horizontal.addView(friends);
                        Button viewMore = new Button(getBaseContext());
                        viewMore.setText("View More");
                        viewMore.setWidth(400);
                        viewMore.setId(count);
                        viewMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewMore(v);
                            }
                        });
                        horizontal.addView(viewMore);
                        ImageButton heartButton = new ImageButton(getBaseContext());
                        heartButton.setBackgroundResource(R.drawable.gray_heart);
                        heartButton.setOnClickListener(new View.OnClickListener() {
                            boolean isGray = true;

                            @Override
                            public void onClick(View v) {
                                if (isGray) {
                                    heartButton.setBackgroundResource(R.drawable.red_heart);
                                    isGray = false;
                                } else {
                                    heartButton.setBackgroundResource(R.drawable.gray_heart);
                                    isGray = true;
                                }
                                // TODO update database on foat on god
                            }
                        });
                        horizontal.addView(heartButton);
                        ll.addView(horizontal);
                        Space space = new Space(getBaseContext());
                        space.setMinimumHeight(20);
                        ll.addView(space);
                    }
                    count++;
                }
                setContentView(v);

                xIcon = findViewById(R.id.imageViewX);
                xIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Favorites.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }.execute();
    }

    public void viewMore(View view) {
        Intent intent = new Intent(this, BarInfo.class);
        List<Bars> bars = barsDao.getAllEntities();
        Bars currBar = bars.get(view.getId());
        intent.putExtra("title", currBar.getName());
        intent.putExtra("description", currBar.getDescription());
        startActivity(intent);
    }
}