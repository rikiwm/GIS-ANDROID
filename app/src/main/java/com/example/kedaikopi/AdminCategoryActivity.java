package com.example.kedaikopi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView kLow,kMedium,kHight;
    private Button LogoutBtn, CheckOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


//        <---------------Tombol Buton Logout adm----------------->
        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

//        <---------------Tombol Buton Cek order reservasi----------------->
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

//        <---------------Tombol Buton add kadai----------------->
        kLow = (ImageView) findViewById(R.id.k01);
        kLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewKedaiActivity.class);
                intent.putExtra("category", "kLow");
                startActivity(intent);
            }
        });

        kMedium = (ImageView) findViewById(R.id.k02);
        kLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewKedaiActivity.class);
                intent.putExtra("category", "kMedium");
                startActivity(intent);
            }
        });

        kHight = (ImageView) findViewById(R.id.k03e);
        kLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewKedaiActivity.class);
                intent.putExtra("category", "kHight");
                startActivity(intent);
            }
        });

    }
}