package com.resturant_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

TextView userName , userEmail;
ListView orders;
CartAdapter adapter;
ArrayList<Cart> carts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.joUserName1);
        userEmail = view.findViewById(R.id.joEmail);
        orders = view.findViewById(R.id.orders_list);
        carts = WelcomeActivity.user.getOrders();
        adapter = new CartAdapter(getContext(), carts);

        orders.setAdapter(adapter);

        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        return view;
    }
}