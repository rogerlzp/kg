package com.abc.cellsite;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import com.abc.utility.CellSiteApplication;

/**
 * BaseActivity ��Ҫ���������˹���
 * �������Ҫ�Ļ�������ɾ����ֱ�Ӽ̳�Activity
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
