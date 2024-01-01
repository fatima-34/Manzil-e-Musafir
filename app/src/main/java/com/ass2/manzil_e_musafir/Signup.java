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

import java.util.UUID;

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
                String id = UUID.randomUUID().toString();
                String UID = id.toString().substring(0,8);
                //Start ProgressBar first (Set visibility VISIBLE)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[5];
                        field[0] = "UID";
                        field[1] = "name";
                        field[2] = "email";
                        field[3] = "phono";
                        field[4] = "password";
                        //Creating array for data
                        String[] data = new String[5];
                        data[0] = UID;
                        data[1] = name.getText().toString();
                        data[2] = email.getText().toString();
                        data[3] = contact.getText().toString();
                        data[4] = password.getText().toString();
                        PutData putData = new PutData("http://172.17.64.106/manzilmusafir/signup.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Sign Up Success")) {
                                    Toast.makeText(Signup.this, result, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Signup.this, Login.class);
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