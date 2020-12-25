package com.resturant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ArrayList<Product> products;
    GridView gridView;
    ProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridView = findViewById(R.id.grid);

//        Product product1 = new Product("Susage Pizza", "Spicy Chicken Dimsum", "9.80", "10-15 Min", "4.5", "Spicy Food Contains Spicy");
        products = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){

                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        Product product = documentSnapshot.toObject(Product.class);
                        product.setPid(documentSnapshot.getId());
                        products.add(product);
                    }
                    adapter = new ProductAdapter(HomeActivity.this, products);
                    gridView.setAdapter(adapter);

                }else{
                    Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
//FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(HomeActivity.this, AuthActivity.class));
//                HomeActivity.this.finishAffinity();