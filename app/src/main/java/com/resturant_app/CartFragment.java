package com.resturant_app;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;

public class CartFragment extends Fragment {
    ListView cartList;
    ArrayList<Cart> carts;
    CartAdapter adapter;
    FloatingActionButton buy;
    String text;
    AlertDialog alertDialog;

    private ShowNotification showNotification;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        showNotification = (ShowNotification) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartList = view.findViewById(R.id.cart_list);
        buy = view.findViewById(R.id.buy);
        carts = WelcomeActivity.user.getCarts();
        adapter = new CartAdapter(getContext(), carts, "Cart");
        cartList.setAdapter(adapter);



        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carts.isEmpty()) {
                    Toast.makeText(getContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                alertDialog = new AlertDialog.Builder(getContext()).create();
                text = "";
                for(int i = 0; i < carts.size(); i++){
                    int finalI = i;
                    FirebaseFirestore.getInstance().collection("Products").document(carts.get(i).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful() && task.getResult() != null){
                                Formatter fmt = new Formatter();
                                fmt.format("%.2f", Double.parseDouble(carts.get(finalI).getQuantity()) * Double.parseDouble((String)task.getResult().get("price")));
                                text += "You Ordered: "+task.getResult().get("title")+" , For: "+
                                        fmt + "\n";
//                            Log.i("tag", text);
                                alertDialog.setMessage(text);
                                alertDialog.show();
                            }
                        }
                    });
                }
//                Log.i("tag", text);

                alertDialog.setTitle("Confirm Order");

                alertDialog.setCancelable(true);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Check Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("orders", carts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                    WelcomeActivity.user.addtoOrder(carts);
                                    Log.i("tag", carts.get(0).getId()+"");
                                    Log.i("tag", WelcomeActivity.user.getOrders().size()+"");
                                    showNotification.Show();

                                    WelcomeActivity.user.removeAll();
                                    adapter.notifyDataSetChanged();
                                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .update("carts",WelcomeActivity.user.getCarts()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
//                        Log.i("tag", carts.size()+"");



                    }
                });

            }
        });

        return view;
    }

}