package com.openclassrooms.go4lunch.ui;

import static com.openclassrooms.go4lunch.repository.LunchRepository.getTodayLunchByRestaurant;
import static com.openclassrooms.go4lunch.utils.Utils.createRestaurantFromResultObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.go4lunch.BuildConfig;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.ViewModels.ListRestoViewModel;
import com.openclassrooms.go4lunch.ViewModels.ViewModelFactory;
import com.openclassrooms.go4lunch.databinding.FragmentMapsBinding;
import com.openclassrooms.go4lunch.models.Restaurant;
import com.openclassrooms.go4lunch.models.pojofromjson.DataListRestaurant;
import com.openclassrooms.go4lunch.models.pojofromjson.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MapsFragment extends Fragment {
    private FragmentMapsBinding binding;
    private ListRestoViewModel viewModel;
    private List<Restaurant> listRestaurant = new ArrayList<>();
    private Toolbar mToolbar;
    PlacesClient placeClient;
    static final String KEY_RESTAURANT_ITEM="RESTAURANT_ITEM";
    AutocompleteSupportFragment autocompleteSupportFragment;
    FloatingActionButton currentLoc;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code = 101;
    private double lag, lng;
    static String TAG="ERROR";
    private Restaurant restaurant;
    private MutableLiveData<Location> currentLocation=new MutableLiveData<>();
    public static final String url = "https://maps.googleapis.com/maps/api/place/";
    public static final int radius= 1500;
    public static final String type="restaurant";
    public static final String key="AIzaSyAGCAfrDFWFkaOAD7ymPWaOJyoQ54M08hI";
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
              for (Restaurant restaurantItem : listRestaurant) {
                    LatLng retaurantLatLng = new LatLng(getLatLngFromAdresse(restaurantItem.getAdresse()).latitude, getLatLngFromAdresse(restaurantItem.getAdresse()).longitude);
                  getTodayLunchByRestaurant(restaurantItem).addOnCompleteListener(task -> {
                      MarkerOptions markerOptions = new MarkerOptions().position(retaurantLatLng)
                              .title(restaurantItem.getName());
                      if (task.getResult().size()>0) {
                          markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                          Log.e("TAG", "markerOptions is GREEN  FOR "+restaurantItem.getName());
                      }
                      Marker  marker= mMap.addMarker(markerOptions);
                      Objects.requireNonNull(marker).setTag(restaurantItem);
                      mMap.moveCamera(CameraUpdateFactory.newLatLng(retaurantLatLng));
                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(retaurantLatLng, 15));
                      mMap.setOnMarkerClickListener(marker1 -> {
                          Restaurant r=(Restaurant) marker1.getTag();
                          Log.e("TAG", "onMarkerClick  "+ Objects.requireNonNull(r).getAdresse());
                            Intent intent = new Intent(getActivity(), DetailRestaurant.class);
                           // Restaurant detailRestaurant=new Restaurant();
                         intent.putExtra(KEY_RESTAURANT_ITEM, r);
                         startActivity(intent);
                              return true;

                      });
                  });
                }
            }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
      //  View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mToolbar=(Toolbar)view.findViewById(R.id.toolbarlistresto);
        //  mToolbar.setLogo(R.drawable.com_facebook_close);
        autocompleteSupportFragment= (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autoCompleteFragment);
        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentLoc = view.findViewById(R.id.currentLoc);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
       getCurrentLocation();
        MutableLiveData<List<Result>> res=new MutableLiveData<>();
        viewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(getContext()))
                .get(ListRestoViewModel.class);
        currentLocation.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                updateListRestaurant(location);
            }
        });
      //  viewModel.getAllRestaurants().observe(this, restaurantItems -> listRestaurant.addAll(restaurantItems));
        if (!listRestaurant.isEmpty()) {
            listRestaurant.get(0).getAdresse();
            Log.e("maps",
                    "onMapReady: " + listRestaurant.get(0).getAdresse()
            );
        } else {
            Log.e("maps",
                    "onMapReady: list vide"
            );
        }
        currentLoc.setOnClickListener(v -> {
             getCurrentLocation();
            Log.e("CURRENT LOCATION", "onClick: " );
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mToolbar.setNavigationIcon(R.drawable.ic_menu_24);
        autocompleteSupportFragment.getView().setVisibility(View.INVISIBLE);
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
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getContext()))
                .get(ListRestoViewModel.class);
       // String stringLocation=currentLocation.getValue().getLatitude()+","+currentLocation.getValue().getLongitude();
        MutableLiveData<List<Result>> res=new MutableLiveData<>();
        String stringLocation="48.696701,2.503480";
        currentLocation.observe(this, new Observer<Location>() {
                    @Override
                    public void onChanged(Location location) {
                        updateListRestaurant(location);
                        configureautocompleteSupportFragment(location);
                    }
                });


    }

    private void updateListRestaurant(Location location) {
        MutableLiveData<List<Result>> res=new MutableLiveData<>();
        String stringLocation=location.getLatitude()+","+location.getLongitude();
        viewModel.getAllRestaurants2(url, stringLocation, radius,type,key)
                .enqueue(new Callback<DataListRestaurant>() {
                    @Override
                    public void onResponse(Call<DataListRestaurant> call, Response<DataListRestaurant> response) {
                        DataListRestaurant data = response.body();
                        System.out.println("isSuccessful "+response.isSuccessful());
                        res.postValue(data.getResults());
                        List <Restaurant> list=new ArrayList<>();
                        for(int i=0; i<data.getResults().size();i++) {
                            list.add(createRestaurantFromResultObject(data.getResults().get(i)));
                            System.out.println(" getBusinessStatus:" + data.getResults().get(i).getName() + "\n");
                        }
                        listRestaurant.addAll(list);
                        for (Restaurant restaurantItem : listRestaurant) {
                            LatLng retaurantLatLng = new LatLng(getLatLngFromAdresse(restaurantItem.getAdresse()).latitude, getLatLngFromAdresse(restaurantItem.getAdresse()).longitude);
                            getTodayLunchByRestaurant(restaurantItem).addOnCompleteListener(task -> {
                                MarkerOptions markerOptions = new MarkerOptions().position(retaurantLatLng)
                                        .title(restaurantItem.getName());
                                if (task.getResult().size()>0) {
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    Log.e("TAG", "markerOptions is GREEN  FOR "+restaurantItem.getName());
                                }
                                Marker  marker= mMap.addMarker(markerOptions);
                                Objects.requireNonNull(marker).setTag(restaurantItem);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(retaurantLatLng));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(retaurantLatLng, 15));
                                mMap.setOnMarkerClickListener(marker1 -> {
                                    Restaurant r=(Restaurant) marker1.getTag();
                                    Log.e("TAG", "onMarkerClick  "+ Objects.requireNonNull(r).getAdresse());
                                    Intent intent = new Intent(getActivity(), DetailRestaurant.class);
                                    // Restaurant detailRestaurant=new Restaurant();
                                    intent.putExtra(KEY_RESTAURANT_ITEM, r);
                                    startActivity(intent);
                                    return true;

                                });
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<DataListRestaurant> call, Throwable t) {
                        System.out.println("onFailure "+t.fillInStackTrace());
                    }
                });
    }

    public final LatLng getLatLngFromAdresse(String adresse) {
        Geocoder geocoder=new Geocoder(getContext());
        LatLng latlng = null;
        try {
            List<Address> adr=  geocoder.getFromLocationName(adresse,1);
            double lat=adr.get(0).getLatitude();
            double lng=adr.get(0).getLongitude();
            latlng= new LatLng(lat, lng);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latlng;
    }
    public Location getCurrentLocation(){
        //CHeck PERMISSION if it's not GRANTED
        if(ActivityCompat.checkSelfPermission(
                Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (getContext(),Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);
        }
        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval((5000));
        LocationCallback locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationRequest==null){
                    Log.e("currentlocation", "locationResult is null");
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
        task.addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
            if(location!=null){
                lag=location .getLatitude();
                lng=location.getLongitude();
                currentLocation.postValue(location);
                LatLng latLng=new LatLng(lag,
                        lng);
                mMap.addMarker(
                        new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                                 .title("You are here!," +latLng.toString()));
                System.out.println("getLatitude="+lag+"et " +
                        "getLongitude="+lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }
        });
        Location location = new Location("service Provider");
        location.setLongitude(lng);
        location.setLatitude(lag);
        currentLocation.postValue(location);
       return location;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (Request_code) {
            case Request_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
        }
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

}