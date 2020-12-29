package com.resturant_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> products;
    private Product item;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_item_view, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getItemCount(){
        return products.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        item = products.get(position);
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDesc());
        holder.price.setText(item.getPrice());
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SinglePageApp.class);
                intent.putExtra("title", products.get(position).getTitle());
                intent.putExtra("desc", products.get(position).getDesc());
                intent.putExtra("price", products.get(position).getPrice());
                intent.putExtra("details", products.get(position).getDetails());
                intent.putExtra("ArrivalTime", products.get(position).getArrivalTime());
                intent.putExtra("rating", products.get(position).getRating());
                intent.putExtra("Image", products.get(position).getImageURL());
                intent.putExtra("id", products.get(position).getPid());
                context.startActivity(intent);
            }
        });
//        Log.i("tag", ""+(products.get(position).getImageURL() ));
        if(item.getImageURL() != null){

            Glide.with(context).load(item.getImageURL()).into(holder.photo);
        } else {
            holder.photo.setImageResource(R.mipmap.logo_test);
        }



    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView title, desc, price;
        LinearLayout linear;
        ViewHolder(View view){
            super(view);
             title = view.findViewById(R.id.item_title);
             desc = view.findViewById(R.id.item_desc);
             price = view.findViewById(R.id.item_price);
             photo = view.findViewById(R.id.item_image);
             linear = view.findViewById(R.id.linear1);

        }

    }
}
//TextView title = convertView.findViewById(R.id.item_title);
//        TextView desc = convertView.findViewById(R.id.item_desc);
//        TextView price = convertView.findViewById(R.id.item_price);
//        ImageView photo = convertView.findViewById(R.id.item_image);
//        Product item = products.get(position);
//        title.setText(item.getTitle());
//        desc.setText(item.getDesc());
//        price.setText(item.getPrice());
//        StorageReference ref = FirebaseStorage.getInstance().getReference();
//        ref.child("Products").child(item.getPid()).child("Product_Image").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful() && task.getResult() != null) {
//                    Glide.with(context).load(task.getResult()).into(photo);
//                } else {
//                    photo.setImageResource(R.mipmap.logo_test);
//                }
//            }
//        });
