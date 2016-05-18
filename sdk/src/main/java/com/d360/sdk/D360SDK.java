package com.d360.sdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONObject;



public class D360SDK {

    private static final String TAG = "D360SDK";
    private static Context context;

	/**
	 * Init the Http connection of the SDK
	 * @param apiKey: String, personal key of the api
	 * @param ctx: Context of the app
	 */
    public static void init(String apiKey, Context ctx) {
		context = ctx;

		try {
			Http.init(apiKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(TAG, "Starting SDK with key " + apiKey);
    }

	/**
	 * Send a custom event to API
	 * @param name: String, name of the event
	 * @param parameters: JSONObject, object containing information about the event
	 */
    public static void sendEvent(String name, JSONObject parameters) {
        Log.i(TAG, "Sending event " + name + "with parameters " + parameters.toString());

		ConnectionInfo ci = new ConnectionInfo(context);
		if(ci.isConnected()) {
			new AsyncPostEvent(name, parameters, context).execute((Void[]) null);
		} else {

			Toast.makeText(context, "You're offline", Toast.LENGTH_SHORT).show();

		}
    }
}
