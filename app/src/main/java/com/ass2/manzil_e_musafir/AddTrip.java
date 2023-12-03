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
                //Start ProgressBar first (Set visibility VISIBLE)
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[7];
                        field[0] = "place";
                        field[1] = "price";
                        field[2] = "description";
                        field[3] = "location";
                        field[4] = "details";
                        field[5] = "itinerary";
                        field[6] = "picture";
                        //Creating array for data
                        String[] data = new String[7];
                        data[0] = place.getText().toString();
                        data[1] = price.getText().toString();
                        data[2] = description.getText().toString();
                        data[3] = location.getText().toString();
                        data[4] = details.getText().toString();
                        data[5] = itinerary.getText().toString();
                        data[6] = imagePath;
                        PutData putData = new PutData("http://192.168.18.114/manzilmusafir/addtrip.php", "POST", field, data);
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