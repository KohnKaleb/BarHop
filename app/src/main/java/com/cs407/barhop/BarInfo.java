package com.cs407.barhop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BarInfo extends AppCompatActivity{
    public double latitude;
    public double longitude;
    public String barName;

    public String username;

    ImageButton directionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_info);

        Intent givenIntent = getIntent();
        String title = givenIntent.getStringExtra("title");
        username = givenIntent.getStringExtra("username");
        directionButton = findViewById(R.id.imageButton);
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bringUserToMaps(v);
            }
        });
        barName = title;
        latitude = Double.parseDouble(givenIntent.getStringExtra("latitude"));
        longitude = Double.parseDouble(givenIntent.getStringExtra("longitude"));

        TextView barTitle = findViewById(R.id.barTitle);
        barTitle.setText(title);
        String description = givenIntent.getStringExtra("description");
        TextView barDesc = findViewById(R.id.barDescription);
        barDesc.setText(description);

    }

    private void bringUserToMaps(View view) {
        String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude + " (" + barName + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);
            // TODO they don't have google mpas installed, figure out what to do
        }
    }

}
