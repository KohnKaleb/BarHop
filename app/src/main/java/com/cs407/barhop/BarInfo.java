package com.cs407.barhop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BarInfo extends AppCompatActivity{
    ImageView xIcon;
    public double latitude;
    public double longitude;
    public String barName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_info);

        Intent givenIntent = getIntent();
        String title = givenIntent.getStringExtra("title");
        barName = title;
        latitude = Double.parseDouble(givenIntent.getStringExtra("latitude"));
        longitude = Double.parseDouble(givenIntent.getStringExtra("longitude"));

        TextView barTitle = findViewById(R.id.barTitle);
        barTitle.setText(title);
        String description = givenIntent.getStringExtra("description");
        TextView barDesc = findViewById(R.id.barDescription);
        barDesc.setText(description);

        xIcon = findViewById(R.id.imageViewX);
        xIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bringUserToMaps(double lat, double lng, String resturantName) {
        String uri = "http://maps.google.com/maps?daddr=" + lat + "," + lng + " (" + resturantName + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // TODO they don't have google mpas installed, figure out what to do
        }
    }

}
