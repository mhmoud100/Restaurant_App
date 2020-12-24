package com.resturant_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class AuthActivity extends AppCompatActivity implements GotoHome{
    private static final int PAGER_LAYOUTS_COUNT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(
                AuthActivity.this.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0 : return new LoginFragment();
                    case 1 : return new RegisterFragment();
                }
                return new LoginFragment();
            }
            @Override
            public int getCount() {
                return PAGER_LAYOUTS_COUNT;
            }
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0 : return "Login";
                    case 1 : return "Signup";
                }
                return super.getPageTitle(position);
            }
        });

        tabLayout.setupWithViewPager(viewPager);


    }


    @Override
    public void GoToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        AuthActivity.this.finishAffinity();
    }
}