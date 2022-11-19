package com.openclassrooms.go4lunch.repository;

import com.openclassrooms.go4lunch.models.pojofromjson.DataListRestaurant;
import com.openclassrooms.go4lunch.retrofitapi.myapi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository2 {
    private static volatile RestaurantRepository2 instance;
    public RestaurantRepository2() {
    }

    public static RestaurantRepository2 getInstance() {
        if(instance==null)
        {
            instance=new RestaurantRepository2();
        }
        return instance;
    }

    public  Call<DataListRestaurant> getAllRestaurant(String url,String location,
                                                       int radius,String type,String key) {
        //Retrofit object creation
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Convert json data to model class object
        // Convertit les données json en objet de classe de modèle
        myapi api=retrofit.create(myapi.class);

        // Create call of model class and enqueue for processing
        /// Créer un appel de classe de modèle et mettre en file d'attente
        // pour le traitement
        Call<DataListRestaurant> call=api.getAllRestaurant(location,radius,type,key);
       return call;

    }
}
