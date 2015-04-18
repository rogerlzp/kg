package com.abc.huoyun;

import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.abc.huoyun.net.CellSiteHttpClient;
import com.abc.huoyun.utility.CellSiteApplication;
import com.abc.huoyun.utility.CellSiteConstants;

public class ChangeNameActivity extends BaseActivity {

	EditText usernameEt;
	UpdateUserTask mUpdateUserTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_name);

		usernameEt = (EditText) findViewById(R.id.update_username_et);
		if (CellSiteApplication.getUser().getName() != null) {
			usernameEt.setText(CellSiteApplication.getUser().getName());

		} else {
			usernameEt.setText(R.string.updateNameHint);
		}
	}

	// 保存用户名
	// TODO: 如果用户名没有更新，则这个处于disabled状态

	public void saveUsername(View v) {

		mUpdateUserTask = new UpdateUserTask();
		mUpdateUserTask.execute("" + CellSiteApplication.getUser().getId(),
				usernameEt.getText().toString().trim());
	
	}

	private class UpdateUserTask extends AsyncTask<String, String, Integer> {
		@Override
		public Integer doInBackground(String... params) {
			return updateUsername(params[0], params[1]);
		}

		@Override
		public void onPostExecute(Integer result) {
			if (this.isCancelled()) {
				return;
			}
			Integer resCode = result;// Integer.parseInt(result);
			if (resCode == CellSiteConstants.RESULT_SUC) {
				// TODO: set for different user
				finish();

			}
		}

		protected Integer updateUsername(String _userId, String _name) {

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair(
					CellSiteConstants.USER_ID, _userId));
			postParameters.add(new BasicNameValuePair(CellSiteConstants.NAME,
					_name));

			JSONObject response = null;
			try {
				response = CellSiteHttpClient.executeHttpPost(
						CellSiteConstants.UPDATE_USERNAME_URL, postParameters);

				int resultCode = Integer.parseInt(response.get(
						CellSiteConstants.RESULT_CODE).toString());
				if (resultCode == CellSiteConstants.RESULT_SUC) {
					CellSiteApplication.getUser().setName(_name);

					Editor sharedUser = getSharedPreferences(
							CellSiteConstants.CELLSITE_CONFIG, MODE_PRIVATE)
							.edit();

					sharedUser.putString(CellSiteConstants.NAME, _name);
					sharedUser.commit();

				}
				return resultCode;

			} catch (UnknownHostException e) {
				return CellSiteConstants.UNKNOWN_HOST_ERROR;
			} catch (Exception e) {
				// TODO
			}
			return CellSiteConstants.UNKNOWN_ERROR;
		}
	}

}
