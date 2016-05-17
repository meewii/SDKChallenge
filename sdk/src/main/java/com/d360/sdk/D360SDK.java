package com.d360.sdk;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;


public class D360SDK {

    private static final String TAG = "SDKchallenge";
    private static Context context;

    public static void init(String apiKey, Context ctx) {
		context = ctx;
		try {
			Http.init(apiKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(TAG, "D360SDK - Starting SDK with key " + apiKey);
    }

    public static void sendEvent(String name, JSONObject parameters) {
        Log.i(TAG, "D360SDK - Sending event " + name + "with parameters " + parameters.toString());

		new AsyncPostEvent(name, parameters, context).execute((Void[]) null);
    }
}
