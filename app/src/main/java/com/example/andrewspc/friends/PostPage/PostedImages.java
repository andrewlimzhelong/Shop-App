package com.example.andrewspc.friends.PostPage;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.andrewspc.friends.Chats.ChatsDisplay;
import com.example.andrewspc.friends.Explore.ExploreHomePage;
import com.example.andrewspc.friends.Explore.ExploreModelClass;
import com.example.andrewspc.friends.R;
import com.example.andrewspc.friends.UserProfileSettings.ProfilePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Collections;

public class PostedImages extends AppCompatActivity {

    // Firebase
    private DatabaseReference reference;
    ArrayList<ExploreModelClass> list;
    ArrayList<ExploreModelClass> profileList;
    PostsAdapter adapterThree;
    // Recyclerview
    RecyclerView recyclerView;

    // Bottom Navigation Icons
    private EditText searchET;
    private RelativeLayout ExploreBtn;
    private RelativeLayout sellBtn;
    private RelativeLayout ChatsBtn;
    private RelativeLayout ProfileBtn;

    private static final int GALLERY_REQUEST = 1;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_images);

        searchET = findViewById(R.id.searchET);
        recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Youtube : https://www.youtube.com/watch?v=SD2t75T5RdY&t=716s
        // 3 item grid view is this below line of code
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        list = new ArrayList<ExploreModelClass>();

        // Bottom Navigation Icons
        ExploreBtn = findViewById(R.id.ExploreBtn);
        sellBtn = findViewById(R.id.sellBtn);
        ChatsBtn = findViewById(R.id.ChatsBtn);
        ProfileBtn = findViewById(R.id.ProfileBtn);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Posts");
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
                adapterThree = new PostsAdapter(PostedImages.this, list, profileList);
                recyclerView.findViewHolderForAdapterPosition(5);
                recyclerView.setAdapter(adapterThree);
                adapterThree.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PostedImages.this, "Error something is not right", Toast.LENGTH_SHORT).show();
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

        ExploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(PostedImages.this, ExploreHomePage.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i2);
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
                Intent i3 = new Intent(PostedImages.this, ChatsDisplay.class);
                i3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i3);
            }
        });

        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(PostedImages.this, ProfilePage.class);
                i5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i5);
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

                final Intent intent = new Intent(PostedImages.this, PostActivity.class);
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
            if (obj.getTitle().toLowerCase().contains(str.toLowerCase())) {
                myCurrentList.add(obj);
            }
        }
        PostsAdapter postsAdapter = new PostsAdapter(PostedImages.this, myCurrentList, list);
        recyclerView.setAdapter(postsAdapter);
    }
}
