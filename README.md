# SDKchallenge

##Challenge Description

Build a mobile SDK that allows an app developer to track user events in his application. Those events are sent to a Events REST API.

The SDK you build should cover the following user stories:
* As an app developer, I want to initialize the SDK with my API key
* As an app developer, I want to track events with a custom name
* As an app developer, I want to optionally add a list of key/value parameters to each event
* As an app developer, I want to have a sample app that can be used to demonstrate the SDK functionality

##My SDK

I implemented in the application two examples of how to send events:

1. The app developer can register to a BroadcastReceiver that watches the phone connectivity (ConnectivityBroadcastReceiver). When this receiver is triggered, an event is automatically sent to the server.

2. The app developer can send events directly when the user clicks on Views. The example is an advert that the user can click. If the user clicks on the ad, we send "when" and "how long after opening the page".

###How to use

To initialize with your API-key:

    D360SDK.init("myApiKey", getApplicationContext());

To start watching the user's phone connectivity, you can register the ConnectivityBroadcastReceiver:

    D360SDK.registerToConnectivity(getApplicationContext());

To send a custom event with key/value parameters:

    JSONObject params = new JSONObject();
    params.put("myKey", "myValue");
    D360SDK.sendEvent("My event name", params);
