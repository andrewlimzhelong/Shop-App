package com.example.andrewspc.friends.UserProfileSettings;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.friends.Explore.ExploreModelClass;

import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyOwnProfileAdapter extends RecyclerView.Adapter<MyOwnProfileAdapter.MyViewHolder>{

    Context context;
    ArrayList<ExploreModelClass> ExploreModelClass;

    //Youtube Links : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s
    //Youtube Links : https://www.youtube.com/watch?v=Zd0TUuoPP-s
    //Youtube Links : https://www.youtube.com/watch?v=ZXoGG2XTjzU&t=64s
    //Stopped at 13:19 minutes

    /// Database Reference For retrieving stored portfolio images
    DatabaseReference reference;

    public MyOwnProfileAdapter(Context c, ArrayList<ExploreModelClass> p)
    {
        context = c;
        ExploreModelClass = p;
    }

    @NonNull
    @Override
    public MyOwnProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.my_own_posts, parent, false);
        final MyOwnProfileAdapter.MyViewHolder vHolder = new MyOwnProfileAdapter.MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyOwnProfileAdapter.MyViewHolder holder, final int position) {
        Picasso.get().load(ExploreModelClass.get(position).getPostedImage()).fit().into(holder.ServiceImageUploaded);
        holder.TitleOfItem.setText(ExploreModelClass.get(position).getTitle());
        holder.DescriptionOfPost.setText(ExploreModelClass.get(position).getDescription());
        holder.PricingDisplay.setText(ExploreModelClass.get(position).getPrice());
        holder.decimal.setText(ExploreModelClass.get(position).getDecimal());
        holder.postedServicesPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(context, MyOwnPostView.class);
                intent.putExtra("PostedImage", ExploreModelClass.get(position).getPostedImage());
                intent.putExtra("Title", ExploreModelClass.get(position).getTitle());
                intent.putExtra("Description", ExploreModelClass.get(position).getDescription());
                intent.putExtra("Price", ExploreModelClass.get(position).getPrice());
                intent.putExtra("Decimal", ExploreModelClass.get(position).getDecimal());
                intent.putExtra("UserProfileImage", ExploreModelClass.get(position).getProfilePicture());
                intent.putExtra("Username", ExploreModelClass.get(position).getUsername());
                intent.putExtra("UserID", ExploreModelClass.get(position).getUserID());
                intent.putExtra("Occupation", ExploreModelClass.get(position).getOccupation());
                intent.putExtra("ContactNumber", ExploreModelClass.get(position).getContactNumber());
                /////////////////////////////////////////////// CHAT KEYS ARE ALL HERE ////////////////////////////////////////////////
                // LINK FOR THIS CODE : https://github.com/SimCoderYoutube/WhatsAppClone/blob/master/app/src/main/java/com/simcoder/whatsappclone/MainPageActivity.java
                context.startActivity(intent);
            }
        });

        holder.postedServicesPage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.binImage.setVisibility(View.VISIBLE);
                holder.binImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final String imagePushId = ExploreModelClass.get(position).getImageUniqueKey();
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child("Profile").child(FirebaseAuth.getInstance().getUid()).child("Posts");

                        Toast.makeText(context, imagePushId, Toast.LENGTH_SHORT).show();

                        databaseReference.child(imagePushId).setValue(null);
                        FirebaseDatabase.getInstance().getReference().child("Users").child("Posts")
                                .child(imagePushId).setValue(null);
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return ExploreModelClass.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private CardView postedServicesPage;
        private ImageView binImage;
        private ImageView ServiceImageUploaded;
        private TextView TitleOfItem;
        private TextView DescriptionOfPost;
        private TextView PricingDisplay;
        private TextView decimal;

        public MyViewHolder(View itemView){
            super(itemView);
            postedServicesPage = (CardView) itemView.findViewById(R.id.postedServicesPage);
            ServiceImageUploaded = (ImageView) itemView.findViewById(R.id.ServiceImageUploaded);
            binImage = (ImageView) itemView.findViewById(R.id.binImage);
            TitleOfItem = (TextView) itemView.findViewById(R.id.TitleOfItem);
            DescriptionOfPost = (TextView) itemView.findViewById(R.id.DescriptionOfPost);
            PricingDisplay = (TextView) itemView.findViewById(R.id.PricingDisplay);
            decimal = (TextView) itemView.findViewById(R.id.decimal);
        }

    }

}