package com.d360.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.d360.sdk.D360SDK;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

	long pageOpenTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        D360SDK.init("kilctuhEeONbf-V1JMH7", getApplicationContext());

		/*
		Example A of how the App dev could use the SDK:
		register a BroadcastReceiver that watches the phone connectivity
		*/
        //D360SDK.registerToConnectivity(getApplicationContext());


		/*
		Example B of how the App dev could use the SDK:
		watch if the user clicks on ads, when and how long after opening the page
		*/
		Button myAd = (Button) findViewById(R.id.myAd);
		assert myAd != null;
		myAd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// go to ad's links

				// send a custom event when the user of the app clicks on this ad
				JSONObject clickEvent = new JSONObject();
				try {
					clickEvent.put("user_id", "myAppUserId");
					long clickTime = System.currentTimeMillis() / 1000L;
					clickEvent.put("clicked_at", clickTime);
					clickEvent.put("opened_page_at", pageOpenTime);
					D360SDK.sendEvent("Ad's clicks", clickEvent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

    }
}
