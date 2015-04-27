package com.abc.huoyun.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.abc.huoyun.net.CellSiteHttpClient;
import com.abc.huoyun.utility.CellSiteConstants;

public class HorderChangeTask extends AsyncTask<Integer, String, String> {
	static final String TAG_FAIL = "FAIL";
	static final String TAG_SUCC = "SUCCESS";

	List<HashMap<String, String>> nTmpNewsData;

	@Override
	protected String doInBackground(Integer... params) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		//postParameters.add(new BasicNameValuePair(CellSiteConstants.USER_ID, ""
		//		+ app.getUser().getId()));
		
		postParameters.add(new BasicNameValuePair(CellSiteConstants.HORDER_ID,
				"" + params[0]));

		JSONObject response = null;
		try {
			response = CellSiteHttpClient.executeHttpPost(
					CellSiteConstants.GET_HUOZHU_UPDATE_HORDER_URL,
					postParameters);

			int resultCode = response.getInt(CellSiteConstants.RESULT_CODE);
			if (CellSiteConstants.RESULT_SUC == resultCode) {

				}
		} catch (Exception ex) {

		}
		return TAG_SUCC;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// update UI status
		
		
	}
}