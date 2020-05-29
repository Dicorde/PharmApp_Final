package com.example.diego.diploma.pharmapp_final.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.diego.diploma.pharmapp_final.Modelo.UsuarioMode;
import com.example.diego.diploma.pharmapp_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText nombreUsuario, editcorreo, editcontraseña, edittelefono;
    private Button registro, galeria_foto;
    private ImageView perfilIv;
    private ProgressDialog progressDialog;
    private Switch typeUser;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;
    private String name = "";
    private String email = "";
    private String password = "";
    private String phone = "";
    private String imagenUsu="";
    private Boolean type = false;
    Uri imageUri;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();


        nombreUsuario = (EditText) findViewById(R.id.reg_name);
        editcorreo = (EditText) findViewById(R.id.reg_correo);
        editcontraseña = (EditText) findViewById(R.id.reg_contra);
        edittelefono = (EditText) findViewById(R.id.reg_telefono);
        registro = (Button) findViewById(R.id.enviar_registro);
        galeria_foto = (Button) findViewById(R.id.galeria_foto);
        perfilIv = (ImageView) findViewById(R.id.perfilIv);
        progressDialog = new ProgressDialog(this);
        typeUser = (Switch) findViewById(R.id.typeUser);


        galeria_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nombreUsuario.getText().toString();
                email = editcorreo.getText().toString();
                password = editcontraseña.getText().toString();
                phone = edittelefono.getText().toString();
                type = typeUser.isChecked();
                imagenUsu = perfilIv.toString();


                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phone.isEmpty() && !imagenUsu.isEmpty()) {
                    if ((password.length() >= 6) && (imagenUsu != null)) {
                        registerUser();
                    } else {
                        Toast.makeText(Registro.this, "La contraseña debe tener al menos 6 caracteres o la imagen no fue insertada", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Registro.this, "Debe completar los demas campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).resize(330, 330).into(perfilIv);

        }
    }


    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final String email = user.getEmail();
                    final String idUsu = user.getUid();
                    final String nombreImage = imageUri.getPath().substring(imageUri.getPath().lastIndexOf("/"));
                    final StorageReference prfe = mStorage.child("fotos/" + nombreImage); //nombre de la carpeta storage

                        prfe.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri miuri = task.getResult();
                                                imagenUsu = miuri.toString();//converti en string

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("name", name);
                                                map.put("uid", idUsu);
                                                map.put("email", email);
                                                map.put("password", password);
                                                map.put("phone", phone);
                                                map.put("imagenUsu", imagenUsu);
                                                map.put("type", type);

                                                progressDialog.setMessage("Realizando Ingreso en linea...");
                                                progressDialog.show();

                                                db.collection("Users").document(user.getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            startActivity(new Intent(Registro.this, Login.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(Registro.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(Registro.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });


                }
            }
        });
    }
}


