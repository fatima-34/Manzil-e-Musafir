package com.ass2.manzil_e_musafir;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.UUID;

public class AddTrip extends AppCompatActivity {

    EditText place, price, description, location, details, itinerary;
    ImageView picture;
    Button addTrip;
    Uri selectedImage = null;
    String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        addTrip = findViewById(R.id.addtrip);
        place = findViewById(R.id.place);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        details = findViewById(R.id.details);
        itinerary = findViewById(R.id.itinerary);
        picture = findViewById(R.id.picture);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                String TID = id.toString().substring(0,8);
                //Start ProgressBar first (Set visibility VISIBLE)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[8];
                        field[0] = "TID";
                        field[1] = "place";
                        field[2] = "price";
                        field[3] = "description";
                        field[4] = "location";
                        field[5] = "details";
                        field[6] = "itinerary";
                        field[7] = "picture";
                        //Creating array for data
                        String[] data = new String[8];
                        data[0] = TID;
                        data[1] = place.getText().toString();
                        data[2] = price.getText().toString();
                        data[3] = description.getText().toString();
                        data[4] = location.getText().toString();
                        data[5] = details.getText().toString();
                        data[6] = itinerary.getText().toString();
                        data[7] = imagePath;
                        PutData putData = new PutData("http://172.17.64.106/manzilmusafir/addtrip.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Trip Addition Success")) {
                                    Toast.makeText(AddTrip.this, result, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(AddTrip.this, TripPackages.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                    Toast.makeText(AddTrip.this, result, Toast.LENGTH_LONG).show();
                            }
                        }
                        //End Write and Read data with URL
                    }
                });
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200 & resultCode==RESULT_OK) {
            selectedImage = data.getData();
            imagePath = selectedImage.toString();
        }
    }
}