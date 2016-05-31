package com.d360.sdk.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.d360.sdk.App;
import com.d360.sdk.ConnectionInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceMain extends Service {


	private final String TAG = "Service";
	private Timer timer;

	@Override
	public void onCreate() {
		super.onCreate();

		initSchedulers();
	}


	/**
	 * init all schedulers
	 */
	private void initSchedulers() {

		if(timer != null){
			timer.cancel();
		}

		timer = new Timer();

		// Schedule 0ms delay, every 5s
		timer.schedule(new TimerTask() {

			@Override
			public void run() {


				try {
					Log.i(TAG, "Total event in prefs: "+App.getStoredEventsSize());
					if(
						App.isConnected() &&
						App.getStoredEventsSize() != 0) {

						Map<String,?> events = App.getStoredEvents();

						for(Map.Entry<String,?> entry : events.entrySet()){

							String event = entry.getValue().toString();

							String[] parts = event.split("°");
							String name = parts[0];
							String strJson = parts[1];
							JSONObject parameters = new JSONObject(strJson);

							Log.w(TAG, "SEND to API: "+name+" -- "+strJson);
							new AsyncPostEvent(entry.getKey(), name, parameters).execute((Void[]) null);
						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}

		}, 0, 5000);

	}



	@Nullable
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onTaskRemoved(Intent rootIntent){
		Log.e(TAG, "onTaskRemove...");
		super.onTaskRemoved(rootIntent);
	}


}

