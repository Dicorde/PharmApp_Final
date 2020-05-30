package com.example.diego.diploma.pharmapp_final.Fragmento;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diego.diploma.pharmapp_final.Activity.Login;
import com.example.diego.diploma.pharmapp_final.Activity.MainActivity;
import com.example.diego.diploma.pharmapp_final.Modelo.UsuarioMode;
import com.example.diego.diploma.pharmapp_final.R;
import com.example.diego.diploma.pharmapp_final.Adapter.AdapterUsuario;
import com.example.diego.diploma.pharmapp_final.Activity.RegistrarFarmacia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    AdapterUsuario adapterUsuario;
    List<UsuarioMode> Lista;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    ImageView perfilIv;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflar el diseño de este fragmento
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        perfilIv = view.findViewById(R.id.perfilIv);


        recyclerView = view.findViewById(R.id.users_recyclerView);
        //propiedades
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();

        //init user list
        Lista = new ArrayList<>();
        //get all users
        AllgetUser();
        return view;
    }

        private void AllgetUser(){
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Lista.clear();
                        for (DocumentSnapshot ds : task.getResult()) {
                            UsuarioMode usuarioMode = ds.toObject(UsuarioMode.class);
                            if (!usuarioMode.getUid().equals(user.getUid())) {

                                Lista.add(usuarioMode);
                                String name = "" + usuarioMode.getName();
                                String email = "" + usuarioMode.getEmail();
                                String phone = "" + usuarioMode.getPhone();
                                String image = "" + usuarioMode.getImagenUsu();

                               Log.d("ITEMCLICK",  "ff"+phone);
                                //adaptador
                                adapterUsuario = new AdapterUsuario(getActivity(), Lista);
                                //set adaptador to recycler view
                                recyclerView.setAdapter(adapterUsuario);
                                Log.d("ITEMCLICK", "siiii : "+ recyclerView);
                            }
                        }
                    }
                }
            });

    }




    private void searchUsers(final String query){
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //get path of database named "users" containing users info
        firebaseFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Lista.clear();
                for (DocumentSnapshot ds :task.getResult()){
                    UsuarioMode usuarioMode = ds.toObject(UsuarioMode.class);

                    //conditiones to fulfil seearch:
                    //1) ser not current user
                    //2) the user name or email contains text entered in searchview (case insensitive)

                    //get all user excep currently signed in user
                    if(!usuarioMode.getUid().equals(user.getUid())){
                        if(usuarioMode.getName().toLowerCase().contains(query.toLowerCase()) ||
                                usuarioMode.getEmail().toLowerCase().contains(query.toLowerCase())){
                            Lista.add(usuarioMode);
                        }

                    }

                } //adaptador
                adapterUsuario = new AdapterUsuario(getActivity(),Lista);
                //refresh adapter
                //set adaptador to recycler view
                recyclerView.setAdapter(adapterUsuario);

        }
        });
    }


    private  void  checkUserStatus(){
     FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!= null) {
            //usuario ha iniciado sesión aquí
        }else{
          startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();
       }
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState){
        setHasOptionsMenu(true); //to show menu
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.menu_buscar, menu);
        //search view
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when user press search button frm keyboard
                //if search query is not empty the search
                if(!TextUtils.isEmpty(s.trim())){
                    searchUsers(s);
                }else{
                    AllgetUser();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called when user press search button frm keyboard
                //if search query is not empty the search
                if(!TextUtils.isEmpty(s.trim())){
                    searchUsers(s);
                }else{
                   AllgetUser();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }



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