package com.abc.utility;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.abc.cellsite.model.User;

public class CellSiteApplication extends Application {

	final static String TAG = CellSiteApplication.class.getSimpleName();

	int mNetworkUnknowHostTimes = 0;
	public NETWORK_STATUS mNetworkStatus = NETWORK_STATUS.NORMAL;

	public enum NETWORK_STATUS {
		NORMAL, OFFLINE, OTHERS
	}

	public enum NETWORK_UPDATE_MODE {
		WIFI_ONLY, ALL_UPDATE
	}

	public boolean isWillShowFirstRecomm = true;
	public boolean isShowHelp = true;
	public boolean mIsSdCardAvaible = false;
	CacheData cacheData;

	public String userMode;
	public String CacheFilePath;
	String PicsDirPath;

	protected String username = null;
	protected String password = null;
	protected User user = null;

	File cacheFileDir; // Download the file, dir

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// must be initialization first!!!

		checkSDCardAvaible();
		initCacheFileDir();
		
		initUser();

		// initial the unnecessarily-started component
		// startService(new Intent(this, InitService.class));
	}

	public void initUser() {
		SharedPreferences sp = getSharedPreferences(
				CellSiteConstants.CELLSITE_CONFIG, MODE_PRIVATE);
		String username = sp.getString(CellSiteConstants.USER_NAME, null);
		String userId = sp.getString(CellSiteConstants.USER_ID, null);

		user = new User();
		if (username != null) 
		{
				user.setUsername(username);
				if (userId != null) {
					user.setId(Long.parseLong(sp.getString(
							CellSiteConstants.USER_ID, null)));
				}

			//	if (portrait != null) {
				//	user.setProfileImageUrl(sp.getString(
					//		CellSiteConstants.USER_PORTARIT, null));
		///		}
			//	setUserMode(CellSiteConstants.NORMAL_USER_MODE);
		

		} else 
		{ // visitor mode
			user.setId(User.INVALID_ID);
		//	setUserMode(CellSiteConstants.VISITOR_MODE);
		}

		return ;
	}

	boolean checkSDCardAvaible() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			mIsSdCardAvaible = true;
		} else {
			mIsSdCardAvaible = false;
		}
		return mIsSdCardAvaible;

	}

	void initConfig() // TODO: change later
	{
		/*
		 * SharedPreferences sp = getSharedPreferences(CellSiteConstants.CONFIG,
		 * MODE_PRIVATE); Boolean bFirstLogin =
		 * sp.getBoolean(CellSiteConstants.FIRST_LOGIN_FLAG, true);
		 * 
		 * if(bFirstLogin) { Editor sharedUser =
		 * getSharedPreferences(CellSiteConstants.CONFIG, MODE_PRIVATE).edit();
		 * sharedUser.putBoolean(CellSiteConstants.FIRST_LOGIN_FLAG, true);
		 * sharedUser.putLong(CellSiteConstants.NOTIFY_INTERNAL,
		 * CheckNotificationService.DELAY); sharedUser.commit(); }
		 */

	}

	private void initCacheFileDir() {
		Log.d(TAG, Environment.getExternalStorageDirectory().toString());
		CacheFilePath = Environment.getExternalStorageDirectory().getPath()
				+ "/.cellsite/";
		Log.d(TAG, CacheFilePath);

		cacheFileDir = new File(CacheFilePath);
		if (!cacheFileDir.exists()) {
			cacheFileDir.mkdirs();
		}

	}

	public CacheData getCacheData() {
		if (cacheData == null) {
			cacheData = new CacheData(this);
		}
		return cacheData;
	}

	public void attachUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public NETWORK_STATUS ConnectNetwork() {
		ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {

			return NETWORK_STATUS.NORMAL;
		} else {
			return NETWORK_STATUS.OFFLINE;
		}
	}

	public void setNetworkStatus(NETWORK_STATUS aStatus) {
		mNetworkStatus = aStatus;
	}

}
