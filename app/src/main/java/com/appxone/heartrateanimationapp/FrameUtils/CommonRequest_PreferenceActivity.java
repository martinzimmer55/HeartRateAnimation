package com.appxone.heartrateanimationapp.FrameUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class CommonRequest_PreferenceActivity extends
		AsyncTask<String, String, String> {

	private String url;
	private String key;

	private String progressTitle;

	private ProgressDialog dialog;

	private MyPreferenceActivity activity;
	private Activity context;

	public CommonRequest_PreferenceActivity(Context context) {
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
	protected String doInBackground(String... arg0) {
		String _url = url;

		Log.e("do In Background url ", _url);

		DefaultHttpClient client = new DefaultHttpClient();
		String responseString = null;
		try {
			HttpGet get = new HttpGet(_url);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			Log.e("CommonRequest ClientProtocolException => ", e.getMessage());
			return ("error");
			// Toast.makeText(context,"Please make sure your interent is working before proceeding",Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Log.e("CommonRequest IOException => ", e.getMessage());
			// Toast.makeText(context,"Please make sure your interent is working before proceeding",Toast.LENGTH_LONG).show();
			return ("error");
		}
		return responseString;

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
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onPostExecute(String result) {
		closeDialog();

		Log.e("Result from Webservice", result);
		if (result.contentEquals("error"))
			Toast.makeText(context, "No Internet Access", Toast.LENGTH_LONG)
					.show();

		else {
			if (!isCancelled()) {
				if (null != activity && null != result) {
					activity.onTaskComplete(result, key);

				}
			}
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setProgressTitle(String progressTitle) {
		this.progressTitle = progressTitle;
	}

	public void setActivity(MyPreferenceActivity activity) {
		this.activity = activity;
	}

}
