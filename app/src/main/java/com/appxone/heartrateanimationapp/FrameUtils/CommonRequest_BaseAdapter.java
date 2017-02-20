package com.appxone.heartrateanimationapp.FrameUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class CommonRequest_BaseAdapter extends
		AsyncTask<String, String, String> {

	private String url;
	private String key;

	private String progressTitle;

	private ProgressDialog dialog;

	private MyBaseAdapter baseAdapterObject;
	private Activity activity;

	public CommonRequest_BaseAdapter(Activity act, MyBaseAdapter adapter) {
		super();
		this.activity = act;
		this.baseAdapterObject = adapter;
	}

	public void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
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

//		setTimeOuts();

		Log.e("do In Background url ", _url);

		DefaultHttpClient client = new DefaultHttpClient();
		String responseString = null;
		try {
			HttpGet get = new HttpGet(_url);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("CommonRequest ClientProtocolException => ", e.getMessage());
			return ("error");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("CommonRequest IOException => ", e.getMessage());
			return ("error");
		} catch (Exception e) {
			Log.e("Exception => ", e.getMessage());
			e.printStackTrace();
			return ("error1");
		}
		return responseString;
	}

//	private void setTimeOuts() {
//		// TODO Auto-generated method stub
//		HttpParams httpParameters = new BasicHttpParams();
//
//		int timeoutConnection = WebServices.TIMEOUT_CONNECTION;
//
//		HttpConnectionParams.setConnectionTimeout(httpParameters,
//                timeoutConnection);
//
//		int timeoutSocket = WebServices.TIMEOUT_SOCKET;
//		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//	}

	@Override
	protected void onPreExecute() {
		if (StringUtils.isNetworkConnected(activity)) {
			if (null != baseAdapterObject) {
				if (null != progressTitle) {
					dialog = new ProgressDialog(activity);
					String title = progressTitle;

					if (title.trim().length() < 1) {
						title = "Please wait for few seconds...";
					}

					dialog.setMessage(title);
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				}
			}
		} else {
			Toast.makeText(
                    activity,
                    "No Internet connection found, please connect to Internet and try again.",
                    Toast.LENGTH_LONG).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					activity.finish();
				}
			}, 150);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		closeDialog();

		Log.e("Result from Webservice", result);
		if (result.contentEquals("error"))
			Toast.makeText(activity, "No Internet Connection",
                    Toast.LENGTH_LONG).show();
		else if (result.contentEquals("error1")) {

		} else {
			if (!isCancelled()) {
				if (null != activity && null != result) {
					baseAdapterObject.onTaskComplete(result, key);
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

	public void setActivity(MyBaseAdapter activity) {
		this.baseAdapterObject = activity;
	}

}
