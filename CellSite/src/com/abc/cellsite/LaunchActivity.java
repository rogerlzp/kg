package com.abc.cellsite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.abc.cellsite.model.User;
import com.abc.utility.CellSiteApplication;

public class LaunchActivity extends BaseActivity {

	public final String TAG = LaunchActivity.class.getSimpleName();

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Thread waitThread = new Thread() {
			@Override
			public void run() {
				try {
					app = (CellSiteApplication) LaunchActivity.this
							.getApplication();
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				;

				runOnUiThread(new Runnable() {
					// @Override
					public void run() {

						if (app.getUser().getId() == User.INVALID_ID) {
							enterLoginActivity();
						} else {
							enterMainActivity();
						}
					}
				});
			}
		};
		waitThread.start();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
	}

	private void enterMainActivity() {

		Intent mainAct = new Intent();
		mainAct.setClass(this, MainActivity.class);
		mainAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(mainAct);
		// isGifstopped = true;

	}

	private void enterLoginActivity() {
		// if (!isGifstopped) {
		Intent loginIntent = new Intent();
		loginIntent.setClass(this, LoginActivity.class);
		startActivity(loginIntent);
		// isGifstopped = true;
	}

}
