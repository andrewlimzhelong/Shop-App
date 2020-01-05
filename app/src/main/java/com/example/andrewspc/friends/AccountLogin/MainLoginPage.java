package com.example.andrewspc.friends.AccountLogin;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.friends.Explore.ExploreHomePage;
import com.example.andrewspc.friends.R;
import com.example.andrewspc.friends.SetUpProfile.SetUpProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainLoginPage extends AppCompatActivity {

    private EditText emailTextBox;
    private EditText passwordTextBox;
    private Button login_btn;
    private TextView loginPageText;

    //FIREBASE
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseDesign;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login_page);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        emailTextBox = findViewById(R.id.emailTextBox);
        passwordTextBox = findViewById(R.id.passwordTextBox);
        login_btn = findViewById(R.id.login_btn);
        loginPageText = findViewById(R.id.loginPageText);

        //Code for the login btn
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = emailTextBox.getText().toString();
                String loginPassword = passwordTextBox.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)){

                    // This line below help to check if the enter username and password tallies with the one registered and if yes, the user can login into the app.
                    mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                sendToMain();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(MainLoginPage.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(MainLoginPage.this, "Please Ensure All Fields Are Filled.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Clickable link below the login btn
        String text = "Sign Up";
        //Clickable link below the login btn
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent mainIntentRegPage = new Intent(MainLoginPage.this, RegisterAccount.class);
                startActivity(mainIntentRegPage);
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
            }
        };

        ss.setSpan(clickableSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginPageText.setText(ss);
        loginPageText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void sendToMain() {

        //Storage Reference for uploading of images
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(current_uid);

        // For Checking if a particular data is present in firebase database in this case we are checking if the child "name" exists in the database.
        mDatabaseDesign.addListenerForSingleValueEvent(new ValueEventListener() {

            boolean dataEmpty = true;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("UniqueName") && snapshot.hasChild("HPcontact")) {

                    Intent mainIntent = new Intent(MainLoginPage.this, ExploreHomePage.class);
                    startActivity(mainIntent);
                    finish();
                    dataEmpty = false;
                }

                if(dataEmpty){
                    Intent mainIntent = new Intent(MainLoginPage.this, SetUpProfile.class);
                    startActivity(mainIntent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
