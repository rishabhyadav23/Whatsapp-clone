package com.example.android.whatsapp.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.whatsapp.Models.MessagesModel;
import com.example.android.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessagesModel> messagesModels;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.smaple_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(messagesModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessagesModel messagesModel = messagesModels.get(position);
        //
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(messagesModel.getTimestamp());
        String date = DateFormat.format("hh:mm aaa", calendar).toString();
        //
        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMess.setText(messagesModel.getMessage());
            // the change
            ((SenderViewHolder)holder).senderTime.setText(date);
        }else {
            ((ReceiverViewHolder)holder).receiverMess.setText((messagesModel.getMessage()));
            ((ReceiverViewHolder)holder).receiverTime.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMess,receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMess = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMess,senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMess = itemView.findViewById(R.id.senderText);
            senderTime= itemView.findViewById(R.id.timeSender);
        }
    }
}
