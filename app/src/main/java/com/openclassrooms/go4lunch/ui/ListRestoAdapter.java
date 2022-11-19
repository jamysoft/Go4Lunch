package com.openclassrooms.go4lunch.ui;

import static com.openclassrooms.go4lunch.repository.LunchRepository.getTodayLunchByRestaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.models.Restaurant;
import java.io.IOException;
import java.util.List;

public class ListRestoAdapter extends ListAdapter<Restaurant,ListRestoAdapter.ViewHolder> {

    private static Context context;
    private Location currentLocation;
    static final String KEY_RESTAURANT_ITEM="RESTAURANT_ITEM";


    protected ListRestoAdapter( Context context, Location currentLocation) {
        super(new ListNeighbourItemCallback());
        this.context=context;
        this.currentLocation=currentLocation;
    }

    @NonNull
    @Override
    public ListRestoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurants, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListRestoAdapter.ViewHolder holder, int position) {
        Restaurant restaurant =getItem(position);
        holder.bind(restaurant,currentLocation);
        holder.itemView.setOnClickListener(view -> {
            Intent monIntent = new Intent(getContext(),DetailRestaurant.class);
            monIntent.putExtra(KEY_RESTAURANT_ITEM, restaurant);
            getContext().startActivity(monIntent);
            System.out.println("restaurantItem.getIdR()"+restaurant.getIdR());
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView NameRestoTextView;
        private final TextView DistanceRestoTextView;
        private final TextView AdresseRestoTextView;
        private final TextView NbPersonneTextView;
        private final TextView TimingRestoTextView;
        private final RatingBar RatingRestoRatingBar;
        private final   ImageView PictureRestoImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NameRestoTextView = itemView.findViewById(R.id.name_resto);
            AdresseRestoTextView = itemView.findViewById(R.id.adresse_resto);
            DistanceRestoTextView=itemView.findViewById(R.id.distance_resto);
            NbPersonneTextView=itemView.findViewById(R.id.nb_personne);
            TimingRestoTextView=itemView.findViewById(R.id.timing_resto);
            RatingRestoRatingBar=itemView.findViewById(R.id.rating);
            PictureRestoImageView=itemView.findViewById(R.id.picture_resto);
        }
        public void bind(Restaurant item,Location currentLocation) {
            NameRestoTextView.setText(item.getName());
            AdresseRestoTextView.setText(item.getType()+" - "+item.getAdresse());
            TimingRestoTextView.setText(item.getHourClosed());
            RatingRestoRatingBar.setRating(item.getRating());
            String ref= "restaurant/"+item.getUrlPicture();
           Glide.with(PictureRestoImageView.getRootView())
                    .load(item.getUrlPicture())
                    .into(PictureRestoImageView);
            getTodayLunchByRestaurant(item).addOnCompleteListener(task -> {
                if(task.getResult().size()==0){
                    NbPersonneTextView.setVisibility(View.INVISIBLE);
                }else {
                    NbPersonneTextView.setText("(" + task.getResult().size() + ")");
                }
            });
            if(currentLocation.getLatitude()!=0 && currentLocation.getLongitude()!=0) {
                double distance=getDistanceBetween(currentLocation,item.getAdresse());
                DistanceRestoTextView.setText(String.format("%.2f", distance / 1000) + "km");
            }
        }
    }


    private static class ListNeighbourItemCallback extends DiffUtil.ItemCallback<Restaurant> {

        @Override
        public boolean areItemsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getAdresse().equals(newItem.getAdresse());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Restaurant oldItem, @NonNull Restaurant newItem) {
            return oldItem.equals(newItem);
        }
    }


    public static Context getContext() {
        return context;
    }

    public  static Double getDistanceBetween(Location location, String adress2) {
        LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng latLng2 = new LatLng(getLatLngFromAdress(adress2).latitude, getLatLngFromAdress(adress2).longitude);
        // on below line we are calculating the distance between latLng1 and latLng2
        return SphericalUtil.computeDistanceBetween(latLng1, latLng2);
    }
    public static  LatLng getLatLngFromAdress(String adress) {
        Geocoder geocoder=new Geocoder(getContext());
        LatLng latlng = new LatLng(0,0);
        try {
            List<Address> adr=  geocoder.getFromLocationName(adress,1);
            double lat=adr.get(0).getLatitude();
            double lng=adr.get(0).getLongitude();
            latlng= new LatLng(lat, lng);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latlng;
    }
}
