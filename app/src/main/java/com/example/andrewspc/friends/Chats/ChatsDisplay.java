package com.example.andrewspc.friends.Chats;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.andrewspc.friends.Explore.ExploreHomePage;
import com.example.andrewspc.friends.PostPage.PostActivity;
import com.example.andrewspc.friends.PostPage.PostedImages;
import com.example.andrewspc.friends.UserProfileSettings.ProfilePage;
import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class ChatsDisplay extends AppCompatActivity {

    EditText searchET;

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;

    ArrayList<ChatObject> chatList;
    ArrayList<ChatObject> profileList;
    ChatListAdapters ChatAdapterForChatList;

    private RelativeLayout sellButton;
    private static final int GALLERY_REQUEST = 1;
    private Uri filePath;

    // Bottom Navigation Icons
    private RelativeLayout ExploreBtn;
    private RelativeLayout PostsBtn;
    private RelativeLayout sellBtn;
    private RelativeLayout ProfileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_display);

        searchET = findViewById(R.id.searchET);

        // Bottom Navigation Icons
        ExploreBtn = findViewById(R.id.ExploreBtn);
        PostsBtn = findViewById(R.id.PostsBtn);
        sellBtn = findViewById(R.id.sellBtn);
        ProfileBtn = findViewById(R.id.ProfileBtn);

        chatList = new ArrayList<ChatObject>();
        mChatList = (RecyclerView) findViewById(R.id.chatListOfChat);
        mChatList.setLayoutManager( new LinearLayoutManager(this));

        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile")
                .child(FirebaseAuth.getInstance().getUid()).child("Chats");

        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                        if (dataSnapshot1.child("UniqueUserID").getValue() != null){

                            String UniqueUserID = dataSnapshot1.child("UniqueUserID").getValue().toString();
                            final DatabaseReference userChatInfo = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile")
                                    .child(UniqueUserID);

                            userChatInfo.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ChatObject chatobj = dataSnapshot.getValue(ChatObject.class);
                                    chatList.add(chatobj);
                                    ChatAdapterForChatList = new ChatListAdapters(ChatsDisplay.this, chatList, profileList);
                                    mChatList.setAdapter(ChatAdapterForChatList);
                                    ChatAdapterForChatList.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                Intent i2 = new Intent(ChatsDisplay.this, ExploreHomePage.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i2);
            }
        });

        PostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ChatsDisplay.this, PostedImages.class);
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

        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(ChatsDisplay.this, ProfilePage.class);
                i5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i5);
            }
        });

    }

    public void search(String str) {

        ArrayList<ChatObject> myCurrentList = new ArrayList<>();
        for (ChatObject obj : chatList)
        {
            if (obj.getUsername().toLowerCase().contains(str.toLowerCase())) {
                myCurrentList.add(obj);
            }
        }
        ChatListAdapters chatListAdapters = new ChatListAdapters(ChatsDisplay.this, myCurrentList, chatList);
        mChatList.setAdapter(chatListAdapters);
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

                final Intent intent = new Intent(ChatsDisplay.this, PostActivity.class);
                intent.putExtra("imageSelected", resultUri);
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
