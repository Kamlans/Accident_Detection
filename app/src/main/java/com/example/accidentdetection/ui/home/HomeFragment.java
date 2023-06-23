package com.example.accidentdetection.ui.home;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.accidentdetection.R;
import com.example.accidentdetection.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private GoogleMap googleMap;
    LocationListener locationListener;
    LocationManager locationManager;

    private double ltt, lnn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // Initialize ViewModel here
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Add a marker

        LatLng markerLocation = new LatLng(22.25068, 84.90182);

        LatLng xyz = getCurrentLocation();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(markerLocation)
                .title("Your Location")
                .snippet("Marker Snippet");
        Marker marker = googleMap.addMarker(markerOptions);

        // Move the camera to the marker location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLocation, 12));

    }



    private LatLng getCurrentLocation() {

       locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        final double[] lat = new double[1];
        final double[] lng = new double[1];

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double new_latitude = location.getLatitude();
                double new_longitude = location.getLongitude();

                lat[0] = new_latitude;
                lng[0] = new_longitude;

                ltt = new_latitude;
                lnn = new_longitude;


                Log.d("kamlans", "onLocationChanged: "+new_longitude+" "+new_latitude);


                // Stop listening for location updates after receiving the current location
               locationManager.removeUpdates(locationListener);
//                if (locationManager != null) {
//                    locationManager.removeUpdates(locationListener);
//                }
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
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }





        // Check if location providers are enabled
        if (locationManager != null) {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // Select the appropriate location provider
            String provider = null;
            if (isGPSEnabled) {
                provider = LocationManager.GPS_PROVIDER;
            } else if (isNetworkEnabled) {
                provider = LocationManager.NETWORK_PROVIDER;
            }

            if (provider != null) {
                // Set location criteria for the provider
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE); // Or ACCURACY_COARSE if preferred
                criteria.setPowerRequirement(Criteria.POWER_LOW); // Or POWER_HIGH if preferred

                // Request location updates with the specified
                // Request location updates with the specified criteria
                locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            } else {
// Prompt user to enable location services
                Toast.makeText(getContext(), "Please enable location services", Toast.LENGTH_SHORT).show();
            }
        }

// Handle location permission request result


        return new LatLng(ltt, lnn);
    }


}