package com.openclassrooms.go4lunch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.ViewModels.ListRestoViewModel;
import com.openclassrooms.go4lunch.ViewModels.ViewModelFactory;
import com.openclassrooms.go4lunch.databinding.ActivityDetailRestaurantBinding;
import com.openclassrooms.go4lunch.models.Restaurant;

public class DetailRestaurant extends AppCompatActivity {
    static final String KEY_RESTAURANT_ITEM="RESTAURANT_ITEM";
    ActivityDetailRestaurantBinding binding;
    private ListRestoViewModel viewModel;
    private ListWorkmatesAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRestaurantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(ListRestoViewModel.class);

        Restaurant restaurant= (Restaurant) getIntent().getSerializableExtra(KEY_RESTAURANT_ITEM);
        System.out.println("KEY_ID_RESTAURANT_ITEM "+  restaurant.getAdresse()+restaurant.getIdR());
        String currentUid= viewModel.getCurrentWorkmates().getUid();
        displayRestaurant(restaurant,currentUid);

        adapter = new ListWorkmatesAdapter(this,true);
        recyclerView=binding.listworkmatesjoining;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
       viewModel.getWorkmatesThatAlreadyChooseRestaurantForTodayLunch(restaurant).observe(this, adapter::submitList);
    }

    private void displayRestaurant(Restaurant restaurant, String currentUid) {

        String ref= "restaurant/"+restaurant.getUrlPicture();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(ref);
        Glide.with(binding.pictureResto.getRootView())
                .load(restaurant.getUrlPicture())
                .into(binding.pictureResto);
        binding.adresseResto.setText(restaurant.getAdresse());
        binding.nameRestaurant.setText(restaurant.getName());
        binding.rating.setRating(restaurant.getRating());
        binding.ButtonWebSite.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(),WebViewActivity.class);
            intent.putExtra("Website", restaurant.getWebSite());
            startActivity(intent);
        });
        viewModel.checkIfCurrentWorkmateLikeThisRestaurant(restaurant).observe(this, aBoolean -> {
            if(!aBoolean) {
                Drawable img = binding.ButtonLike.getContext().getResources().getDrawable( R.drawable.ic_star_empty );
                binding.ButtonLike.setCompoundDrawablesWithIntrinsicBounds( null, img, null, null);
                binding.ButtonLike.setText("Like");
            }
       else{

                Drawable img = binding.ButtonLike.getContext().getResources().getDrawable( R.drawable.ic_star_24 );
                binding.ButtonLike.setCompoundDrawablesWithIntrinsicBounds( null, img, null, null);
                binding.ButtonLike.setText("UnLike");
            }
            binding.ButtonLike.setOnClickListener(v -> {
                if(binding.ButtonLike.getText()=="Like") {
                    viewModel.addLikeRestaurant(restaurant);
                    Drawable img = binding.ButtonLike.getContext().getResources().getDrawable(R.drawable.ic_star_24);
                    binding.ButtonLike.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
                    binding.ButtonLike.setText("UnLike");
                }
                else{
                    viewModel.deleteLikeRestaurant(restaurant);
                    Drawable img = binding.ButtonLike.getContext().getResources().getDrawable(R.drawable.ic_star_empty);
                    binding.ButtonLike.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
                    binding.ButtonLike.setText("Like");
                }
            });
        });
        viewModel.checkIfCurrentWorkmateChoseThisRestaurantForLunch(restaurant,currentUid).observe(this, aBoolean -> {
               if(aBoolean){
                   binding.floatingActionButtonJoin.setImageResource(R.drawable.ic_cancel_24);
            }
            else{
                   binding.floatingActionButtonJoin.setImageResource(R.drawable.ic_check_circle_24);
            }

        });


         binding.floatingActionButtonJoin.setOnClickListener(v -> viewModel.checkIfCurrentWorkmateChoseThisRestaurantForLunch2(restaurant,currentUid).addOnCompleteListener(task -> {
             if(task.getResult().size()>0) {
                 Log.e("tag", "onChanged: deleteLunch");
                 viewModel.deleteLunch(restaurant,currentUid);
                 updateRecyclerview(restaurant);
                 binding.floatingActionButtonJoin.setImageResource(R.drawable.ic_check_circle_24);
             }
             else{
                 Log.e("tag", "onChanged: createLunch");
                 viewModel.createLunch(restaurant);
                 updateRecyclerview(restaurant);
                 binding.floatingActionButtonJoin.setImageResource(R.drawable.ic_cancel_24);
             }
         }));

        binding.ButtonPhone.setOnClickListener(v -> {
            if (restaurant.getPhone() != null){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+restaurant.getPhone()));
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.restaurant_detail_no_phone), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateRecyclerview(Restaurant restaurant) {
        viewModel.getWorkmatesThatAlreadyChooseRestaurantForTodayLunch(restaurant).observe(this, adapter::submitList);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}