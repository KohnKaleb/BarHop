package com.cs407.barhop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        System.out.println(view.getTag());
        intent.putExtra("barId", view.getTag().toString());
        startActivity(intent);
    }
}