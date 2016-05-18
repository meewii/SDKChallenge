package com.d360.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/*
* This BroadcastReceiver is registered by the App's dev with the method D360SDK.registerToConnectivity();
* It is receiving actions CONNECTIVITY_ACTION
* */
public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "BroadcastReceiver";


	@Override
	public void onReceive(Context context, Intent intent) {

		// When the event CONNECTIVITY_ACTION is triggered
		// we check the connection
		ConnectionInfo ci = new ConnectionInfo(context);
		String status = ci.getCurrentStatus();
		Log.d(TAG, "Receiver - connection status: " + status);


		try {

			JSONObject parameters = new JSONObject();
			long unixTime = System.currentTimeMillis() / 1000L;
			parameters.put("changed_at", unixTime);
			parameters.put("new_status", status);

			// We send the connection status to the server
			if(ci.isConnected()) {

				// the information about the last disconnection is sent
				Long lastDisconnection = App.getLastDisconnection();
				if(lastDisconnection!=null && lastDisconnection!=0L) {
					parameters.put("last_disconnected_at", lastDisconnection);
				} else {
					parameters.put("last_disconnected_at", "-1"); // -1 for "timestamp unknown"
				}

				new AsyncPostEvent("Change Connectivity Status", parameters, context).execute((Void[]) null);

			} else {

				// as we can't send information to the API when the phones gets offline,
				// this information is stored until the next connection
				App.setLastDisconnection(unixTime);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
