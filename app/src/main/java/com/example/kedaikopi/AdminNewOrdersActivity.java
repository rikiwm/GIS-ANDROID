package com.example.kedaikopi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kedaikopi.Model.AdminBok;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminNewOrdersActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Reservasi");
        ordersList = findViewById(R.id.orders_list);

        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminBok> options= new FirebaseRecyclerOptions.Builder<AdminBok>()
                        .setQuery(ordersRef, AdminBok.class)
                        .build();
        FirebaseRecyclerAdapter<AdminBok, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminBok, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminBok model) {

                        holder.userName.setText(model.getName());
                        holder.userPhoneNumber.setText(model.getPhone());
                        holder.userTotalPrice.setText(" Rp."+model.getTotalAmount());
                        holder.userDateTime.setText(model.getDate()+"/ Jam "+ model.getTime());
                        holder.userRek.setText(model.getRek());
                        holder.userCatatan.setText(model.getCatatan());
                        holder.userShippingAddress.setText(model.getAddress()+", "+model.getCity());
                        Picasso.get().load(model.getBukti()).into(AdminOrdersViewHolder.userBukti);
                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uID = getRef(position).getKey();
                                Intent intent = new Intent(AdminNewOrdersActivity.this,ActivityAdminUserReseKedai.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] =new CharSequence[]{
                                        "Yes",
                                        "No"

                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Sudahkah Anda Konfirmasi bookingan ini?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i==0){
                                            String uID = getRef(position).getKey();
                                            RemoverOrder(uID);

                                        }
                                        else {
                                            finish();
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boking_layout,parent,false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();

    }



    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public static ImageView userBukti;
        public TextView userName, userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress,userRek,userCatatan;
        public Button showOrdersBtn;
        public AdminOrdersViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            userRek=itemView.findViewById(R.id.order_rekening);
            userCatatan=itemView.findViewById(R.id.order_cat);
            userBukti = itemView.findViewById(R.id.bukti_tf);
            showOrdersBtn = itemView.findViewById(R.id.show_all);
        }
    }
    private void RemoverOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }
}
