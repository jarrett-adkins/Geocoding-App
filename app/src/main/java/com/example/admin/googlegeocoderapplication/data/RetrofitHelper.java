package com.example.admin.googlegeocoderapplication.data;

import com.example.admin.googlegeocoderapplication.model.GeocodeQuery;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitHelper {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/";

    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                .build();

        return retrofit;
    }

    public static Observable<GeocodeQuery> getCoordinates( String query ) {
        Retrofit retrofit = create();
        RequestService remoteService = retrofit.create( RequestService.class );

        return remoteService.geocode( query );
    }

    public static Observable<GeocodeQuery> getAddress( String latLng ) {
        Retrofit retrofit = create();
        RequestService remoteService = retrofit.create( RequestService.class );

        return remoteService.reveseGeocode( latLng );
    }

    public interface RequestService{
        @GET( "json?key=AIzaSyBUAQ92de3gr5JI4jW46OFL_li-EFhHMD8" )
        Observable<GeocodeQuery> geocode(@Query("address") String query);

        @GET( "json?key=AIzaSyBUAQ92de3gr5JI4jW46OFL_li-EFhHMD8" )
        Observable<GeocodeQuery> reveseGeocode(@Query("latlng") String query);

    }
}
//https://maps.googleapis.com/maps/api/geocode/json?address=
// 1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY