package com.example.accidentdetection;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.accidentdetection.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

   private Button loginButton;
    private static final int PERMISSION_REQUEST_SMS = 1;
    private static final int PERMISSION_REQUEST_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reqPermission();

        EditText editText_sms_phone_num = findViewById(R.id.phonenum_for_sms);

        loginButton =findViewById(R.id.buttonLogin);

        // Start the receiving activity


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = editText_sms_phone_num.getText().toString().trim();

                // Create an Intent to start the receiving activity
                Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);

                // Pass the data as an extra to the Intent
                intent.putExtra("data", data);
                Log.d("kamlans", "onCreate: login "+data);
                startActivity(intent);
            reqPermission();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // SMS permission granted
                    // Perform SMS-related operations
                } else {
                    // SMS permission denied
                    Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSION_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission granted
                    // Perform location-related operations
                } else {
                    // Location permission denied
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    private void reqPermission() {

        // Request SMS permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SMS);
        } else {
            // Permission already granted
            // Perform SMS-related operations
        }

        // Request fine/coarse location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        } else {
            // Permission already granted
            // Perform location-related operations
        }


    }


}