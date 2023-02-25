package com.example.locationawaredemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private LocationManager locationManager;
    private LocationListener locationListener;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }
        };

        // if the permission is granted, we request the update.
        // if the permission is not granted, we request for the access.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, you can now access the device's location
        } else {
            // Permission denied, show a message to the user
            Toast.makeText(this, "Please allow location to perform app", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationInfo(Location location) {
        TextView latText = findViewById(R.id.lat_text);
        TextView lngText = findViewById(R.id.lng_text);
        TextView accText = findViewById(R.id.acc_text);
        TextView altText = findViewById(R.id.alt_text);
        TextView addressText = findViewById(R.id.address_text);

        latText.setText("Latitude: " + location.getLatitude());
        lngText.setText("Longitude: " + location.getLongitude());
        accText.setText("Accuracy: " + location.getAccuracy());
        altText.setText("Altitude: " + location.getAltitude());

        String address = "Could not find the address";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList != null && addressList.size() > 0) {
                address = "\n";

                // street name
                if (addressList.get(0).getThoroughfare() != null)
                    address += addressList.get(0).getThoroughfare() + "\n";
                if (addressList.get(0).getLocality() != null)
                    address += addressList.get(0).getLocality() + " ";
                if (addressList.get(0).getPostalCode() != null)
                    address += addressList.get(0).getPostalCode() + " ";
                if (addressList.get(0).getAdminArea() != null)
                    address += addressList.get(0).getAdminArea();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addressText.setText("Address: " + address);
    }

}