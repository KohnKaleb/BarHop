package com.cs407.barhop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    private BarHopDatabase barhopDatabase;
    private BarsDao barsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateAndRetrieveData();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
        };

        if(Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null) {
                    updateLocationInfo(location);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void populateAndRetrieveData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Get a reference to your database instance
                BarHopDatabase database = BarHopDatabase.getDatabase(getApplicationContext());

                // Access the DAO
                barsDao = database.barsDao();
                barsDao.deleteAll();
                // Check if the database is empty
                if (barsDao.getAllEntities().size() == 0) {
                    // If empty, populate the database

                    // All below can be deleted after we decide the database defined
                    // below has all necessary components
                    Bars redRock = new Bars();
                    redRock.setDescription("cool");
                    redRock.setName("Red Rock Saloon");
                    barsDao.insert(redRock);

                    Bars whiskeys = new Bars();
                    whiskeys.setDescription("cool");
                    whiskeys.setName("Whiskey Jack's Saloon");
                    barsDao.insert(whiskeys);

                    Bars chasers = new Bars();
                    chasers.setDescription("cool");
                    chasers.setName("Chasers");
                    barsDao.insert(chasers);

                    Bars mondays = new Bars();
                    mondays.setDescription("cool");
                    mondays.setName("Mondays");
                    barsDao.insert(mondays);

                    Bars dannys = new Bars();
                    dannys.setDescription("cool");
                    dannys.setName("Danny's Pub");
                    barsDao.insert(dannys);

                    Bars mackeseys = new Bars();
                    mackeseys.setDescription("cool");
                    mackeseys.setName("Mackesey's Irish Pub");
                    barsDao.insert(mackeseys);

                    Bars plaza = new Bars();
                    plaza.setDescription("cool");
                    plaza.setName("The Plaza Tavern");
                    barsDao.insert(plaza);

                    Bars cask = new Bars();
                    cask.setDescription("cool");
                    cask.setName("Cask & Ale");
                    barsDao.insert(cask);

                    Bars pauls = new Bars();
                    pauls.setDescription("cool");
                    pauls.setName("Paul's Club");
                    barsDao.insert(pauls);

                    Bars silver = new Bars();
                    silver.setDescription("cool");
                    silver.setName("Silver Dollar Tavern");
                    barsDao.insert(silver);

                    Bars coopers = new Bars();
                    coopers.setDescription("cool");
                    coopers.setName("The Coopers Tavern");
                    barsDao.insert(coopers);

                    Bars gennas = new Bars();
                    gennas.setDescription("cool");
                    gennas.setName("Genna's Lounge");
                    barsDao.insert(gennas);

                    Bars paradise = new Bars();
                    paradise.setDescription("cool");
                    paradise.setName("Paradise Lounge");
                    barsDao.insert(redRock);
                }

                // Retrieve data after populating the database

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Handle any UI updates or further processing here
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.activity_main, null);
                ScrollView sv = v.findViewById(R.id.scrollView);
                LinearLayout ll = new LinearLayout(getBaseContext());
                ll.setOrientation(LinearLayout.VERTICAL);
                List<Bars> bars = barsDao.getAllEntities();
                int count = 0;
                for(Bars b: bars){
                    Log.e("please", b.getName());
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
//                    Button favorite = new Button(getBaseContext());
//                    favorite.setBackground("@drawable/baseline_favorite_24");
                    ll.addView(horizontal);
                    Space space = new Space(getBaseContext());
                    space.setMinimumHeight(20);
                    ll.addView(space);
                    count++;
                }
                sv.addView(ll);
                setContentView(v);
            }
        }.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent intent = null;
        if (itemId == R.id.favorites) {
            intent = new Intent(this, Favorites.class);
        } else if (itemId == R.id.history) {
            intent = new Intent(this, History.class);
        } else if (itemId == R.id.profile) {
            intent = new Intent(this, Profile.class);
        } else if (itemId == R.id.friends) {
            intent = new Intent(this, Friends.class);
        }

        startActivity(intent);

        return true;
    }

    public void viewMore(View view) {
        Intent intent = new Intent(this, BarInfo.class);
        List<Bars> bars = barsDao.getAllEntities();
        Bars currBar = bars.get(view.getId());
        intent.putExtra("title", currBar.getName());
        intent.putExtra("description", currBar.getDescription());
        startActivity(intent);
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void updateLocationInfo(Location location) {

        // TODO: do stuff with the location of the user
        // here is where we will do many fun and interesting things with the users location data!
        // see lab 4 milestone 2 for example


    }
}