package com.d360.sdk;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.d360.sdk.objects.Event;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Map;
import java.util.UUID;

public class App extends Application {

	private static final String TAG = "App";

	private static Context context;
	private static Long lastDisconnection;
	private static SharedPreferences sharedPref;

	/*
	* Getter&Setter application's context
	* */
	public static Context getContext() {
		return context;
	}

	public static void init(Context ctx) {
		context = ctx;
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



	/*
	SHARED PREFERENCES
	if an event was sent to the server but the process failed (e.g. the phone is offline)
	the event is stored in a SharedPreferences
	 */
	//TODO: store events in a DB

	/*
	 * return the SharedPreferences sheet in which we store our events, there can be several
	 */
	private static SharedPreferences getEventsSharedPreferences() {
		if(sharedPref == null) {
			sharedPref = context.getSharedPreferences(
					context.getString(R.string.pref_key_stored_events), Context.MODE_PRIVATE);
		}
		return sharedPref;
	}


	/*
	 * Add a event to the sheet
	 */
	public static void storeEvent(Event event) {
		SharedPreferences.Editor editor = getEventsSharedPreferences().edit();

		Gson gson = new Gson();
		String json = gson.toJson(event);
		editor.putString(event.getKey(), json);
		editor.commit();
	}

	/*
	* Two statuses: Processing or Idle,
	* The event is processing when it is being sent through AsyncPostEvent
	* It is Idle when the evnt is created or after processing+failure through AsyncPostEvent
	* */
	public static void updateEventStatus(Event event, String status) {
		SharedPreferences.Editor editor = getEventsSharedPreferences().edit();

		Log.d(TAG, "Update status to "+status+", id: "+event.getKey());
		event.setStatus(status);
		Gson gson = new Gson();
		String json = gson.toJson(event);
		editor.putString(event.getKey(), json);
		editor.commit();
	}

	/*
	 * delete one event by id
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

	/*
	 * Generate an id for each event that is stored in the SharedPreferences
	 */
	public static String generateKey() {
		UUID key = UUID.randomUUID();
		return key.toString();
	}
}
