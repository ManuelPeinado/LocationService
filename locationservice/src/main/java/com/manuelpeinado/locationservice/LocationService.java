package com.manuelpeinado.locationservice;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by manuel on 21/07/13.
 */
public class LocationService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    protected static final String TAG = LocationService.class.getSimpleName();
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");

        super.onCreate();

        if (!servicesAvailable()) {
            stopSelf();
            return;
        }

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(15 * 60 * 1000)
                .setFastestInterval(1 * 60 * 1000);

        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");

        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
            mLocationClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "New location: " + Utils.format(location));
        Toast.makeText(this, "New location: " + Utils.format(location), Toast.LENGTH_SHORT).show();
    }

    private boolean servicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(TAG, "Google Play services is available.");
            return true;
        }
        Log.e(TAG, "Google Play services NOT available.");
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected to Google Play Services.");
        Log.d(TAG, "Last location is: " + Utils.format(mLocationClient.getLastLocation()));
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "Disconnected from Google Play Services.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to Google Play Services failed.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
