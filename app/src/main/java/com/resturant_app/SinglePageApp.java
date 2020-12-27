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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.util.Formatter;

public class SinglePageApp extends AppCompatActivity {
    TextView nameOfitem , Price ,des;
    TextView plus , btnNummer ,mins ;
    ImageView imageView;
    int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_page_app);
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String Des  = getIntent().getStringExtra("details");
        String imageUrl = getIntent().getStringExtra("Image");
        x = 1;

        nameOfitem =findViewById(R.id.jotext);
        Price = findViewById(R.id.jocost);
        btnNummer = findViewById(R.id.jobtnNumber);
        plus = findViewById(R.id.jobtnplus);
        mins = findViewById(R.id.jobtnmis);
        des = findViewById(R.id.joDes);
        imageView = findViewById(R.id.imageView);
        if(imageUrl != null){
            Glide.with(this).load(imageUrl).into(imageView);
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                btnNummer.setText(x+"");
                double temp = Double.parseDouble(price) * x;
                Formatter fmt = new Formatter();
                fmt.format("%.2f", temp);
                Price.setText("$" +fmt);

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
                    Price.setText("$" +fmt);
                }

            }
        });



        nameOfitem.setText(title);
        Price.setText("$" +price);
        des.setText(Des);
    }

}