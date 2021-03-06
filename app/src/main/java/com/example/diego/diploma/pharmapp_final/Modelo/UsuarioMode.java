package com.example.diego.diploma.pharmapp_final.Modelo;


import java.util.ArrayList;
import java.util.List;

public class UsuarioMode {

    String name, email, search, phone, imagenUsu, cover, uid, onlineStatus, type;//adding two more fields


    public UsuarioMode() {
    }

    //use same name as in firebase database

    public UsuarioMode(String name, String email, String search, String phone, String imagenUsu, String cover, String uid, String onlineStatus,
                       String typingTo) {
        this.name = name;
        this.email = email;
        this.search = search;
        this.phone = phone;
        this.imagenUsu = imagenUsu;
        this.cover = cover;
        this.uid = uid;
        this.onlineStatus = onlineStatus;
        this.type = typingTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImagenUsu() {
        return imagenUsu;
    }

    public void setImagen(String imagen) {
        this.imagenUsu = imagen;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getTypingTo() {
        return type;
    }

    public void setTypingTo(String typingTo) {
        this.type= typingTo;
    }
}