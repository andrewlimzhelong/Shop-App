package com.example.andrewspc.friends.PostPage;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrewspc.friends.Chats.ChatObject;
import com.example.andrewspc.friends.Chats.MessagingChat;
import com.example.andrewspc.friends.Explore.PostedImage;
import com.example.andrewspc.friends.Explore.ExploreModelClass;
import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    String keyOfChat;
    private boolean match = false;
    private boolean noMatch = false;
    boolean tfBool = true;

    private String userName;

    private DatabaseReference mDatabaseRef2;

    Context context;
    ArrayList<ExploreModelClass> ExploreModelClass;

    //Youtube Links : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s
    //Youtube Links : https://www.youtube.com/watch?v=Zd0TUuoPP-s
    //Youtube Links : https://www.youtube.com/watch?v=ZXoGG2XTjzU&t=64s
    //Stopped at 13:19 minutes

    //Firebase Database
    private FirebaseUser mCurrentUser;

    //Image Retrieve
    private StorageReference mImageStorage;

    private Button newPost;

    ArrayList<ChatObject> chatList;

    /// Database Reference For retrieving stored portfolio images
    DatabaseReference reference;
    RecyclerView recyclerView;

    ArrayList<ExploreModelClass> profileInfo;

    public PostsAdapter(Context c, ArrayList<ExploreModelClass> p, ArrayList<ExploreModelClass> pf) {
        context = c;
        ExploreModelClass = p;
        profileInfo = pf;
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

        Picasso.get().load(ExploreModelClass.get(position).getPostedImage()).fit().into(holder.userProfilePic);
        holder.TitleOfItem.setText(ExploreModelClass.get(position).getTitle());
        holder.DescriptionOfPost.setText(ExploreModelClass.get(position).getDescription());
        holder.PricingDisplay.setText(ExploreModelClass.get(position).getPrice());
        holder.decimal.setText(ExploreModelClass.get(position).getDecimal());

        holder.userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(context, PostedImage.class);
                intent.putExtra("PostedImage", ExploreModelClass.get(position).getPostedImage());
                intent.putExtra("Title", ExploreModelClass.get(position).getTitle());
                intent.putExtra("Description", ExploreModelClass.get(position).getDescription());
                intent.putExtra("Price", ExploreModelClass.get(position).getPrice());
                intent.putExtra("Decimal", ExploreModelClass.get(position).getDecimal());
                intent.putExtra("Username", ExploreModelClass.get(position).getUsername());
                intent.putExtra("UserID", ExploreModelClass.get(position).getUserID());
                intent.putExtra("UserProfileImage", ExploreModelClass.get(position).getProfilePicture());

                final Intent intent2 = new Intent(context, MessagingChat.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ExploreModelClass.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView userProfilePic;
        private TextView TitleOfItem;
        private TextView DescriptionOfPost;
        private TextView PricingDisplay;
        private TextView decimal;

        public MyViewHolder(View itemView) {
            super(itemView);
            userProfilePic = (ImageView) itemView.findViewById(R.id.ServiceImageUploaded);
            TitleOfItem = (TextView) itemView.findViewById(R.id.TitleOfItem);
            DescriptionOfPost = (TextView) itemView.findViewById(R.id.DescriptionOfPost);
            PricingDisplay = (TextView) itemView.findViewById(R.id.PricingDisplay);
            decimal = (TextView) itemView.findViewById(R.id.decimal);
        }
    }
}

