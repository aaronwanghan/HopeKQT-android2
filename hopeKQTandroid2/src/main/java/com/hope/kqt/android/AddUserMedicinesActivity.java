package com.hope.kqt.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.entity.UserMedicineLog;
import com.hope.kqt.entity.UserMedicineLogRecord;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class AddUserMedicinesActivity extends KqtActivity 
{
	public static final int RESULT_CODE = 0x01;
	
	private static final int UPDATE_USER_MEDICINES = 0x01;
	private static final int RESULT_LOG = 0x02;
	private static final int ADD_USER_MMEDICINE_LOG_RECORD = 0x03;
	private static final int ADD_USER_MMEDICINE_LOG = 0x04;
	
	private Button addButton;
	
	private ListView listView;
	private List<Map<String,Object>> datas;
	
	private List<UserMedicineLog> logs;
	
	private long taskTime;
	
	private boolean addMlogsState = false;			//添加用药记录状态
	
	private Handler handler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) 
		{
			UserMedicineLogRecord log = null;
			UserMedicineLog uml = null;
			switch(msg.what)
			{
			case UPDATE_USER_MEDICINES:
				setDatas();
				setListView();
				break;
			case RESULT_LOG:
				Log.i("handler", "RESULT_LOG");
				log = (UserMedicineLogRecord) msg.obj;
				add(log);
				break;
			case ADD_USER_MMEDICINE_LOG_RECORD:
				log = (UserMedicineLogRecord) msg.obj;
				Intent i = new Intent(AddUserMedicinesActivity.this,UserLogDatasActivity.class);
				i.putExtra("new_log", log);
				setResult(RESULT_CODE,i);
				finish();
				break;
			case ADD_USER_MMEDICINE_LOG:
				uml = (UserMedicineLog) msg.obj;
				if(logs==null)
					logs = new ArrayList<UserMedicineLog>();
				
				logs.add(uml);
				setDatas();
				setListView();
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.user_medicines);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);
		
		TextView title = (TextView) this.findViewById(R.id.kqt_text);
		//this.iconsLayout = (LinearLayout) this.findViewById(R.id.kqt_user_icons_layout);
		//this.setIcons();
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);
		
		title.setText("添加用药记录");
		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		this.taskTime = this.getIntent().getLongExtra("task_time", 0l);
		this.listView = (ListView) this.findViewById(R.id.user_medicines_list);
		this.addButton = (Button) this.findViewById(R.id.user_medicines_add_button);
		
		this.setListener();
		
		ThreadPoolUtils.execute(new Runnable()
		{
			@Override
			public void run() 
			{
				if(user==null)
					return;
				
				try {
					logs = httpService.findUserMedicineItemsByUserId(user.getId());
				} catch (Exception e) {
					showException(e);
				}
				
				if(logs!=null)
					handler.sendEmptyMessage(UPDATE_USER_MEDICINES);
			}
		});
	}

	private void setListener()
	{
		this.addButton.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				View view = LayoutInflater.from(v.getContext()).inflate(R.layout.add_user_medicine, null, false);
				
				final TextView dtext = (TextView) view.findViewById(R.id.add_um_dname_text);
				final TextView mtext = (TextView) view.findViewById(R.id.add_um_mname_text);
				final TextView unittext = (TextView) view.findViewById(R.id.add_um_unit_text);
				
				new AlertDialog.Builder(v.getContext())
				.setTitle("添加用药")
				.setView(view)
				.setPositiveButton("添加", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						final String dname = dtext.getText().toString();
						final String mname = mtext.getText().toString();
						final String unit = unittext.getText().toString();
						
						ThreadPoolUtils.execute(new Runnable()
						{
							@Override
							public void run() 
							{
								UserMedicineLog uml = null;
								try {
									uml = httpService.addUserMedicineLog(user.getId(), mname, dname, unit);
								} catch (Exception e) {
									showException(e);
								}
								if(uml!=null)
								{
									Message msg = new Message();
									msg.what = ADD_USER_MMEDICINE_LOG;
									msg.obj = uml;
									
									handler.sendMessage(msg);
								}
							}
						});
						
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
					}
				})
				.show();
			}
		});
	}
	
	private void setDatas()
	{
		if(logs==null || logs.isEmpty())
			return;
		
		this.datas = new ArrayList<Map<String,Object>>();
		
		for(UserMedicineLog log:logs)
		{
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("log_data", log);
			
			this.datas.add(data);
		}
	}
	
	private void setListView()
	{
		if(this.datas==null || this.datas.isEmpty())
			return;
		
		this.listView.setAdapter(new UserMedicineItemAdapter(this,this.datas));
		this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) 
			{
				final Map<String,Object> data = datas.get(position);
				final UserMedicineLog log = logs.get(position);
				View v = LayoutInflater.from(view.getContext()).inflate(R.layout.add_user_medicine_log, null, false);
				
				final EditText num = (EditText) v.findViewById(R.id.add_um_num_edit);
				final TimePicker time = (TimePicker) v.findViewById(R.id.add_um_timePicker);
				
				time.setOnTimeChangedListener(new OnTimeChangedListener()
				{
					public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
					{
						if(taskTime>0)
							data.put("create_date", taskTime+hourOfDay*60*60*1000+minute*60*1000);
					}
				});
				
				new AlertDialog.Builder(view.getContext()).setTitle(log.getMedicine().getName()
						+(log.getMedicine().getUnit()!=null? "["+log.getMedicine().getUnit()+"]":""))
				.setView(v)
				.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						try {
							double numint = Double.valueOf(num.getText().toString());

							UserMedicineLogRecord l = new UserMedicineLogRecord();
							
							java.util.Calendar c = java.util.Calendar.getInstance();
							
							Long createdate = taskTime + c.get(java.util.Calendar.HOUR_OF_DAY)*60*60*1000 + c.get(java.util.Calendar.MINUTE)*60*1000;
							
							Log.i("time HOUR_OF_DAY",c.get(java.util.Calendar.HOUR_OF_DAY)+"");
							Log.i("time MINUTE",c.get(java.util.Calendar.MINUTE)+"");
							if(data.get("create_date")!=null)
								createdate = (Long) data.get("create_date");
							
							l.setCreateDate(createdate);
							
							l.setMedicine(log.getMedicine());
							l.setUser(user);
							l.setNum(numint);
							l.setTaskTime(taskTime);
							
							Message msg = new Message();
							msg.what = RESULT_LOG;
							msg.obj = l;
							
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
					}
				})
				.show();
			}
		});
	}
	
	private class UserMedicineItemAdapter extends BaseAdapter
	{
		private Context context;
		private List<Map<String,Object>> datas;
		
		public UserMedicineItemAdapter(Context context,
				List<Map<String, Object>> datas) {
			super();
			this.context = context;
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return this.datas.size();
		}

		@Override
		public Object getItem(int position) {
			
			return this.datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			if(convertView==null)
				convertView = LayoutInflater.from(context).inflate(R.layout.user_medicibes_item, parent,false);
			
			TextView nameview = (TextView) convertView.findViewById(R.id.um_item_name_text);
			
			Map<String,Object> data =  this.datas.get(position);
			UserMedicineLog log = (UserMedicineLog) data.get("log_data");
			
			nameview.setText(log.getMedicine().getName());
			
			return convertView;
		}
		
	}
	
	private void add(final UserMedicineLogRecord log)
	{
		if(addMlogsState)
			return;
		
		addMlogsState = true;
		ThreadPoolUtils.execute(new Runnable()
		{
			@Override
			public void run() 
			{
				UserMedicineLogRecord l=null;
				try {
					l = httpService.addUserMedicineLogRecord(log);
				} catch (Exception e) {
					showException(e);
				}
				if(l!=null)
				{
					Message msg = new Message();
					msg.what = ADD_USER_MMEDICINE_LOG_RECORD;
					msg.obj = l;
					
					handler.sendMessage(msg);
				}
				
				addMlogsState = false;
			}
		});
	}
}
