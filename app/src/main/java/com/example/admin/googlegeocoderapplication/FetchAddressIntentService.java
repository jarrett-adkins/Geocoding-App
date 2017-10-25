package com.example.admin.googlegeocoderapplication;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {

    private static final String TAG = "FetchAddressService";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    private ResultReceiver receiver;
    private double latitude;
    private double longitude;
    private String reverseGeocodedAddress = "";

    public FetchAddressIntentService() {
        super( TAG );
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");

        int result = SUCCESS_RESULT;

        receiver = intent.getParcelableExtra( "receiver" );
        Location location = intent.getParcelableExtra( "location" );
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Bundle bundle = new Bundle();

        List<Address> addresses = null;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        bundle.putDouble( "latitude", latitude );
        bundle.putDouble( "longitude", longitude );

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1 );

            if (addresses == null || addresses.size() == 0) {
                // no address was found.
                Log.d(TAG, "onHandleIntent: No addresses where found.");
                result = FAILURE_RESULT;
            } else {
                for(int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    reverseGeocodedAddress += addresses.get(0).getAddressLine(i) + " ";
                }

                bundle.putString( "reverseGeocodedAddress", reverseGeocodedAddress );
            }

            addresses = geocoder.getFromLocationName(reverseGeocodedAddress, 1);

            if (addresses == null || addresses.size() == 0) {
                // no address was found.
                Log.d(TAG, "onHandleIntent: Geocoding failed.");
                result = FAILURE_RESULT;
            } else {
                bundle.putDouble( "geocodedLatitude", addresses.get(0).getLatitude() );
                bundle.putDouble( "geocodedLongitude", addresses.get(0).getLongitude() );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiver.send( result, bundle);
    }
}
