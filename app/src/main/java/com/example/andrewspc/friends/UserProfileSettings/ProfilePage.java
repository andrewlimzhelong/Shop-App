package com.example.andrewspc.friends.UserProfileSettings;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.friends.Chats.ChatsDisplay;
import com.example.andrewspc.friends.Explore.ExploreHomePage;
import com.example.andrewspc.friends.Explore.ExploreModelClass;
import com.example.andrewspc.friends.PostPage.PostActivity;
import com.example.andrewspc.friends.PostPage.PostedImages;
import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Collections;

public class ProfilePage extends AppCompatActivity {

    private ImageView editProfileBtn;
    private ImageView DisplayPic;
    private TextView userSavedName;
    private TextView userSavedSkills;
    private TextView Hpnum;
    RecyclerView myRecycler;
    private RelativeLayout ExploreBtn;
    private RelativeLayout PostsBtn;
    private RelativeLayout sellBtn;
    private RelativeLayout ChatsBtn;

    ArrayList<ExploreModelClass> list;

    //Firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference reference;
    private static final int GALLERY_PICK = 1;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference storageReference;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        editProfileBtn = findViewById(R.id.editProfileBtn);
        DisplayPic = findViewById(R.id.DisplayPic);
        userSavedName = findViewById(R.id.userSavedName);
        userSavedSkills = findViewById(R.id.userSavedSkills);
        Hpnum = findViewById(R.id.Hpnum);

        myRecycler = findViewById(R.id.myRecycler);

        // Bottom Navigation Bar
        ExploreBtn = findViewById(R.id.ExploreBtn);
        PostsBtn = findViewById(R.id.PostsBtn);
        sellBtn = findViewById(R.id.sellBtn);
        ChatsBtn = findViewById(R.id.ChatsBtn);

        list = new ArrayList<ExploreModelClass>();

        myRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Youtube : https://www.youtube.com/watch?v=SD2t75T5RdY&t=716s
        // 3 item grid view is this below line of code
        myRecycler.setLayoutManager(new GridLayoutManager(this, 3));



        ////////////// Firebase Image uploading database reference
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        //Firebase instances
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        getUserPosts();

        //Referencing to specific value in the firebase database
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(current_uid);

        EditProfilePage editProfilePage = new EditProfilePage();
        editProfilePage.retrievingPicture("ProfilePicture",DisplayPic, mStatusDatabase);
        editProfilePage.retrivingDataFromFirebaseTV("Username", userSavedName, mStatusDatabase);
        editProfilePage.retrivingDataFromFirebaseTV("Occupation", userSavedSkills, mStatusDatabase);
        editProfilePage.retrivingDataFromFirebaseTV("ContactNumber", Hpnum, mStatusDatabase);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ProfilePage.this, EditProfilePage.class);
                startActivity(loginIntent);
            }
        });

        ExploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ProfilePage.this, ExploreHomePage.class);
                startActivity(loginIntent);
            }
        });

        PostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ProfilePage.this, PostedImages.class);
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
            public void onClick(View view) {
                Intent i3 = new Intent(ProfilePage.this, ChatsDisplay.class);
                i3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i3);
            }
        });
    }

    public void getUserPosts() {
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(FirebaseAuth.getInstance().getUid()).child("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ExploreModelClass p = dataSnapshot1.getValue(ExploreModelClass.class);
                    list.add(p);
                }
                Collections.reverse(list);
                MyOwnProfileAdapter myOwnProfileAdapter = new MyOwnProfileAdapter(ProfilePage.this, list);
                myRecycler.setAdapter(myOwnProfileAdapter);
                myOwnProfileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfilePage.this, "Error something is not right", Toast.LENGTH_SHORT).show();
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

                final Intent intent = new Intent(ProfilePage.this, PostActivity.class);
                intent.putExtra("imageSelected", resultUri);
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
