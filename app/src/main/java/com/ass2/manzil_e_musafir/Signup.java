package com.ass2.manzil_e_musafir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Signup extends AppCompatActivity {

    EditText name, contact, email, password;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ProgressBar first (Set visibility VISIBLE)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[4];
                        field[0] = "name";
                        field[1] = "email";
                        field[2] = "phono";
                        field[3] = "password";
                        //Creating array for data
                        String[] data = new String[4];
                        data[0] = name.getText().toString();
                        data[1] = email.getText().toString();
                        data[2] = contact.getText().toString();
                        data[3] = password.getText().toString();
                        PutData putData = new PutData("http://192.168.18.114/manzilmusafir/signup.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Sign Up Success")) {
                                    Toast.makeText(Signup.this, result, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Signup.this, TripPackages.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(Signup.this, result, Toast.LENGTH_LONG).show();
                            }
                        }
                        //End Write and Read data with URL
                    }
                });
            }
        });
    }
}