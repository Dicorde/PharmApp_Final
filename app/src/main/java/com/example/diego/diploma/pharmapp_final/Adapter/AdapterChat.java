package com.example.diego.diploma.pharmapp_final.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.diego.diploma.pharmapp_final.R;
import com.example.diego.diploma.pharmapp_final.Modelo.ChatMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.mutation.ArrayTransformOperation;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{


    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGTH = 1;
    Context context;
    List<ChatMode> chalist;
    String imageUrl;
    FirebaseUser fUser;
    FirebaseFirestore firebaseFirestore;



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
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
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

     //cuadro de dialogo para mensaje de elimianr
        holder.mensajeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Eliminar Mensaje");
                        builder.setMessage("¿Desea eliminar mensaje?");

                 builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);

                          }
                   });

                builder.setNegativeButton("Anular ", new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                   });
                        builder.show();
                }
        });




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


    private void deleteMessage( int position){

        final String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();

        String msgTimeStamp = chalist.get(position).getTimestamp();

        CollectionReference chatRef = firebaseFirestore.collection("Chat");
        Log.d("ITEMCLICK", "paraqu : "+ chatRef);

        Query query = chatRef.orderBy("timestamp");
        Log.d("ITEMCLICK", "para : "+ query.orderBy("timestamp"));

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult()) {

                        if (ds.getString("sender").equals( myUID)) {
                         //   Log.d("ITEMCLICK", "para : "+ chat.getSender());
                          ds.getReference().delete();


                          // HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                           //hasSeenHashMap.put("message", "mensaje");
                           //ds.getReference().update(hasSeenHashMap);


                            Toast.makeText(context, "message eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "message no eliminado", Toast.LENGTH_SHORT).show();
                        }
                }

                }
            }
        });

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
        LinearLayout  mensajeLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            perfilIv = itemView.findViewById(R.id.perfilIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv= itemView.findViewById(R.id.HoraTv);
            visto = itemView.findViewById(R.id.vistoTv);
            mensajeLayout = itemView.findViewById(R.id.mensajeLayout);
        }
    }
}
