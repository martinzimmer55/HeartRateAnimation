package com.appxone.heartrateanimationapp.FrameUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommonRequest_Payload extends AsyncTask<String[], String, String> {

	private String key;

	private String progressTitle;

	private ProgressDialog dialog;

	private MyActivity activity;
	private Activity context;

	public CommonRequest_Payload(Context context) {
		super();
		this.context = (Activity) context;

	}

	public void closeDialog() {
		if (dialog != null) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		closeDialog();
	}

	@Override
	protected String doInBackground(String[]... params) {
		// TODO Auto-generated method stub

		String line;
		StringBuffer jsonString = new StringBuffer();
		try {

			URL url = new URL(params[0][0]);
			String payload = params[0][1];

			Log.e("Payload Async", "Url => " + params[0][0]);
			Log.e("Payload Async", "Payload => " + params[0][1]);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type",
					"application/json; charset=UTF-8");

			// Writing the payload to the Connection
			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8");
			writer.write(payload);
			writer.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}

			Log.e("Payload Async", "" + jsonString.toString());
			br.close();
			connection.disconnect();
		} catch (Exception e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
			return "error";
		}
		return jsonString.toString();
	}

	@Override
	protected void onPreExecute() {
		if (null != activity) {
			if (null != progressTitle) {
				dialog = new ProgressDialog(activity);
				String title = progressTitle;
				if (title.trim().length() < 1) {
					title = "Please wait for few seconds...";
				}
				dialog.setMessage(title);
				dialog.setCanceledOnTouchOutside(false);
				try {
					dialog.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onPostExecute(String result) {
		closeDialog();

		Log.e("Result from Webservice", result);
		if (result.contentEquals("error")) {
//			Toast.makeText(context, "No Internet Access", Toast.LENGTH_LONG)
//					.show();
			if (null != activity && null != result) {
				activity.onTaskComplete(result, key);

			}
		} else {
			if (!isCancelled()) {
				if (null != activity && null != result) {
					activity.onTaskComplete(result, key);

				}
			}
		}
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setProgressTitle(String progressTitle) {
		this.progressTitle = progressTitle;
	}

	public void setActivity(MyActivity activity) {
		this.activity = activity;
	}

}
