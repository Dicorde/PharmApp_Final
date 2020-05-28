package com.example.diego.diploma.pharmapp_final.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diego.diploma.pharmapp_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditarUsuario extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    DatabaseReference databaseReference;

    private EditText nombreUsuario, editcorreo, editcontraseña, edittelefono;
    private Button registro;
    private ProgressDialog progressDialog;
    ImageView avatarIv;
    TextView nameTv, emailTv, phoneTv;
    Button Update;

    private String name = "";
    private String email = "";
    private String password = "";
    private String phone = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        nombreUsuario = (EditText) findViewById(R.id.Edreg_name);
        editcorreo = (EditText) findViewById(R.id.Edreg_correo);
        editcontraseña = (EditText) findViewById(R.id.Edreg_contra);
        edittelefono = (EditText) findViewById(R.id.Edreg_telefono);
        Update = (Button) findViewById(R.id.Actualizar);
        progressDialog = new ProgressDialog(this);

        //Metodo Buttoon para Actualizar
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actualizar();
                finish();
            }
        });


    //Obtengo los datos y los muestros en un edit text
     firebaseFirestore.collection("Users").document(user.getUid()).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists() ){
                            String uid = "" + documentSnapshot.getString("uid");
                            String name = "" + documentSnapshot.getString("name");
                            String email = "" + documentSnapshot.getString("email");
                            String phone = "" + documentSnapshot.getString("phone");
                            String image = "" + documentSnapshot.getString("imagenUsu");
                            String password = "" +documentSnapshot.getString( "password");

                            nombreUsuario.setText(name);
                            editcorreo.setText(email);
                            edittelefono.setText(phone);
                            editcontraseña.setText(password);
                           }
                      }
                        });
        }

        //Metodo para Actualizar Datos
            private void Actualizar(){
                    String idUsu = user.getUid();
                    String name =  nombreUsuario.getText().toString();
                    String email = editcorreo.getText().toString();
                    String phone = edittelefono.getText().toString();
                    String password = editcontraseña.getText().toString();


                    Map<String,Object> map = new HashMap<>();
                            map.put("name", name);
                            map.put("uid", idUsu);
                            map.put("email", email);
                            map.put("password", password);
                            map.put("phone", phone);
                            map.put("imagenUsu", "");

                     firebaseFirestore.collection("Users").document(user.getUid()).update(map).
                             addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),
                                            "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getApplicationContext(),
                                            "Datos no Actualizados!", Toast.LENGTH_SHORT).show();
                             }
                          });
                       }



}

