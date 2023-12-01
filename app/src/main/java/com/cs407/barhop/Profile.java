package com.cs407.barhop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
//    ImageView xIcon;
    TextView username;
    private UsersDao usersDao;

    private String currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.username);

        currUser = getIntent().getStringExtra("username");

        username.setText("Username: " + currUser);

        // Saved here just incase we need it back
//        xIcon = findViewById(R.id.imageViewX);
//        xIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Profile.this, MainActivity.class);
//                intent.putExtra("username", currUser);
//                startActivity(intent);
//            }
//        });
    }
}