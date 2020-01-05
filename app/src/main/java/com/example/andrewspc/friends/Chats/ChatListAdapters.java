package com.example.andrewspc.friends.Chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChatListAdapters extends RecyclerView.Adapter<ChatListAdapters.MyViewHolder> {

    ArrayList<ChatObject> ChatListHelp;
    ArrayList<ChatObject> searchInfo;
    Context context;

    public ChatListAdapters(Context c, ArrayList<ChatObject> p, ArrayList<ChatObject> q) {
        context = c;
        ChatListHelp = p;
        searchInfo = q;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title.setText(ChatListHelp.get(position).getUsername());
        holder.tagLine.setText(ChatListHelp.get(position).getOccupation());
        Picasso.get().load(ChatListHelp.get(position).getProfilePicture()).fit().into(holder.imgProfilePic);

        holder.specificChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                /// HERE RETRIEVE THE KEY THAT CREATED THE CHAT ////
                final DatabaseReference chatID = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile")
                        .child(FirebaseAuth.getInstance().getUid()).child("Chats").child(ChatListHelp.get(holder.getAdapterPosition()).getUserID())
                        .child("UniqueChatID");

                chatID.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String chatIDVariable = dataSnapshot.getValue().toString();
                        Intent intent = new Intent(view.getContext(), MessagingChat.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("chatIDD", chatIDVariable);
                        bundle.putString("chatImageOfUser", ChatListHelp.get(holder.getAdapterPosition()).getProfilePicture());
                        bundle.putString("usernameOfOtheruser", ChatListHelp.get(holder.getAdapterPosition()).getUsername());
                        intent.putExtras(bundle);
                        view.getContext().startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return ChatListHelp.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView specificChat;
        private TextView title;
        private TextView tagLine;
        private CircleImageView imgProfilePic;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            tagLine = (TextView) itemView.findViewById(R.id.tagLine);
            specificChat = (CardView) itemView.findViewById(R.id.ChatsOfUser);
            imgProfilePic = (CircleImageView) itemView.findViewById(R.id.chatProfilePic);

        }

    }
}
