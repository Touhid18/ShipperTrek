/**
 * 
 */
package com.touhiDroid.backgroundgpsgetter;

/**
 * @author Touhid
 * 
 */
public class Constants {

	public static final String URL_PARENT = "http://pulse.dbaservices.com.au/";

	/** HTTP Request keys */
	public static final int REQUEST_TYPE_GET = 1;
	public static final int REQUEST_TYPE_POST = 2;
	public static final int REQUEST_TYPE_PUT = 3;
	public static final int REQUEST_TYPE_DELETE = 4;

	/** Notification type-constants */
	public static final String NOTIF_TYPE_KEY = "com.dbaservicesptyltd.NOTIF_TYPE";
	public static final int NOTIF_TYPE_UNASSIGNED = 1;
	public static final int NOTIF_TYPE_ACTIONED = 2;
	public static final int NOTIF_TYPE_IGNORED = 3;
	public static final int NOTIF_TYPE_RESOLVED = 4;

	/** Notification severity-constants */
	public static final int NOTIF_SEVERITY_ALERT = 1;
	public static final int NOTIF_SEVERITY_WARNING = 3;
	public static final int NOTIF_SEVERITY_SIMPLE = 5;
	public static final int NOTIF_SEVERITY_RESOLVED = 40;

	/** Preference keys */
	public static final String ACCESS_TOKEN = "com.dbaservicesptyltd.dbaservices.ACCESS_TOKEN";
	public static final String USER_ID = "com.dbaservicesptyltd.dbaservices.UID";
	public static final String USER_FIRST_NAME = "com.dbaservicesptyltd.dbaservices.UFNAME";
	public static final String USER_LAST_NAME = "com.dbaservicesptyltd.dbaservices.ULNAME";
	public static final String USER_EMAIL = "com.dbaservicesptyltd.dbaservices.UEMAIL";

	/** Intent extra constant keys */
	public static final String U_ID = "com.dbaservicesptyltd.dbaservices.U_ID";
	public static final String U_NAME = "com.dbaservicesptyltd.dbaservices.UNAME";
	public static final String U_ACTIVE_COUNT = "com.dbaservicesptyltd.dbaservices.UACTIVE";
	public static final String U_PENDING_COUNT = "com.dbaservicesptyltd.dbaservices.UPENDING";
	public static final String U_RESOLVED_COUNT = "com.dbaservicesptyltd.dbaservices.URESOLVED";
	public static final String U_IS_ONLINE = "com.dbaservicesptyltd.dbaservices.UONLINE";

}
