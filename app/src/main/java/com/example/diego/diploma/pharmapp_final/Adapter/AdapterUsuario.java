package com.example.diego.diploma.pharmapp_final.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.diego.diploma.pharmapp_final..Activity.Chat;
import com.example.diego.diploma.pharmapp_final.Activity.Chat;
import com.example.diego.diploma.pharmapp_final.Fragmento.UserFragment;
import com.example.diego.diploma.pharmapp_final.Modelo.UsuarioMode;
import com.example.diego.diploma.pharmapp_final.R;
import com.example.diego.diploma.pharmapp_final.Modelo.UsuarioMode;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.Myholder> {
    Context context;
    List<UsuarioMode> userLista;

    public AdapterUsuario(Context context, List<UsuarioMode> userLista) {
        this.context = context;
        this.userLista = userLista;
    }

    public AdapterUsuario(Class<UserFragment> userFragmentClass, List<UsuarioMode> lista) {
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_user, viewGroup, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        //get datos
        final String miuid= userLista.get(position).getUid();
        final String userImagen = userLista.get(position).getImagenUsu();
        final String userName=userLista.get(position).getName();
        final String userEmail=userLista.get(position).getEmail();
        //set datos
        holder.nNameTv.setText(userName);
        holder.mEmailTv.setText(userEmail);

        try{
            Picasso.get().load(userImagen).into(holder.perfilIv);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.ic_person_black_24dp).into(holder.perfilIv);

        }

        //handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Haga clic en el usuario de la lista de usuarios para comenzar a chatear / enviar mensajes
                // comienza la actividad colocando el uid del receptor
                // usaremos ese uid para identificar al usuario con el que vamos a chatear
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("hisUid", miuid);
                intent.putExtra("nameUid", userName);
                intent.putExtra("images", userImagen);

                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return userLista.size();
    }

    //view holder class
    class Myholder extends RecyclerView.ViewHolder{

        ImageView perfilIv;
        TextView nNameTv, mEmailTv;


        public Myholder(@NonNull View itemView) {
            super(itemView);

            perfilIv= itemView.findViewById(R.id.perfilIv);
            nNameTv= itemView.findViewById(R.id.nameTv);
            mEmailTv= itemView.findViewById(R.id.emailTv);

        }




    }
}
