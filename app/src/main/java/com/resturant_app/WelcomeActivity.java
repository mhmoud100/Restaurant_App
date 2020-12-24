package com.resturant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Timer();

    }

    public void Timer() {

        // for (long i = Time; i < 86400000; i = i += 60000) {

        new CountDownTimer(4000, 4000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                        WelcomeActivity.this.finishAffinity();
                    }else {
                        startActivity(new Intent(WelcomeActivity.this, AuthActivity.class));
                        WelcomeActivity.this.finishAffinity();
                    }
                }


            }
        }.start();
        // }


    }
}
