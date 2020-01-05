package com.example.andrewspc.friends.Explore;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrewspc.friends.Chats.ChatObject;
import com.example.andrewspc.friends.Chats.MessagingChat;
import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ThirdAdapter extends RecyclerView.Adapter<ThirdAdapter.MyViewHolder> {

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

    /////////////////// Setting Up the Timer /////////////////
    private static final long START_TIME_IN_MILLIS = 600000;
    private TextView mTextViewCountDown;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    /////////////////// Setting Up the Timer /////////////////

    public ThirdAdapter(Context c, ArrayList<ExploreModelClass> p, ArrayList<ExploreModelClass> pf) {
        context = c;
        ExploreModelClass = p;
        profileInfo = pf;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.postedservicecardview, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.Username.setText(ExploreModelClass.get(position).getUsername());
        Picasso.get().load(ExploreModelClass.get(position).getProfilePicture()).fit().into(holder.userProfilePic);

        holder.userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(context, BrowsedProfile.class);
                intent.putExtra("profileImage", ExploreModelClass.get(position).getProfilePicture());
                intent.putExtra("Username", ExploreModelClass.get(position).getUsername());
                intent.putExtra("UserID", ExploreModelClass.get(position).getUserID());
                intent.putExtra("Occupation", ExploreModelClass.get(position).getOccupation());
                intent.putExtra("ContactNumber", ExploreModelClass.get(position).getContactNumber());

                /////////////////////////////////////////////// CHAT KEYS ARE ALL HERE ////////////////////////////////////////////////
                // LINK FOR THIS CODE : https://github.com/SimCoderYoutube/WhatsAppClone/blob/master/app/src/main/java/com/simcoder/whatsappclone/MainPageActivity.java
                String ImageOfUser = ExploreModelClass.get(position).getProfilePicture();
                String Name1 = ExploreModelClass.get(position).getUsername();
                String userID = ExploreModelClass.get(position).getUserID();
                //String chatID = ExploreModelClass.get(position).getUserID();

                intent.putExtra("ProfilePicture", ImageOfUser);
                intent.putExtra("Username", Name1);
                intent.putExtra("UserID", userID);
                //intent.putExtra("userChatId", chatID);

                final Intent intent2 = new Intent(context, MessagingChat.class);
                //intent2.putExtra("userChatId", chatID);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return ExploreModelClass.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // private CardView specificService;
        private TextView Username;
        private ImageView userProfilePic;

        public MyViewHolder(View itemView) {
            super(itemView);

            userProfilePic = (ImageView) itemView.findViewById(R.id.ServiceImageUploaded);
            Username = (TextView) itemView.findViewById(R.id.Username);
            // specificService = (CardView) itemView.findViewById(R.id.postedServicesPage);
        }
    }
}