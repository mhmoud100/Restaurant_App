package com.resturant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;

public class SinglePageApp extends AppCompatActivity {
    TextView nameOfitem , Price ,des, Details, arrivalTime, Rating;
    TextView plus , btnNummer ,mins ;
    ImageView imageView;
    FloatingActionButton fab;
    int x;
    Boolean AlreadyAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_page_app);
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String Des  = getIntent().getStringExtra("desc");
        String details  = getIntent().getStringExtra("details");
        String imageUrl = getIntent().getStringExtra("Image");
        String ArrivalTime = getIntent().getStringExtra("ArrivalTime");
        String rating = getIntent().getStringExtra("rating");
        String id = getIntent().getStringExtra("id");
        x = 1;
        AlreadyAdded = false;
        nameOfitem =findViewById(R.id.jotext);
        Price = findViewById(R.id.jocost);
        btnNummer = findViewById(R.id.jobtnNumber);
        plus = findViewById(R.id.jobtnplus);
        mins = findViewById(R.id.jobtnmis);
        des = findViewById(R.id.joDes);
        imageView = findViewById(R.id.imageView);
        fab = findViewById(R.id.fab);
        Details = findViewById(R.id.desc);
        Rating = findViewById(R.id.rating);
        arrivalTime = findViewById(R.id.arrival_time);
        if(imageUrl != null){
            Glide.with(this).load(imageUrl).into(imageView);
        }

        for(int i = 0; i < WelcomeActivity.user.getCarts().size(); i++){
            if(WelcomeActivity.user.getCarts().get(i).getId().equals(id)){
                fab.setImageResource(R.drawable.ic_done);
                AlreadyAdded = true;
            }
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = new Cart(id, btnNummer.getText().toString());

                if(AlreadyAdded){
                    Toast.makeText(SinglePageApp.this, "Already Added", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update("carts",FieldValue.arrayUnion(cart)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        WelcomeActivity.user.addtoCart(cart);
                        fab.setImageResource(R.drawable.ic_done);
                        AlreadyAdded = true;
                    }
                });
            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                btnNummer.setText(x+"");
                double temp = Double.parseDouble(price) * x;
                Formatter fmt = new Formatter();
                fmt.format("%.2f", temp);
                Price.setText(String.valueOf(fmt));

            }
        });
        mins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x>1){
                    x--;
                    btnNummer.setText(x+"");
                    double temp = Double.parseDouble(price) * x;
                    Formatter fmt = new Formatter();
                    fmt.format("%.2f", temp);
                    Price.setText(String.valueOf(fmt));
                }

            }
        });



        nameOfitem.setText(title);
        Price.setText(price);
        des.setText(details);
        Details.setText(Des);
        Rating.setText(rating);
        arrivalTime.setText(ArrivalTime);
    }

}