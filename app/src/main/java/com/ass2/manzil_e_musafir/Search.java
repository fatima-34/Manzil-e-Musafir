package com.ass2.manzil_e_musafir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.ass2.manzil_e_musafir.models.ItemAdapter;
import com.ass2.manzil_e_musafir.models.Trip;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    RecyclerView recyclerView;
    List<String> places;
    ItemAdapter itemAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        places = new ArrayList<>();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                FetchData fetchData = new FetchData("http://172.17.64.106/manzilmusafir/getPlace.php");
                if (fetchData.startFetch()) {
                    if (fetchData.onComplete()) {
                        String result = fetchData.getResult();
                        Log.i("FetchData", "Response: " + result);
                        try {
                            //JSONObject placesArray = new JSONObject(result);
                            JSONObject responseObject = new JSONObject(result);
                            if (responseObject.getInt("Status") == 1) {
                                JSONArray placesArray = responseObject.getJSONArray("Places");

                                for (int i = 0; i < placesArray.length(); i++) {
                                    String placeName = placesArray.getString(i);
                                    places.add(placeName);
                                    Log.i("FetchData", "Place Added: " + placeName);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        itemAdapter.notifyDataSetChanged();
                                    }
                                });
                            } else {
                                Log.i("FetchData", "No places found");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FetchData", "JSONException: " + e.getMessage());
                        }
                    }
                }
            }
        });

        itemAdapter = new ItemAdapter(places, this);
        recyclerView.setAdapter(itemAdapter);
    }

    private void filterList(String text) {
        List<String> filteredList = new ArrayList<>();
        for(String placeName : places) {
            if(placeName.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(placeName);
            }
        }

        if(filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else {
            itemAdapter.setFilteredList(filteredList);
        }
    }

    @Override
    public void onItemClick(String place) {
        Toast.makeText(Search.this, "Clicked on " + place, Toast.LENGTH_SHORT).show();

        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "place";
                //Creating array for data
                String[] data = new String[1];
                data[0] = place;
                PutData putData = new PutData("http://172.17.64.106/manzilmusafir/getDetails.php", "GET", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = "error";
                        try {
                            JSONObject userObject = new JSONObject(putData.getResult());
                            int status = userObject.getInt("Status");
                            JSONArray userArray = userObject.getJSONArray("Trip");

                            JSONObject tripDetails = userArray.getJSONObject(0);
                            String TID = tripDetails.getString("TID");
                            String place = tripDetails.getString("place");
                            String price = tripDetails.getString("price");
                            String description = tripDetails.getString("description");
                            String location = tripDetails.getString("location");
                            String details = tripDetails.getString("details");
                            String itinerary = tripDetails.getString("itinerary");
                            String picture = tripDetails.getString("picture");

                            Trip trip = new Trip(TID,place,price,description,location,details,itinerary,picture);

                            Intent intent = new Intent(Search.this, TripDetails.class);
                            intent.putExtra("TID", trip.getTID());
                            intent.putExtra("place" , trip.getPlace());
                            intent.putExtra("price" , trip.getPrice());
                            intent.putExtra("description" , trip.getDescription());
                            intent.putExtra("details" , trip.getDetails());
                            intent.putExtra("location" , trip.getLocation());
                            intent.putExtra("itinerary" , trip.getItinerary());
                            intent.putExtra("picturePath" , trip.getPicturePath());
                            Search.this.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //End Write and Read data with URL
            }
        });
    }
}