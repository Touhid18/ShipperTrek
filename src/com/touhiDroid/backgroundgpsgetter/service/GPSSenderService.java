/**
 * 
 */
package com.touhiDroid.backgroundgpsgetter.service;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.touhiDroid.backgroundgpsgetter.model.ServerResponse;
import com.touhiDroid.backgroundgpsgetter.parser.JsonParser;
import com.touhiDroid.backgroundgpsgetter.utils.AppConstants;

/**
 * @author Touhid
 * 
 */
public class GPSSenderService extends Service implements LocationListener {

	private final String TAG = this.getClass().getSimpleName();

	// private static ProgressDialog pDialog;
	private static Context tContext;

	// private ScheduledThreadPoolExecutor schThPoolExecutor;
	private String locationStr = "";
	private Location location;

	// private Set<Integer> scheduledNotifIdSet;
	// private HashMap<Integer, ScheduledFuture<?>> scheduledThreads;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// private Looper mServiceLooper;
	// private ServiceHandler mServiceHandler;

	// // Handler that receives messages from the thread
	// private final class ServiceHandler extends Handler {
	//
	// public ServiceHandler(Looper looper) {
	// super(looper);
	// Log.d(TAG, "Service handler constructed");
	// }
	//
	// @Override
	// public void handleMessage(Message msg) {
	// // Normally we would do some work here, like download a file.
	// // For our sample, we just sleep for 10 seconds.
	// Toast.makeText(GPSSenderService.this, "service starting",
	// Toast.LENGTH_SHORT).show();
	// long endTime = System.currentTimeMillis() + 10 * 1000;
	// Log.d("End-time: ", endTime + "");
	// while (System.currentTimeMillis() < endTime) {
	// Log.d("Current location: ", locationStr);
	// synchronized (this) {
	// try {
	// Log.d("Current location: ", locationStr);
	// wait(endTime - System.currentTimeMillis());
	// } catch (Exception e) {
	// }
	// }
	// }
	// // Stop the service using the startId, so that we don't stop
	// // the service in the middle of handling another job
	// stopSelf(msg.arg1);
	// }
	// }

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate inside");
		tContext = getApplicationContext();
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		// HandlerThread thread = new HandlerThread("ServiceStartArguments",
		// android.os.Process.THREAD_PRIORITY_BACKGROUND);

		// Log.d(TAG, "Starting thread");
		// thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		// mServiceLooper = thread.getLooper();
		// mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (tContext == null)
			tContext = getApplicationContext();
		Toast.makeText(tContext, "service starting", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "service starting");

		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job
		// mLocationManager = (LocationManager)
		// getSystemService(LOCATION_SERVICE);
		//
		// mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 1000 * 60,
		// 10, this);

		// schThPoolExecutor = (ScheduledThreadPoolExecutor)
		// Executors.newScheduledThreadPool(3);
		// schThPoolExecutor.scheduleAtFixedRate(new Runnable() {
		// @Override
		// public void run() {
		//
		// Log.i(TAG,"Getting current location ... ");
		// Location location = getLocation();
		// locationStr = "Latitude: " + location.getLatitude() + ", Longitude: "
		// + location.getLongitude();
		// Log.d(TAG, "Current Location : " + locationStr);
		// }
		// }, 0, 5, TimeUnit.SECONDS);

		// Message msg = mServiceHandler.obtainMessage();
		// msg.arg1 = startId;
		// mServiceHandler.sendMessage(msg);

		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							Log.i(TAG, "Getting current location ... ");
							Location location = getLocation();
							double latitude, longitude;
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							locationStr = "Latitude: " + latitude + ", Longitude: " + longitude;
							Log.d(TAG, "Current Location : " + locationStr);
							// TODO send the GPS position to a server
							new UpdateGeoCoordinate().execute(latitude, longitude, 01d, 5001d);
						} catch (Exception e) {
						}
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, AppConstants.GPS_UPDATE_INTERVAL);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	private Location getLocation() {
		Log.i(TAG, "getLocation : inside");
		final Handler handler = new Handler();
		// double latitude, longitude;
		handler.post(new Runnable() {

			@Override
			public void run() {
				Log.w(TAG, "getLocation : thread is run");

				try {
					LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

					// getting GPS status
					boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

					// getting network status
					boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

					if (!isGPSEnabled && !isNetworkEnabled) {
						// no network provider is enabled
						Log.e(TAG, "No network provider is enabled");
					} else {
						// boolean canGetLocation = true;
						// First get location from Network Provider
						if (isNetworkEnabled) {
							locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
									AppConstants.MIN_TIME_BW_UPDATE_REQUEST,
									AppConstants.MIN_DISTANCE_CHANGE_FOR_UPDATES, GPSSenderService.this);
							Log.d(TAG, "Getting location from Network provider");
							if (locationManager != null) {
								location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								if (location != null) {
									Log.e(TAG, "NETWORK_PROVIDER : Got location is null!");
									// latitude = location.getLatitude();
									// longitude = location.getLongitude();
								}
							}
						}
						// if GPS Enabled get lat/long using GPS Services
						if (isGPSEnabled) {
							if (location == null) {
								locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
										AppConstants.MIN_TIME_BW_UPDATE_REQUEST,
										AppConstants.MIN_DISTANCE_CHANGE_FOR_UPDATES, GPSSenderService.this);
								Log.d(TAG, "Getting location from GPS provider");
								if (locationManager != null) {
									location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
									if (location != null) {
										Log.e(TAG, "GPS_PROVIDER : Got location is null!");
										// latitude = location.getLatitude();
										// longitude = location.getLongitude();
									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Log.d(TAG, "Returning location as: latti=" + location.getLatitude() + ", longi=" + location.getLongitude());
		return location;
	}

	@Override
	public void onLocationChanged(Location location) {
		locationStr = location.getLatitude() + ", " + location.getLongitude();
		Log.d(TAG, "onLocationChanged : " + locationStr);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(TAG, "onStatusChanged : " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "onProviderEnabled : " + provider);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "onProviderDisabled : " + provider);
	}

	private static class UpdateGeoCoordinate extends AsyncTask<Double, Void, JSONObject> {
		private JsonParser jsonParser;
		private static boolean isRunning = false;
		private static String TAG_TASK = "UpdateGeoCoordinate";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				if (jsonParser == null)
					jsonParser = new JsonParser();
				// if (pDialog == null)
				// pDialog = new ProgressDialog(tContext);
				// if (!pDialog.isShowing()) {
				// pDialog.setMessage("Deciding the issue ...");
				// pDialog.setCancelable(false);
				// pDialog.setIndeterminate(true);
				// pDialog.show();
				// }
			} catch (Exception e) {
				Log.e(TAG_TASK, "Exception inside DecideNotification:onPreExecute :: \n" + e.getMessage());
			}
		}

		@Override
		protected JSONObject doInBackground(Double... params) {
			if (isRunning == true) {
				Log.e(TAG_TASK, "");
				return null;
			}
			double latti = params[0];
			double longi = params[1];
			double id = params[2];
			double deviceId = params[3];

			JSONObject jObj = new JSONObject();
			try {
				jObj.put("id", id);
				jObj.put("latitude", latti);
				jObj.put("longitude", longi);
				jObj.put("AndroidDeviceID", deviceId);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// TODO Access-token
			ServerResponse response = jsonParser.retrieveServerData(AppConstants.REQUEST_TYPE_POST,
					AppConstants.URL_API + "geolocation", null, jObj.toString(), "");
			if (response.getStatus() == 200) {
				Log.d(">>>><<<<", "success in retrieving notifications.");
				JSONObject responseObj = response.getjObj();
				return responseObj;
			} else
				return null;
		}

		@Override
		protected void onPostExecute(JSONObject responseObj) {
			super.onPostExecute(responseObj);
			if (responseObj != null) {
				try {
					Log.i(TAG_TASK, "Response is ok: " + responseObj.toString());
					// TODO handle response
				} catch (Exception e) {
					Log.w(TAG_TASK, "Malformed data received!");
					e.printStackTrace();
				}
			} else
				Log.e(TAG_TASK, "Response is null!");
			// if (pDialog.isShowing())
			// pDialog.dismiss();
		}
	}

}
