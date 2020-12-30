package com.resturant_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class activity_add_product extends AppCompatActivity {
    ImageView productImage;
    EditText title, desc, price, arrivalTime, rating, details;
    Button submit;
    private final int PICK_IMAGE_REQUEST = 71;
    Uri fileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        title = findViewById(R.id.edit_text_item_title);
        desc = findViewById(R.id.edit_text_item_description);
        price = findViewById(R.id.edit_text_item_price);
        arrivalTime = findViewById(R.id.edit_text_item_arrivalTime);
        rating = findViewById(R.id.edit_text_item_rating);
        details = findViewById(R.id.edit_text_item_grediants);
        productImage = findViewById(R.id.edit_item_image);
        submit = findViewById(R.id.button_submit);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
                if (ActivityCompat.checkSelfPermission(activity_add_product.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity_add_product.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
                } else {
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String Desc = desc.getText().toString();
                String Price = price.getText().toString();
                String ArrivalTime = arrivalTime.getText().toString();
                String Rating = rating.getText().toString();
                String Details = details.getText().toString();
                if(Title.isEmpty() || Desc.isEmpty() || Price.isEmpty() || ArrivalTime.isEmpty() || Rating.isEmpty() || Details.isEmpty() || fileImage == null){
                    Toast.makeText(activity_add_product.this, "Put Data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> product = new HashMap<>();
                product.put("title", Title);
                product.put("desc", Desc);
                product.put("price", Price);
                product.put("arrivalTime", ArrivalTime);
                product.put("rating", Rating);
                product.put("details", Details);

                FirebaseFirestore.getInstance().collection("Products").add(product).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(activity_add_product.this, "done", Toast.LENGTH_SHORT).show();
                            FirebaseStorage.getInstance().getReference().child("Products").child(task.getResult().getId()).child("Product_Image").putFile(fileImage);

                            startActivity(new Intent(activity_add_product.this, HomeActivity.class));

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PICK_IMAGE_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            } else {
                //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            fileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileImage);
                productImage.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}