package com.d360.sdk.objects;

import org.json.JSONObject;

public class Event {

	private String name;
	private JSONObject data;
	private String key;
	private String status;

	public static final String STATUS_PROCESSING = "processing";
	public static final String STATUS_IDLE = "not_processing";

	public Event(String name, JSONObject data, String key, String status) {
		this.name = name;
		this.data = data;
		this.key = key;
		this.status = status;
	}

	public Event(String name, JSONObject data) {
		this.name = name;
		this.data = data;
	}


	public void setStatus(String status) {
		if(status.contentEquals(STATUS_PROCESSING)
				|| status.contentEquals(STATUS_IDLE)) {
			this.status = status;
		} else {
			this.status = STATUS_IDLE;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setkey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public JSONObject getData() {
		return data;
	}


}
