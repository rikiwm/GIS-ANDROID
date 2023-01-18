package com.example.kedaikopi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewKedaiActivity extends AppCompatActivity {
    private String CategoryName;
    private String Description;
    private String Price;
    private String Kedainame;
    private String Alamat;
    private String noHp;
    private String saveCurrentDate;
    private String saveCurrentTime;
    private Button AddNewKedaitButton;
    private ImageView InputKedaiImage;
    private EditText InputKedaitName, InputKedaiDescription, InputKedaiPrice, InputAlamat, InputnoHp;
    private static final int GalleryPick = 3;
    private Uri ImageUri;
    private String kedaiRandomKey, downloadImageUrl;
    private StorageReference KedaiImagesRef;
    private DatabaseReference KedaiRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_kedai);

        CategoryName = getIntent().getExtras().get("category").toString();
        KedaiImagesRef = FirebaseStorage.getInstance().getReference().child("Kedai Images");
        KedaiRef = FirebaseDatabase.getInstance().getReference().child("Kedai");

        AddNewKedaitButton = (Button) findViewById(R.id.add_new_kadai);
        InputKedaiImage = (ImageView) findViewById(R.id.select_product_image);
        InputKedaitName = (EditText) findViewById(R.id.kopi_name);
        InputKedaiDescription = (EditText) findViewById(R.id.kopi_description);
        InputKedaiPrice = (EditText) findViewById(R.id.kopi_price);
        InputAlamat= (EditText)findViewById(R.id.alamatkadai);
        InputnoHp= (EditText)findViewById(R.id.hp);

        loadingBar = new ProgressDialog(this);


        InputKedaiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();

            }
        });
        AddNewKedaitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateDataKedai();

            }
        });
    }


    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputKedaiImage.setImageURI(ImageUri);
        }
    }
    private void ValidateDataKedai() {
        Description = InputKedaiDescription.getText().toString();
        Price = InputKedaiPrice.getText().toString();
        Kedainame = InputKedaitName.getText().toString();
        Alamat = InputAlamat.getText().toString();
        noHp = InputnoHp.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(this, "Gambar tidak valid", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Silahkan isi deskripsi Kedai", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Silahkan isi harga per gelas nya", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Kedainame))
        {
            Toast.makeText(this, "Silahkan isi Nama Kedai", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Alamat))
        {
            Toast.makeText(this, "Silahkan isi Alamat Kedai", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(noHp))
        {
            Toast.makeText(this, "Silahkan isi nomor hp Kedai", Toast.LENGTH_SHORT).show();
        }
        else
        {
            InformationKedai();
        }

    }
    private void InformationKedai()
    {
        loadingBar.setTitle("Add New Kedai");
        loadingBar.setMessage("Harap tunggu ! ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        kedaiRandomKey = Kedainame + saveCurrentDate;


        final StorageReference filePath = KedaiImagesRef.child(ImageUri.getLastPathSegment() + kedaiRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewKedaiActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewKedaiActivity.this, "Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewKedaiActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveKedaiInfoToDatabase();
                        }
                    }
                });
            }
        });

    }
    private void SaveKedaiInfoToDatabase()
    {
        HashMap<String, Object> kedaiMap = new HashMap<>();
        kedaiMap.put("pid", kedaiRandomKey);
        kedaiMap.put("date", saveCurrentDate);
        kedaiMap.put("time", saveCurrentTime);
        kedaiMap.put("description", Description);
        kedaiMap.put("image", downloadImageUrl);
        kedaiMap.put("category", CategoryName);
        kedaiMap.put("price", Price);
        kedaiMap.put("kname", Kedainame);
        kedaiMap.put("alamat", Alamat);
        kedaiMap.put("nohp", noHp);

        KedaiRef.child(kedaiRandomKey).updateChildren(kedaiMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewKedaiActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewKedaiActivity.this, "Kedai is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewKedaiActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
