package com.d360.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.d360.sdk.D360SDK;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        D360SDK.init("kilctuhEeONbf-V1JMH7", getApplicationContext());

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("mykey", "myvalue");
            D360SDK.sendEvent("MyCustomEevent", parameters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
