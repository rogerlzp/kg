package com.abc.huoyun;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.abc.huoyun.utility.CellSiteApplication;

/**
 * 
 * @author zhengpli
 *
 */


public class BaseActivity extends Activity {
	
    final static String BASETAG = "BaseActivity";
	public CellSiteApplication app;
	protected Resources res;
	long waitTime = 2000;
	long touchTime = 0;
	
	String deviceToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		res = getResources();
		app = (CellSiteApplication) getApplication();

	}
	
	public void onBackButton(View v)
	{
		finish();
	//	onBackPressed();
	}
	
	
	
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime-touchTime ) >= waitTime){
			Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			finish();
		}
	}

}
