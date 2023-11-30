package com.cs407.barhop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class History extends AppCompatActivity {

    public ImageView xIcon;
    public String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        xIcon = findViewById(R.id.imageViewX);
        username = getIntent().getStringExtra("username");
        xIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(History.this, MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }
}