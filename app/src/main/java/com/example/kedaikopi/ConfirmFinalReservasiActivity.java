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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kedaikopi.Prevalent.Prevalent;
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
import java.util.SimpleTimeZone;

public class ConfirmFinalReservasiActivity extends AppCompatActivity {
    private EditText nameEditText,phoneEditText,addressEditText,cityEditText,rekeningUser,email,catat;

    private Button confirmOrderBtn;
    private String totalAmount = "";
    private ImageView inputBukti;
    private static final int GalleryPick = 1;
    private StorageReference buktiImagesRef;
    private DatabaseReference ordersRef;
    private Uri ImgUri;
    private String buktiRandomKey, downloadImgUrl;
    private String saveCurrentDate;
    private String saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_reservasi);

        buktiImagesRef = FirebaseStorage.getInstance().getReference().child("Bukti Transfer");
        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = Rp. "+totalAmount,Toast.LENGTH_SHORT).show();
        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText =(EditText) findViewById(R.id.shippment_name);
        phoneEditText =(EditText) findViewById(R.id.shippment_phone_number);
        addressEditText =(EditText) findViewById(R.id.shippment_address);
        cityEditText =(EditText) findViewById(R.id.shippment_city);
        rekeningUser =(EditText) findViewById(R.id.no_rek);
        email =(EditText) findViewById(R.id.mail);
        catat= (EditText)findViewById(R.id.catatan_user);
        inputBukti=(ImageView) findViewById(R.id.imgBukti);

        inputBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();

            }
        });
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
    }

    private void OpenGallery() {
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
            ImgUri = data.getData();
            inputBukti.setImageURI(ImgUri);
        }

        Check();
    }

    private void Check() {
        if (ImgUri == null)
        {
            Toast.makeText(this, "Gambar tidak valid", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this,"Berikan Nama Lengkap Anda!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this,"Harap Berikan Nomor Telepon Anda!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this,"Harap berikan alamat anda yang valid!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this,"Tolong berikan nama kota Anda!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(rekeningUser.getText().toString())){
            Toast.makeText(this,"Tolong masukan no rekening anda!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(this,"Tolong masukan email anda!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(catat.getText().toString())){
            Toast.makeText(this,"Masukan catatan yg anda perlukan!",Toast.LENGTH_SHORT).show();
        }
        else {

            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final StorageReference filePath = buktiImagesRef.child(ImgUri.getLastPathSegment() + buktiRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImgUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(ConfirmFinalReservasiActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ConfirmFinalReservasiActivity.this, "Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImgUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImgUrl = task.getResult().toString();

                            Toast.makeText(ConfirmFinalReservasiActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveBuktiInfoToDatabase();
                        }

                    }

                });
            }
        });

    }
    private void SaveBuktiInfoToDatabase() {

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Reservasi")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("rek", rekeningUser.getText().toString());
        ordersMap.put("email", email.getText().toString());
        ordersMap.put("catatan", catat.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "Shipped");
        ordersMap.put("bukti", downloadImgUrl);
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Booking List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalReservasiActivity.this, "Pesanan Anda akan dikonfirmasi ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalReservasiActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void multiSelectionDialog(){

    }

}