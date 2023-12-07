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
import android.os.Build;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History extends AppCompatActivity {

    public String username;
    private LinearLayout ll;
    private UsersHistoryDao usersHistoryDao;
    private UsersFavoriteBarsDao favoritesDao;
    private UsersDao usersDao;
    private BarsDao barsDao;
    private List<Bars> history;
    LocationManager locationManager;
    LocationListener locationListener;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        username = getIntent().getStringExtra("username");
        populateAndRetrieveData();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
                populateAndRetrieveData();
            }
        };

        if(Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            5,
                            locationListener
                    );
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null) {
                    updateLocationInfo(location);
                    populateAndRetrieveData();
                }
            }
        }
    }

    public void updateLocationInfo(Location location) {
        Log.e("test", "test");
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        List<Bars> bars = barsDao.getAllEntities();
        for (Bars bar : bars) {
            double barLatitude = Double.parseDouble(bar.getLatitude());
            double barLongitude = Double.parseDouble(bar.getLongitude());

            float[] results = new float[1];
            Location.distanceBetween(currentLatitude, currentLongitude, barLatitude, barLongitude, results);
            float distanceInMeters = results[0];

            double proximityThreshold = 6;
            if (distanceInMeters <= proximityThreshold) {
                updateHistoryInformation(bar);
            }
        }
        populateAndRetrieveData();
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

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                populateAndRetrieveData();
            }
        }.execute();
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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
                usersHistoryDao = database.historyDao();
                usersDao = database.usersDao();
                barsDao = database.barsDao();
                favoritesDao = database.favoritesDao();


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
                List<UsersFavoriteBars> favorites = favoritesDao.getFavoriteBars(currUser.getId());
                List<UsersHistory> userHistory = usersHistoryDao.getUsersHistory(currUser.getId());
                history = new ArrayList<>();

                for (UsersHistory currHistory : userHistory) {
                    Bars bar = barsDao.getBarById(currHistory.getBarId());
                    history.add(bar);
                }
                int count = 0;
                for(Bars b: history){
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
                    TextView date = new TextView(getBaseContext());
                    date.setText(b.getDate());
                    horizontal.addView(date);
                    UsersFavoriteBars thisBar = new UsersFavoriteBars(usersDao.getUser(username).getId(), b.getBarId());
                    barLayout.addView(horizontal);
                    Space space = new Space(getBaseContext());
                    space.setMinimumHeight(20);
                    barLayout.addView(space);
                    barLayout.setId(count);
                    count++;
                    ll.addView(barLayout);
                }
                Button clearHistory = new Button(getBaseContext());
                clearHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearHistory(username);
                    }
                });
                clearHistory.setText("Clear History");
                ll.addView(clearHistory);
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
        Bars currBar = history.get(view.getId());
        intent.putExtra("title", currBar.getName());
        intent.putExtra("description", currBar.getDescription());
        intent.putExtra("latitude", currBar.getLatitude());
        intent.putExtra("longitude", currBar.getLongitude());
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    public void clearHistory(String username) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                BarHopDatabase db = BarHopDatabase.getDatabase(getApplicationContext());
                BarsDao barsDao = db.barsDao();
                UsersDao usersDao = db.usersDao();
                UsersHistoryDao historyDao = db.historyDao();
                Users currUser = usersDao.getUser(username);

                if (currUser != null) {
                    historyDao.deleteAll(currUser.getId());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                populateAndRetrieveData();
            }
        }.execute();
    }
}