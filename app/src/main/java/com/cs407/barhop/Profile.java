package com.cs407.barhop;

import static android.media.ThumbnailUtils.createImageThumbnail;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.activity.result.ActivityResultCallback;


public class Profile extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    //    ImageView xIcon;
    TextView username;

    Button button;
    private UsersDao usersDao;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int REQUEST_IMAGE_PICK = 2;

    private Uri imageUri; // To store the selected image URI
    private ImageView imageView; // Reference to the ImageView in your layout
    private ImageView imageView2;


    private String currUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        button = (Button) findViewById(R.id.changePhoto);

        username = findViewById(R.id.username);

        currUser = getIntent().getStringExtra("username");

        imageView = findViewById(R.id.imageView);

        username.setText(currUser);

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        //imageView.setImageResource(R.drawable.badger);
                        imageView.setImageURI(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
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