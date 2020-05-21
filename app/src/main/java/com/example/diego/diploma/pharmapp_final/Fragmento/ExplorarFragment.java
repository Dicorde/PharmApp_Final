package com.example.diego.diploma.pharmapp_final.Fragmento;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.diego.diploma.pharmapp_final.Activity.Login;
import com.example.diego.diploma.pharmapp_final.Activity.MainActivity;
import com.example.diego.diploma.pharmapp_final.R;
import com.example.diego.diploma.pharmapp_final.Activity.Explorar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ExplorarFragment extends Fragment {
    Button ir_mapa;
    FirebaseAuth firebaseAuth;

    public ExplorarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorar, container, false);
        // Inflar el diseño de este fragmento
            ir_mapa= view.findViewById(R.id.ir_mapa);
        ir_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Explorar.class));


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
