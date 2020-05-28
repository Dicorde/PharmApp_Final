package com.example.diego.diploma.pharmapp_final;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.diego.diploma.pharmapp_final.Activity.Explorar;
import com.example.diego.diploma.pharmapp_final.Activity.Login;
//import com.example.diego.diploma.pharmapp_final.Fragmento.ChatListaFragment;
import com.example.diego.diploma.pharmapp_final.Activity.RegistrarFarmacia;
import com.example.diego.diploma.pharmapp_final.Activity.Registro;
import com.example.diego.diploma.pharmapp_final.Fragmento.ExplorarFragment;
//import com.example.diego.diploma.pharmapp_final.Fragmento.PerfilFragment;
//import com.example.diego.diploma.pharmapp_final.Fragmento.UserFragment;

import com.example.diego.diploma.pharmapp_final.Fragmento.InformacionFragment;
import com.example.diego.diploma.pharmapp_final.Fragmento.PerfilFragment;
import com.example.diego.diploma.pharmapp_final.Fragmento.UserFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.BoolValue;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    ActionBar actionBar;
    private boolean showMap = false;
    private static final int REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        actionBar = getSupportActionBar();
        actionBar.setTitle("perfil");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //opcione para inicar la navegacion inferior
        InformacionFragment fragment1 = new InformacionFragment();
        FragmentTransaction  ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();



        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        } else {
            showMap = true;
        }
    }
private BottomNavigationView.OnNavigationItemSelectedListener selectedListener=
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
         // opcines para las navegaxion inferior
                switch (menuItem.getItemId()) {
                    case R.id.nav_Info:
                        actionBar.setTitle("Info");
                        InformacionFragment fragment1 = new InformacionFragment();
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.content, fragment1, "");
                        ft1.commit();
                        return true;

                    case R.id.nav_perfil:
                        actionBar.setTitle("Perfil");
                        PerfilFragment fragment2 = new PerfilFragment();
                        FragmentTransaction  ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.content,fragment2,"");
                        ft2.commit();
                        return true;
                  case  R.id.nav_user:
                        actionBar.setTitle("Usuario");
                       UserFragment fragment3 = new UserFragment();
                        FragmentTransaction  ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.content,fragment3,"");
                        ft3.commit();
                        return true;
                    case  R.id.nav_Explorar:
                        if (showMap) {
                            startActivity(new Intent(DashboardActivity.this, Explorar.class));
                        } else {
                            actionBar.setTitle("Explorar");
                            ExplorarFragment fragment5 = new ExplorarFragment();
                            FragmentTransaction  ft5 = getSupportFragmentManager().beginTransaction();
                            ft5.replace(R.id.content,fragment5,"");
                            ft5.commit();
                            return true;
                        }
                }

                return false;
            }

        };
    private  void  checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!= null) {
        // usuario ha iniciado sesión aquí


        }else{
            startActivity(new Intent(DashboardActivity.this, Login.class));
            finish();
        }
    }
    @Override
    protected  void onStart(){
        checkUserStatus();
        super.onStart();
    }
}


