package com.abc.cellsite;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import com.abc.utility.CellSiteApplication;

/**
 * BaseActivity 主要用来做回退管理，
 * 如果不需要的话，后面删除，直接继承Activity
 * 
 * @author zhengpli
 *
 */

public class BaseActivity extends Activity {
	
    final static String BASETAG = "BaseActivity";
	public CellSiteApplication app;
	protected Resources res;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		res = getResources();
		app = (CellSiteApplication) getApplication();

	}

}
