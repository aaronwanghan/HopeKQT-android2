package com.hope.kqt.android;

import java.io.File;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;
import org.json.JSONObject;

import com.hope.kqt.android.dao.SystemValueDao;
import com.hope.kqt.android.dao.impl.SystemValueFileDaoImpl;
import com.hope.kqt.android.http.HttpService;
import com.hope.kqt.android.http.impl.HttpServiceImpl;
import com.hope.kqt.android.util.HelpMessagesHelper;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.entity.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KqtActivity extends FragmentActivity 
{
	public static final int HTTP_TIME_OUT_MESSAGE = 0x01;
	public static final int HTTP_HOST_CONNECT_MESSAGE = 0x02;
	public static final int SYSTEM_ERROR_MESSAGE = 0x03;
	
	protected TextView messages;
	protected User user;
	
	protected LinearLayout iconsLayout;
	
	protected final HttpService httpService = new HttpServiceImpl();
	protected SystemValueDao systemValueDao = null;
	
	protected File cache;
	
	protected Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case HTTP_TIME_OUT_MESSAGE:
				getMessage("请求超时!");
				break;
			case HTTP_HOST_CONNECT_MESSAGE:
				getMessage("网络连接失败或服务器无相应.");
				break;
			case SYSTEM_ERROR_MESSAGE:
				getMessage("系统错误!");
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if(systemValueDao==null)
			systemValueDao = new SystemValueFileDaoImpl(this);
		
		this.cache = new File(Environment.getExternalStorageDirectory(),"kqt_cache");
		
		if(!this.cache.exists())
			this.cache.mkdirs();
		
		if(this.user==null)
		{
			String s = systemValueDao.load(Application.USER_SYSTEM_KEY);
			try {
				if(s!=null && !"".equals(s))
					this.user = User.valueOf(new JSONObject(new String(KQTUtils.parseHexStr2Byte(s))));
			} catch (JSONException e) {
				login();
			}
		}
		else
			login();
	}
	
	protected boolean isNetworkAvailable() 
	{  
	    Context context = getApplicationContext();  
	    ConnectivityManager connectivity = (ConnectivityManager) context  
	            .getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if (connectivity != null) 
	    {  
	    	//获取所有网络连接信息  
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();  
	        if (info != null) 
	        {
	        	//逐一查找状态为已连接的网络  
	            for (int i = 0; i < info.length; i++)
	                if (info[i].getState() == NetworkInfo.State.CONNECTED)  
	                    return true; 
	        }  
	    }  
	    return false;  
	}

	protected void getMessage(String message)
	{
		if(this.messages!=null)
			this.messages.setText(message);
		else
			Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
	}
	
	@SuppressLint("NewApi")
	protected void getNotification(long time,String title,String message,int icon,int id)
	{
		NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		//Notification cation = new Notification(icon,title,time);
		Intent i = new Intent(this,MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification cation = new Notification.Builder(this.getBaseContext())
		.setWhen(time)
		.setContentTitle(title)
		.setContentText(message)
		.setSmallIcon(icon)
		.setContentIntent(contentIntent)
		.build();

		cation.ledARGB = Color.YELLOW;
		
		cation.ledOffMS = 500;
		cation.ledOnMS = 2000;
		cation.flags = cation.flags|Notification.FLAG_SHOW_LIGHTS;
		
		cation.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		//震动
		long [] pattern = {100,400,100,400};
		cation.vibrate = pattern;

		manager.notify(id, cation);
	}
	
	protected MediaPlayer ring() throws Exception
	{
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		MediaPlayer player = new MediaPlayer();
		player.setDataSource(this,uri);
		AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		
		if(audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)!=0)
		{
			player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
			player.setLooping(false);
			player.prepare();
			player.start();
		}
		
		return player;
	}
	
	protected final class ImgIntentAsyncTask extends AsyncTask<String,Integer,Uri>
	{
		private File cache;
		private Context context;
		private ProgressDialog progressBar;
		
		public ImgIntentAsyncTask(File cache, Context context) {
			super();
			this.cache = cache;
			this.context = context;
		}

		@Override
		protected Uri doInBackground(String... params) {
			try {
				return httpService.getImage(params[0], params[1], cache);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		
		
		@Override
		protected void onPreExecute() {
			this.progressBar = new ProgressDialog(this.context);
			this.progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.progressBar.setTitle("提示");
			this.progressBar.setMessage("读取文件中...");
			this.progressBar.setIndeterminate(false);
			this.progressBar.setCancelable(true);
			
			this.progressBar.show();
		}

		@Override
		protected void onPostExecute(Uri result) 
		{
			super.onPostExecute(result);
			if(result!=null)
			{
				if(this.progressBar!=null)
					this.progressBar.dismiss();
				
				Intent i = new Intent("android.intent.action.VIEW");
				i.addCategory("android.intent.category.DEFAULT");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				i.setDataAndType(result, "image/*");
				
				this.context.startActivity(i);
			}
		}
	}
	
	protected final class ImgAsyncTask extends AsyncTask<String,Integer,Uri>
	{
		private File cache;
		private ImageView imgView;
		
		public ImgAsyncTask(File cache,ImageView imgView) 
		{
			this.cache = cache;
			this.imgView = imgView;
		}

		@Override
		protected Uri doInBackground(String... params) 
		{
			try {
				return httpService.getImage(params[0], params[1], cache);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Uri result) 
		{
			super.onPostExecute(result);
			
			if(imgView!=null && result!=null)
				imgView.setImageURI(result);
		}
		
		
	}
	
/*	protected void setIcons()
	{
		if(user!=null && this.iconsLayout!=null)
		{
			int[] icons = {R.drawable.ya,R.drawable.yao_1,R.drawable.yao,R.drawable.zhong};
			for(int i:icons)
			{
				ImageView v = new ImageView(this);
				v.setImageResource(i);
				
				this.iconsLayout.addView(v);
			}
		}
	}
	*/
	
	protected void login()
	{
		Intent i = new Intent(this,LoginActivity.class);
		startActivity(i);
		finish();
	}
	
	protected void clearUser()
	{
		user = null;
		systemValueDao.set(Application.USER_SYSTEM_KEY, "");
		systemValueDao.set(Application.USER_TREATMENT_VERSION_SYSTEM_KEY, "-1");
		
		this.deleteDatabase(Application.SQL_DB);
	}
	
	protected void loginAgain()
	{
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("用户信息有变更需要重新登录!")
		.setPositiveButton("确认", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				login();
			}
		}).show();
	}
	
	protected void showException(Exception e)
	{
		if(e instanceof HttpHostConnectException)
			handler.sendEmptyMessage(HTTP_HOST_CONNECT_MESSAGE);
		else if (e instanceof ConnectionPoolTimeoutException || 
				e instanceof ConnectTimeoutException ||
				e instanceof SocketTimeoutException)
			handler.sendEmptyMessage(HTTP_TIME_OUT_MESSAGE);
		else
			handler.sendEmptyMessage(SYSTEM_ERROR_MESSAGE);
		
		e.printStackTrace();
	}
}
