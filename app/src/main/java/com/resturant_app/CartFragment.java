package com.resturant_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartList = view.findViewById(R.id.cart_list);
        buy = view.findViewById(R.id.buy);
        carts = WelcomeActivity.user.getCarts();
        adapter = new CartAdapter(getContext(), carts);
        cartList.setAdapter(adapter);
        text = "";
        for(int i = 0; i < carts.size(); i++){
            int finalI = i;
            FirebaseFirestore.getInstance().collection("Products").document(carts.get(i).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Formatter fmt = new Formatter();
                    fmt.format("%.2f", Double.parseDouble(carts.get(finalI).getQuantity()) * Double.parseDouble((String)task.getResult().get("price")));
                    text += "You Ordered: "+task.getResult().get("title")+" , For: "+
                              fmt + "\n";
                }
            });
        }
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Confirm Order");
                alertDialog.setMessage(text);
                alertDialog.setCancelable(true);


                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("orders", carts).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                    WelcomeActivity.user.addtoOrder(carts);

                                }
                            });

                        carts.clear();
                        WelcomeActivity.user.removeAll();
                        adapter.notifyDataSetChanged();
                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("carts",carts).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        return view;
    }
}