package com.d360.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.d360.sdk.services.AsyncPostEvent;
import com.d360.sdk.services.ServiceMain;

import org.json.JSONObject;



public class D360SDK {

    private static final String TAG = "D360SDK";

	/**
	 * Init the Http connection of the SDK
	 * @param apiKey: String, personal key of the api
	 * @param ctx: Context of the app
	 */
    public static void init(String apiKey, Context ctx) {

		try {
			Http.init(apiKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(TAG, "Starting SDK with key " + apiKey);

		App.init(ctx);

		ctx.startService(new Intent(ctx, ServiceMain.class));
		Log.d(TAG, "Service started...");
    }

	/**
	 * Register the BroadcastReceiver that watches the Connectivity actions
	 * @param ctx: Context of the app
	 */
	public static void registerToConnectivity(Context ctx) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);

		BroadcastReceiver receiver = new ConnectivityBroadcastReceiver();
		ctx.registerReceiver(receiver, filter);
	}

	/**
	 * Send a custom event to API
	 * @param name: String, name of the event
	 * @param parameters: JSONObject, object containing information about the event
	 */
    public static void sendEvent(String name, JSONObject parameters) {

		Log.i(TAG, "Sending event " + name + ", with parameters " + parameters.toString());

		new AsyncPostEvent(name, parameters).execute((Void[]) null);

    }
}
