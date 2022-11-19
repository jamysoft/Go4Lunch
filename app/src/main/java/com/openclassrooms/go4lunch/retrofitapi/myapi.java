package com.openclassrooms.go4lunch.retrofitapi;

import com.openclassrooms.go4lunch.models.pojofromjson.DataListRestaurant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//Interface wish return retrofit Call of model class type (model in our DataListRestaurant)
public interface myapi
{

    @GET("nearbysearch/json")
    Call<DataListRestaurant> getAllRestaurant(
         //   @Query("keyword") String keyword,
           @Query("location") String Location,
            @Query("radius") int  radius,
            @Query("type") String Type,
            @Query("key") String KeyMap
    );
}
