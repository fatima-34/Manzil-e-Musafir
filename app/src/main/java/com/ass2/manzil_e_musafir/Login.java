package com.ass2.manzil_e_musafir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    TextView signup, skip;
    Button login;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup);
        skip = findViewById(R.id.skip);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, TripPackages.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ProgressBar first (Set visibility VISIBLE)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[2];
                        field[0] = "email";
                        field[1] = "password";
                        //Creating array for data
                        String[] data = new String[2];
                        data[0] = email.getText().toString();
                        data[1] = password.getText().toString();
                        PutData putData = new PutData("http://192.168.18.114/manzilmusafir/login.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Login Success")) {
                                    Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Login.this, AddTrip.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
                            }
                        }
                        //End Write and Read data with URL
                    }
                });
            }
        });
    }
}