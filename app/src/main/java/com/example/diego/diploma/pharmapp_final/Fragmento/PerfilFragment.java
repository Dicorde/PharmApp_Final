package com.example.diego.diploma.pharmapp_final.Fragmento;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.diego.diploma.pharmapp_final.Activity.EditarUsuario;
import com.example.diego.diploma.pharmapp_final.Activity.Login;
import com.example.diego.diploma.pharmapp_final.Activity.MainActivity;
import com.example.diego.diploma.pharmapp_final.Activity.Registro;
import com.example.diego.diploma.pharmapp_final.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;


public class PerfilFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;

    ImageView perfilIv;
    TextView nameTv, emailTv, phoneTv;
    Button Update;



    public PerfilFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflar el diseño de este fragmento
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        perfilIv = view.findViewById(R.id.perfilIv);
        nameTv = view.findViewById(R.id.nameTv);
        emailTv = view.findViewById(R.id.emailTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        Update = view.findViewById(R.id.Update);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditarUsuario.class);
                startActivity(intent);
                Log.d("ITEMCLICK", "siiii : "+intent );

            }
        });
        //metodo para obtener los datos y mostrarlos en perfil
       firebaseFirestore.collection("Users").document(user.getUid()).get().
               addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {

               if (documentSnapshot.exists() ){
                       String name = "" + documentSnapshot.getString("name");
                       String email = "" + documentSnapshot.getString("email");
                       String phone = "" + documentSnapshot.getString("phone");
                       String image = "" + documentSnapshot.getString("imagenUsu");

                       nameTv.setText(name);
                       emailTv.setText(email);
                       phoneTv.setText(phone);
                   try {
                       Picasso.get().load(image).into(perfilIv);
                   } catch (Exception e) {
                       Picasso.get().load(R.drawable.ic_person_black_24dp).into(perfilIv);
                   }

               } else {


               }



           }
       });





return view;
    }

    private  void  checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!= null) {
            // usuario ha iniciado sesión aquí.
        }else{
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();
        }
    }
    public void onCreate(@NonNull Bundle savedInstanceState){
        setHasOptionsMenu(true); //para mostrar el menú
        super.onCreate(savedInstanceState);
    }

    //inflar opcion menu
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    //manejar clics de elementos de menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {

            case R.id.action_logout:
                FirebaseAuth miAuth = FirebaseAuth.getInstance();
                miAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    }

