package com.example.kedaikopi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kedaikopi.Model.Reservasi;
import com.example.kedaikopi.Prevalent.ReservasiViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityAdminUserReseKedai extends AppCompatActivity {

    private RecyclerView kedaiList;
            RecyclerView.LayoutManager layoutManager;
    private DatabaseReference reservasiListRef;
    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_rese_kedai);
        userID=getIntent().getStringExtra("uid");
        kedaiList = findViewById(R.id.kedai_list);
        kedaiList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        kedaiList.setLayoutManager(layoutManager);
        reservasiListRef = FirebaseDatabase.getInstance().getReference().child("Booking List").child("Admin view").child(userID).child("Kedai");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Reservasi> options=
                new FirebaseRecyclerOptions.Builder<Reservasi>()
                        .setQuery(reservasiListRef,Reservasi.class).build();
            FirebaseRecyclerAdapter<Reservasi, ReservasiViewHolder> adapter =new FirebaseRecyclerAdapter<Reservasi, ReservasiViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReservasiViewHolder holder, int i, @NonNull Reservasi model) {
                holder.txtProductQuantity.setText(model.getQuantity()+" Meja");
                holder.txtProductPrice.setText("Rp."+model.getPrice());
                holder.txtProductName.setText(model.getKname());
            }

            @NonNull
            @Override
            public ReservasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservasi_item_layout,parent,false);
                ReservasiViewHolder holder = new ReservasiViewHolder(view);
                return holder;
            }
        };
            kedaiList.setAdapter(adapter);
            adapter.startListening();
    }
}