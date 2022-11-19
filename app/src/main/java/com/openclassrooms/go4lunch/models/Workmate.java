package com.openclassrooms.go4lunch.models;

import org.jetbrains.annotations.Nullable;

public class Workmate {

    private String idWorkmate;
    private String name;
    private String email;
    private Boolean isNotificationActive;
    @Nullable
    private String urlPicture;
    public Workmate() {
    }

    public Workmate(String idWorkmate, String name, String email, String urlPicture, Boolean isNotificationActive) {
        this.idWorkmate = idWorkmate;
        this.name = name;
        this.email = email;
        this.urlPicture = urlPicture;
        this.isNotificationActive=isNotificationActive;
    }
    public Workmate(String idWorkmate, String name, String email, String urlPicture) {
        this.idWorkmate = idWorkmate;
        this.name = name;
        this.email = email;
        this.urlPicture = urlPicture;
        this.isNotificationActive=true;
    }

    // --- GETTERS ---

    public String getName() {
        return name;
    }
    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }
    public String getEmail() {
        return email;
    }

    public String getIdWorkmate() {
        return idWorkmate;
    }

    public Boolean getIsNotificationActive() {
        return isNotificationActive;
    }

    // --- SETTERS ---
    public void setIdWorkmate(String idWorkmate) {
        this.idWorkmate = idWorkmate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void getIsNotificationActive(Boolean isNotificationActive) {
        isNotificationActive = isNotificationActive;
    }
}
