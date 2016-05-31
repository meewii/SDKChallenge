package com.d360.sdk;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

public class App extends Application {

	private static final String TAG = "App";

	private static Context context;
	private static Long lastDisconnection;
	private static SharedPreferences sharedPref;
	private static ConnectionInfo connectionInfo;

	/*
	* Getter&Setter application's context
	* */
	public static Context getContext() {
		return context;
	}

	public static void init(Context ctx) {
		context = ctx;
		connectionInfo = new ConnectionInfo();
	}

	/*
	* Getter&Setter lastDisconnection
	* */
	public static Long getLastDisconnection() {
		return lastDisconnection;
	}

	public static void setLastDisconnection(Long timestamp) {
		lastDisconnection = timestamp;
	}


	private static SharedPreferences getEventsSharedPreferences() {
		int mode = Context.MODE_PRIVATE;
		if(sharedPref == null) {
			sharedPref = context.getSharedPreferences(
					context.getString(R.string.pref_key_stored_events), mode);
		}
		return sharedPref;
	}

	public static void storeEvent(String id, String event, JSONObject json) {
		SharedPreferences.Editor editor = getEventsSharedPreferences().edit();
		// To be able to store the complete event name+json, we put both in a string
		editor.putString(id, event +"°"+json.toString());
		editor.apply();
	}

	/*
	 * delete one event by id (key)
	 */
	public static void clearEventFromStorage(String id) {
		Log.w(TAG, "Clear event: "+id);

		SharedPreferences.Editor editor = getEventsSharedPreferences().edit();
		editor.remove(id);
		editor.commit();
	}

	/*
	 * Clear all preferences
	 */
	public static void clearEventsSharedPreferences() {
		SharedPreferences.Editor editor = getEventsSharedPreferences().edit();
		editor.clear();
		editor.commit();
	}

	/*
	 * return the size of the Map in our events SharedPreferences
	 */
	public static Integer getStoredEventsSize() throws JSONException {
		return getEventsSharedPreferences().getAll().size();
	}

	/*
	 * return the content Map of the events SharedPreferences
	 */
	public static Map<String,?> getStoredEvents() throws JSONException {
		return getEventsSharedPreferences().getAll();
	}


	public static boolean isConnected() {
		return connectionInfo.isConnected();
	}


	public static String generateId() {
		UUID id = UUID.randomUUID();
		return id.toString();
	}
}
