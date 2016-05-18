package com.d360.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/*
* This BroadcastReceiver is registered in the SDK manifest.
* It is receiving actions CONNECTIVITY_CHANGE and WIFI_STATE_CHANGED
* */
public class ConnectionChangeEvent extends BroadcastReceiver {

	private static final String TAG = "ConnectionChangeEvent";


	@Override
	public void onReceive(Context context, Intent intent) {

		// When events CONNECTIVITY_CHANGE or WIFI_STATE_CHANGED are triggered
		// we check the connection
		ConnectionInfo ci = new ConnectionInfo(context);
		String status = ci.getCurrentStatus();

		Log.d(TAG, "Receiver - connection status: " + status);

		try {

			JSONObject parameters = new JSONObject();
			long unixTime = System.currentTimeMillis() / 1000L;
			parameters.put("changed_at", unixTime);
			parameters.put("new_status", status);

			// We send the status to the server through an event
			new AsyncPostEvent("Change Connectivity Status", parameters, context).execute((Void[]) null);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
