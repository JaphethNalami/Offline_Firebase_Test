package com.example.offlinefirebasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private ArrayList<MessageClass> messageList;
    private LayoutInflater inflater;
    private Context context;

    public MessageAdapter(Context context, ArrayList<MessageClass> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.inflater = LayoutInflater.from(context);
    }

    //create the view holder
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.message_body, parent, false);
        return new MessageViewHolder(v);
    }

    //bind the data to the view
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageClass message = messageList.get(position);
        holder.message.setText(message.getMessage());
    }

    //get the number of items in the list
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    //declare the view holder class
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        CheckBox status;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            status = itemView.findViewById(R.id.checkBox);
        }
    }


}
