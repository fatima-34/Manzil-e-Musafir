package com.ass2.manzil_e_musafir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ass2.manzil_e_musafir.models.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    TextView signup;
    Button login;
    EditText email, password;
    UserModel userModel;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask().execute(email.getText().toString(), password.getText().toString());
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.d("LoginActivity", "In doInbackground");
            String userEmail = params[0];
            String userPassword = params[1];

            String[] field = new String[2];
            field[0] = "email";
            field[1] = "password";
            //Creating array for data
            String[] data = new String[2];
            data[0] = userEmail;
            data[1] = userPassword;

            PutData putData = new PutData("http://172.17.64.106/manzilmusafir/login.php", "POST", field, data);
            if (putData.startPut()) {
                Log.d("LoginActivity", "In if 1");
                if (putData.onComplete()) {
                    Log.d("LoginActivity", "In if 2");
                    return putData.getResult();
                }
            }

            Log.d("LoginActivity", "doInbackground DONE");
            return "error";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("LoginActivity", "Login result: " + result);
            if (!"error".equals(result)) {
                try {
                    JSONObject userObject = new JSONObject(result);
                    int status = userObject.getInt("Status");

                        JSONArray userArray = userObject.getJSONArray("Users");
                        for (int i = 0; i < userArray.length(); i++) {
                            Log.d("LoginActivity", "findingg");
                            JSONObject userDetails = userArray.getJSONObject(i);
                            String UID = userDetails.getString("UID");
                            String name = userDetails.getString("name");
                            String email = userDetails.getString("email");
                            String phono = userDetails.getString("phono");
                            String password = userDetails.getString("password");

                            SharedPreferences sharedPref = getSharedPreferences("Current_user_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("UID", UID);
                            editor.putString("name", name);
                            editor.putString("phono", phono);
                            editor.apply();

                            userModel = new UserModel(UID, name, email, phono, password);
                            databaseReference.child(UID).setValue(userModel);

                            Toast.makeText(Login.this, "Login Success", Toast.LENGTH_LONG).show();
                            if(email.equals("admin@gmail.com")) {
                                Intent intent = new Intent(Login.this, adminTripPackages.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(Login.this, TripPackages.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Error parsing response", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(Login.this, "Network Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}
