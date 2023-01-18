package com.example.kedaikopi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kedaikopi.Model.Kedai;
import com.example.kedaikopi.Model.Reservasi;
import com.example.kedaikopi.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class KedaiDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseReference KedaiRef;
    private Spinner spinner;
    private Button addToCartButton;
    private ImageSlider mainslider;
    private ElegantNumberButton numberButton;
    private TextView productPrice,productDescription,productName,phoneKedai,alamatKedai, menuKopi;
    private String productID="", state = "Normal";
    private String menu;
    private Reservasi reservasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kedai_details);
        productID = getIntent().getStringExtra("pid");
        addToCartButton =(Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        mainslider = (ImageSlider) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        phoneKedai = (TextView)findViewById(R.id.product_phone_details);
        alamatKedai = (TextView)findViewById(R.id.alamat_details);
        getKedaiDetails(productID);
        KedaiRef = FirebaseDatabase.getInstance().getReference().child("Kedai");



        menuKopi=(TextView)findViewById(R.id.tvpmenu);
        spinner=(Spinner)findViewById(R.id.spiner);
        spinner.setOnItemSelectedListener(this);
        String[] menu = {"Latte ","Cappuccino ","Kopi Susu Gula Aren ","Cold Brew ",
                "Esspresso ","Americano ","Coffee Mocha ",};
        reservasi = new Reservasi();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,menu);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(KedaiDetailsActivity.this,"Anda dapat menambahkan lebih banyak Pemesanan, setelah pesanan Anda valid atau dikonfirmasi",Toast.LENGTH_LONG).show();
            }
                else {
                addToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckBookingState();
    }
    private void addToCartList() {



        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        final DatabaseReference reservasiListRef = FirebaseDatabase.getInstance().getReference().child("Booking List");
        final HashMap<String, Object>cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("kname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("menu",menuKopi.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        reservasiListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Kedai").child(productID).updateChildren(cartMap).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            reservasiListRef.child("Admin view").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Kedai").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(KedaiDetailsActivity.this, "Added to Booking", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(KedaiDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    //<---------------get data detail kedai ----------------->
    private void getKedaiDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Kedai");

        final List<SlideModel> remoteimages =new ArrayList<>();

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if (snapshot.exists()) {
                    Kedai products = snapshot.getValue(Kedai.class);
                    remoteimages.add(new SlideModel(snapshot.child("url").getValue().toString(),snapshot.child("kname").getValue().toString(), ScaleTypes.CENTER_CROP));
                    mainslider.setImageList(remoteimages,ScaleTypes.CENTER_CROP);

                    productName.setText(products.getKname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    phoneKedai.setText(products.getNohp());
                    alamatKedai.setText(products.getAlamat());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //--------------------status booking for user---------------------------------
    private void CheckBookingState() {
        DatabaseReference bookingRef;
        bookingRef = FirebaseDatabase.getInstance().getReference().child("Reservasi").child(Prevalent.currentOnlineUser.getPhone());
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    if (shippingState.equals("Valid")){
                        state ="Order Shipped";
                    }
                    else if (shippingState.equals("Not Valid")){
                        state ="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        menu=spinner.getSelectedItem().toString();
        menuKopi.setText(menu);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}