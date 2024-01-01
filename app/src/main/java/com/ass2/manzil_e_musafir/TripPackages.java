package com.ass2.manzil_e_musafir;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ass2.manzil_e_musafir.adapters.TripAdapter;
import com.ass2.manzil_e_musafir.models.Trip;
import com.onesignal.OneSignal;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TripPackages extends AppCompatActivity {

    private RecyclerView recyclerViewTrips;
    private TripAdapter tripAdapter;
    private String place, price;
    private TextView yourTrips;
    private ImageView search, notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_packages);

        search = findViewById(R.id.search);
        notification = findViewById(R.id.notification);

        tripAdapter= new TripAdapter(this);
        recyclerViewTrips = findViewById(R.id.tripPackagesRecyclerView);
        recyclerViewTrips.setAdapter(tripAdapter);
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(this));

        yourTrips = findViewById(R.id.your_trips_button);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification("Trip Registration successful");
            }
        });
        yourTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripPackages.this, user_trip.class);
                startActivity(intent);
                finish();
            }
        });

        fetchTripsData();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripPackages.this, Search.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void sendNotification(String message) {
//        private void setupOneSignalNotificationHandler() {
//            OneSignal.setNotificationReceivedHandler(new OneSignal.OSNotificationReceivedHandler() {
//                @Override
//                public void notificationReceived(OSNotification notification) {
//                    // Handle notification when received
//                    Log.d("OneSignal", "Notification received with data: " + notification.toString());
//                    // You can perform actions or update the UI as needed
//                }
//            });
//        }
        OkHttpClient client = new OkHttpClient();
        String url = "http://172.17.64.106/manzilmusafir/send_notification.php";

        RequestBody body = new FormBody.Builder()
                .add("message", message)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Run on UI thread
                runOnUiThread(() -> Toast.makeText(TripPackages.this, "Error", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Run on UI thread
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(TripPackages.this, "Success", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private void fetchTripsData(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("http://172.17.64.106/manzilmusafir/getitems.php");
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {

                        try {
                            JSONObject tripObject = new JSONObject(fetchData.getResult());

                            int status = tripObject.getInt("Status");
                            JSONArray tripArray = tripObject.getJSONArray("Trip");

                            for (int i = 0; i < tripArray.length(); i++) {
                                JSONObject tripDetails = tripArray.getJSONObject(i);
                                String TID = tripDetails.getString("TID");
                                place = tripDetails.getString("place");
                                price = tripDetails.getString("price");
                                String description = tripDetails.getString("description");
                                String location = tripDetails.getString("location");
                                String details = tripDetails.getString("details");
                                String itinerary = tripDetails.getString("itinerary");
                                String picture = tripDetails.getString("picture");
                                Trip trip = new Trip(TID,place,price,description,location,details,itinerary,picture);
                                tripAdapter.add(trip);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("FetchData", "here");
                    }
                }
            }
        });
    }
}
