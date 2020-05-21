package com.example.diego.diploma.pharmapp_final.Modelo;

public class ModelChatLista {
    String id; //we wil need id to get chat list sender/recivd uid

    public ModelChatLista() {
    }

    public ModelChatLista(String id)
    {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
