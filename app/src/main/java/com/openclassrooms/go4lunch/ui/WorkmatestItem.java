package com.openclassrooms.go4lunch.ui;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.go4lunch.models.Lunch;
import com.openclassrooms.go4lunch.models.Workmate;

public class WorkmatestItem {
    private String name;
    private String urlPicture;
    private MutableLiveData<Lunch> todayLunch;

    public WorkmatestItem(Workmate workmate, MutableLiveData<Lunch> todayLunch) {
        name=workmate.getName();
        urlPicture =workmate.getUrlPicture();
        this.todayLunch =todayLunch;
    }

    public String getName() {
        return name;
    }

    public MutableLiveData<Lunch> getTodayLunch() {
        return todayLunch;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setTodayLunch(MutableLiveData<Lunch> todayLunch) {
        this.todayLunch = todayLunch;
    }

    public String getUrlPicture() {
        return urlPicture;
    }
}
