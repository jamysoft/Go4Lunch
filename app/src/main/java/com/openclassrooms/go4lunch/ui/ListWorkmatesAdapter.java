package com.openclassrooms.go4lunch.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.R;

public class ListWorkmatesAdapter extends ListAdapter<WorkmatestItem,ListWorkmatesAdapter.ViewHolder> {
    LifecycleOwner owner;
    Boolean fromDetailFragement;
    protected ListWorkmatesAdapter(LifecycleOwner owner,boolean fromDetailFragement) {
        super(new ListNeighbourItemCallback());
        this.owner =owner;
        this.fromDetailFragement=fromDetailFragement;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_workmates,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), owner,fromDetailFragement);

    }

    private static class ListNeighbourItemCallback extends DiffUtil.ItemCallback<WorkmatestItem> {

        @Override
        public boolean areItemsTheSame(@NonNull WorkmatestItem oldItem, @NonNull WorkmatestItem newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getUrlPicture().equals(newItem.getUrlPicture());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull WorkmatestItem oldItem, @NonNull WorkmatestItem newItem) {
            return oldItem.equals(newItem);
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView nameTextView;
        private final ImageView PictureImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            PictureImageView=itemView.findViewById(R.id.picture);

        }

        public void bind(WorkmatestItem item, LifecycleOwner owner,Boolean fromDetailFragement ) {
            nameTextView.setText(item.getName() + " has 'nt decided yet");
            if(fromDetailFragement){
               nameTextView.setText(item.getName() + " is join !");
           }
           else {
               item.getTodayLunch().observe(owner, lunch -> nameTextView.setText(item.getName() + " is eating " +
                       lunch.getRestaurantChoosed().getType() + " (" +
                       lunch.getRestaurantChoosed().getName() + " )"));
           }


            Glide.with(PictureImageView.getRootView())
                    .load(item.getUrlPicture())
                    .circleCrop()
                    .into(PictureImageView);
        }
    }
}
