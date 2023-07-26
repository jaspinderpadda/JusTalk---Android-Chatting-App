package com.jspadda.justalk.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.jspadda.justalk.databinding.ActivityPhoneNumberBinding;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser()!=null){
            Intent i = new Intent(PhoneNumberActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        getSupportActionBar().hide();

        binding.phoneBox.requestFocus();

        binding.phonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), OTPActivity.class);
                i.putExtra("phoneNumber", binding.phoneBox.getText().toString());
                startActivity(i);
            }
        });
    }
}