package com.cs407.barhop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private BarHopDatabase barhopDatabase;
    private BarsDao barsDao;
    private UsersFavoriteBarsDao favoriteBarsDao;
    private UsersDao usersDao;
    private String username;
    private LinearLayout ll;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    private static final long MIN_TIME_BETWEEN_UPDATES = 5 * 60000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");
        populateAndRetrieveData();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener
                    );
                }
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
                favoriteBarsDao = database.favoritesDao();
                usersDao = database.usersDao();
                // Check if the database is empty
                if (barsDao.getAllEntities().size() == 0) {
                    // If empty, populate the database

                    // All below can be deleted after we decide the database defined
                    // below has all necessary components
                    Bars redRock = new Bars();
                    redRock.setDescription("Vibrant late-night saloon features a full bar, classic BBQ meals & live country music every Friday.");
                    redRock.setName("Red Rock Saloon");
                    redRock.setLatitude(String.valueOf(43.0751));
                    redRock.setLongitude(String.valueOf(-89.3918));
                    barsDao.insert(redRock);
                    Bars whiskeys = new Bars();
                    whiskeys.setDescription("Wild West-theme saloon with big dance floor, mechanical bull & live music plus many beers on draft.");
                    whiskeys.setName("Whiskey Jack's Saloon");
                    whiskeys.setLatitude(String.valueOf(43.0750));
                    whiskeys.setLongitude(String.valueOf(-89.3948));
                    barsDao.insert(whiskeys);

                    Bars chasers = new Bars();
                    chasers.setDescription("High-energy, college sports bar with a 3rd floor rooftop patio. Join us at Chasers 2.0 for a party good time.");
                    chasers.setName("Chasers");
                    chasers.setLatitude(String.valueOf(43.0740));
                    chasers.setLongitude(String.valueOf(-89.3932));
                    barsDao.insert(chasers);

                    Bars mondays = new Bars();
                    mondays.setDescription("Known for its strong drinks, this watering hole offers a lively atmosphere in an unassuming setting.");
                    mondays.setName("Mondays");
                    mondays.setLatitude(String.valueOf(43.0747));
                    mondays.setLongitude(String.valueOf(-89.3946));
                    barsDao.insert(mondays);

                    Bars dannys = new Bars();
                    dannys.setDescription("Irish Pub style bar serving beer & spirits as well as burgers, sandwiches, salads, and comfort food.");
                    dannys.setName("Danny's Pub");
                    dannys.setLatitude(String.valueOf(43.0744));
                    dannys.setLongitude(String.valueOf(-89.3926));
                    barsDao.insert(dannys);

                    Bars mackeseys = new Bars();
                    mackeseys.setDescription("Old-school Irish tavern in a vintage building offering draft beer & a loaded jukebox.\n");
                    mackeseys.setName("Mackesey's Irish Pub");
                    mackeseys.setLatitude(String.valueOf(43.0747));
                    mackeseys.setLongitude(String.valueOf(-89.3909));
                    barsDao.insert(mackeseys);

                    Bars plaza = new Bars();
                    plaza.setDescription("Old-time bar & grill slinging signature burgers & beer until late, with daily drink specials.");
                    plaza.setName("The Plaza Tavern");
                    plaza.setLatitude(String.valueOf(43.0655));
                    plaza.setLongitude(String.valueOf(-89.4022));
                    barsDao.insert(plaza);

                    Bars cask = new Bars();
                    cask.setDescription("Casual cocktail bar in pared-down quarters features a big whiskey menu & rotating craft taps.");
                    cask.setName("Cask & Ale");
                    cask.setLatitude(String.valueOf(43.0748));
                    cask.setLongitude(String.valueOf(-89.3886));
                    barsDao.insert(cask);

                    Bars pauls = new Bars();
                    pauls.setDescription("Lively watering hole boasting a large tree inside offering draft beers & a jukebox.");
                    pauls.setName("Paul's Club");
                    pauls.setLatitude(String.valueOf(43.0750));
                    pauls.setLongitude(String.valueOf(-89.3884));
                    barsDao.insert(pauls);

                    Bars silver = new Bars();
                    silver.setDescription("Cash-only neighborhood tavern offering drinks & shuffleboard in a cozy, old-school atmosphere.");
                    silver.setName("Silver Dollar Tavern");
                    silver.setLatitude(String.valueOf(43.0743));
                    silver.setLongitude(String.valueOf(-89.3869));
                    barsDao.insert(silver);

                    Bars coopers = new Bars();
                    coopers.setDescription("Upscale Irish tavern serving pub grub & international beer & wine in a warm, contemporary venue.");
                    coopers.setName("The Coopers Tavern");
                    coopers.setLatitude(String.valueOf(43.0751));
                    coopers.setLongitude(String.valueOf(-89.3862));
                    barsDao.insert(coopers);

                    Bars gennas = new Bars();
                    gennas.setDescription("Rollicking haunt offering a capitol view from its outdoor patio, plus cocktails & a rotating tap.");
                    gennas.setName("Genna's Lounge");
                    gennas.setLatitude(String.valueOf(43.0728));
                    gennas.setLongitude(String.valueOf(-89.3844));
                    barsDao.insert(gennas);

                    Bars paradise = new Bars();
                    paradise.setDescription("Classic bar with a pool table serving burgers & sandwiches amid wood-paneled walls & casual digs.");
                    paradise.setName("Paradise Lounge");
                    paradise.setLatitude(String.valueOf(43.0724));
                    paradise.setLongitude(String.valueOf(-89.3847));
                    barsDao.insert(paradise);
                }

                // Retrieve data after populating the database

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Handle any UI updates or further processing here
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.activity_main, null);
                ll = v.findViewById(R.id.parentLayout);
                List<Bars> bars = barsDao.getAllEntities();
                int count = 0;
                for(Bars b: bars){
                    count = generateBar(ll, b, count);
                }
                setContentView(v);
            }
        }.execute();
    }

    protected int generateBar(LinearLayout linLay, Bars b, int count) {
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
        List<UsersFavoriteBars> favorites = favoriteBarsDao.getFavoriteBars(usersDao.getUser(username).getId());
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
                List<UsersFavoriteBars> favorites = favoriteBarsDao.getFavoriteBars(usersDao.getUser(username).getId());
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
        linLay.addView(barLayout);
        return count;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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
                for(int i = 0; i < barsDao.getAllEntities().size(); i++) {
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
        } else if (itemId == R.id.logout) {
            intent = new Intent(this, UserLogin.class);
        }

        intent.putExtra("username", username);
        startActivity(intent);

        return true;
    }

    public void viewMore(View view) {
        Intent intent = new Intent(this, BarInfo.class);
        List<Bars> bars = barsDao.getAllEntities();
        Bars currBar = bars.get(view.getId());
        intent.putExtra("title", currBar.getName());
        intent.putExtra("description", currBar.getDescription());
        intent.putExtra("latitude", currBar.getLatitude());
        intent.putExtra("longitude", currBar.getLongitude());
        intent.putExtra("username", username);
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
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        List<Bars> bars = barsDao.getAllEntities();
        for (Bars bar : bars) {
            double barLatitude = Double.parseDouble(bar.getLatitude());
            double barLongitude = Double.parseDouble(bar.getLongitude());

            float[] results = new float[1];
            Location.distanceBetween(currentLatitude, currentLongitude, barLatitude, barLongitude, results);
            float distanceInMeters = results[0];

            double proximityThreshold = 10;
            if (distanceInMeters <= proximityThreshold) {
                updateHistoryInformation(bar);
            }
        }
        // TODO: do stuff with the location of the user
        // here is where we will do many fun and interesting things with the users location data!
        // see lab 4 milestone 2 for example
    }

    @SuppressLint("StaticFieldLeak")
    public void updateHistoryInformation(Bars bar) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                BarHopDatabase database = BarHopDatabase.getDatabase(getApplicationContext());
                UsersHistoryDao usersHistoryDao = database.historyDao();
                UsersDao usersDao = database.usersDao();
                UsersHistory userHistory = new UsersHistory();
                int userId = usersDao.getUser(username).getId();
                Date currentDate = new Date();
                String currDate = sdf.format(currentDate);
                bar.setDate(currDate);
                barsDao.updateBar(bar);
                userHistory.setUserId(userId);
                userHistory.setBarId(bar.getBarId());
                usersHistoryDao.insert(userHistory);
                Log.e("test", String.valueOf(usersHistoryDao.getUsersHistory(userId).size()));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void addFavoriteBar(Boolean isAdd, String barName) {
        // add favorite to database
        if (isAdd) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
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