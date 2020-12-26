package com.resturant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class HomeActivity extends AppCompatActivity {

    ImageView drawer;
    private SlidingRootNav slidingRootNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = findViewById(R.id.drawer);

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
        TextView name = view.findViewById(R.id.textname);
        TextView home = view.findViewById(R.id.home);
        TextView profile= view.findViewById(R.id.profile);
        TextView logout = view.findViewById(R.id.logout);
        name.setText("Hello "+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.setTextColor(Color.BLACK);
                home.setTextColor(Color.parseColor("#F1C852"));
                setFragment(new HomeFragment());
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setTextColor(Color.BLACK);
                profile.setTextColor(Color.parseColor("#F1C852"));
                setFragment(new ProfileFragment());
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

        setFragment(new HomeFragment());
        home.setTextColor(Color.parseColor("#F1C852"));
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.coordinator_layout, fragment);
        fragmentTransaction.commit();
    }
}
//FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(HomeActivity.this, AuthActivity.class));
//                HomeActivity.this.finishAffinity();