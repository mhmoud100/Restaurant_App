package com.resturant_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.activity_item_view, null, false);
        TextView title = convertView.findViewById(R.id.item_title);
        TextView desc = convertView.findViewById(R.id.item_desc);
        TextView price = convertView.findViewById(R.id.item_price);
        ImageView photo = convertView.findViewById(R.id.item_image);
        Product item = products.get(position);
        title.setText(item.getTitle());
        desc.setText(item.getDesc());
        price.setText(item.getPrice());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),SinglePageApp.class);
                intent.putExtra("title",item.getTitle());
                intent.putExtra("Price",item.getPrice());
                intent.putExtra("Details",item.getDetails());
                context.startActivity(intent);


            }
        });
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child("Products").child(item.getPid()).child("Product_Image").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Glide.with(context).load(task.getResult()).into(photo);
                } else {
                    photo.setImageResource(R.mipmap.logo_test);
                }
            }
        });

        return convertView;
    }
}
