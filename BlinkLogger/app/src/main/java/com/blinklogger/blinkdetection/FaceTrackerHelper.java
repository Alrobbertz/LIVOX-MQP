package com.blinklogger.blinkdetection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.io.IOException;

import com.blinklogger.R;
import com.blinklogger.utils.PermissionsUtils;

/**
 * Created by Jason on 7/11/17.
 */

public class FaceTrackerHelper extends Tracker<Face>  {
    private static final String TAG = "FaceTrackerHelper";

    private Activity mActivity;
    private CameraSource mCameraSource;
    private FaceDetector mFaceDetector;

    private static final int RC_HANDLE_GMS = 9001;

    public FaceTrackerHelper(Activity activity){
        mActivity = activity;

        checkCameraPermission();
    }

    public void checkCameraPermission(){
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        boolean hasPermission = PermissionsUtils.isPermissionGranted(mActivity, PermissionsUtils.Permission.CAMERA);
        if(hasPermission){
            createCameraSource();
        } else {
            Log.e(TAG, "ERROR: FAILED TO LOAD PERMISSIONS FOR THE CAMERA");
            PermissionsUtils.makePermissionRequest(mActivity, PermissionsUtils.Permission.CAMERA, PermissionsUtils.PermissionRequestCode.CAMERA);
        }
    }

    private void createCameraSource(){
        Context applicationContext = mActivity.getApplicationContext();
        mFaceDetector = new FaceDetector.Builder(applicationContext)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setProminentFaceOnly(true)
                .setMode(FaceDetector.ACCURATE_MODE)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        if (!mFaceDetector.isOperational()) {
            // Note: The first time that an app using onFaceUpdate API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
//            String message = applicationContext.getString(R.string.face_detector_dependencies_are_not_avail);
//            Log.w(TAG, message);
//            Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
            return;
        }

        mFaceDetector.setProcessor(new LargestFaceFocusingProcessor(mFaceDetector, this));

        mCameraSource = new CameraSource.Builder(applicationContext, mFaceDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }

    protected void startCameraFeed() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                mActivity.getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(mActivity, code, RC_HANDLE_GMS);
            dlg.show();
        }

        // Start CameraSource
        if(mCameraSource != null){
            try {
                mCameraSource.start();
            } catch (IOException e) {
//                Toast.makeText(mActivity, mActivity.getString(R.string.start_camera_failed), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    protected void stopCameraFeed(){

        if (mCameraSource != null) {
            mCameraSource.stop();
        }

        releaseCameraAndDetector();
    }

    private void releaseCameraAndDetector(){
        if(mFaceDetector != null){
            mFaceDetector.release();
            mFaceDetector = null;
        }

        if(mCameraSource != null) {
            mCameraSource.release();
            mCameraSource = null;
        }
    }
}
