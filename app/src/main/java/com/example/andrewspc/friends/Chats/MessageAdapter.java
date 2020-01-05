package com.example.andrewspc.friends.Chats;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andrewspc.friends.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

//FOR CHATING WITH OTHER USERS LOOK AT HE BELOW LINKS (EPISODES: 8 , 9 , 10)
//Youtube Link : https://www.youtube.com/watch?v=h1lv1K6jx5g&list=PLxabZQCAe5fgGQggJxp5nuI1ESzP-oMED&index=9
//Youtube Link : https://www.youtube.com/watch?v=OVPiu36-5lA&index=10&list=PLxabZQCAe5fgGQggJxp5nuI1ESzP-oMED
//Youtube Link : https://www.youtube.com/watch?v=b5arRfzMWPE&index=11&list=PLxabZQCAe5fgGQggJxp5nuI1ESzP-oMED

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    ArrayList<MessageObject> messageList;

    FirebaseUser currentUser;

    public MessageAdapter(ArrayList<MessageObject> messageList){
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        // Setting the text to be left or right of the screen respectively tutorial is in the link below
        // Youtube : https://www.youtube.com/watch?v=1mJv4XxWlu8&list=PLzLFqCABnRQftQQETzoVMuteXzNiXmnj8&index=8
        if (viewType == MSG_TYPE_RIGHT){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);

            MessageViewHolder rcv = new MessageViewHolder(layoutView);
            return rcv;

        } else{
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);

            MessageViewHolder rcv = new MessageViewHolder(layoutView);
            return rcv;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position){
        holder.mSender.setText(messageList.get(position).getCreator());
        holder.mMessage.setText(messageList.get(position).getText());
        holder.time.setText(messageList.get(position).getTime());
    }

    @Override
    public int getItemCount(){
        return messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView mMessage;
        TextView mSender;
        TextView time;
        CardView mLayout;
        MessageViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.layout);
            mSender = view.findViewById(R.id.sender);
            mMessage = view.findViewById(R.id.message);
            time = view.findViewById(R.id.timeStamp);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (messageList.get(position).getCurrentUserFromDB().equals(FirebaseAuth.getInstance().getUid())){
            return MSG_TYPE_RIGHT;
        } else{
            return MSG_TYPE_LEFT;
        }
    }
}