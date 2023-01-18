package com.example.kedaikopi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;



import com.example.kedaikopi.Model.Kedai;
import com.example.kedaikopi.Prevalent.Prevalent;
import com.example.kedaikopi.ViewHolder.KedaiViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference KedaiRef;
            DrawerLayout drawerLayout;
            NavigationView navigationView;
            Toolbar toolbar;
            TextView textView;
    private RecyclerView recyclerView;
                RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        KedaiRef = FirebaseDatabase.getInstance().getReference().child("Kedai");

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//navigation drawer------------------------------
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name1);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image1);
        userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReservasiActivity.class);
                startActivity(intent);
            }
        });
    }

//    <---------------GET DATA KEDAI----------------->
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Kedai> options = new FirebaseRecyclerOptions.Builder<Kedai>()
                .setQuery(KedaiRef, Kedai.class)
                .build();
        FirebaseRecyclerAdapter<Kedai, KedaiViewHolder> adapter = new FirebaseRecyclerAdapter<Kedai, KedaiViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull KedaiViewHolder kedaiViewHolder, int i, @NonNull Kedai model) {
                kedaiViewHolder.txtKedaiName.setText(model.getKname());
                kedaiViewHolder.txtKedaiDescription.setText(model.getDescription());
               /* kedaiViewHolder.txtAlamat.setText(model.getAlamat());
                kedaiViewHolder.txtNohp.setText(model.getNohp());
                kedaiViewHolder.txtKedaiPrice.setText("Harga = " + model.getPrice() + "/ Cup"); */
                Picasso.get().load(model.getImage()).into(kedaiViewHolder.imageView);
                kedaiViewHolder.itemView.setOnClickListener (new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(HomeActivity.this,KedaiDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });
            }


            @NonNull
            @Override
            public KedaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kedai_items_layout, parent, false);
                KedaiViewHolder holder = new KedaiViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

//    NavigationItem---------------------------------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            Intent intent = new Intent(HomeActivity.this,ReservasiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(HomeActivity.this, SearchKedaiActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            Intent intent=new Intent(HomeActivity.this,MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent=new Intent(HomeActivity.this,SettinsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent=new Intent(HomeActivity.this,About.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();
            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);
            finish();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}