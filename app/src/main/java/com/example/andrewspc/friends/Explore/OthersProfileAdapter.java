package com.example.andrewspc.friends.Explore;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrewspc.friends.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OthersProfileAdapter extends RecyclerView.Adapter<OthersProfileAdapter.MyViewHolder>{

    Context context;
    ArrayList<ExploreModelClass> ExploreModelClass;

    //Youtube Links : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s
    //Youtube Links : https://www.youtube.com/watch?v=Zd0TUuoPP-s
    //Youtube Links : https://www.youtube.com/watch?v=ZXoGG2XTjzU&t=64s
    //Stopped at 13:19 minutes

    /// Database Reference For retrieving stored portfolio images
    DatabaseReference reference;

    public OthersProfileAdapter(Context c, ArrayList<ExploreModelClass> p)
    {
        context = c;
        ExploreModelClass = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.postimagescardview, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Picasso.get().load(ExploreModelClass.get(position).getPostedImage()).fit().into(holder.ServiceImageUploaded);
        holder.TitleOfItem.setText(ExploreModelClass.get(position).getTitle());
        holder.DescriptionOfPost.setText(ExploreModelClass.get(position).getDescription());
        holder.PricingDisplay.setText(ExploreModelClass.get(position).getPrice());
        holder.decimal.setText(ExploreModelClass.get(position).getDecimal());
        holder.postedServicesPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(context, PostedImage.class);
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
    }

    @Override
    public int getItemCount() {
        return ExploreModelClass.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private CardView postedServicesPage;
        private ImageView ServiceImageUploaded;
        private TextView TitleOfItem;
        private TextView DescriptionOfPost;
        private TextView PricingDisplay;
        private TextView decimal;

        public MyViewHolder(View itemView){
            super(itemView);
            postedServicesPage = (CardView) itemView.findViewById(R.id.postedServicesPage);
            ServiceImageUploaded = (ImageView) itemView.findViewById(R.id.ServiceImageUploaded);
            TitleOfItem = (TextView) itemView.findViewById(R.id.TitleOfItem);
            DescriptionOfPost = (TextView) itemView.findViewById(R.id.DescriptionOfPost);
            PricingDisplay = (TextView) itemView.findViewById(R.id.PricingDisplay);
            decimal = (TextView) itemView.findViewById(R.id.decimal);
        }

    }

}