/**
 * 
 */
package com.touhiDroid.backgroundgpsgetter.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author Touhid
 * 
 */
public class LocationUpdateReceiver extends BroadcastReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("LocationUpdateReceiver", "onReceive");
		context.startService(new Intent(context, GPSSenderService.class));
	}

}
