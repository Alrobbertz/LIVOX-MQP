package com.blinklogger.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
//import android.support.annotation.IntDef;
//import android.support.annotation.StringDef;
//import android.support.v4.app.ActivityCompat;
import androidx.annotation.IntDef; //<- Add this
import androidx.annotation.StringDef; //<- Add this
import androidx.core.app.ActivityCompat; // <- Add this

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Jason on 4/19/17.
 */

public class PermissionsUtils {
    private static final String TAG = "PermissionsUtils";

    // RequestCodes to identify the type of request
    @IntDef({
            PermissionRequestCode.FINE_AND_COARSE_LOCATION,
            PermissionRequestCode.READ_AND_WRITE_EXTERNAL,
            PermissionRequestCode.CAMERA,
            PermissionRequestCode.MULTIPLE_REQUESTS,
            PermissionRequestCode.RECORD_AUDIO
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionRequestCode {
        int FINE_AND_COARSE_LOCATION = 0;
        int READ_AND_WRITE_EXTERNAL = 1;
        int CAMERA = 2;
        int MULTIPLE_REQUESTS = 3;
        int RECORD_AUDIO = 4;
    }

    // Permissions
    @StringDef({
            Permission.COARSE_LOCATION,
            Permission.FINE_LOCATION,
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE,
            Permission.CAMERA,
            Permission.RECORD_AUDIO
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Permission {
        String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
        String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
        String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
        String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String CAMERA = Manifest.permission.CAMERA;
        String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    }

    public static void makePermissionRequest(Activity activity, @Permission String permission, @PermissionRequestCode int requestCode){
        String[] permissions = { permission };

        ActivityCompat.requestPermissions(
                activity,
                permissions,
                requestCode);
    }

    public static void checkPermission(Activity activity, @Permission String permission, @PermissionRequestCode int requestCode){
        PackageManager packageManager = activity.getPackageManager();

        int hasPermission = packageManager.checkPermission(permission,
                activity.getPackageName());

        if(hasPermission != PackageManager.PERMISSION_GRANTED) {
            makePermissionRequest(activity, permission, requestCode);
        }
    }

    public static void checkPermission(Activity activity, String[] permissions, @PermissionRequestCode int requestCode) {
        PackageManager packageManager = activity.getPackageManager();

        boolean permissionIsGranted = true;
        for(String permission : permissions) {
            int hasPermission = packageManager.checkPermission(permission,
                    activity.getPackageName());

            if(hasPermission != PackageManager.PERMISSION_GRANTED){
                permissionIsGranted = false;
                break;
            }
        }

        //Check if Permission is granted
        if(!permissionIsGranted){
            ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    requestCode);
        }
    }

    public static boolean isPermissionGranted(Context context, @Permission String permission){
        PackageManager packageManager = context.getPackageManager();
        int hasPermission = packageManager.checkPermission(permission,
                context.getPackageName());
        //Check if Permission is granted
        return hasPermission == PackageManager.PERMISSION_GRANTED;
    }

}
