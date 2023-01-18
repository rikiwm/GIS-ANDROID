package com.example.kedaikopi.Prevalent;

import android.view.View;
import android.widget.TextView;
import com.example.kedaikopi.Interface.ItemClickListner;
import com.example.kedaikopi.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReservasiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName,txtProductPrice,txtProductQuantity,txtProductMenu;
    private ItemClickListner itemClickListner;

    public ReservasiViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.reservasi_name);
        txtProductPrice = itemView.findViewById(R.id.reservasi_price);
        txtProductQuantity = itemView.findViewById(R.id.reservasi_quantity);
        txtProductMenu = itemView.findViewById(R.id.reservasi_menu);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }
    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
