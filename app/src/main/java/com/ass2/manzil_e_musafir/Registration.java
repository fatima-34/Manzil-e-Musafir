package com.ass2.manzil_e_musafir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Registration extends AppCompatActivity {
    private EditText cnic_ed,emergency_contact_number,emergency_contact_relation,ticket;
    private Button registration_final_btn;
    private String TID;
    private String cnic_text, emergency_contact_number_text, emergency_contact_relation_text,ticket_number;
    private String curr_phono, curr_name, UID;
    SensorManager sensorManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.activity_registration);
        cnic_ed = findViewById(R.id.CNIC);
        emergency_contact_number = findViewById(R.id.emergency_contact_number);
        emergency_contact_relation = findViewById(R.id.emergency_contact_relation);
        ticket = findViewById(R.id.tickets);
        registration_final_btn = findViewById(R.id.register_final);

        cnic_text = cnic_ed.getText().toString();
        emergency_contact_number_text = emergency_contact_number.getText().toString();
        emergency_contact_relation_text = emergency_contact_relation.getText().toString();
        ticket_number = ticket.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("Current_user_details", Context.MODE_PRIVATE);
        UID = sharedPref.getString("UID", "Default");
        curr_name = sharedPref.getString("name","Default");
        curr_phono = sharedPref.getString("phono", "default");

        TID = getIntent().getStringExtra("TID");


        registration_final_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addRegister();
                //notificationDialog();

                Intent intent = new Intent(Registration.this, user_trip.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addRegister() {
        String id = UUID.randomUUID().toString();
        String RID = id.toString().substring(0,8);
        //Start ProgressBar first (Set visibility VISIBLE)
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[9];
                field[0] = "RID";
                field[1] = "TID";
                field[2] = "UID";
                field[3] = "name";
                field[4] = "phono";
                field[5] = "cnic";
                field[6] = "tickets";
                field[7] = "emergency_contact";
                field[8] = "emergency_relation";
                //Creating array for data
                String[] data = new String[9];
                data[0] = RID;
                data[1] = TID;
                data[2] = UID;
                data[3] = curr_name;
                data[4] = curr_phono;
                data[5] = cnic_text;
                data[6] = ticket_number;
                data[7] = emergency_contact_number_text;
                data[8] = emergency_contact_relation_text;

                Log.d("addRegister", "Data sent");

                PutData putData = new PutData("http://172.17.64.106/manzilmusafir/addRegisteration.php", "POST", field, data);

                Log.d("addRegister", "Data received");

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if(result.equals("Registration Success")) {
                            Toast.makeText(Registration.this, result, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Registration.this, Registration.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(Registration.this, result, Toast.LENGTH_LONG).show();
                    }
                }
                //End Write and Read data with URL
            }
        });
    }
    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "C_id";
        int notificationId = 1;

        Intent intent = new Intent(this, user_trip.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Successfully Registered",
                    NotificationManager.IMPORTANCE_MAX
            );
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("String")
                .setContentText("discription")
                .setContentInfo("content")
                        .setContentIntent(pendingIntent);
        notificationManager.notify(1, notificationBuilder.build());
    }
}