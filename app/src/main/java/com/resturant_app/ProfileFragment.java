package com.resturant_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

TextView userName , userEmail , userPhone;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.joUserName1);
        userEmail = view.findViewById(R.id.joEmail);
        userPhone = view.findViewById(R.id.joPhone);

        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        userEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        userPhone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        return view;
    }
}