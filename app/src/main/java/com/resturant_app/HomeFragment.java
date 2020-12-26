package com.resturant_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<Product> products;
    RecyclerView productView;
    ProductAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        productView = view.findViewById(R.id.recycler_view);
        productView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
//        Product product1 = new Product("Susage Pizza", "Spicy Chicken Dimsum", "100", "10-15 Min", "4.5", "Spicy Food Contains Spicy");
        products = new ArrayList<>();

//        FirebaseFirestore.getInstance().collection("Products").add(product1);

        FirebaseFirestore.getInstance().collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){

                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        Product product = documentSnapshot.toObject(Product.class);
                        product.setPid(documentSnapshot.getId());
                        products.add(product);
                    }
                    adapter = new ProductAdapter(getContext(), products);
                    productView.setItemViewCacheSize(products.size());
                    productView.setAdapter(adapter);

                }else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}