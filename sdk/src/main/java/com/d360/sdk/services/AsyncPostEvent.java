package com.d360.sdk.services;

import android.os.AsyncTask;
import android.util.Log;

import com.d360.sdk.App;
import com.d360.sdk.ConnectionInfo;
import com.d360.sdk.Http;
import com.d360.sdk.objects.Event;

import org.json.JSONException;
import org.json.JSONObject;

public class AsyncPostEvent extends AsyncTask<Void, Void, Integer> {

	private static final String TAG = "AsyncPostEvent";

	private Event mEvent;

	private final int STATUS_FAIL = 0;
	private final int STATUS_OK = 1;

	/*
	Constructor used when this is the 1st time we send this event to the API
	 */
	public AsyncPostEvent(String name, JSONObject parameters) {
		mEvent = new Event(name, parameters);
	}

	/*
	Constructor used when the event was already created and the connection failed,
	the event was stored in the SharedPreferences
	 */
	public AsyncPostEvent(Event event) {
		mEvent = event;
	}


	@Override
	protected Integer doInBackground(Void... v) {

		Log.d(TAG, "doInBackground...");

		try {

			// Build json that will be sent
			JSONObject meta = new JSONObject();
			JSONObject parent = new JSONObject();

			// Build the meta
			// add name given by the App's dev
			meta.put("name", mEvent.getName());

			// add timestamp
			long unixTime = System.currentTimeMillis() / 1000L;
			meta.put("localTimeStamp", unixTime);

			// watch the connection status and add it to meta
			ConnectionInfo ci = new ConnectionInfo();
			String status = ci.getCurrentStatus();
			meta.put("connectionInfo", status);

			// create an event number (i didn't know how to generate it so I created a random number)
			int eventNo = (int)(Math.random()*10000);
			meta.put("eventNo", eventNo);

			parent.put("meta", meta);

			// Build the data (parameters given by the App's dev
			if(mEvent.getData() != null) {
				parent.put("data", mEvent.getData());
			} else {
				parent.put("data", "");
			}

			if(ci.isConnected()) {

				// Post data and get json response
				JSONObject response = Http.post(parent);

				// check status and do things according to status
				if (response != null && response.has("meta") && !response.isNull("meta")) {

					JSONObject responseMeta = response.getJSONObject("meta");
					if (responseMeta.has("httpCode") && !responseMeta.isNull("httpCode")) {

						int responseHttpCode = responseMeta.getInt("httpCode");
						if(responseHttpCode == 201) {
							Log.i(TAG, "SuccessAPI: "+response.toString());
							Log.i(TAG, "ID: "+mEvent.getKey());

							// If "POST event" is a success and if the event was previously
							// stored in the prefs, we delete it from prefs
							if(mEvent.getKey() != null)	App.clearEventFromStorage(mEvent.getKey());

							return STATUS_OK;
						} else {
							Log.e(TAG, "\"httpCode\" is not 201");
							return STATUS_FAIL;
						}

					} else {
						Log.e(TAG, "\"httpCode\" is null");
						return STATUS_FAIL;
					}

				} else {
					Log.e(TAG, "\"meta\" is null");
					return STATUS_FAIL;
				}

			} else {
				Log.e(TAG, "phone is offline");
				return STATUS_FAIL;
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}

		return STATUS_FAIL;
	}

	@Override
	protected void onPostExecute(Integer status) {


		switch(status) {
			case STATUS_FAIL:
				Log.d(TAG, "AsyncPostEvent.onPostExecute - STATUS_FAIL");


				if(mEvent.getKey() == null) {
					// if POST event has failed, we store the event in the preferences

					// set status as "idle" when the event is stored
					mEvent.setStatus(Event.STATUS_IDLE);

					// we use a generated key if the event was never stored before
					mEvent.setkey(App.generateKey());

					App.storeEvent(mEvent);
				} else {
					// the event is already stored but its status is set back to idle
					// as the process is done
					App.updateEventStatus(mEvent, Event.STATUS_IDLE);
				}

				break;
			case STATUS_OK:
				Log.d(TAG, "AsyncPostEvent.onPostExecute - STATUS_OK");

				break;
		}


	}

}

