package com.cs407.barhop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
//    ImageView xIcon;
    TextView username;

    Button button;
    private UsersDao usersDao;

    private String currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        button = (Button) findViewById(R.id.changePhoto);

        username = findViewById(R.id.username);

        currUser = getIntent().getStringExtra("username");

        username.setText(currUser);

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the Supabutton");

                // Include only one of the following calls to launch(), depending on the types
                // of media that you want to let the user choose from.

                // Launch the photo picker and let the user choose images and videos.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());

                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

                // Launch the photo picker and let the user choose only videos.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                        .build());

                // Launch the photo picker and let the user choose only images/videos of a
                // specific MIME type, such as GIFs.
                String mimeType = "image/gif";
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType))
                        .build());

            }
        });

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