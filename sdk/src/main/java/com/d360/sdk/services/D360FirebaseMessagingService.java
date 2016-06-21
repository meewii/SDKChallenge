package com.d360.sdk.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.d360.sdk.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/*
Service that will receive the FCMessages sent by the server
(tested with the console, it works)
 */
public class D360FirebaseMessagingService extends FirebaseMessagingService {

	final String TAG = "FCM";

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		Log.d(TAG, "onMessageReceived() called with: " + "remoteMessage = [" + remoteMessage + "]");

		RemoteMessage.Notification n = remoteMessage.getNotification();
		Log.d(TAG, "onMessageReceived: " + n.getBody());


		// TODO: Handle FCM messages here.
		Map<String, String> data = remoteMessage.getData();
		Log.d(TAG, "data: "+data.toString());

		String title = remoteMessage.getNotification().getTitle();
		String message = remoteMessage.getNotification().getBody();
		String imageUrl = data.get("image"); // set in key/value of the Firebase console
		String action = data.get("action"); // set in key/value of the Firebase console
		Log.i(TAG, "onMessageReceived: title : "+title);
		Log.i(TAG, "onMessageReceived: message : "+message);
		Log.i(TAG, "onMessageReceived: imageUrl : "+imageUrl);
		Log.i(TAG, "onMessageReceived: action : "+action);

		if (imageUrl == null) {
			sendNotification(title,message,action);
		} else {
			sendNotification(title,message,action);
			//TODO: BigPictureNotification class that will process the URL
			//new BigPictureNotification(this,title,message,imageUrl,action);
		}

	}


	private void sendNotification(String title, String message, String action) {

		Log.d(TAG, "sendNotification() called with: " + "title = [" + title + "], message = [" + message + "], action = [" + action + "]");

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						// TODO: get icon from app, not from sdk
						//context.getResources().getDrawable(R.drawable.mastercard_securecode);
						.setSmallIcon(R.drawable.notification_icon)
						.setContentTitle(title)
						.setContentText(message);


		// Sets an ID for the notification
		int mNotificationId = 001;

		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, mBuilder.build());

	}

}
