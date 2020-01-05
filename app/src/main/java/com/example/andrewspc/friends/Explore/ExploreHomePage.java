package com.example.andrewspc.friends.Explore;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.andrewspc.friends.AccountLogin.MainLoginPage;
import com.example.andrewspc.friends.Chats.ChatsDisplay;
import com.example.andrewspc.friends.PostPage.PostActivity;
import com.example.andrewspc.friends.PostPage.PostedImages;
import com.example.andrewspc.friends.R;
import com.example.andrewspc.friends.UserProfileSettings.ProfilePage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Collections;

public class ExploreHomePage extends AppCompatActivity {

    EditText searchET;
    private ImageView logoutBtn;

    // Recyclerview
    RecyclerView recyclerView;
    ArrayList<ExploreModelClass> list;
    ThirdAdapter adapterThree;
    ArrayList<ExploreModelClass> profileList;

    // Firebase
    private DatabaseReference reference;

    // Bottom Navigation Bar
    private RelativeLayout ProfileBtn;
    private RelativeLayout ChatsBtn;
    private RelativeLayout sellBtn;
    private RelativeLayout PostsBtn;
    private static final int GALLERY_REQUEST = 1;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_home_page);

        searchET = findViewById(R.id.searchET);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Bottom Navigation Bar
        ChatsBtn = findViewById(R.id.ChatsBtn);
        ProfileBtn = findViewById(R.id.ProfileBtn);
        sellBtn = findViewById(R.id.sellBtn);
        PostsBtn = findViewById(R.id.PostsBtn);

        recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Youtube : https://www.youtube.com/watch?v=SD2t75T5RdY&t=716s
        // 3 item grid view is this below line of code
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        list = new ArrayList<ExploreModelClass>();
        // searchField = new ArrayList<ExploreModelClass>();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        ExploreModelClass p = dataSnapshot1.getValue(ExploreModelClass.class);
                        list.add(p);
                    }
                }
                Collections.reverse(list);
                adapterThree = new ThirdAdapter(ExploreHomePage.this, list, profileList);
                recyclerView.findViewHolderForAdapterPosition(5);
                recyclerView.setAdapter(adapterThree);
                adapterThree.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ExploreHomePage.this, "Error something is not right", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting up the search function
        // Youtube Tutorial : https://www.youtube.com/watch?v=PmqYd-AdmC0
        if (searchET != null) {
            searchET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    search(editable.toString());
                }
            });
        }


        /// Everything from here onwards is for the bottom navigation bar

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(ExploreHomePage.this, MainLoginPage.class);
                startActivity(loginIntent);
                finish();
            }
        });

        PostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ExploreHomePage.this, PostedImages.class);
                startActivity(loginIntent);
            }
        });

        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
            }
        });

        ChatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ExploreHomePage.this, ChatsDisplay.class);
                startActivity(loginIntent);
            }
        });

        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ExploreHomePage.this, ProfilePage.class);
                startActivity(loginIntent);
            }
        });
    }

    //Image Cropper and posting code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            filePath = data.getData();
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setMinCropResultSize(350, 350)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                final Intent intent = new Intent(ExploreHomePage.this, PostActivity.class);
                intent.putExtra("imageSelected", resultUri);
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void search(String str) {

        ArrayList<ExploreModelClass> myCurrentList = new ArrayList<>();
        for (ExploreModelClass obj : list)
        {
            if (obj.getUsername().toLowerCase().contains(str.toLowerCase())) {
                myCurrentList.add(obj);
            }
        }
        ThirdAdapter chatListAdapters = new ThirdAdapter(ExploreHomePage.this, myCurrentList, list);
        recyclerView.setAdapter(chatListAdapters);
    }
}
