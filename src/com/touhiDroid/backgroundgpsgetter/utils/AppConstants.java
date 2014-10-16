/**
 * 
 */
package com.touhiDroid.backgroundgpsgetter.utils;

/**
 * @author Touhid
 * 
 */
public class AppConstants {

	/** HTTP Request keys */
	public static final int REQUEST_TYPE_GET = 1;
	public static final int REQUEST_TYPE_POST = 2;
	public static final int REQUEST_TYPE_PUT = 3;
	public static final int REQUEST_TYPE_DELETE = 4;

	/** Timing Constants */
	public static final int GPS_UPDATE_INTERVAL = 10 * 1000; // 10 sec.
	public static final int MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meter
	public static final int MIN_TIME_BW_UPDATE_REQUEST = 5 * 1000; // 5 sec.

	/** GCM Constants */

	// CONSTANTS
	public static final String SERVER_URL = "http://touhidroid.site90.com/register.php";
	public static final String URL_BASE = "http://localhost/";

	// Place here your Google project id
	public static final String GOOGLE_SENDER_ID = "285512166450";

	/**
	 * Tag used on log messages.
	 */
	public static final String TAG = "GCM_Touhid";
	public static final String DISPLAY_MESSAGE_ACTION = "com.touhiDroid.gpssetter.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";

	/** Preference keys */

	/** Intent extra constant keys */

}
