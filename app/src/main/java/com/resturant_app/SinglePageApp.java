package com.resturant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Formatter;

public class SinglePageApp extends AppCompatActivity {
    TextView nameOfitem , Price ,des;
    TextView plus , btnNummer ,mins ;
    int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_page_app);
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String Des  = getIntent().getStringExtra("details");
        x = 1;

        nameOfitem =findViewById(R.id.jotext);
        Price = findViewById(R.id.jocost);
        btnNummer = findViewById(R.id.jobtnNumber);
        plus = findViewById(R.id.jobtnplus);
        mins = findViewById(R.id.jobtnmis);
        des = findViewById(R.id.joDes);

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
                if(x>0){
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