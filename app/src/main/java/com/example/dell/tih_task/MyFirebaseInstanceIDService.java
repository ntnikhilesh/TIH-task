package com.example.dell.tih_task;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by DELL on 12/17/2016.
 */

//Set custom default icon. This is used when no icon is set for incoming notification messages.

public class MyFirebaseInstanceIDService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


   /* @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("tag1", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    } */
}
