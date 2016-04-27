package com.hope.kqt.android;

import java.util.ArrayList;
import java.util.List;

import com.hope.kqt.android.service.ServiceTask;
import com.hope.kqt.android.util.Date;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.entity.UserTask;
import com.hope.kqt.entity.UserTaskMenus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TaskMessageActivity extends Activity 
{
	private final static int[] IMGS = {R.drawable.type00,R.drawable.type01,R.drawable.type02,
		R.drawable.type03,R.drawable.type04,R.drawable.type05,R.drawable.type05,R.drawable.type05};
	public final static String TASKS_KEY = "tasks";
	public final static String TASKMENUS_KEY = "task_menus";
	
	private ListView listView;
	private Button button;

	private List<UserTask> tasks = new ArrayList<UserTask>();
	private UserTaskMenus taskMenus;
	
	private MediaPlayer mplayer;
	
	private Handler handler = new Handler()
	{
		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case 0:
				button.setText("关闭");
				button.setOnClickListener(new View.OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						finish();
					}
				});
				break;
			}
			super.dispatchMessage(msg);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.task_message);
		
		this.button = (Button) this.findViewById(R.id.task_message_close_button);
		this.listView = (ListView) this.findViewById(R.id.task_message_list_view);
		
		this.button.setText("确认");
		this.button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				stop();
				handler.sendEmptyMessage(0);
			}
		});
		
		this.init();
		
		ServiceTask st = (ServiceTask) this.getIntent().getSerializableExtra(TASKS_KEY);
		this.taskMenus = (UserTaskMenus) this.getIntent().getSerializableExtra(TASKMENUS_KEY);
		
		if(st!=null && st.getTasks()!=null &&!st.getTasks().isEmpty())
		{
			this.tasks.addAll(st.getTasks());
			this.listView.setAdapter(new TaskMessageAdapter());
			if(this.taskMenus!=null)
			{
				this.listView.setOnItemClickListener(new OnItemClickListener()
				{

					@Override
					public void onItemClick(AdapterView<?> adapter, View view,
							int position, long id) 
					{
						UserTask task = tasks.get(position);
						Intent i = null;
						switch(task.getType())
						{
						case 5:
							i = new Intent(view.getContext(),UserLogDatasActivity.class);
							i.putExtra("taskMeuns", taskMenus);
							i.putExtra("current_tab", 0);
							KQTUtils.setStartAndEnd(i,task.getStartTime());
							view.getContext().startActivity(i);
							break;
							
						case 6:
							i = new Intent(view.getContext(),UserLogDatasActivity.class);
							i.putExtra("taskMeuns", taskMenus);
							i.putExtra("current_tab", 1);
							view.getContext().startActivity(i);
							break;
							
						case 7:
							i = new Intent(view.getContext(),UserLogDatasActivity.class);
							i.putExtra("taskMeuns", taskMenus);
							i.putExtra("current_tab", 3);
							view.getContext().startActivity(i);
							break;
							
						}
					}
				});
			}
		}
		
		onAttachedToWindow();
		this.play();
	}
	
	
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		this.mplayer.release();
	}

	private void init()
	{
		this.mplayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
		this.mplayer.setLooping(true);
	}
	
	private void play()
	{
		this.mplayer.start();
	}
	
	private void stop()
	{
		if(this.mplayer.isPlaying())
			this.mplayer.stop();
	}
	
	private class TaskMessageAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			return tasks.size();
		}

		@Override
		public Object getItem(int position) {
			return tasks.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView==null)
				convertView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.tasks_item, parent, false);
			
			UserTask task = tasks.get(position);
			
			TextView name = (TextView) convertView.findViewById(R.id.task_name);
			TextView content = (TextView) convertView.findViewById(R.id.task_content);
			TextView time = (TextView) convertView.findViewById(R.id.task_time);
			ImageView image = (ImageView) convertView.findViewById(R.id.task_image);
			
			name.setText(task.getTitle());
			content.setText(task.getContent());
			setTimeTextView(time,task.getStartTime());
			
			if(task.getType()<=7)
				image.setImageResource(IMGS[task.getType()]);
			else
				image.setImageResource(IMGS[0]);
			
			return convertView;
		}
		
		private void setTimeTextView(TextView v,long time)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(Date.valueOf(time).toTime());
			v.setText(sb.toString());
			
			v.setBackgroundColor(Color.parseColor("#66CC33"));
		}

	}
	
	public void onAttachedToWindow() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }
}
