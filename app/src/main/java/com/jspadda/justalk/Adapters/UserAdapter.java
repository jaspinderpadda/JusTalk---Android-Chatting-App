package com.jspadda.justalk.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jspadda.justalk.Activities.ChatsActivity;
import com.jspadda.justalk.R;
import com.jspadda.justalk.Models.User;
import com.jspadda.justalk.databinding.RowConversationBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{


    ArrayList<User> usersList;
    Context context;

    public UserAdapter(ArrayList<User> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = usersList.get(position);

        String senderId = FirebaseAuth.getInstance().getUid();

        String senderRoom = senderId + user.getUid();

        FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(senderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if (snapshot.exists()){
                                       String lastMSg = snapshot.child("lastMsg").getValue(String.class);
                                       long time = snapshot.child("lastMsgTime").getValue(Long.class);
                                       SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                                       holder.binding.lastMsg.setText(lastMSg);
                                       holder.binding.msgTime.setText(dateFormat.format(new Date(time)));
                                   }else {
                                       holder.binding.lastMsg.setText("Tap to chat");
                                   }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

        holder.binding.userName.setText(user.getName());

        Glide.with(context).load(user.getProfileImg())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.userImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatsActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("uid", user.getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RowConversationBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);
        }
    }

}
