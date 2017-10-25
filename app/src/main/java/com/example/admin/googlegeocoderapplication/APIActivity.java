package com.example.admin.googlegeocoderapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.admin.googlegeocoderapplication.data.RetrofitHelper;
import com.example.admin.googlegeocoderapplication.model.AddressComponent;
import com.example.admin.googlegeocoderapplication.model.GeocodeQuery;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class APIActivity extends AppCompatActivity {

    private static final String TAG = "APIActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        geocodeAddress( "2034+Palace+Dr+SE,+Smyrna,+GA+30080,+USA" );
        reverseGeocodeCoordinates( "33.8605079,-84.51232480000002" );
    }

    private void geocodeAddress( String address ) {
        RetrofitHelper.getCoordinates( address )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribeOn(  Schedulers.io() )
                .subscribe(new Observer<GeocodeQuery>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(@NonNull GeocodeQuery geocodeQuery) {
                        // GeocodeQuery > Result > Geometry > Location

                        if( geocodeQuery.getResults().size() > 0 ) {
                            Log.d(TAG, "onNext: Lat " + geocodeQuery.getResults().get(0).getGeometry().getLocation().getLat());
                            Log.d(TAG, "onNext: Lng " + geocodeQuery.getResults().get(0).getGeometry().getLocation().getLng());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void reverseGeocodeCoordinates( String latLng ) {
        RetrofitHelper.getAddress( latLng )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribeOn(  Schedulers.io() )
                .subscribe(new Observer<GeocodeQuery>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: Reverse");
                    }

                    @Override
                    public void onNext(@NonNull GeocodeQuery geocodeQuery) {
                        // GeocodeQuery > Result > AddressComponent
                        Log.d(TAG, "onNext: Reverse");

                        if( geocodeQuery.getResults().size() > 0 ) {
                            List<AddressComponent> addressComponentList =
                                    geocodeQuery.getResults().get(0).getAddressComponents();

                            String address = "";

                            for( AddressComponent a : addressComponentList )
                                address += a.getShortName() + " ";

                            Log.d(TAG, "onNext: Address is " + address);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: Reverse" + e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
