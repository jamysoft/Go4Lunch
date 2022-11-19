package com.openclassrooms.go4lunch.notifications;

import static com.openclassrooms.go4lunch.repository.LunchRepository.getTodayLunch2;
import static com.openclassrooms.go4lunch.repository.LunchRepository.getTodayLunchByRestaurant;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.models.Lunch;
import com.openclassrooms.go4lunch.models.Restaurant;
import com.openclassrooms.go4lunch.ui.MainActivity2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    private List<String> mUsersList;
    private Restaurant mRestaurant;

    @Override
    public void onReceive(Context context, Intent intent) {
        mUsersList = new ArrayList<>();
        mRestaurant=new Restaurant();
        getTodayLunch2(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if(task.getResult().size()!=0) {
                    mRestaurant=querySnapshot.toObjects(Lunch.class).get(0).getRestaurantChoosed();
                    getTodayLunchByRestaurant(mRestaurant).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (task.getResult().size() != 0) {
                                    mUsersList.add(querySnapshot.toObjects(Lunch.class).get(0).getWorkmates().getName());
                                } else {
                                    Log.e("Error", "Current user does not booked restaurant for today ");
                                }
                            } else {
                                Log.e("Error", "Error getting documents: ", task.getException());
                            }
                            Log.e("Error", "booked restaurant for today is "+mRestaurant.getName());
                                Intent i = new Intent(context, MainActivity2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "go4lunch")
                                        .setSmallIcon(R.drawable.ic_launcher_background)
                                        .setContentTitle("go4lunch Reminder ")
                                        .setContentText("Hi "  + ",don't forget your lunch at !"
                                                +mRestaurant.getName()+"on :"+ mRestaurant.getAdresse() + " with " +
                                                mUsersList.toString())
                                        .setAutoCancel(true)
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Hi " + ",don't forget your lunch at !"
                                                +mRestaurant.getName()+" restaurant in : "+ mRestaurant.getAdresse() + " with " +
                                                mUsersList.toString()))
                                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(pendingIntent);

                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                                notificationManagerCompat.notify(123, builder.build());

                        }
                    });
                }
                else{
                    Log.d("Error", "Current user not yet booked a restaurant for lunch today !");
                }
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });

   }
}
