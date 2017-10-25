package com.example.admin.googlegeocoderapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class ClassActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 10;
    private static final String TAG = "ClassActivity";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private ClassActivity.AddressResultReceiver addressResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( this );
        addressResultReceiver = new ClassActivity.AddressResultReceiver(new Handler());

        checkPermission();
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: Permission not Granted");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Log.d(TAG, "onCreate: Permission already granted");
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getLocation() {
        Log.d(TAG, "getLocation: ");
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        currentLocation = location;

//                        double latitude = location.getLatitude();
//                        double longitude = location.getLongitude();
//
//                        Log.d(TAG, "onSuccess: You're Latitude is " + latitude);
//                        Log.d(TAG, "onSuccess: You're Longitude is " + longitude);
//
//                        try {
//                            List<Address> addressList = geocoder.getFromLocation( latitude, longitude, 20 );
//                            for( Address a : addressList )
//                                Log.d(TAG, "onSuccess: Printing Addresses: " + a.toString());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        //Geocoder is slow, going to do it in an intent service
                        startIntentService();
                    }
                })
                .addOnFailureListener( this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                        e.printStackTrace();
                    }
                } );
    }

    private void startIntentService() {
        Log.d(TAG, "startIntentService: ");
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra( "receiver", addressResultReceiver );
        intent.putExtra( "location", currentLocation );
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if( resultCode == FetchAddressIntentService.SUCCESS_RESULT ) {
                Log.d(TAG, "onReceiveResult: You're Latitude is " + resultData.getDouble( "latitude" ));
                Log.d(TAG, "onReceiveResult: You're Longitude is " + resultData.getDouble( "longitude" ));
                Log.d(TAG, "onReceiveResult: You're reverse Geocoded address is " + resultData.getString( "reverseGeocodedAddress" ));
                Log.d(TAG, "onHandleIntent: You're Geocoded latitude is " + resultData.getDouble( "geocodedLatitude" ));
                Log.d(TAG, "onHandleIntent: You're Geocoded longitude is " + resultData.getDouble( "geocodedLongitude" ));
            }
        }
    }
}

/*
Consider structuring it more like this:
https://github.com/googlesamples/android-play-location/blob/master/LocationAddress/app/src/main/java
/com/google/android/gms/location/sample/locationaddress/MainActivity.java
 */