package com.example.diego.diploma.pharmapp_final.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.diego.diploma.pharmapp_final.DashboardActivity;
import com.example.diego.diploma.pharmapp_final.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button ir_login;
    Button registrarse;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        ir_login=findViewById(R.id.ir_login);
        registrarse=findViewById(R.id.registrarse);

        ir_login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    });

        registrarse.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, Registro.class));
            finish();
        }
    });
}

    private  void  checkUserStatus(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null) {
            // user is signed in stay here
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }else{

        }
    }
    protected  void onStart(){
        checkUserStatus();
        super.onStart();
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this, Home.class));
            finish();
        }
    }*/
}
