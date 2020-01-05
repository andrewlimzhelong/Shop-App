package com.example.andrewspc.friends.AccountLogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.andrewspc.friends.Explore.ExploreHomePage;
import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartLoadingPage extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseDesign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_loading_page);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            sendToMain();
        }
        if(user == null){
            Intent loginIntent = new Intent(StartLoadingPage.this, MainLoginPage.class);
            startActivity(loginIntent);
            finish();
        }
    }

    private void sendToMain() {

        //Storage Reference for uploading of images
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = mCurrentUser.getUid();

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(current_uid);

        // For Checking if a particular data is present in firebase database in this case we are checking if the child "name" exists in the database.
        mDatabaseDesign.addListenerForSingleValueEvent(new ValueEventListener() {

            boolean dataEmpty = true;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Username") && snapshot.hasChild("Occupation") && snapshot.hasChild("ContactNumber")) {
                    Intent mainIntent = new Intent(StartLoadingPage.this, ExploreHomePage.class);
                    startActivity(mainIntent);
                    finish();
                    dataEmpty = false;
                }

                if(dataEmpty){
                    Intent mainIntent = new Intent(StartLoadingPage.this, MainLoginPage.class);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
