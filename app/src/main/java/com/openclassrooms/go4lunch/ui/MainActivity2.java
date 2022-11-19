package com.openclassrooms.go4lunch.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.ViewModels.ListRestoViewModel;
import com.openclassrooms.go4lunch.ViewModels.ViewModelFactory;
import com.openclassrooms.go4lunch.databinding.ActivityMain2Binding;
import com.openclassrooms.go4lunch.notifications.AlarmReceiver;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {
    ActivityMain2Binding binding;
    BottomNavigationView mBottomNavigationView;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private AppBarConfiguration mAppBarConfiguration;
    private ListRestoViewModel viewModel;
    private Toolbar mToolbar ;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code = 101;
    private double lag, lng;
  private Location currentLocation=new Location("service Provider");
    static String TAG="ERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.ic_menu_24);
        mToolbar=binding.toolbar;
        mBottomNavigationView=binding.bottomnvigationview;
        mNavigationView=binding.navView;
        mDrawer = binding.drawerLayout;
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplicationContext())).get(ListRestoViewModel.class);
      //  setCurrentLocation();
     //   configureBottomNavigationView();
         configureNavigationView();
        configureProfile();
        createNotificationChannel();
        setAlarm();

    }

    private void configureProfile() {
        String currentWorkmateName= viewModel.getCurrentWorkmates().getDisplayName();
        String currentWorkmateEmail= viewModel.getCurrentWorkmates().getEmail();
        Uri currentWorkmatePicture=viewModel.getCurrentWorkmates().getPhotoUrl();
        TextView txtProfileName = mNavigationView.getHeaderView(0).findViewById(R.id.name);
        txtProfileName.setText(currentWorkmateName);
        TextView txtProfileEmail = mNavigationView.getHeaderView(0).findViewById(R.id.email);
        txtProfileEmail.setText(currentWorkmateEmail);
        ImageView pictureImageView=mNavigationView.getHeaderView(0).findViewById(R.id.imageView);
        Glide.with(pictureImageView.getRootView())
                .load(currentWorkmatePicture)
                .circleCrop()
                .into(pictureImageView);
    }

    private void configureNavigationView() {
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.settingFragment, R.id.aboutFragment)
                .setOpenableLayout(mDrawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);
        NavigationUI.setupWithNavController(mBottomNavigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText( getApplicationContext(),"MenuItem"+item.getItemId(), Toast.LENGTH_SHORT).show();
       System.out.println("getItemId"+item.getItemId());
        return super.onOptionsItemSelected(item);
    }
    private void setAlarm() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,12);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ReminderRestaurant";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("go4lunch",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
/*
    public void setCurrentLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getApplicationContext());
        //CHeck PERMISSION if it's not GRANTED
        if(ActivityCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_code);
        }
        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval((5000));
        LocationCallback locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //Toast.makeText(getApplicationContext(), "locationResult is "+locationResult, Toast.LENGTH_SHORT).show();
                if(locationRequest==null){
                    //  Toast.makeText(getContext(), "locationResult is null ", Toast.LENGTH_SHORT).show();
                    Log.e("currentlocation", "locationResult is null");
                }
                for (Location location:locationResult.getLocations() ){
                    if(location!=null){

                        currentLocation.setLongitude(location.getLongitude());
                        currentLocation.setLatitude(location.getLatitude());
                        viewModel.setCurrentLocation(currentLocation);
                        //  Toast.makeText(getContext(), "locationResult  Longitude ="+location.getLongitude()+" Altitude "+location.getAltitude(), Toast.LENGTH_SHORT).show();
                        Log.e("currentlocation", "onLocationResult: MAINACTIVITY "+location.getLongitude()+" Latitude "+location.getLatitude());
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this, location -> {
            if(location!=null){
                lag=location .getLatitude();
                lng=location.getLongitude();
                viewModel.setCurrentLocation(location);
                currentLocation.setLongitude(lng);
                 currentLocation.setLatitude(lag);
                Log.e(TAG, "onSuccess  "+viewModel.getCurrentLocation().getLongitude()+" "+lag);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (Request_code) {
            case Request_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Request_code  "+Request_code);
                    setCurrentLocation();
                }
        }
    }

 */


}