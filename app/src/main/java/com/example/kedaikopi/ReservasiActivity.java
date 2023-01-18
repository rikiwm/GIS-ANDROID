package com.example.kedaikopi;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kedaikopi.Model.Reservasi;
import com.example.kedaikopi.Prevalent.Prevalent;
import com.example.kedaikopi.Prevalent.ReservasiViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class    ReservasiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;
    private int overTotalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasi);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextProcessBtn = (Button)findViewById(R.id.next_btn);
        txtTotalAmount = (TextView)findViewById(R.id.total_price);
        txtMsg1 = (TextView)findViewById(R.id.msg1);
        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTotalAmount.setText("Total Price = Rp."+String.valueOf(overTotalPrice));
                Intent intent = new Intent(ReservasiActivity.this,ConfirmFinalReservasiActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckBookingState();
        final DatabaseReference reservasiListRef = FirebaseDatabase.getInstance().getReference().child("Booking List");
        FirebaseRecyclerOptions<Reservasi> options =new FirebaseRecyclerOptions.Builder<Reservasi>().
                setQuery(reservasiListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Kedai"),Reservasi.class)
                .build();
        FirebaseRecyclerAdapter<Reservasi, ReservasiViewHolder> adapter =
                new FirebaseRecyclerAdapter<Reservasi, ReservasiViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReservasiViewHolder holder, int i, @NonNull Reservasi model) {
                        holder.txtProductQuantity.setText(model.getQuantity());
                        holder.txtProductPrice.setText("Rp."+model.getPrice()+"/ CUP");
                        holder.txtProductName.setText(model.getKname());
                        holder.txtProductMenu.setText(model.getMenu());
                        int oneTypeProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity()) ;
                        overTotalPrice = overTotalPrice + oneTypeProductTPrice;

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Edit",
                                                "Remove"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReservasiActivity.this);
                                builder.setTitle(" Options: ");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            Intent intent = new Intent(ReservasiActivity.this, KedaiDetailsActivity.class);
                                            intent.putExtra("pid", model.getPid());
                                            startActivity(intent);
                                        }
                                        if (i == 1) {
                                            reservasiListRef.child("User View")
                                                    .child(Prevalent.currentOnlineUser.getPhone())
                                                    .child("Kedai")
                                                    .child(model.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ReservasiActivity.this, "Item removed Successfully.", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ReservasiActivity.this, HomeActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });

                                        }
                                    }

                                });
                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ReservasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservasi_item_layout,parent,false);
                        ReservasiViewHolder holder = new ReservasiViewHolder(view);
                        return holder;
                    }
                };
                        recyclerView.setAdapter(adapter);
                        adapter.startListening();
}
    private void CheckBookingState() {
        DatabaseReference bookingRef;
        bookingRef = FirebaseDatabase.getInstance().getReference().child("Reservasi").child(Prevalent.currentOnlineUser.getPhone());
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    if (shippingState.equals("Shipped")){
                        txtTotalAmount.setText("To Dear "+userName+"\n order is successfully.");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Selamat, pesanan Anda kami Konfirmasi Secepatnya. Segera Anda akan menerima pesan Di email Anda!.");
                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(ReservasiActivity.this,"Anda dapat melakukan reservasi lagi, Setelah Anda pembayaran Anda",Toast.LENGTH_SHORT).show();
                    }
                    else if (shippingState.equals("Not Shipped")){
                        txtTotalAmount.setText("Not valid");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);

                        NextProcessBtn.setVisibility(View.GONE);
                        Toast.makeText(ReservasiActivity.this,"Anda dapat membeli lebih banyak produk, Setelah Anda menerima pesanan pertama Anda",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}