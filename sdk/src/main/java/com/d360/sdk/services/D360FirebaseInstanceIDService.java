package com.d360.sdk.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/*

 */
public class D360FirebaseInstanceIDService extends FirebaseInstanceIdService {

	final String TAG = "FCM";
	@Override
	public void onTokenRefresh() {
		// Get updated InstanceID token.
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "Refreshed token: " + refreshedToken);

		// TODO: Implement this method to send any registration to your app's servers.
		//sendRegistrationToServer(refreshedToken);
	}


}