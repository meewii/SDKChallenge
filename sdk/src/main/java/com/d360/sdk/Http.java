package com.d360.sdk;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {

	private static final String TAG = "SDKchallenge";

	private static final String API_URL = "http://api.dev.staging.crm.slace.me/v2/events";
	private static Http http;
	private static String mKey;

	public static void init(String apiKey) throws Exception {

		if(http == null) {
			http = new Http();
			mKey = apiKey;
		}

	}


	public static JSONObject post(JSONObject data) {
		JSONObject json;
		try {
			json = execute(RestMethod.POST, data);
			return json;
		} catch(IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}


	private static JSONObject execute(RestMethod method, JSONObject data) throws IOException, JSONException {

		URL obj = new URL(API_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add common header
		con.setRequestProperty("D360-Api-Key", mKey);
		con.setRequestProperty("Content-Type", "application/json");

		switch (method) {
			case GET: {
				con.setRequestMethod("GET");
				break;
			}
			case POST: {
				con.setRequestMethod("POST");


				// Send post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());

				//sending
				Log.i(TAG, "Http - Sending object : " + data.toString());
				wr.writeBytes(data.toString());

				wr.flush();
				wr.close();

				break;
			}
		}


		int responseCode = con.getResponseCode();
		String responseMsg = con.getResponseMessage();
		Log.i(TAG, "Http - Sending 'POST' request to URL : " + API_URL);
		Log.i(TAG, "Http - Response - " + responseCode + ": " + responseMsg);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONObject responseJson = new JSONObject(response.toString());

		Log.i(TAG, "Http - Response JSON - " + responseJson.toString());

		return responseJson;
	}

	private enum RestMethod {
		DELETE, GET, POST, PUT
	}


}