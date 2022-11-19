package com.openclassrooms.go4lunch.ViewModels;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.models.Lunch;
import com.openclassrooms.go4lunch.models.Restaurant;
import com.openclassrooms.go4lunch.models.Workmate;
import com.openclassrooms.go4lunch.models.pojofromjson.DataListRestaurant;
import com.openclassrooms.go4lunch.repository.LunchRepository;
import com.openclassrooms.go4lunch.repository.RestaurantRepository2;
import com.openclassrooms.go4lunch.repository.WorkmateRepository;
import com.openclassrooms.go4lunch.ui.WorkmatestItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class ListRestoViewModel extends ViewModel {
    private RestaurantRepository2 restaurantRepository2;
    private WorkmateRepository workmateRepository;
    private LunchRepository lunchRepository;
    private Location currentLocation;
    private Context context;

    public ListRestoViewModel(Context context) {
        this.restaurantRepository2=RestaurantRepository2.getInstance();
        this.lunchRepository=LunchRepository.getInstance();
        this. workmateRepository=WorkmateRepository.getInstance();
        this.context=context;
    }

    public Call<DataListRestaurant> getAllRestaurants2(String url, String location,
                                                      int radius, String type, String key) {
        return restaurantRepository2.getAllRestaurant(url,location,radius,type,key);
    }

    public void createLunch( Restaurant restaurantChoosed) {
        String name=workmateRepository.getCurrentWorkmate().getDisplayName();
        String uid=workmateRepository.getCurrentWorkmate().getUid();
        System.out.println("uid "+uid);
        String urlPicture=workmateRepository.getCurrentWorkmate().getPhotoUrl().toString();
        String email=workmateRepository.getCurrentWorkmate().getEmail();
        Workmate workmate=new Workmate(uid,name,email,urlPicture);
        Date today= Calendar.getInstance().getTime();
        today.setHours(13);
        today.setMinutes(00);
        today.setSeconds(00);
        System.out.println("ADD today LUNCH "+today.toString());
        lunchRepository.createLunch(today.toString(),restaurantChoosed,workmate);
    }

    public LunchRepository getLunchRepository() {
        return lunchRepository;
    }

    public void addLikeRestaurant(Restaurant restaurant) {
        workmateRepository.addLikeRestaurant(restaurant);
    }

    public void deleteLikeRestaurant(Restaurant restaurant) {
        workmateRepository.deleteLikeRestaurant(restaurant);
    }



    private Context getContext() {
        return context;
    }

    public WorkmateRepository getWorkmateRepository() {
        return workmateRepository;
    }
    @NonNull
    public Location getCurrentLocation() {
        return currentLocation;
    }
    public MutableLiveData<Boolean> checkIfCurrentWorkmateLikeThisRestaurant(Restaurant restaurant){
       return workmateRepository.checkIfCurrentWorkmateLikeThisRestaurant(restaurant);
    }
    public MutableLiveData<Boolean> checkIfCurrentWorkmateChoseThisRestaurantForLunch(Restaurant restaurant,String uid) {
        return lunchRepository.checkIfCurrentWorkmateChoseThisRestaurantForLunch(restaurant,uid);
    }
    public Task<QuerySnapshot> checkIfCurrentWorkmateChoseThisRestaurantForLunch2(Restaurant restaurant, String uid) {
        return lunchRepository.checkIfCurrentWorkmateChoseThisRestaurantForLunch2(restaurant,uid);
    }

    public void deleteLunch(Restaurant restaurant, String currentUid) {
        lunchRepository.deleteLunch(restaurant, currentUid);
    }
    public Task<Void> signOut(Context context) {
        return workmateRepository.signOut(context);
    }
    public FirebaseUser getCurrentWorkmates() {
        return workmateRepository.getCurrentWorkmate();
    }
    public LiveData<List<WorkmatestItem>> getAllWorkmates() {
        return mapDataToViewState(workmateRepository.getAllWorkmate());
    }
    public LiveData<List<WorkmatestItem>> getWorkmatesThatAlreadyChooseRestaurantForTodayLunch(Restaurant restaurant) {
        return mapDataToViewState(lunchRepository.getWorkmatesThatAlreadyChooseRestaurantForTodayLunch(restaurant));
    }
    private LiveData<List<WorkmatestItem>> mapDataToViewState(MutableLiveData<ArrayList<Workmate>> allWorkmates) {
        LiveData<List<WorkmatestItem>> listWorkmatestItem
                =Transformations.map(allWorkmates, workmate -> {
            List<WorkmatestItem> workmateViewStateItems = new ArrayList<>();
            Date today= Calendar.getInstance().getTime();
            today.setHours(13);
            today.setMinutes(00);
            today.setSeconds(00);
            System.out.println(today.toString());
            for (Workmate w : workmate) {
                System.out.println(w.getName());
                MutableLiveData<Lunch> todaylunch=lunchRepository.getTodayLunch(w.getIdWorkmate());
                WorkmatestItem workmatestItem= new WorkmatestItem(w,todaylunch);
                workmateViewStateItems.add(workmatestItem);
            }
            return workmateViewStateItems;
        });
        return listWorkmatestItem;
    }

    public void setWorkmateRepository(WorkmateRepository workmateRepository) {
        this.workmateRepository = workmateRepository;
    }

    public void setLunchRepository(LunchRepository lunchRepository) {
        this.lunchRepository = lunchRepository;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setIsNotificationActiveOfCurrentWorkmates(Boolean isNotificationActivate){
        this.workmateRepository.setIsNotificationActiveOfCurrentWorkmates(isNotificationActivate);
    }
    public MutableLiveData<Boolean> getIsNotificationActiveOfCurrentWorkmates(){
        return this.workmateRepository.getIsNotificationActiveOfCurrentWorkmates();
    }
}
