package com.hope.kqt.android.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.hope.kqt.android.Application;
import com.hope.kqt.android.LauncherActivity;
import com.hope.kqt.android.TaskMessageActivity;
import com.hope.kqt.android.dao.DbHelper;
import com.hope.kqt.android.dao.SystemValueDao;
import com.hope.kqt.android.dao.UserTaskLogDao;
import com.hope.kqt.android.dao.impl.SystemValueFileDaoImpl;
import com.hope.kqt.android.dao.impl.UserTaskLogDaoImpl;
import com.hope.kqt.android.util.Date;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.android.util.UserTaskMenusDatasHelper;
import com.hope.kqt.android.widget.TasksWidget;
import com.hope.kqt.entity.User;
import com.hope.kqt.entity.UserTask;
import com.hope.kqt.entity.UserTaskLog;
import com.hope.kqt.entity.UserTaskMenus;
import com.hope.kqt.android.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
//import android.os.Vibrator;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

@SuppressLint("NewApi")
public class TasksService extends Service 
{
	public static final int[] icons = {R.drawable.type00,
		R.drawable.type01,R.drawable.type02,R.drawable.type03,R.drawable.type04,
		R.drawable.type05,R.drawable.type05,R.drawable.type05};

	private static UserTaskMenus taskMenus;
	private List<ServiceTask> serviceTasks;
	
	//private Vibrator vibrator;
	private SystemValueDao systemValueDao;
	private UserTaskLogDao logDao;
	
	private UserTaskMenusDatasHelper dataHelper;
	
	//private UserTaskMenusDao utmDao;
	private long time = 0l;
	private User user;
	
	//private SetTasksRunnable stRunnable;
	
	private Timer timer;
	private int taskNum;
	private int notificationNum = 0;
	
	private static final int[] FLAGS = {0,Notification.FLAG_SHOW_LIGHTS,Notification.FLAG_SHOW_LIGHTS};
	
	private int flags = 2;

	private Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {

            switch (msg.what){
                case 0x01:
                    UserTask ut = (UserTask)msg.obj;
                    updateWidget(ut);
                    break;
            }

            super.dispatchMessage(msg);
        }
    };

	@Override
	public IBinder onBind(Intent i) {
		Log.i("TasksService", "onBind");
		return null;
	}

	@Override
	public void onCreate()
	{
		Log.i("TasksService", "onCreate");
		//this.vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		
		if(systemValueDao==null)
			systemValueDao = new SystemValueFileDaoImpl(this);
		DbHelper dh = new DbHelper(this, Application.SQL_DB, null);
		this.logDao = new UserTaskLogDaoImpl(dh.getWritableDatabase());
		
		if(user==null)
		{
			String s = systemValueDao.load(Application.USER_SYSTEM_KEY);
			
			if(s == null || "".equals(s))
			{
				return;
			}
			
			Log.i("initUser s",(s==null? "null":s));
			try {
				Log.i("systemValueDao user", new String(KQTUtils.parseHexStr2Byte(s)));
				
				this.user = User.valueOf(new JSONObject(new String(KQTUtils.parseHexStr2Byte(s))));
				this.dataHelper = new UserTaskMenusDatasHelper(this,user);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			String selectedString = systemValueDao.load(Application.USER_ALERT_SETTINGS_KEY);
			if(selectedString!=null && !"".equals(selectedString))
			{
				Log.i("TasksService", "flags:"+selectedString);
				try {
					this.flags = Integer.parseInt(selectedString);
				} catch (NumberFormatException e) {
				}
				
			}
		}

		if(user!=null)
		{
			if(this.timer!=null)
				this.timer.cancel();
			else
				this.timer = new Timer();

			this.timer.schedule(new KqtTimerTask(), 1000);
		}
	}
	
	@Override
	public void onDestroy()
	{
		Log.i("TasksService", "onDestroy");
		
		if(this.timer!=null)
			this.timer.cancel();
		
		if(this.dataHelper!=null)
			this.dataHelper.close();
		
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Log.i("service", "onStartCommand");

		if(intent!=null)
		{
			int selected = intent.getIntExtra("alert_setting_selected", -1);
			if(selected>=0)
				this.flags = selected;
			
			Log.i("service", "flags:"+this.flags);
			
			Object o = intent.getSerializableExtra("user");
			if(o!=null)
			{
				User user = (User)o;
				if(this.user==null || this.user.getId()!=user.getId())
				{
					this.user = user;
					this.dataHelper = new UserTaskMenusDatasHelper(this,user);
					this.taskNum = 0;
					
					if(this.timer!=null)
						this.timer.cancel();
					else
						this.timer = new Timer();
					
					taskMenus = null;
					this.timer.schedule(new KqtTimerTask(), 1000);
					
					
				}
				
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void getNotification(long time,String title,String message,int icon,int id)
	{
		if(this.flags==0)
			return;
		
		NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		//Notification cation = new Notification(icon,title,time);
		//Intent i = new Intent(this,MainActivity.class);
		//i.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		//PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification cation = new Notification.Builder(this.getBaseContext())
		.setWhen(time)
		.setContentTitle(title)
		.setContentText(message)
		.setSmallIcon(icon)
		//.setContentIntent(contentIntent)
		.build();
		
		//i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		//cation.ledARGB = Color.YELLOW;
		
		//cation.ledOffMS = 500;
		//cation.ledOnMS = 2000;
		
		//cation.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		
		cation.flags = cation.flags|FLAGS[this.flags];
		
		cation.defaults = Notification.DEFAULT_ALL;
		
		//震动
		//long [] pattern = {100,400,100,400};
		//cation.vibrate = pattern;
		//cation.setLatestEventInfo(this.getApplicationContext(), title, message, contentIntent);
		manager.notify(id, cation);
	}
	
	/*private MediaPlayer ring() throws Exception
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
	}*/
	
	private UserTaskMenus getNow()
	{
		long now = KQTUtils.getNowTime();
		
		if(time==now)
			return null;
		
		this.dataHelper.updateDatas();
		UserTaskMenus utm = this.dataHelper.getNow();
		if(utm!=null)
		{
			Log.i("TaskService", "UserTaskMenus:"+utm.toString());
			this.time = now;
			return utm;
		}
		
		
		return null;
	}

	private void updateWidget(UserTask ut)
    {
		RemoteViews remoteViews = new RemoteViews(this.getPackageName(),R.layout.tasks_widget);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

		remoteViews.setImageViewResource(R.id.tw_img,icons[ut.getType()]);
		remoteViews.setTextViewText(R.id.tw_text,ut.getTitle());
		remoteViews.setTextViewText(R.id.tw_context_text,(ut.getContent() == null? "":ut.getContent()));
        remoteViews.setTextViewText(R.id.tw_time_text, Date.valueOf(ut.getStartTime()).toTime());

        int color = Color.parseColor(KQTUtils.taskTimeToColor(ut.getCallTime(),ut.getStartTime(),ut.getEndTime()));

		remoteViews.setInt(R.id.tw_text_layout,"setBackgroundColor",color);

        Intent intent = new Intent(this, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.tw_layout,pendingIntent);

		ComponentName componentName = new ComponentName(this,TasksWidget.class);
		appWidgetManager.updateAppWidget(componentName,remoteViews);
	}
	
	private void initServiceTasks()
	{
		if(taskMenus==null)
			return;
		
		List<UserTask> tasks = taskMenus.getTasks();
		
		if(tasks==null || tasks.isEmpty())
			return;
		
		this.serviceTasks = ServiceTask.userTasksToServiceTasks(tasks);
		taskNum = 0;
	}
	
	private class KqtTimerTask extends TimerTask
	{
		@Override
		public void run() 
		{
			//Log.i("KqtTimerTask", "start");
			
			if(taskMenus==null)
			{
				taskMenus = getNow();
				initServiceTasks();

                /*if(serviceTasks!=null && !serviceTasks.isEmpty())
                {
                    long now = System.currentTimeMillis();
                    for(int i=0;i<serviceTasks.size();i++){
                        ServiceTask task = serviceTasks.get(i);

                        if(task.getTime()<now && i!=serviceTasks.size()-1)
                            continue;

                        if(task!=null && task.getTasks()!=null){
                            Message msg = new Message();
                            msg.what = 0x01;
                            msg.obj = task.getTasks().get(0);

                            handler.sendMessage(msg);
                            break;
                        }
                    }
                }*/
			}
			
			this.action();
			timer.schedule(new KqtTimerTask(), 1000*5);
		}
		
		private void action()
		{
			if(serviceTasks==null || serviceTasks.isEmpty() || taskNum>serviceTasks.size()-1)
			{
				taskMenus=null;
				return;
			}
			
			ServiceTask task = serviceTasks.get(taskNum);
			long now = System.currentTimeMillis();
			Log.i("TaskRunnable", task.toString());
			
			if(now>task.getTime()+10*60*1000)
			{
				//Log.i("TaskRunnable 1", task.toString());
				taskNum++;
				action();
			}
			else if(now>=task.getTime() && now<=task.getTime()+10*60*1000)
			{	
				this.getMessage(task);
				taskNum++;
			}
		}
		
		private void getMessage(ServiceTask task)
		{
			ServiceTask st = new ServiceTask();
			List<UserTask> list = new ArrayList<UserTask>();
			st.setTasks(list);
			
			for(UserTask ut:task.getTasks())
			{
				Log.i("getNotification", ut.getTitle());
				
				if(logDao.find(ut.getId(), 0)!=null)
					continue;
				
				list.add(ut);
				getNotification(ut.getStartTime(), 
						ut.getTitle(),
						ut.getContent(),
						icons[ut.getType()],notificationNum++); //通知

				UserTaskLog log = new UserTaskLog();
				log.setTid(ut.getId());
				log.setType(0);
				logDao.add(log);
				
				Log.i("task service", ut.getId()+"");
				
				//long [] pattern = {100,400,100,400}; //震动频率
				//vibrator.vibrate(pattern,-1);	// -1 不重复
				//try {ring(); //声音
				//} catch (Exception e) {}
			}
			
			if(!list.isEmpty())
			{
                Message msg = new Message();
                msg.what = 0x01;
                msg.obj = list.get(list.size()-1);
                handler.sendMessage(msg);

                if(flags==2){
                    Intent i = new Intent(getApplicationContext(),TaskMessageActivity.class);
                    i.putExtra(TaskMessageActivity.TASKS_KEY, st);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                }

			}

		}
	}
}
