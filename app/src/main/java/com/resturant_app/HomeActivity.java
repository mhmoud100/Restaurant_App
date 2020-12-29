package com.resturant_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ShowNotification {

    ImageView drawer;
    private SlidingRootNav slidingRootNav;
    Boolean isAdmin;
    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = findViewById(R.id.drawer);
        isAdmin = false;
        notificationManager = NotificationManagerCompat.from(this);
        slidingRootNav = new SlidingRootNavBuilder(this)

                .withMenuOpened(false)
                .withMenuLayout(R.layout.nav_menu)
                .inject();

        View view = slidingRootNav.getLayout();
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slidingRootNav.isMenuOpened()){

                    slidingRootNav.closeMenu();
                    drawer.setImageResource(R.drawable.ic_drawer_icon);

                }else{
                    slidingRootNav.openMenu();
                    drawer.setImageResource(R.drawable.ic_back);
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("file" , Context.MODE_PRIVATE);
        Boolean isAdmin = sharedPreferences.getBoolean("isAdmin" , false);
        TextView name = view.findViewById(R.id.textname);
        TextView home = view.findViewById(R.id.home);
        TextView profile= view.findViewById(R.id.profile);
        TextView cart = view.findViewById(R.id.cart);
        TextView logout = view.findViewById(R.id.logout);
        TextView add = view.findViewById(R.id.add);

        if(isAdmin){
            add.setVisibility(View.VISIBLE);
        }
        name.setText("Hello "+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setTextColor(Color.BLACK);
                cart.setTextColor(Color.BLACK);
                home.setTextColor(Color.parseColor("#F1C852"));
                setFragment(new HomeFragment());
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setTextColor(Color.BLACK);
                cart.setTextColor(Color.BLACK);
                profile.setTextColor(Color.parseColor("#F1C852"));
                setFragment(new ProfileFragment());
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setTextColor(Color.BLACK);
                profile.setTextColor(Color.BLACK);
                cart.setTextColor(Color.parseColor("#F1C852"));
                setFragment(new CartFragment());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, AuthActivity.class));
                HomeActivity.this.finishAffinity();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, activity_add_product.class));
            }
        });

        setFragment(new HomeFragment());
        home.setTextColor(Color.parseColor("#F1C852"));
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.coordinator_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void Show() {


            // for (long i = Time; i < 86400000; i = i += 60000) {

            new CountDownTimer(10000, 10000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            String CHANNEL_ID = "my_channel_01";
                            CharSequence name = "my_channel";
                            String Description = "This is my channel";
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                            mChannel.setDescription(Description);
                            mChannel.enableLights(true);
                            mChannel.setLightColor(Color.RED);
                            mChannel.enableVibration(true);
                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            mChannel.setShowBadge(false);
                            notificationManager.createNotificationChannel(mChannel);
                        }

                        Notification notification = new NotificationCompat.Builder(HomeActivity.this, "my_channel_01")
                                .setContentTitle("Resturant App")
                                .setContentText("الاكل وصل يا زميييلي (:")
                                .setSmallIcon(R.mipmap.logo_test)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                                .build();

                        notificationManager.notify(1, notification);



                    WelcomeActivity.user.removeAllorders();
                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update("orders",WelcomeActivity.user.getOrders()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(HomeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
                    }


                }
            }.start();
            // }



//        @RequiresApi(api = Build.VERSION_CODES.M)
//        public void ShowNotification() {
//
////        Toast.makeText(getContext(), "Time", Toast.LENGTH_SHORT).show();
//
//        }
    }
}
//FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(HomeActivity.this, AuthActivity.class));
//                HomeActivity.this.finishAffinity();