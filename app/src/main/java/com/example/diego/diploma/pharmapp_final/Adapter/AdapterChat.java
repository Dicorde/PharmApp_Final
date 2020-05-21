package com.example.diego.diploma.pharmapp_final.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.diego.diploma.pharmapp_final.R;
import com.example.diego.diploma.pharmapp_final.Modelo.ChatMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{


    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGTH = 1;
    Context context;
    List<ChatMode> chalist;
    String imageUrl;

    FirebaseUser fUser;

    public AdapterChat(Context context, List<ChatMode> chalist, String imageUrl) {
        this.context = context;
        this.chalist = chalist;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int M) {
        //inflate layout: row_chat left for receiver, row_chat_right for sender
        if(M==MSG_TYPE_RIGTH){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new MyHolder(view);

        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new MyHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String message = chalist.get(position).getMessage();
        String timestamp = chalist.get(position).getTimestamp();

        //convert time stamp to dd/mm/yy hh:mm am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
        //set dat
        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);
        try {
            Picasso.get().load(imageUrl).into(holder.perfilIv);
        }
        catch (Exception e){

        }

        //set seen/delivered status of message
        if(position==chalist.size()-1) {

            if (chalist.get(position).isVisto()) {
                holder.visto.setText("visto");

            } else {
                holder.visto.setText("entregado");

            }
        }
           else {
                holder.visto.setVisibility(View.GONE);
            }


    }

    @Override
    public int getItemCount() {

        return chalist.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chalist.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGTH;
        }else {
            return MSG_TYPE_LEFT;
        }

    }

    //view holder class
    class MyHolder extends RecyclerView.ViewHolder{
        //views
        ImageView perfilIv;
        TextView messageTv, timeTv, visto;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            perfilIv = itemView.findViewById(R.id.perfilIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv= itemView.findViewById(R.id.HoraTv);
            visto = itemView.findViewById(R.id.vistoTv);
        }
    }
}
