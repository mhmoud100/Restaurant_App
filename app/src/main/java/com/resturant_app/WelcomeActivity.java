package com.resturant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class WelcomeActivity extends AppCompatActivity {
Boolean isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        isAdmin = false;
        FirebaseFirestore.getInstance().collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    for(DocumentSnapshot doc : task.getResult()){
                        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(doc.getId())){
                            isAdmin = doc.getBoolean("admin");
                        }
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("file" , Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isAdmin" , isAdmin);
                    editor.apply();
                }
            }
        });
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
