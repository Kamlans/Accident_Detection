package com.example.accidentdetection;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.accidentdetection.ui.dialog.TimerDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accidentdetection.databinding.ActivityDrawerBinding;
import android.Manifest;
public class DrawerActivity extends AppCompatActivity implements TimerDialogFragment.TimerDialogListener {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1001 ;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding binding;

    private String number = "7978886146";
    private String message = "text message demo";
    private String data ="";

    private LocationManager locationManager;
    private LocationListener locationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDrawer.toolbar);
        binding.appBarDrawer.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();


               // openGoogleMapsNavigation();
                showTimerDialog();

            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent intent = getIntent();
        if (intent != null) {
            data = intent.getStringExtra("data");
        }
        else{
            Log.d("kamalns", "onCreate: null");
        }

        number = data;
        Log.d("kamlans", "onCreate: "+number.toString());
        Log.d("kamlans", "onCreate: "+data.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send SMS
                getCurrentLocation();
                sendSMS("ACCIDENT!!!!! "+"click on the following link to find the route for accident. "+message);

                // showTimerDialog();
            } else {
                // Permission denied
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getCurrentLocation();
                sendSMS("ACCIDENT!!!!! "+"click on the following link to find the route for accident. "+message);

            } else {
                // Permission denied
                // Handle the case when the user denies location permission
            }
        }
    }


    private void sendSMS( String _message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null , _message , null, null );
        Log.d("kamlans", "sendSMS: "+_message);
    }

    // 3 dots at right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);

        MenuItem logoutButton = menu.findItem(R.id.logout_button);
        View view = logoutButton.getActionView();

        logoutButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Perform desired action when the menu item is clicked
                Toast.makeText(DrawerActivity.this, "Logout clicked!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent( DrawerActivity.this , LoginActivity.class));
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showTimerDialog() {
        TimerDialogFragment dialogFragment = new TimerDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "timer_dialog");
//        sendSMS("sms");
    }


    //dialog on continue method
    @Override
    public void onContinue() {

        Toast.makeText(this, "continue clicked", Toast.LENGTH_SHORT).show();

//        String txt="";
//        txt = "ACCIDENT!!!!! "+"click on the following link to find the route for accident. "+message;
//        Log.d("kamlans", "onContinue: "+txt);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(DrawerActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, send SMS
            //openGoogleMapsNavigation();
            getCurrentLocation();

        }

    }

    // dialog on cancel method
    @Override
    public void onCancel() {
        Toast.makeText(this, "cancel clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(DrawerActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, send SMS
            //openGoogleMapsNavigation();
            Log.d("kamlans", "onFinish: ");
            getCurrentLocation();

        }
    }


    private void openGoogleMapsNavigation(double latitude, double longitude) {


        double currentLatitude = 22.25; // Replace with the actual current latitude of the device
        double currentLongitude = 84.85; // Replace with the actual current longitude of the device


        String mapsUrl = "https://www.google.com/maps/dir/?api=1&";
        String destinationLatitude = "22.25068";
        String destinationLongitude = "84.90182";
        String destination = "destination=" + destinationLatitude + "," + destinationLongitude;
        String origin = "origin=" + latitude + "," + longitude;

        Uri uri = Uri.parse(mapsUrl + destination + "&" + origin);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            Log.d("kamlans", "openGoogleMapsNavigation: "+uri.toString());
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

            message = uri.toString();
//
            sendSMS("click to open navigation to accident destination " +message);

        }
        else {
            // No browser app available, handle error
        }


    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                 double new_latitude = location.getLatitude();
                 double new_longitude = location.getLongitude();

                 openGoogleMapsNavigation(new_latitude, new_longitude);

               Log.d("kamlans", "onLocationChanged: "+new_longitude+" "+new_latitude);

                // Use the latitude and longitude values
                // ...

                // Stop listening for location updates after receiving the current location
                locationManager.removeUpdates(locationListener);
            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // Check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }


}


