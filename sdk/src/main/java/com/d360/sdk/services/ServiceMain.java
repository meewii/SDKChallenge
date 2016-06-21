package com.d360.sdk.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.d360.sdk.App;
import com.d360.sdk.ConnectionInfo;
import com.d360.sdk.objects.Event;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceMain extends Service {

	private ConnectionInfo ci;
	private final String TAG = "Service";
	private Timer timer;

	@Override
	public void onCreate() {
		super.onCreate();

		ci = new ConnectionInfo();
		initScheduler();
	}


	/**
	 * Starts the Timer that check the stored events every 5s
	 */
	private void initScheduler() {

		if(timer != null){
			timer.cancel();
		}

		timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				try {
					//Log.i(TAG, "Total event in prefs: "+App.getStoredEventsSize());
					if(ci.isConnected() &&
						App.getStoredEventsSize() != 0) {
						// Process the events only if the phone is online
						// and if the SharedPreferences contain events

						processStoredEvents(App.getStoredEvents());

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}, 0, 5000); // Delay 0ms, every 5s

	}

	/*
	* Send stored events to API
	* */
	private void processStoredEvents(Map<String, ?> events) {

		// Loop through all shared preferences
		for(Map.Entry<String,?> entry : events.entrySet()){

			// Get back event objects for each pref
			String eventStr = entry.getValue().toString();
			Gson gson = new Gson();
			Event event = gson.fromJson(eventStr, Event.class);

			//Log.w(TAG, "GET from Gson: "+event.getName()+" -- "+event.getId()+" -- "+event.getStatus());

			// Check the status, must be Idle to be sent
			String status = event.getStatus();
			if(status != null && status.contentEquals(Event.STATUS_IDLE)) {
				// Update status as it's now being processed
				App.updateEventStatus(event, Event.STATUS_PROCESSING);

				new AsyncPostEvent(event)
						.execute((Void[]) null);
			} else {
				Log.w(TAG, "Status was on \"processing\", for id: "+event.getKey());
			}
//;
		}
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

