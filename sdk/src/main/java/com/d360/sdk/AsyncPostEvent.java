package com.d360.sdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class AsyncPostEvent extends AsyncTask<Void, Void, String> {

	private static final String TAG = "SDKchallenge";

	private String mName;
	private JSONObject mData;
	private Context mContext;

	public AsyncPostEvent(String name, JSONObject parameters, Context ctx) {
		mName = name;
		mData = parameters;
		mContext = ctx;
	}


	@Override
	protected String doInBackground(Void... v) {

		try {

			// Build json that will be sent
			JSONObject meta = new JSONObject();
			JSONObject parent = new JSONObject();

			// Build the meta
			meta.put("name",mName);

			long unixTime = System.currentTimeMillis() / 1000L;
			meta.put("localTimeStamp", unixTime);

			String connectionInfo = "wifi"; // getConnectionInfo();
			meta.put("connectionInfo", connectionInfo);

			int eventNo = (int)(Math.random()*10000);
			meta.put("eventNo", eventNo);

			parent.put("meta", meta);

			// Build the data
			if(mData != null) {
				parent.put("data", mData);
			} else {
				parent.put("data", "");
			}

			// Post data and get json response
			JSONObject response = Http.post(parent);
			if (response != null) {

				// check status and do things according to status
				if (response.has("meta") && !response.isNull("meta")) {

					JSONObject responseMeta = response.getJSONObject("meta");
					if (responseMeta.has("httpCode") && !responseMeta.isNull("httpCode")) {

						int responseHttpCode = responseMeta.getInt("httpCode");
						if(responseHttpCode == 201) {
							Log.i(TAG, "Successfully sent!");
							return "Event successfully sent!";
						} else {
							Log.e(TAG, "AsyncPostEvent - \"httpCode\" is not 201");
							return "Error post event: \"httpCode\" is not 201";
						}

					} else {
						Log.e(TAG, "AsyncPostEvent - \"httpCode\" is null");
						return "Error: \"httpCode\" is null";
					}

				} else {
					Log.e(TAG, "AsyncPostEvent - \"meta\" is null");
					return "Error: \"meta\" is null";
				}
			} else {
				Log.e(TAG, "AsyncPostEvent - response is null");
				return "Error: response is null";
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}


		return "Error: unknown error";
	}

	@Override
	protected void onPostExecute(String status) {

		Log.i(TAG, "AsyncPostEvent.onPostExecute - status:"+status);

		Toast toast = Toast.makeText(mContext, status, Toast.LENGTH_SHORT);
		toast.show();

	}

}

