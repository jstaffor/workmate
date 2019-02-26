package jstaffor.android.jobsight.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import jstaffor.android.jobsight.R;

public class CaptureCurrentLocation
{
    private static final String TAG = "CaptureCurrentLocation";
    private Context context;
    private double dLatitude = -1;
    private double dLongitude = -1;

    public CaptureCurrentLocation(Context context) throws SecurityException, RemoteException, CaptureCurrentLocationException
    {
        super();

        if(context == null)
            throw new IllegalArgumentException("CaptureCurrentLocation.CaptureCurrentLocation(Context context) - context cannot be null");

        this.context = context;
        this.obtainCurrentLocation();
    }

    /**
     * Set as part of constructor call
     *
     * @return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()
     */
    public double getLatitude() {
        return dLatitude;
    }
    /**
     * Set as part of constructor call
     *
     * @return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()
     */
    public double getLongitude() {
        return dLongitude;
    }

    private void obtainCurrentLocation() throws SecurityException, RemoteException, CaptureCurrentLocationException
    {
        final LocationManager locationManager = Objects.requireNonNull((LocationManager) context.getSystemService(Context.LOCATION_SERVICE), "CaptureCurrentLocation.obtainCurrentLocation (getSystemService --> LocationManager) cannot be null");

        final boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        final boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled)
        {
            throw new RemoteException("CaptureCurrentLocation.obtainCurrentLocation (RemoteException) - issue capturing coordinates");
        }

        if(isGPSEnabled)
        {
            final LocationListener mLocationListenerGPS = new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };

            if (Build.VERSION.SDK_INT >= 23 &&
                    context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListenerGPS);

                if (locationManager != null && locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                    dLatitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                    dLongitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                }
            }
            else
            {
                throw new SecurityException("CaptureCurrentLocation.obtainCurrentLocation (SecurityException) - issue capturing coordinates");
            }
        }
        //Only use network location IF GPS was not set!
        if(isNetworkEnabled && dLatitude == -1 && dLongitude == -1) {
            final LocationListener mLocationListenerNetwork = new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };

            if (Build.VERSION.SDK_INT >= 23 &&
                    context.checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListenerNetwork);

                if (locationManager != null && locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                    dLatitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
                    dLongitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
                }

            } else {
                throw new SecurityException("CaptureCurrentLocation.obtainCurrentLocation (SecurityException) - issue capturing coordinates");
            }
        }

        if(dLatitude ==-1 || dLongitude ==-1)
            throw new CaptureCurrentLocationException(((Context) context).getString(R.string.capture_location_error));
    }

    public String getAddressFromCoordinates(Double latitude, Double longitude)
    {
        if(latitude == null || longitude == null)
            throw new IllegalArgumentException("CaptureCurrentLocation.getAddressFromCoordinates(Double latitude, Double longitude) - inputs cannot be null");

        String tempAddress = "-1";
        try
        {
            Geocoder geo = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty())
            {
                tempAddress= "Latitude: " + latitude + ", Longitude:" + longitude + ".";
                return tempAddress;
            }
            else
            {
                if (addresses.size() > 0) {
                    tempAddress = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                }
                else
                {
                    tempAddress= "Latitude: " + latitude + ", Longitude:" + longitude + ".";
                }
            }
        }
        catch (IOException iOException)
        {
            tempAddress= "Latitude: " + latitude + ", Longitude:" + longitude + ".";
        }

        return tempAddress;
    }

    public static String getInternetBrowserLinkForGMaps(Double latitude, Double longitude)
    {
        if(latitude == null || longitude == null)
            throw new IllegalArgumentException("CaptureCurrentLocation.getInternetBrowserLinkForGMaps(Double latitude, Double longitude) - inputs cannot be null");

        return "https://www.google.com/maps/?q="
                + latitude
                + ","
                + longitude;
    }
    public static String getInternetBrowserLinkForGMaps(String latitude, String longitude)
    {
        if(latitude == null || longitude == null)
            throw new IllegalArgumentException("CaptureCurrentLocation.getInternetBrowserLinkForGMaps(String latitude, String longitude) - inputs cannot be null");

        return getInternetBrowserLinkForGMaps(Double.parseDouble(latitude) , Double.parseDouble(longitude));
    }

    public class CaptureCurrentLocationException extends Exception
    {
        public CaptureCurrentLocationException(String msg) {
            super(msg);
        }
    }
}
