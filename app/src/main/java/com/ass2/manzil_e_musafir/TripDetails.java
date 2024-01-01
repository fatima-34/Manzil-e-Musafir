package com.ass2.manzil_e_musafir;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TripDetails extends AppCompatActivity {

    private String TID, place, price, description, itinerary,details,location,picturePath;
    private TextView place_view, price_view, description_view, itinerary_view,details_view,location_view;
    private ImageView chat_btn, back;
    private Button register_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetails.this, TripPackages.class);
                startActivity(intent);
                finish();
            }
        });

        TID = getIntent().getStringExtra("TID");
        place = getIntent().getStringExtra("place");
        price = getIntent().getStringExtra("price");
        description = getIntent().getStringExtra("description");
        details = getIntent().getStringExtra("details");
        location = getIntent().getStringExtra("location");
        itinerary = getIntent().getStringExtra("itinerary");
        picturePath = getIntent().getStringExtra("picturePath");

        place_view = findViewById(R.id.Location_trip_details);
        price_view = findViewById(R.id.price_details);
        description_view = findViewById(R.id.description_details);
        itinerary_view = findViewById(R.id.itinerary_text);
        details_view = findViewById(R.id.details_text);
        location_view = findViewById(R.id.location_details);
        register_btn = findViewById(R.id.register);
        chat_btn = findViewById(R.id.chat_bubble_trip_details);

        place_view.setText(place);
        price_view.setText(price);
        description_view.setText(description);
        itinerary_view.setText(itinerary);
        details_view.setText(details);
        location_view.setText(location);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetails.this, Registration.class);
                intent.putExtra("TID", TID);
                startActivity(intent);
                finish();
            }
        });

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetails.this, chat_activity.class);
                /*intent.putExtra("TID", TID);*/
                startActivity(intent);
                finish();
            }
        });

    }
}