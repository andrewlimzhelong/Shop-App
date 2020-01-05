package com.example.andrewspc.friends.Chats;

import android.content.Intent;
import android.media.session.MediaSession;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.friends.GeneralViewPages.ViewFullImage;
import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingChat extends AppCompatActivity {

    // FOR RECYCLER VIEW
    //Youtube Link : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s (recycler view)

    //FOR CHATING WITH OTHER USERS LOOK AT HE BELOW LINKS (EPISODES: 8 , 9 , 10)
    //Youtube Link : https://www.youtube.com/watch?v=h1lv1K6jx5g&list=PLxabZQCAe5fgGQggJxp5nuI1ESzP-oMED&index=9
    //Youtube Link : https://www.youtube.com/watch?v=OVPiu36-5lA&index=10&list=PLxabZQCAe5fgGQggJxp5nuI1ESzP-oMED
    //Youtube Link : https://www.youtube.com/watch?v=b5arRfzMWPE&index=11&list=PLxabZQCAe5fgGQggJxp5nuI1ESzP-oMED

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<MessageObject> list;
    //ChatAdapter chatAdapter;

    // Database reference to retrieve the data
    DatabaseReference mChatDb;
    DatabaseReference chatIDRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mCurrentUser;
    String myOwnUsername;
    String usernameOfOtheruser;
    String BioDesc;

    Button sendBtn;

    String chatID;
    String chatIDFromYourChat;
    String chatIDOfChat;

    //////////////////////////// CHAT ACTIVITY RECYCLERVIEW ////////////////////////////////

    private RecyclerView mChat, mMedia;
    private RecyclerView.Adapter mChatAdapter, mMediaAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager, mMediaLayoutManager;

    ArrayList<MessageObject> messageList;

    //////////////////////////// CHAT ACTIVITY RECYCLERVIEW ////////////////////////////

    String NameOfUser;

    // For Top Bar of Layout
    private ImageView backButton;
    private CircleImageView chatProfileImage;
    private TextView usernameOfUser;

    private static final int GALLERY_REQUEST = 1;
    private Uri filePath;
    ArrayList<String> mediaUriList = new ArrayList<>();

    String otherUserId;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_chat);

        // Top Bar variable id linking
        backButton = findViewById(R.id.backArrow);
        chatProfileImage = findViewById(R.id.userChatProfilePic);
        usernameOfUser = findViewById(R.id.chatUsernameOfUser);

        initializeRecyclerView();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = mCurrentUser.getUid();

        // To store data passed in from your chats java file
        chatID = getIntent().getStringExtra("chatIDD");

        Bundle b = getIntent().getExtras();
        final String chatProfileImageString = b.getString("chatImageOfUser");
        usernameOfOtheruser = b.getString("usernameOfOtheruser");
        otherUserId = b.getString("selectedUsersId");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(FirebaseAuth.getInstance().getUid())
                .child("Username");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myOwnUsername = dataSnapshot.getValue().toString();
                getChatMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Picasso.get().load(chatProfileImageString).fit().into(chatProfileImage);
        usernameOfUser.setText(usernameOfOtheruser);

        chatProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MessagingChat.this, ViewFullImage.class);
                intent.putExtra("ProfilePic", chatProfileImageString);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(MessagingChat.this, ChatsDisplay.class);
                startActivity(i5);
                finish();
            }
        });

        // CHAT DATABASE REFERENCE
        mChatDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Chats").child(chatID);

        sendBtn = (Button) findViewById(R.id.send);
        recyclerView = (RecyclerView) findViewById(R.id.messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MessageObject>();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                String time = mdformat.format(calendar.getTime());
                sendMessage(time);
            }
        });

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
                    .setMinCropResultSize(450, 450)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String selectedImage = resultUri.toString();

                //Picasso.get().load(selectedImage).fit().into();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void initializeRecyclerView() {
        messageList = new ArrayList<>();
        mChat = findViewById(R.id.messageList);
        mChat.setNestedScrollingEnabled(false);
        mChat.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        mChat.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new MessageAdapter(messageList);
        mChat.setAdapter(mChatAdapter);
    }

    private void sendMessage(String time) {

        String uid = FirebaseAuth.getInstance().getUid();
        EditText mMessage = findViewById(R.id.messageBoxInput);

        if (!mMessage.getText().toString().isEmpty()) {
            reference = mChatDb.push();
            Map newMessageMap = new HashMap<>();
            newMessageMap.put("text", mMessage.getText().toString());
            newMessageMap.put("creator", myOwnUsername);
            newMessageMap.put("time", time);
            newMessageMap.put("senderUserId", FirebaseAuth.getInstance().getCurrentUser().getUid());

            reference.updateChildren(newMessageMap);
        }
        mMessage.setText(null);
    }

    private void getChatMessages() {

        // Explaination in the video : https://www.youtube.com/watch?v=AKMdZTmndDA&list=PLxabZQCAe5fgGQggJxp5nuI1ESzP-oMED&index=12
        mChatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    String creatorID = "";
                    String text = "";
                    String timing = "";
                    String currentUserFromDB = "";

                    if (dataSnapshot.child("text").getValue() != null && dataSnapshot.child("creator").getValue() != null) {
                        creatorID = dataSnapshot.child("creator").getValue().toString();
                        text = dataSnapshot.child("text").getValue().toString();
                    }

                    if (dataSnapshot.hasChild("time")) {
                        timing = dataSnapshot.child("time").getValue().toString();
                    }

                    if (dataSnapshot.child("senderUserId").exists()) {
                        currentUserFromDB = dataSnapshot.child("senderUserId").getValue().toString();
                    }

                    MessageObject mMessage = new MessageObject(timing, creatorID, text, currentUserFromDB);
                    messageList.add(mMessage);
                    mChatLayoutManager.scrollToPosition(messageList.size() - 1);
                    mChatAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i5 = new Intent(MessagingChat.this, ChatsDisplay.class);
        startActivity(i5);
        finish();
    }
}
