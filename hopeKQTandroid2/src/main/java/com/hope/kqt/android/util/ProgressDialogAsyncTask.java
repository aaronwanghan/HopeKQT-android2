package com.hope.kqt.android.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ProgressDialogAsyncTask extends
		AsyncTask<String, Void, Void> 
{
	private Context context;
	private ProgressDialog progressBar;
	private Runnable runnable;
	private String title;
	private String message;

	public ProgressDialogAsyncTask(Context context, Runnable runnable,
			String title, String message) {
		super();
		this.context = context;
		this.runnable = runnable;
		this.title = title;
		this.message = message;
	}

	@Override
	protected Void doInBackground(String... params) 
	{
		this.runnable.run();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if(this.progressBar!=null)
			this.progressBar.dismiss();
	}

	@Override
	protected void onPreExecute() 
	{
		this.progressBar = new ProgressDialog(this.context);
		this.progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.progressBar.setTitle(title);
		this.progressBar.setMessage(message);
		this.progressBar.setIndeterminate(false);
		this.progressBar.setCancelable(true);
		
		this.progressBar.show();
	}

}
