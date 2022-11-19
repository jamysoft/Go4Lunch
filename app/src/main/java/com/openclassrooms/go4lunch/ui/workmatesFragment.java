package com.openclassrooms.go4lunch.ui;

import static com.openclassrooms.go4lunch.utils.Utils.createRestaurantFromResultObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.ViewModels.ListRestoViewModel;
import com.openclassrooms.go4lunch.ViewModels.ViewModelFactory;
import com.openclassrooms.go4lunch.models.Restaurant;
import com.openclassrooms.go4lunch.models.pojofromjson.DataListRestaurant;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class workmatesFragment extends Fragment {

    private ListWorkmatesAdapter adapter;
    private ListRestoViewModel viewModel;
    private RecyclerView recyclerView;
    Toolbar mToolbar ;
    static String TAG="ERROR";
    PlacesClient placeClient;
    AutocompleteSupportFragment autocompleteSupportFragment;
    private Restaurant restaurant;
    static final String KEY_RESTAURANT_ITEM="RESTAURANT_ITEM";
    public static final String url = "https://maps.googleapis.com/maps/api/place/";
    public static final int radius= 1500;
    public static final String type="restaurant";
    public static final String key= BuildConfig.google_maps_api_key;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code = 101;
    private MutableLiveData<Location> currentLocation=new MutableLiveData<>();


    public workmatesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_workmates, container, false);;
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.recyclerView2);
        mToolbar=view.findViewById(R.id.toolbarlistworkmates);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_24);
     mToolbar.setNavigationOnClickListener(v -> {
         Log.e("TAG", "onClick:  NavigationOnClickListener" );
         //get parent activity
        ((MainActivity2)getActivity()).binding.drawerLayout.open();
     });
        autocompleteSupportFragment= (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autoCompleteFragment);




    //  mToolbar.setLogo(R.drawable.com_facebook_close);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mToolbar.setNavigationIcon(R.drawable.ic_menu_24);
        Objects.requireNonNull(autocompleteSupportFragment.getView()).setVisibility(View.INVISIBLE);
        mToolbar.setNavigationOnClickListener(v -> {
            Log.e("TAG", "onClick:  NavigationOnClickListener" );
            //get parent activity
            ((MainActivity2) Objects.requireNonNull(getActivity())).binding.drawerLayout.open();
        });
        mToolbar.setOnClickListener(v -> {
            autocompleteSupportFragment.getView().setVisibility(View.VISIBLE);
            mToolbar.setTitle("");
            mToolbar.setNavigationIcon(null);
        });
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ListRestoViewModel.class);
        LifecycleOwner owner= this;
        //we transmit the LifecycleOwner to our adapter
        adapter = new ListWorkmatesAdapter(owner,false);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        viewModel.getAllWorkmates().observe(this, adapter::submitList);
        currentLocation.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                configureautocompleteSupportFragment(location);
            }
        });

    }

    private void configureautocompleteSupportFragment(Location location) {
        String stringLocation=location.getLatitude()+","+location.getLongitude();
        if (!Places.isInitialized()) {
            Places.initialize(Objects.requireNonNull(getContext()), key);
        }
        placeClient = Places.createClient(Objects.requireNonNull(getContext()));

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS,Place.Field.NAME));
        autocompleteSupportFragment.setHint("search restaurant");
        autocompleteSupportFragment.setCountries("FR");
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                Log.e(TAG, "onPlaceSelected: " + latLng);
                Log.e(TAG, "onPlaceSelected: " + place.getName());
                viewModel.getAllRestaurants2(url, stringLocation, radius,type,key).enqueue(new Callback<DataListRestaurant>() {
                    @Override
                    public void onResponse(Call<DataListRestaurant> call, Response<DataListRestaurant> response) {
                        DataListRestaurant data = response.body();
                        System.out.println("isSuccessful "+response.isSuccessful());
                        for(int i=0; i<data.getResults().size();i++) {
                            if(data.getResults().get(i).getName().equals(place.getName())){
                                restaurant = createRestaurantFromResultObject(data.getResults().get(i));
                                Intent monIntent = new Intent(getContext(), DetailRestaurant.class);
                                monIntent.putExtra(KEY_RESTAURANT_ITEM, restaurant);
                                getContext().startActivity(monIntent);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataListRestaurant> call, Throwable t) {
                        System.out.println("onFailure "+t.fillInStackTrace());
                    }
                });
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG, "onPlaceSelected: " + status.getStatusMessage());

            }
        });
    }
    public void setCurrentLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());
        //CHeck PERMISSION if it's not GRANTED
        if(ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (getContext(),Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);
        }
        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval((5000));
        LocationCallback locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationRequest==null){
                    Log.e("current location", "locationResult is null");
                }
                for (Location location:locationResult.getLocations() ){
                    if(location!=null){
                        currentLocation.postValue(location);
                        //  viewModel.setCurrentLocation(currentLocation);
                        Log.e("current Location", "onLocationResult:list resto fragement "+location.getLongitude()+" Latitude "+location.getLatitude());
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(getActivity(),new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation.postValue(location);
                    //currentLocation.setLatitude(lag);
                    Log.e(TAG, "onSuccess  long"+currentLocation);
                }
            }
        });

        //  return location;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (Request_code) {
            case Request_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCurrentLocation();
                }
        }
    }
}