package com.d360.sdk;

import android.app.Application;

public class App extends Application {

	private static String LOG_TAG = "App";

	public static Long lastDisconnection;


	public static Long getLastDisconnection() {
		return lastDisconnection;
	}

	public static void setLastDisconnection(Long timestamp) {
		lastDisconnection = timestamp;
	}

}
