package com.jspadda.justalk.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jspadda.justalk.Models.User;
import com.jspadda.justalk.databinding.ActivitySetupProfileBinding;

public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImg;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        binding.profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = binding.nameBox.getText().toString();

                if (userName.isEmpty()){
                    binding.nameBox.setError("Please type a name");
                    return;
                }
                dialog.show();

                if (selectedImg!=null){
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(selectedImg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                         if (task.isSuccessful()){
                             reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     String imageUrl = uri.toString();

                                     String uid = auth.getUid();
                                     String phoneNumber = auth.getCurrentUser().getPhoneNumber();
                                     String name = binding.nameBox.getText().toString();

                                     User user = new User(uid, name, phoneNumber, imageUrl);

                                     database.getReference()
                                             .child("users")
                                             .child(uid)
                                             .setValue(user)
                                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void unused) {
                                                     dialog.dismiss();
                                                Intent i = new Intent(SetupProfileActivity.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                                 }
                                             });
                                 }
                             });
                         }
                        }
                    });
                }else {
                    String uid = auth.getUid();
                    String phoneNumber = auth.getCurrentUser().getPhoneNumber();
                    String name = binding.nameBox.getText().toString();

                    User user = new User(uid, name, phoneNumber, "No Image");

                    database.getReference()
                            .child("users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Intent i = new Intent(SetupProfileActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null){
            if (data.getData()!=null){
                binding.profileimg.setImageURI(data.getData());
                selectedImg = data.getData();
            }
        }
    }
}