package com.openclassrooms.go4lunch.ViewModels;


import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;
   // private static Location currentLocation;
    private static Context context;

    public  static ViewModelFactory getInstance(Context context) {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
               //     ViewModelFactory.currentLocation =currentLocation;
                    ViewModelFactory.context=context;

                }
            }
        }
        return factory;
    }
    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();

                }
            }
        }
        return factory;
    }


    private ViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListRestoViewModel.class)) {
            return (T) new ListRestoViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}