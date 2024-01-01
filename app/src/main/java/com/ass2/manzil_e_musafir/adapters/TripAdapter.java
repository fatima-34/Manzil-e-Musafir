package com.ass2.manzil_e_musafir.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ass2.manzil_e_musafir.R;
import com.ass2.manzil_e_musafir.models.Trip;
import com.ass2.manzil_e_musafir.TripDetails;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<Trip> tripList;
    private Context context;

    public TripAdapter(Context context) {
        this.context = context;
        tripList = new ArrayList<>();
    }

    public void add(Trip trip){
        tripList.add(trip);
        notifyDataSetChanged();
    }
    public void clear(){
        tripList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        if(trip != null) {
            holder.textViewPlace.setText(trip.getPlace());
            holder.textViewPrice.setText(trip.getPrice());
        }else{
            Log.i("adpater", trip.getDescription());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TripDetails.class);
                intent.putExtra("TID", trip.getTID());
                intent.putExtra("place" , trip.getPlace());
                intent.putExtra("price" , trip.getPrice());
                intent.putExtra("description" , trip.getDescription());
                intent.putExtra("details" , trip.getDetails());
                intent.putExtra("location" , trip.getLocation());
                intent.putExtra("itinerary" , trip.getItinerary());
                intent.putExtra("picturePath" , trip.getPicturePath());
                context.startActivity(intent);
            }
        });
        //holder.imageViewPicture.setImageURI(trip.getPicturePath());
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewPlace;
        public TextView textViewPrice;

        public Button details_btn;
        //public ImageView imageViewPicture;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewPlace = itemView.findViewById(R.id.location);
            textViewPrice = itemView.findViewById(R.id.price);
//            details_btn = itemView.findViewById(R.id.detail_btn);
            //imageViewPicture = itemView.findViewById(R.id.picture);
        }
    }
}
