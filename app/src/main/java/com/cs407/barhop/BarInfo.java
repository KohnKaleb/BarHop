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
        String title = givenIntent.getStringExtra("title");
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
}
