package com.example.diego.diploma.pharmapp_final.Modelo;


import java.util.ArrayList;
import java.util.List;

public class UsuarioMode {

    String name, email, search, phoone, imagenUsu, cover, uid, onlineStatus, typingTo;//adding two more fields


    public UsuarioMode() {
    }

    //use same name as in firebase database

    public UsuarioMode(String name, String email, String search, String phoone, String imagenUsu, String cover, String uid, String onlineStatus,
                       String typingTo) {
        this.name = name;
        this.email = email;
        this.search = search;
        this.phoone = phoone;
        this.imagenUsu = imagenUsu;
        this.cover = cover;
        this.uid = uid;
        this.onlineStatus = onlineStatus;
        this.typingTo = typingTo;
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

    public String getPhoone() {
        return phoone;
    }

    public void setPhoone(String phoone) {
        this.phoone = phoone;
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
        return typingTo;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }
}