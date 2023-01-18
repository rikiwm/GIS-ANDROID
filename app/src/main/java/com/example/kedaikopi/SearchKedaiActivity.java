package com.example.kedaikopi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kedaikopi.Model.Kedai;
import com.example.kedaikopi.ViewHolder.KedaiViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class    SearchKedaiActivity extends AppCompatActivity {
    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_kedai);
        inputText = findViewById(R.id.search_product_name);
        searchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchKedaiActivity.this));
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput = inputText.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Kedai");
        FirebaseRecyclerOptions<Kedai> options = new FirebaseRecyclerOptions.Builder<Kedai>()
                .setQuery(reference.orderByChild("kname").startAt(searchInput), Kedai.class)
                .build();
        FirebaseRecyclerAdapter<Kedai, KedaiViewHolder> adapter =
                new FirebaseRecyclerAdapter<Kedai, KedaiViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull KedaiViewHolder holder, int position, @NonNull final Kedai model) {
                        holder.txtKedaiName.setText(model.getKname());
                        holder.txtKedaiDescription.setText(model.getDescription());
                        /*holder.txtKedaiPrice.setText("Price = " + model.getPrice() + "Rp.");*/
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent =new Intent(SearchKedaiActivity.this,KedaiDetailsActivity.class);
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
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
