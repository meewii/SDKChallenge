package com.d360.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionChangeEvent extends BroadcastReceiver {

	private static final String TAG = "SDKchallenge";


	@Override
	public void onReceive(Context context, Intent intent) {

		ConnectionInfo ci = new ConnectionInfo(context);
		String status = ci.getCurrentStatus();

		Log.d("SDKchallenge", "Broadcast receiver status: " + status);

		try {

			JSONObject parameters = new JSONObject();
			long unixTime = System.currentTimeMillis() / 1000L;
			parameters.put("changed_at", unixTime);
			parameters.put("new_status", status);
			new AsyncPostEvent("Change Connectivity Status", parameters, context).execute((Void[]) null);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
