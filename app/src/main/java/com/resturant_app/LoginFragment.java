package com.resturant_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment {

    private LinearLayout messageLayout, loadingLayout;
    private Button loginButton, okButton;
    private EditText emailEditText, passwordEditText;
    private TextView messageTextView;
    GotoHome gotoHome;
    TextView forgetPassword;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        gotoHome = (GotoHome) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        messageLayout = view.findViewById(R.id.layout_message);
        loadingLayout = view.findViewById(R.id.layout_loading);
        forgetPassword = view.findViewById(R.id.text_view_forgot_password);
        loginButton = view.findViewById(R.id.button_login);
        messageTextView = view.findViewById(R.id.text_view_message);
        okButton = view.findViewById(R.id.button_ok);
        emailEditText = view.findViewById(R.id.edit_text_email);
        passwordEditText = view.findViewById(R.id.edit_text_password);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableLoginLayout();

                View resetPasswordLayout = LayoutInflater.from(getContext()).inflate(R.layout.reset_password_layout, null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(resetPasswordLayout);
                builder.setCancelable(true);

                final AlertDialog alertDialog = builder.create();

                alertDialog.show();

                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        enableLoginLayout();
                    }
                });

                final EditText emailEditText = resetPasswordLayout.findViewById(R.id.edit_text_email);
                Button sendEmailButton = resetPasswordLayout.findViewById(R.id.button_send_email);

                sendEmailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String email = emailEditText.getText().toString().trim();

                        if (email.isEmpty()) {
                            alertDialog.dismiss();
                            showMessage("The Email is Empty");
                            return;
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            alertDialog.dismiss();
                            showMessage("Email is badly Formatted");
                            return;
                        }

                        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                alertDialog.dismiss();
                                if (task.isSuccessful()) showMessage("Reset Email is sent");
                                else showMessage(task.getException().getMessage());
                            }
                        });

                    }
                });

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingLayout.setVisibility(View.VISIBLE);
                disableLoginLayout();

                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()){
                    loadingLayout.setVisibility(View.GONE);
                    showMessage("Please put some Data");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    loadingLayout.setVisibility(View.GONE);
                    showMessage("Email is badly Formatted");
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful() && task.getResult() != null) {
                                                WelcomeActivity.user = task.getResult().toObject(User.class);
                                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("file", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putBoolean("isAdmin", WelcomeActivity.user.getAdmin());
                                                editor.apply();
                                                gotoHome.GoToHome();
                                            }
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
                enableLoginLayout();
            }
        });
        return view;
    }
    private void enableLoginLayout() {
        emailEditText.setEnabled(true);
        passwordEditText.setEnabled(true);
        loginButton.setEnabled(true);
    }

    private void disableLoginLayout() {
        emailEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        loginButton.setEnabled(false);
    }
    private void showMessage(String message){
        messageLayout.setVisibility(View.VISIBLE);
        messageTextView.setText(message);
    }

}