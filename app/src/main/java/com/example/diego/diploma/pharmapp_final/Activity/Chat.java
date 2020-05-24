package com.example.diego.diploma.pharmapp_final.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.diego.diploma.pharmapp_final.R;
import com.example.diego.diploma.pharmapp_final.Adapter.AdapterChat;
import com.example.diego.diploma.pharmapp_final.Modelo.ChatMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




public class Chat extends AppCompatActivity {
    //view from xml
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView perfilTv;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;

    FirebaseAuth firebaseAuth;

    //for checking if use has seen message or not
    ValueEventListener seenListener;
    AdapterChat adapterChat;
    List<ChatMode> chatlista;
    FirebaseFirestore firebaseFirestore;


    String hisUid;
    String myUid;
    String hiimage;
    String nameUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chat_recyclerView);
        perfilTv = findViewById(R.id.perfilIv);
        nameTv = findViewById(R.id.nameTv);
        userStatusTv = findViewById(R.id.userStatusTv);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.sendBtn);


        //layout (linearlayout) para recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(Chat.this);
        //linea de codigo que pne los mensaje de arriba hacia abajo
        // layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterChat);
        //reciclerview propiedades

        final Intent intent = getIntent();


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        hisUid = intent.getStringExtra("hisUid");
        nameUid = intent.getStringExtra("nameUid");
        String name = "" + nameUid;
        //set data
        nameTv.setText(name);
        try {
            //image rreceived set it to imageview in toolbat
            Picasso.get().load(hiimage).into(perfilTv);

        } catch (Exception e) {
            //there is exception gettin picture set default picture
            Picasso.get().load(R.drawable.ic_person_black_24dp).into(perfilTv);


        }

        //buscar usuario para obtener esa información de los usuarios


        LeerMessage();


        //click button enviar mensaje
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtener texto de edit text
                String message = messageEt.getText().toString().trim();
                //verificar si el text es vacio
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(Chat.this, "no se envio el mensae", Toast.LENGTH_SHORT).show();


                } else {
                    //text no vacio
                    sendMessage(message);
                    LeerMessage();

                }
            }

        });
    }


   private void VerMessage() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot ds : task.getResult()) {
                        ChatMode chat = ds.toObject(ChatMode.class);
                        if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)) {

                            HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                            hasSeenHashMap.put("visto", true);
                            ds.getReference().update(hasSeenHashMap);
                        //    Log.d("ITEMCLICK", "siiii : "+ myUid + " => " +hasSeenHashMap);
                        }

                    }
                }
            }
        });

    }


    private void LeerMessage() {
        chatlista = new ArrayList<>();
        firebaseFirestore.collection("Chat").orderBy("timestamp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot dat: task.getResult()){
                     ChatMode chat = dat.toObject(ChatMode.class);

                if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)
                        || chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)) {
                    chatlista.add(chat);


                }     adapterChat = new AdapterChat(Chat.this, chatlista, hiimage);
                        //set adapter to recyclerview
                        recyclerView.setAdapter(adapterChat);
                        recyclerView.smoothScrollToPosition(chatlista.size());
            }}
        });
    }


    private void sendMessage(String message) {

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("visto", false);
        FirebaseFirestore.getInstance().collection("Chat").add(hashMap);
        //restablecer editar texto después de enviar el mensaje
        messageEt.setText("");

        VerMessage();

    }

        private void checkUserStatus () {
            //get current user
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // el usuario ha iniciado sesión quédate con ella
                 // establece el correo electrónico del usuario conectado
                 //perfiltv.setText (user.getEmail ())
                myUid = user.getUid();//currently signed in use
            } else {
                startActivity(new Intent(this, Login.class));
                finish();
            }
        }
        protected void onStart () {
            checkUserStatus();
            super.onStart();
        }

       /* protected void onPause () {
            super.onPause();
            firebaseFirestore.r
                    removeEventListener(seenListener);
        }*/

        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            getMenuInflater().inflate(R.menu.menu_main, menu);

            //vista de búsqueda
            menu.findItem(R.id.action_search).setVisible(true);
            return super.onCreateOptionsMenu(menu);
        }

    }


