package com.resturant_app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Formatter;

import androidx.annotation.NonNull;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Cart> carts;
    private String name;

    public CartAdapter(Context context, ArrayList<Cart> carts, String name) {
        this.context = context;
        this.carts = carts;
        this.name = name;
    }

    @Override
    public int getCount() {
        return carts.size();
    }

    @Override
    public Object getItem(int position) {
        return carts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, null, false);
        ImageView foodImage = convertView.findViewById(R.id.food_image);
        ImageView delete = convertView.findViewById(R.id.delete);
        TextView title = convertView.findViewById(R.id.food_name);
        TextView desc = convertView.findViewById(R.id.food_desc);
        TextView price = convertView.findViewById(R.id.food_price);


        TextView q = convertView.findViewById(R.id.quantity);
        Cart item = carts.get(position);
        FirebaseFirestore.getInstance().collection("Products").document(item.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    title.setText((String)task.getResult().get("title"));
                    desc.setText((String) task.getResult().get("desc"));
                    Double newprice = Double.parseDouble((String) task.getResult().get("price")) * Double.parseDouble(item.getQuantity());
                    Formatter fmt = new Formatter();
                    fmt.format("%.2f", newprice);
                    price.setText(String.valueOf(fmt));
                }
            }
        });
        if(name.equals("Profile")){

            delete.setVisibility(View.GONE);
        }
        q.setText("quantity :"+carts.get(position).getQuantity());
        FirebaseStorage.getInstance().getReference().child("Products").child(item.getId()).child("Product_Image").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    Glide.with(context).load(task.getResult()).into(foodImage);
                }else {
                    foodImage.setImageResource(R.mipmap.logo_test);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update("carts", FieldValue.arrayRemove(carts.get(position))).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       WelcomeActivity.user.removeFromCart(carts.get(position));
                       notifyDataSetChanged();
                    }
                });
            }
        });

        return convertView;
    }
}
