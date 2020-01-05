package com.example.andrewspc.friends.Explore;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.friends.Chats.MessagingChat;
import com.example.andrewspc.friends.GeneralViewPages.ViewFullImage;
import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostedImage extends AppCompatActivity {

    private ImageView backArrow;
    private TextView username;
    private CircleImageView UserProfilePic;
    private ImageView postingImage;
    private TextView TitleBox;
    private TextView descriptionOfItem;
    private TextView priceStated;
    private TextView decimalNumber;
    private RelativeLayout messageBtn;
    private RelativeLayout ViewProfileBtn;

    private DatabaseReference mStatusDatabase;
    String userIDOfClickedUser;
    String UserProfileImage;
    String Username;

    String chatIDVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_image);

        backArrow = findViewById(R.id.backArrow);
        username = findViewById(R.id.UsernameOfUser);
        UserProfilePic = findViewById(R.id.UserProfilePic);
        postingImage = findViewById(R.id.postingImage);
        TitleBox = findViewById(R.id.TitleBox);
        descriptionOfItem = findViewById(R.id.descriptionOfItem);
        priceStated = findViewById(R.id.priceStated);
        decimalNumber = findViewById(R.id.decimalNumber);
        messageBtn = findViewById(R.id.messageBtn);
        ViewProfileBtn = findViewById(R.id.ViewProfileBtn);

        final Intent intent = getIntent();
        String imageOfPost = intent.getExtras().getString("PostedImage");
        String titleOfPost = intent.getExtras().getString("Title");
        String Description = intent.getExtras().getString("Description");
        String pricingOfPost = intent.getExtras().getString("Price");
        String decimalOfPost = intent.getExtras().getString("Decimal");
        UserProfileImage = intent.getExtras().getString("UserProfileImage");
        Username = intent.getExtras().getString("Username");
        userIDOfClickedUser = intent.getExtras().getString("UserID");
        final String occupation = intent.getExtras().getString("Occupation");
        final String contactNumber = intent.getExtras().getString("ContactNumber");


        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openChat();
                /*
                // Passing Data to Browsed Profile Page
                final Intent intent2 = new Intent(PostedImage.this, MessagingChat.class);
                intent2.putExtra("chatIDD", chatIDVariable);
                intent2.putExtra("chatImageOfUser", UserProfileImage);
                intent2.putExtra("usernameOfOtheruser", Username);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
                */
            }
        });

        ViewProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passing Data to Browsed Profile Page
                final Intent intent2 = new Intent(PostedImage.this, BrowsedProfile.class);
                intent2.putExtra("profileImage", UserProfileImage);
                intent2.putExtra("Username", Username);
                intent2.putExtra("UserID", userIDOfClickedUser);
                intent2.putExtra("Occupation", occupation);
                intent2.putExtra("ContactNumber", contactNumber);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
            }
        });


        UserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(PostedImage.this, ViewFullImage.class);
                intent.putExtra("PostedImagePassed", UserProfileImage);
                intent.putExtra("Username", Username);
                startActivity(intent);
            }
        });

        Picasso.get().load(imageOfPost).fit().into(postingImage);
        username.setText(Username);
        Picasso.get().load(UserProfileImage).fit().into(UserProfilePic);
        TitleBox.setText(titleOfPost);
        descriptionOfItem.setText(Description);
        priceStated.setText(pricingOfPost);
        decimalNumber.setText(decimalOfPost);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openChat() {
        DatabaseReference refKey = FirebaseDatabase.getInstance().getReference().child("Users").child("Chats");

        refKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                boolean keyPresent = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String currentUsersId = snapshot.child("currentUserId").getValue().toString();
                    String selectedUsersId = snapshot.child("selecteduserId").getValue().toString();

                    if (currentUsersId.equals(FirebaseAuth.getInstance().getUid()) && selectedUsersId.equals(userIDOfClickedUser)
                            || selectedUsersId.equals(FirebaseAuth.getInstance().getUid()) && currentUsersId.equals(userIDOfClickedUser)) {

                        String presentChatId = snapshot.getKey();

                        keyPresent = true;

                        Intent intent = new Intent(PostedImage.this, MessagingChat.class);
                        intent.putExtra("chatIDD", presentChatId);
                        intent.putExtra("chatImageOfUser", UserProfileImage);
                        intent.putExtra("usernameOfOtheruser", Username);
                        startActivity(intent);
                        break;
                    }
                }

                if (!keyPresent) {
                    // Generating Chat Id of current user (yourself)
                    FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(FirebaseAuth.getInstance().getUid()).child("Chats")
                            .child(userIDOfClickedUser).child("UniqueChatID").setValue(key);
                    FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(FirebaseAuth.getInstance().getUid()).child("Chats")
                            .child(userIDOfClickedUser).child("UniqueUserID").setValue(userIDOfClickedUser);

                    // Generating Chat Id of clicked user (the other person)
                    FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(userIDOfClickedUser).child("Chats")
                            .child(FirebaseAuth.getInstance().getUid()).child("UniqueChatID").setValue(key);
                    FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(userIDOfClickedUser).child("Chats")
                            .child(FirebaseAuth.getInstance().getUid()).child("UniqueUserID").setValue(FirebaseAuth.getInstance().getUid());

                    // Generating Chat Id
                    DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Chats").child(key);
                    chatsRef.child("selecteduserId").setValue(userIDOfClickedUser);
                    chatsRef.child("currentUserId").setValue(FirebaseAuth.getInstance().getUid());

                    Intent intent = new Intent(PostedImage.this, MessagingChat.class);
                    intent.putExtra("chatIDD", key);
                    intent.putExtra("chatImageOfUser", UserProfileImage);
                    intent.putExtra("usernameOfOtheruser", Username);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
