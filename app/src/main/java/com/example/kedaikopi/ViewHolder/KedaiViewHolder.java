package com.example.kedaikopi.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kedaikopi.Interface.ItemClickListner;
import com.example.kedaikopi.R;

public class KedaiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtKedaiName, txtKedaiDescription, txtKedaiPrice,txtAlamat,txtNohp;
    public ImageView imageView;
    public ItemClickListner listner;
    public KedaiViewHolder(View itemView)
    {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.kedai_image);
        txtKedaiName = (TextView) itemView.findViewById(R.id.kedai_name);
        txtKedaiDescription = (TextView) itemView.findViewById(R.id.kedai_description);
/*      txtKedaiPrice = (TextView) itemView.findViewById(R.id.kedai_price);
        txtAlamat = (TextView) itemView.findViewById(R.id.alamat_kedai);
        txtNohp = (TextView) itemView.findViewById(R.id.no_hp_kedai);  */
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }
    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);

    }
}
