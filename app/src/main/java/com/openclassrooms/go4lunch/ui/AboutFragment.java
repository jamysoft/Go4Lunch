package com.openclassrooms.go4lunch.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.ViewModels.ListRestoViewModel;
import com.openclassrooms.go4lunch.ViewModels.ViewModelFactory;
import com.openclassrooms.go4lunch.databinding.FragmentAboutBinding;
import com.openclassrooms.go4lunch.models.Lunch;

public class AboutFragment extends Fragment {
    ListRestoViewModel listRestoViewModel;
    private FragmentAboutBinding binding;
    private TextView nameRestaurant;
    private TextView adresseRestaurant;
    private TextView phoneRestaurant;
    private TextView websiteRestaurant;


    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        nameRestaurant=binding.textNameRestaurant;
        adresseRestaurant= binding.textAdresseRestaurant;
        websiteRestaurant= binding.textWebSite;
        phoneRestaurant=binding.textPhone;
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        listRestoViewModel= new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance())
                .get(ListRestoViewModel.class);
        listRestoViewModel.getLunchRepository()
                .getTodayLunch(listRestoViewModel.getCurrentWorkmates().getUid()).observe(this, lunch -> configureLunch(lunch));
    }
    private void configureLunch(Lunch lunch) {
        nameRestaurant.setText(lunch.getRestaurantChoosed().getName());
        adresseRestaurant.setText(lunch.getRestaurantChoosed().getAdresse());
        phoneRestaurant.setText(lunch.getRestaurantChoosed().getPhone());
        websiteRestaurant.setText(lunch.getRestaurantChoosed().getWebSite());
        ImageView restaurantImageView= binding.imageRestaurant;
        String ref= "restaurant/"+lunch.getRestaurantChoosed().getUrlPicture();
        Glide.with(restaurantImageView.getRootView())
                .load(lunch.getRestaurantChoosed().getUrlPicture())
                .into(restaurantImageView);

    }
}