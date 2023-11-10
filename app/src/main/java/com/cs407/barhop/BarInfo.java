package com.cs407.barhop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BarInfo extends AppCompatActivity{
    ImageView xIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_info);

        Intent givenIntent = getIntent();
        String s = givenIntent.getStringExtra("barId");
        TextView barTitle = findViewById(R.id.barTitle);
        barTitle.setText(s + "\nis the bar you clicked on. " +
                "We need to fill this page out still but its good to know they're each unique right?");

        xIcon = findViewById(R.id.imageViewX);
        xIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BarInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
