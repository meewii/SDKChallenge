package com.d360.sdk.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.d360.sdk.App;
import com.d360.sdk.ConnectionInfo;
import com.d360.sdk.Http;

import org.json.JSONException;
import org.json.JSONObject;

public class AsyncPostEvent extends AsyncTask<Void, Void, Integer> {

	private static final String TAG = "AsyncPostEvent";

	private String mId;
	private String mName;
	private JSONObject mData;
	private Context mContext;

	private final int STATUS_FAIL = 0;
	private final int STATUS_OK = 1;

	public AsyncPostEvent(String name, JSONObject parameters) {
		mName = name;
		mData = parameters;
		mContext = App.getContext();
	}

	public AsyncPostEvent(String id, String name, JSONObject parameters) {
		mId = id;
		mName = name;
		mData = parameters;
		mContext = App.getContext();
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
			meta.put("name",mName);

			// add timestamp
			long unixTime = System.currentTimeMillis() / 1000L;
			meta.put("localTimeStamp", unixTime);

			// watch the connection status and add it to meta
			ConnectionInfo ci = new ConnectionInfo();
			String status = ci.getCurrentStatus();
			meta.put("connectionInfo", status);

			// create an event number (i didn't know how to generate it so i created a random number)
			int eventNo = (int)(Math.random()*10000);
			meta.put("eventNo", eventNo);

			parent.put("meta", meta);

			// Build the data (parameters given by the App's dev
			if(mData != null) {
				parent.put("data", mData);
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
							Log.i(TAG, "ID: "+mId);

							if(mId != null)	App.clearEventFromStorage(mId);

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

				if(mId == null) {
					Log.w(TAG, "generating ID...");
					mId = App.generateId();
					App.storeEvent(mId, mName, mData);
				} else {
					Log.w(TAG, "ID: "+mId);
				}

				break;
			case STATUS_OK:
				Log.d(TAG, "AsyncPostEvent.onPostExecute - STATUS_OK");

				break;
		}


	}

}

