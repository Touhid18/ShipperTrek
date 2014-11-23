package com.touhiDroid.backgroundgpsgetter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.touhiDroid.backgroundgpsgetter.R;
import com.touhiDroid.backgroundgpsgetter.service.GPSSenderService;
import com.touhiDroid.backgroundgpsgetter.utils.AppConstants;

public class MainActivity extends Activity {

	private GCMController aController;
	// Asyntask
	private AsyncTask<Void, Void, Void> mRegisterTask;

	private Button btnRegister;
	private EditText etName, etEmail;
	private TextView tvMessage;

	public static String name, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		etName = (EditText) findViewById(R.id.etName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		tvMessage = (TextView) findViewById(R.id.tvGPS);

		this.startService(new Intent(this, GPSSenderService.class));

		initiateGCMRegistrationProcess();
	}

	private void initiateGCMRegistrationProcess() {
		// Get Global Controller Class object (see application tag in
		// AndroidManifest.xml)
		aController = (GCMController) getApplicationContext();

		// Check if Internet Connection present
		if (!aController.isConnectingToInternet()) {

			// Internet Connection is not present
			aController.showAlertDialog(MainActivity.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);

			// stop executing code by return
			return;
		}

		// Check if GCM configuration is set
		if (AppConstants.URL_API == null || AppConstants.GOOGLE_SENDER_ID == null || AppConstants.URL_API.length() == 0
				|| AppConstants.GOOGLE_SENDER_ID.length() == 0) {

			// GCM sernder id / server url is missing
			aController.showAlertDialog(MainActivity.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);

			// stop executing code by return
			return;
		}

		btnRegister = (Button) findViewById(R.id.btnRegister);

		// Click event on Register button
		btnRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Check if Internet present
				if (!aController.isConnectingToInternet()) {

					// Internet Connection is not present
					aController.showAlertDialog(MainActivity.this, "Internet Connection Error",
							"Please connect to Internet connection", false);
					// stop executing code by return
					return;
				}
				// Get data from EditText
				name = etName.getText().toString();
				email = etEmail.getText().toString();

				// Check if user filled the form
				if (name.trim().length() > 0 && email.trim().length() > 0) {
					Log.d(AppConstants.TAG, "Registering user: name=" + name + ", email=" + email);
					registerUser();
				} else {
					// user doen't filled that data
					aController.showAlertDialog(MainActivity.this, "Invalid Info Input!",
							"Please enter your details correctly!", false);
				}
			}
		});
	}

	protected void registerUser() {
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest permissions was properly set
		GCMRegistrar.checkManifest(this);
		// Register custom Broadcast receiver to show messages on activity
		registerReceiver(mHandleMessageReceiver, new IntentFilter(AppConstants.DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		Log.d(AppConstants.TAG, "Getting the registration id ...");
		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.e(AppConstants.TAG, "Got the registration id: " + regId);

		// Check if regid already presents
		if (regId.equals("")) {

			// Register with GCM
			Log.e(AppConstants.TAG, "Doing a new registration for empty regId: " + regId);
			GCMRegistrar.register(this, AppConstants.GOOGLE_SENDER_ID);

		} else {

			// Device is already registered on GCM Server
			Log.e(AppConstants.TAG, "Device already registered with regId: " + regId);
			if (GCMRegistrar.isRegisteredOnServer(this)) {

				// Skips registration.
				Log.e(AppConstants.TAG, "Device already registered also in the server with regId: " + regId);
				Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();

			} else {

				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.

				Log.e(AppConstants.TAG, "Device registered, but not in the server with regId: " + regId);
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						// Register on our server
						// On server creates a new user
						Log.e(AppConstants.TAG, "mRegisterTask : doInBackground : registering: name=" + name + ", email"
								+ email);
						// TODO ID & device ID to set
						aController.register(context, 1, email, regId);

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};

				// execute AsyncTask
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	// Create a broadcast receiver to get message and show on screen
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(AppConstants.TAG, "mHandleMessageReceiver : msg received.");

			String newMessage = intent.getExtras().getString(AppConstants.EXTRA_MESSAGE);
			Log.d(AppConstants.TAG, "mHandleMessageReceiver : msg:" + newMessage);

			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());

			// Display message on the screen
			tvMessage.append(newMessage + "\n");

			Toast.makeText(getApplicationContext(), "Got Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			aController.releaseWakeLock();
		}
	};

	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);

			// Clear internal resources.
			GCMRegistrar.onDestroy(this);

		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	};

}