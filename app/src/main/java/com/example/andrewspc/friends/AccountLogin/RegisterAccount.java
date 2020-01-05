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

import com.example.andrewspc.friends.R;
import com.example.andrewspc.friends.SetUpProfile.SetUpProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class RegisterAccount extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPassword;
    private EditText regPassword2;
    private Button reg_btn;
    private TextView loginPageLink;

    //FIREBASE
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseDesign;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regPassword2 = findViewById(R.id.regPassword2);
        reg_btn = findViewById(R.id.createAccountBtn);
        loginPageLink = findViewById(R.id.loginPageText);

        // Used to create the new user
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = regEmail.getText().toString();
                String pass = regPassword.getText().toString();
                String confirm_pass = regPassword2.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass)){

                    if(pass.equals(confirm_pass)){

                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent setupIntent = new Intent(RegisterAccount.this, SetUpProfile.class);
                                    startActivity(setupIntent);
                                    finish();
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterAccount.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(RegisterAccount.this, "Please Confirm Password Fields Match.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterAccount.this, "Please Ensure All Fields Are Not Empty.", Toast.LENGTH_LONG).show();
                }

            }
        });

        //Clickable link below the login btn
        String text = "Already Have An Account?";

        //Clickable link below the login btn
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent mainIntent = new Intent(RegisterAccount.this, MainLoginPage.class);
                startActivity(mainIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
            }
        };

        ss.setSpan(clickableSpan, 0, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginPageLink.setText(ss);
        loginPageLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterAccount.this, MainLoginPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}
