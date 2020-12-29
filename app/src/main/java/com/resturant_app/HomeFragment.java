package com.resturant_app;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<Product> products;
    RecyclerView productView;
    ProductAdapter adapter;
    int length;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        productView = view.findViewById(R.id.recycler_view);
        productView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        products = new ArrayList<>();
        length = 0;

        FirebaseFirestore.getInstance().collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){

                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        Product product = documentSnapshot.toObject(Product.class);
//                        Log.i("tag", product.getTitle());
                        product.setPid(documentSnapshot.getId());
                        FirebaseStorage.getInstance().getReference().child("Products").child(documentSnapshot.getId()).child("Product_Image").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful() && task.getResult() != null) {
                                    product.setImageURL(task.getResult().toString());
//                                    Log.i("tag", task.getResult().toString());
                                    adapter.notifyDataSetChanged();
                                    length++;
                                }
                            }
                        });

                        products.add(product);
                    }

                    adapter = new ProductAdapter(getContext(), products);
                    productView.setItemViewCacheSize(products.size());
                    productView.setAdapter(adapter);

//                    Log.i("tag", ""+length);
                }else{
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}