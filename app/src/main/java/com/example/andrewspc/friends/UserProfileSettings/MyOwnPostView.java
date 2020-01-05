package com.example.andrewspc.friends.UserProfileSettings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andrewspc.friends.GeneralViewPages.ViewFullImage;
import com.example.andrewspc.friends.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOwnPostView extends AppCompatActivity {

    private ImageView backArrow;
    private TextView username;
    private CircleImageView UserProfilePic;
    private ImageView postingImage;
    private TextView TitleBox;
    private TextView descriptionOfItem;
    private TextView priceStated;
    private TextView decimalNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_own_post_view);

        backArrow = findViewById(R.id.backArrow);
        username = findViewById(R.id.UsernameOfUser);
        UserProfilePic = findViewById(R.id.UserProfilePic);
        postingImage = findViewById(R.id.postingImage);
        TitleBox = findViewById(R.id.TitleBox);
        descriptionOfItem = findViewById(R.id.descriptionOfItem);
        priceStated = findViewById(R.id.priceStated);
        decimalNumber = findViewById(R.id.decimalNumber);

        final Intent intent = getIntent();
        String imageOfPost = intent.getExtras().getString("PostedImage");
        String titleOfPost = intent.getExtras().getString("Title");
        String Description = intent.getExtras().getString("Description");
        String pricingOfPost = intent.getExtras().getString("Price");
        String decimalOfPost = intent.getExtras().getString("Decimal");
        final String UserProfileImage = intent.getExtras().getString("UserProfileImage");
        final String Username = intent.getExtras().getString("Username");
        final String userID = intent.getExtras().getString("UserID");
        final String occupation = intent.getExtras().getString("Occupation");
        final String contactNumber = intent.getExtras().getString("ContactNumber");

        UserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MyOwnPostView.this, ViewFullImage.class);
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
}
