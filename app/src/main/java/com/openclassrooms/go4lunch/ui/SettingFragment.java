package com.openclassrooms.go4lunch.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.ViewModels.ListRestoViewModel;
import com.openclassrooms.go4lunch.ViewModels.ViewModelFactory;
import com.openclassrooms.go4lunch.databinding.ActivityDetailRestaurantBinding;
import com.openclassrooms.go4lunch.databinding.FragmentListRestoBinding;
import com.openclassrooms.go4lunch.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    private ListRestoViewModel viewModel;
    private Switch settingSwitch;
    public SettingFragment() {
        // Required empty public constructor
    }
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance())
                .get(ListRestoViewModel.class);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        settingSwitch=binding.settingSwitch;
        viewModel.getIsNotificationActiveOfCurrentWorkmates().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                settingSwitch.setChecked(aBoolean);
            }
        });
        binding.settingsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setIsNotificationActiveOfCurrentWorkmates(settingSwitch.isChecked());
            }
        });
    }

}