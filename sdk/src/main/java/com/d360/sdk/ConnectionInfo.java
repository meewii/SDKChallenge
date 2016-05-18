package com.d360.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
* ConnectionInfo helps finding the phone's connection information
* */
public class ConnectionInfo {

	String currentStatus;
	ConnectivityManager cm;
	NetworkInfo activeNetwork;

	private static final String TAG = "ConnectionInfo";

	public ConnectionInfo(Context ctx) {
		cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

	}

	/**
	 * Says if the phone is online or offline
	 * @return boolean
	 */
	public boolean isConnected() {
		activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	/**
	 * Returns the connection status of the phone
	 * "WIFI", "MOBILE" or custom "Offline"
	 * @return String
	 */
	public String getCurrentStatus() {

		activeNetwork = cm.getActiveNetworkInfo();

		// phone is connected to the internet
		if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
				// connected to wifi
				currentStatus = activeNetwork.getTypeName();

			} else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
				// connected to the mobile provider's data plan
				currentStatus = activeNetwork.getTypeName();
			} else {
				currentStatus = "Offline";
			}
		// phone is not connected to the internet
		} else {
			currentStatus = "Offline";
		}

		return currentStatus;
	}


}
