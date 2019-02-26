package jstaffor.android.jobsight.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class Permissions
{
    private static final String TAG = "Permissions";
    public static final int CAMERA = 1;
    public static final int ACCESS_FINE_LOCATION = 2;
    public static final int ACCESS_COARSE_LOCATION = 3;
    public static final int RECORD_AUDIO = 4;
    public static final int WRITE_EXTERNAL_STORAGE = 5;
    public static final int READ_EXTERNAL_STORAGE = 6;
    public static final int ACCESS_NETWORK_STATE = 7;
    public static final int ACCESS_WIFI_STATE = 8;

    private Activity activity;

    public Permissions(Activity activity)
    {
        super();

        if(activity == null)
            throw new IllegalArgumentException("Permissions.Permissions(Activity activity) - activity cannot be null");

        this.activity = activity;
    }

    public boolean hasPermissions(
            boolean CAMERA, boolean ACCESS_FINE_LOCATION, boolean ACCESS_COARSE_LOCATION,boolean RECORD_AUDIO,
            boolean WRITE_EXTERNAL_STORAGE, boolean READ_EXTERNAL_STORAGE, boolean ACCESS_NETWORK_STATE,
            boolean ACCESS_WIFI_STATE)
    {
        List<String> permissions = new ArrayList<String>();

        //(1) Get list of permissions to check
        if(CAMERA) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.CAMERA))
                permissions.add(Manifest.permission.CAMERA);
        }

        if(ACCESS_FINE_LOCATION) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(ACCESS_COARSE_LOCATION) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION))
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if(RECORD_AUDIO) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO))
                permissions.add(Manifest.permission.RECORD_AUDIO);
        }

        if(WRITE_EXTERNAL_STORAGE) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(READ_EXTERNAL_STORAGE) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(ACCESS_NETWORK_STATE) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE))
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if(ACCESS_WIFI_STATE) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE))
                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        //(2) Ask for permission for all required permissions
        if(permissions.size()>0)
            activity.requestPermissions(permissions.toArray(new String[0]), 1);

        //(3) If permission was sought but not given, just return false straight away
        if(CAMERA)
            if(PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.CAMERA))
                return false;

        if(ACCESS_FINE_LOCATION)
            if(PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                return false;

        if(ACCESS_COARSE_LOCATION)
            if(PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION))
                return false;

        if(RECORD_AUDIO)
            if(PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO))
                return false;

        if(WRITE_EXTERNAL_STORAGE)
            if(PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                return false;

        if(ACCESS_NETWORK_STATE)
            if(PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE))
                return false;

        if(ACCESS_WIFI_STATE)
            if(PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE))
                return false;

        //(4) If we got this far, all is gravy, baby!
        return true;
    }
}
