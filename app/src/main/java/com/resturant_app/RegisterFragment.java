package com.resturant_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;


public class RegisterFragment extends Fragment {

    private LinearLayout messageLayout, loadingLayout;
    private EditText fullNameEditText, emailEditText, passwordEditText;
    private Button createNewAccountButton, okButton;
    TextView messageTextView;
    GotoHome gotoHome;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        gotoHome = (GotoHome) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        messageLayout = view.findViewById(R.id.layout_message);
        loadingLayout = view.findViewById(R.id.layout_loading);
        messageTextView = view.findViewById(R.id.text_view_message);
        fullNameEditText = view.findViewById(R.id.edit_text_full_name);
        emailEditText = view.findViewById(R.id.edit_text_email);
        passwordEditText = view.findViewById(R.id.edit_text_password);

        okButton = view.findViewById(R.id.button_ok);
        createNewAccountButton = view.findViewById(R.id.button_create_new_account);

        createNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLayout.setVisibility(View.VISIBLE);
                disableSignUpLayout();

                final String fullName = fullNameEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();




                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()){
                    loadingLayout.setVisibility(View.GONE);
                    showMessage("Please Put Some Data");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    loadingLayout.setVisibility(View.GONE);
                    showMessage("Email is badly Formatted");
                    return;
                }

                if (password.length() < 6){
                    loadingLayout.setVisibility(View.GONE);
                    showMessage("Password must be at least 6 chars");
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("file", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isAdmin", false);
                                    editor.apply();


                                            User user = new User(false, new ArrayList<>(), new ArrayList<>());
                                            FirebaseFirestore.getInstance().collection("Users")
                                                    .document(currentUser.getUid()).set(user)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(fullName)
                                                                    .build();
                                                            FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();

                                                            userInfo.updateProfile(profileUpdates)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {



                                                                                gotoHome.GoToHome();
                                                                            }
                                                                        }
                                                                    });


                                                        }
                                                    });

                                } else {
                                    loadingLayout.setVisibility(View.GONE);
                                    showMessage(task.getException().getMessage());
                                }
                            }
                        });


            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageLayout.setVisibility(View.GONE);
                enableSignUpLayout();
            }
        });
        return view;
    }

    private void enableSignUpLayout(){
        fullNameEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        createNewAccountButton.setEnabled(true);
    }

    private void disableSignUpLayout(){
        fullNameEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        createNewAccountButton.setEnabled(false);
    }

    private void showMessage(String message){
        messageLayout.setVisibility(View.VISIBLE);
        messageTextView.setText(message);
    }
}