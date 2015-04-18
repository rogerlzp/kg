package com.abc.huoyun.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalUser {

	public static final String PREFERENCE_NAME = "local_user";
	private static SharedPreferences mSharedPreferences;
	private static LocalUser mPreferenceUtils;
	private static SharedPreferences.Editor editor;
	
	
	private LocalUser(Context ctx) {
		mSharedPreferences = ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		
	}
	
	public static LocalUser getInstance(Context ctx) {
		if (mPreferenceUtils == null) {
			mPreferenceUtils = new LocalUser(ctx);
		}
		editor = mSharedPreferences.edit();
		return mPreferenceUtils;
	}
	
	public void setUser(String _name, String _value) {
		editor.putString(_name, _value);
		editor.commit();
	}
	
	public String getUser(String _name) {
		return mSharedPreferences.getString(_name, "");
		
	}
	
}
