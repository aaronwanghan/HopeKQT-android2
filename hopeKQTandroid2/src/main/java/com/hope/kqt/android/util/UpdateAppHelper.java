package com.hope.kqt.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import com.hope.kqt.android.http.HttpService;
import com.hope.kqt.entity.App;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class UpdateAppHelper 
{
	//private final static int UPDATE_BUTTON_ENABLED = 0x0;
	private final static int HAS_NEW_VERSION_APP = 0x1;
	private final static int IS_LAST_VERSION = 0x2;
	
	private final static int PBER_START = 0x03;
	private final static int PBER_ADD = 0x04;
	private final static int PBER_END = 0x05;
	
	private final static String FILE_NAME = "hope-kqt";
	
	private Context context;
	private String cachePath;
	private App app;
	private AppValues appValues;
	private HttpService httpService;
	private Handler superHandler;
	private boolean state = false;
	
	private boolean showLastVersionDialog = true;
	
	private ProgressBar progressBar;
	
	private Handler handler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			switch(msg.what)
			{
			case HAS_NEW_VERSION_APP:
				showDownloadDialog();
				break;
			case IS_LAST_VERSION:
				showLastVersionDialog();
				break;
			case PBER_START:
				if(progressBar!=null)
				{
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress(0);
					progressBar.setMax(msg.arg1);
				}
				break;
			case PBER_ADD:
				if(progressBar!=null)
				{
					progressBar.setProgress(progressBar.getProgress()+msg.arg1);
				}
				break;
			case PBER_END:
				if(progressBar!=null)
				{
					progressBar.setProgress(progressBar.getMax());
					progressBar.setVisibility(View.GONE);
				}
				break;
			}
			
			super.dispatchMessage(msg);
		}
		
	};
	
	public UpdateAppHelper(Context context,HttpService httpService,Handler superHandler,String cachePath) throws Exception
	{
		this.context = context;
		this.httpService = httpService;
		this.superHandler = superHandler;
		this.cachePath = cachePath;
		appValues = new AppValues(context.getPackageManager(),context.getPackageName());
	}
	
	public void setState(boolean state) {
		this.state = state;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public void setShowLastVersionDialog(boolean showLastVersionDialog) {
		this.showLastVersionDialog = showLastVersionDialog;
	}

	public int getVersion(){
		return this.appValues.getVersion();
	}
	
	public String getVersionString()
	{
		return this.appValues.getVersionString();
	}
	
	public void shop()
	{
		this.state = false;
	}
	
	public boolean isUpdating()
	{
		return state;
	}
	
	public void update()
	{
		if(this.isUpdating())
			return;
		
		ThreadPoolUtils.execute(new Runnable()
		{
			@Override
			public void run() 
			{
				try {
					app = httpService.findAppLast();
					if(app!=null)
					{
						if(app.getVersion()==appValues.getVersion())
						{
							if(showLastVersionDialog)
								handler.sendEmptyMessage(IS_LAST_VERSION);
						}
						else if(app.getVersion()>appValues.getVersion())
							handler.sendEmptyMessage(HAS_NEW_VERSION_APP);
					}
				} catch(HttpHostConnectException hce) {
					//sendEmptyMessage(KqtActivity.HTTP_HOST_CONNECT_MESSAGE);
				} catch (ConnectionPoolTimeoutException cte) {
					//sendEmptyMessage(KqtActivity.HTTP_TIME_OUT_MESSAGE);
				} catch (Exception e) {
					//sendEmptyMessage(KqtActivity.SYSTEM_ERROR_MESSAGE);
					e.printStackTrace();
				}
				
				//handler.sendEmptyMessage(UPDATE_BUTTON_ENABLED);
			}
		});
	}
	
	private void sendEmptyMessage(int what)
	{
		if(this.superHandler!=null)
			this.superHandler.sendEmptyMessage(what);
	}
	
	private void showLastVersionDialog()
	{
		new AlertDialog.Builder(this.context).setTitle("版本更新")
		.setMessage("已是最新版本!")
		.setPositiveButton("确认", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
			}
		}).show();
	}
	
	private void showDownloadDialog()
	{
		new AlertDialog.Builder(this.context).setTitle("版本更新")
		.setMessage(app.getVersionString()+" 已经发布!")
		.setPositiveButton("立即更新", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				downloadApp();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.show();
	}
	
	public void downloadApp()
	{
		state = true;
		ProgressDialogAsyncTask task = new ProgressDialogAsyncTask(this.context,
				new Runnable()
		{
			@Override
			public void run() 
			{
				try {
					int le = readFileValue(app.getUrl());
					Message msg;
					if(le>0)
					{
						msg = new Message();
						msg.what = PBER_START;
						msg.arg1 = le;
						handler.sendMessage(msg);
						
						File f = new File(cachePath,FILE_NAME+"_"+app.getVersionString()+".apk");
						
						InputStream is = WebUtils.getInputStreamByUrl(app.getUrl());
						FileOutputStream os = new FileOutputStream(f);
						byte[] buff = new byte[2048];
						int l = -1;
						
						while( state && (l=is.read(buff))!= -1)
						{
							
							msg = new Message();
							msg.what = PBER_ADD;
							msg.arg1 = l;
							handler.sendMessage(msg);
							os.write(buff,0,l);
						}
						
						is.close();
						os.close();

						handler.sendEmptyMessage(PBER_END);
						
						openApk(cachePath+"/"+FILE_NAME+"_"+app.getVersionString()+".apk");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				state = false;
			}
			
		},"提示","数据加载中...");
		task.execute();
	}
	
	private int readFileValue(String urlPath)
	{
		if(this.cachePath==null)
			return -1;

		int clength = -1;

		try {
			URL url = new URL(urlPath);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			clength = connection.getContentLength();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return clength;
	}
	
	public void openApk(String path)
	{
		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://"+path),
				"application/vnd.android.package-archive");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.context.startActivity(i);
	}
}
