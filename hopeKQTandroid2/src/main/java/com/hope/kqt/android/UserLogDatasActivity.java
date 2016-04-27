/**
 * 
 */
package com.hope.kqt.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import com.hope.kqt.android.util.Date;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.android.util.UserLogCheckBoxAdapter;
import com.hope.kqt.android.util.UserMedicineAdapter;
import com.hope.kqt.android.util.UserMedicineLogRecordComparator;
import com.hope.kqt.android.util.UserTestLogAdapter;
import com.hope.kqt.entity.TestItem;
import com.hope.kqt.entity.UserLog;
import com.hope.kqt.entity.UserLogData;
import com.hope.kqt.entity.UserMedicineLogRecord;
import com.hope.kqt.entity.UserStageSummary;
import com.hope.kqt.entity.UserTask;
import com.hope.kqt.entity.UserTaskMenus;
import com.hope.kqt.entity.UserTestLog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author aaronwang
 *
 */

public class UserLogDatasActivity extends KqtActivity 
{
	private static final int ADD_UM_ACTIVITY_REQUEST_CODE = 0x01;
	
	private static final int UPATE_USER_MEDICINES_MESSAGE = 0x01;
	private static final int ADD_USER_STAGE_SUMMARY_MESSAGE = 0x02;
	private static final int ADD_USER_LOG_MESSAGE = 0x03;
	private static final int ADD_USER_TEST_LOG_MESSAGE = 0x04;
	//private static final int ADD_USER_M_LOG_MESSAGE = 0x05;
	
	private static final int DELETE_USER_M_LOG_MESSAGE = 0x06;
	
	private static final int UPDATE_DATAS_MESSAGE = 0x07;
	
	private TabHost tabHost;
	private boolean[] tabStates = {true,true,true,false};
	
	private ListView logsListView;
	private List<Map<String,Object>> logsValues;
	private UserTestLogAdapter utlAdapter;
	private UserTaskMenus taskMenus;
	
	private ListView mlogsListView;
	private List<Map<String,Object>> mlogsValues;
	private List<UserMedicineLogRecord> umlrList;
	private UserMedicineAdapter umAdapter;
	//private List<UserMedicineLog> medicines;
	
	private Button dataUTLButton;
	//private Button dataMLButton;
	private Button addDataMLButton;
	private Button dataULButton;
	private Button dataEndButton;
	private boolean buttonState = false;
	
	private RatingBar rb01,rb02,rb03;
	private EditText dataEndContent;
	
	private GridView ulGridView;
	private EditText ulContent;
	private UserLogCheckBoxAdapter ulAdapter;
	
	private UserLogData userLogData;
	
	//private boolean userMedicinesPathState = false;	//获得用药记录状态
	
	private int page;
	
	private long start;
	private long end;
	
	private Handler handler = new Handler()
	{

		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case UPATE_USER_MEDICINES_MESSAGE:
				setMLogsValues();
				setMLogsListView();
				//userMedicinesPathState = false;
				break;
			case ADD_USER_STAGE_SUMMARY_MESSAGE:
				getMessage("提交成功~");
				break;
			case ADD_USER_LOG_MESSAGE:
				getMessage("提交成功~");
				break;
			case ADD_USER_TEST_LOG_MESSAGE:
				getMessage("提交成功~");
				break;
			//case ADD_USER_M_LOG_MESSAGE:
			//	setUserMedicines();
			//	break;
			case DELETE_USER_M_LOG_MESSAGE:
				int num = umlrList.indexOf(msg.obj);
				if(num>-1)
				{
					umlrList.remove(num);
					mlogsValues.remove(num);
					umAdapter.notifyDataSetChanged();
				}
				break;
			case UPDATE_DATAS_MESSAGE:
				setUserTestLogsValue();
				setUserMedicinesValue();
				setUserLogValue();
				setUserStageSummaryValue();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.user_log_datas);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);
		
		TextView title = (TextView) this.findViewById(R.id.kqt_text);
		//this.iconsLayout = (LinearLayout) this.findViewById(R.id.kqt_user_icons_layout);
		//this.setIcons();
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);

		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		this.taskMenus = (UserTaskMenus) this.getIntent().getSerializableExtra("taskMeuns");
		title.setText(Date.valueOf(this.taskMenus.getTime()).toDate());
		this.page = this.getIntent().getIntExtra("current_tab", 0);
		this.start = this.getIntent().getLongExtra("start", 0);
		this.end = this.getIntent().getLongExtra("end", 0);
		
		this.tabHost = (TabHost) this.findViewById(R.id.data_tabhost);
		this.logsListView = (ListView) this.findViewById(R.id.data_utl_listview);
		this.mlogsListView = (ListView) this.findViewById(R.id.data_ml_listview);
		
		this.dataEndButton = (Button) this.findViewById(R.id.data_end_button);
		//this.dataMLButton = (Button) this.findViewById(R.id.data_ml_button);
		this.dataULButton = (Button) this.findViewById(R.id.data_ul_button);
		this.dataUTLButton = (Button) this.findViewById(R.id.data_utl_button);
		this.addDataMLButton = (Button) this.findViewById(R.id.data_ml_add_button);
		
		this.rb01 = (RatingBar) this.findViewById(R.id.data_end_ratingbar1);
		this.rb02 = (RatingBar) this.findViewById(R.id.data_end_ratingbar2);
		this.rb03 = (RatingBar) this.findViewById(R.id.data_end_ratingbar3);
		
		this.dataEndContent = (EditText) this.findViewById(R.id.data_end_content_edit);
		
		this.ulGridView = (GridView) this.findViewById(R.id.data_ul_gridview);
		this.ulContent = (EditText) this.findViewById(R.id.data_ul_edit);
		
		if(this.taskMenus.getTime()==this.getNow())
			this.buttonState = true;
		
		this.dataULButton.setEnabled(this.buttonState);
		
		this.setListView();
		this.setTabHost();
		this.setULGridView();
		this.setListener();
		this.setOldDatas();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		Log.i("requestCode", requestCode+"");
		Log.i("resultCode", resultCode+"");
		
		if( requestCode==ADD_UM_ACTIVITY_REQUEST_CODE && resultCode==AddUserMedicinesActivity.RESULT_CODE)
		{
			Log.i("onActivityResult", "AddUserMedicinesActivity.RESULT_CODE");
			
			try {
				UserMedicineLogRecord log = (UserMedicineLogRecord) data.getSerializableExtra("new_log");
				if(log!=null)
				{
					if(umlrList==null)
						umlrList = new ArrayList<UserMedicineLogRecord>();
					
					umlrList.add(log);
					Collections.sort(this.umlrList, new UserMedicineLogRecordComparator());
					handler.sendEmptyMessage(UPATE_USER_MEDICINES_MESSAGE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private String getRatings()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append((int)this.rb01.getRating());
		sb.append((int)this.rb02.getRating());
		sb.append((int)this.rb03.getRating());
		
		return sb.toString();
	}
	
	private void setListener()
	{
		this.dataEndButton.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				final UserStageSummary uss = new UserStageSummary();
				uss.setStageNum(taskMenus.getStageNum());
				uss.setScore(getRatings());
				uss.setUser(user);
				uss.setContent(dataEndContent.getText().toString());
				
				uss.setTaskTime(taskMenus.getTime());
				
				ThreadPoolUtils.execute(new Runnable()
				{
					@Override
					public void run() 
					{
						try {
							if(httpService.addUserStageSummary(uss))
								handler.sendEmptyMessage(ADD_USER_STAGE_SUMMARY_MESSAGE);
						} catch (Exception e) {
							showException(e);
						}
					}
				});
			}
		});
		
		this.addDataMLButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(v.getContext(),AddUserMedicinesActivity.class);
				i.putExtra("task_time", taskMenus.getTime());
				
				startActivityForResult(i,ADD_UM_ACTIVITY_REQUEST_CODE);
			}
		});

		/*this.dataMLButton.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				if(umlrList==null || umlrList.isEmpty())
					return;
				
				ThreadPoolUtils.execute(new Runnable()
				{
					@Override
					public void run() 
					{
						if(!addMlogsState)
						{
							addMlogsState = true;
							if(httpService.addUserMedicineLogRecords(umlrList))
							{
								handler.sendEmptyMessage(ADD_USER_M_LOG_MESSAGE);
							}
							addMlogsState = false;
						}
					}
				});
			}
		});*/
		
		this.dataULButton.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				final UserLog log = new UserLog();
				log.setUser(user);
				log.setCreateDate(taskMenus.getTime());
				log.setType(1);
				log.setContent(getULContent());
				
				ThreadPoolUtils.execute(new Runnable()
				{
					@Override
					public void run() {
						try {
							if(httpService.addUserlog(log))
								handler.sendEmptyMessage(ADD_USER_LOG_MESSAGE);
						} catch (Exception e) {
							showException(e);
						}
					}
				});
			}
		});
		
		this.dataUTLButton.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				if(logsValues==null || logsValues.isEmpty())
					return;
				
				final List<UserTestLog> list = new ArrayList<UserTestLog>();
				
				for(Map<String,Object> map:logsValues)
				{
					String value = (String) map.get("log_value");
					if(value!=null && !"".equals(value))
					{
						UserTestLog log = new UserTestLog();
						log.setUser(user);
						log.setValue(value);
						log.setTaskTime(taskMenus.getTime());
						
						TestItem item = new TestItem();
						item.setId((Long)map.get("test_item_id"));
						log.setItem(item);
						
						list.add(log);
					}
				}
				
				if(!list.isEmpty())
				{
					ThreadPoolUtils.execute(new Runnable()
					{
						@Override
						public void run() 
						{
							try {
								if(httpService.addUserTestLog(list))
									handler.sendEmptyMessage(ADD_USER_TEST_LOG_MESSAGE);
							} catch (Exception e) {
								showException(e);
							}
						}
					});
				}
			}
		});
	}
	
	private void setTabHost()
	{
		this.tabHost.setup();
		
		TabSpec[] tabs = {
				this.tabHost.newTabSpec("test_log")
				.setContent(R.id.data_utl_view)
				.setIndicator(this.getTabTitleView("测量数据")),
				this.tabHost.newTabSpec("m_log")
				.setContent(R.id.data_ml_view)
				.setIndicator(this.getTabTitleView("用药记录")),
				this.tabHost.newTabSpec("uesr_log")
				.setContent(R.id.data_ul_view)
				.setIndicator(this.getTabTitleView("康复反应")),
				this.tabHost.newTabSpec("stage_end")
				.setContent(R.id.data_end_view)
				.setIndicator(this.getTabTitleView("小结"))
		};

		for(int i=0;i<this.tabStates.length;i++)
		{
			if(this.tabStates[i])
				this.tabHost.addTab(tabs[i]);
		}
		
		this.tabHost.setCurrentTab(this.page);
	}
	
	private View getTabTitleView(String title)
	{
		View view = LayoutInflater.from(this).inflate(R.layout.tab_indicator, this.tabHost,false);
		TextView text = (TextView) view.findViewById(R.id.tab_indicator_text);
		text.setText(title);
		
		return view;
	}
	
	private void setListView()
	{
		if(taskMenus==null || taskMenus.getTasks()==null || taskMenus.getTasks().isEmpty())
		{
			Log.i("taskMeuns is null", taskMenus.toString());
			if(taskMenus!=null && taskMenus.getTasks()!=null)
			Log.i("taskMenus.getTasks().isEmpty()", taskMenus.getTasks().size()+"");
			return;
		}
		
		this.logsValues = new ArrayList<Map<String,Object>>();
		List<UserTask> tasks = this.taskMenus.getTasks();
		
		for(UserTask task:tasks)
		{
			switch(task.getType())
			{
			case 5:
				addTestItems(task);
				this.dataUTLButton.setEnabled(buttonState);
				break;
			case 6:
				//this.setUserMedicines();
				//this.dataMLButton.setEnabled(buttonState);
				this.addDataMLButton.setEnabled(buttonState);
				break;
			case 7:
				this.tabStates[3] = true;
				this.dataEndButton.setEnabled(buttonState);
				break;
			default:
				break;
			}
		}

		this.setLogsListView();
	}
	
	private void addTestItems(UserTask task)
	{
		Log.i("addTestItems", "start:"+start+" end:"+end+" task:"+task.getStartTime());
		
		if(start!=0 && end!=0 && (start>task.getStartTime() || end<=task.getStartTime()))
			return;
		
		List<TestItem> testItems = task.getTestItems();
		if(testItems!=null && !testItems.isEmpty())
		{
			for(TestItem item:testItems)
				this.testItemToMap(null,item,task.getStartTime());
		}
		else
			Log.i("testItem is null", "task:"+Date.valueOf(task.getStartTime()).toDate());
	}

	private void setLogsListView()
	{
		if(this.logsValues==null || this.logsValues.isEmpty())
		{
			return;
		}
		
		this.setTimeTitle();
		Log.i("logsValues", logsValues.size()+"");
		
		this.utlAdapter = new UserTestLogAdapter(this,this.logsValues);
		this.logsListView.setAdapter(utlAdapter);
		
		KQTUtils.setListViewHeightBasedOnChildren(this.logsListView);
	}
	
	private void setMLogsValues()
	{
		if(this.umlrList==null || this.umlrList.isEmpty())
			return;
		
		this.mlogsValues = new ArrayList<Map<String,Object>>();
		
		for(UserMedicineLogRecord log:this.umlrList)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("m_item_name", log.getMedicine().getName());
			map.put("m_item_unit", log.getMedicine().getUnit());
			map.put("m_value", log.getNum()+"");
			map.put("create_date", log.getCreateDate());
			
			Log.i("num",log.getNum()+"");
			
			this.mlogsValues.add(map);
		}
	}
	
	/*private void setMLogsValues()
	{
		if(this.medicines==null || this.medicines.isEmpty())
			return;
		
		this.mlogsValues = new ArrayList<Map<String,Object>>();
		
		for(UserMedicineLog m:this.medicines)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("m_item_name", m.getName());
			map.put("m_item_unit", m.getUnit());
			map.put("m_value", "");
			
			this.mlogsValues.add(map);
		}
	}*/

	private void setMLogsListView()
	{
		if(this.mlogsValues==null || this.mlogsValues.isEmpty())
			return;
		
		this.umAdapter = new UserMedicineAdapter(this.mlogsValues,this);
		this.mlogsListView.setAdapter(this.umAdapter);
		
		if(this.buttonState)
		{
			this.mlogsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) 
				{
					final UserMedicineLogRecord log = umlrList.get(position);
					
					StringBuffer sb = new StringBuffer();
					sb.append("是否删除  ");
					sb.append(Date.valueOf(log.getCreateDate()).toTime());
					sb.append(" ");
					sb.append(log.getMedicine().getName());
					sb.append(" 的用药记录");
					
					new AlertDialog.Builder(view.getContext())
					.setTitle("删除用药记录")
					.setMessage(sb.toString()).setPositiveButton("确认", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							ThreadPoolUtils.execute(new Runnable()
							{
								@Override
								public void run() 
								{
									try {
										if(httpService.deleteUserMedicineLogRecord(log.getId()))
										{
											Message msg = new Message();
											msg.what = DELETE_USER_M_LOG_MESSAGE;
											msg.obj = log;
											
											handler.sendMessage(msg);
										}
									} catch (Exception e) {
										showException(e);
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
		
		Log.i("setMLogsListView", this.mlogsValues.size()+"");
	}
	
	private void setTimeTitle()
	{
		this.setTimeTitle(-1, 12, "上午", "#FFFF99", this.logsValues);
		this.setTimeTitle(12, 18, "下午", "#FFCC99", this.logsValues);
		this.setTimeTitle(18, 25, "晚上", "#99CCFF", this.logsValues);
	}
	
	private void setTimeTitle(int min,int max,String title,String color,List<Map<String,Object>> logsValues)
	{
		for(int i=0;i<logsValues.size();i++)
		{
			Map<String,Object> values = logsValues.get(i);
			Long time = (Long) values.get("task_time");
			
			if(time!=null)
			{
				int h = Date.valueOf(time).getHour();
				
				if(h>=min && h<max)
				{
					logsValues.add(i, timeTitleToMap(title,color));
					return;
				}
			}
		}
	}
	
	
	private Map<String,Object> timeTitleToMap(String title,String color)
	{
		Map<String,Object> values = new HashMap<String,Object>();
		values.put("log_type", 1);
		values.put("time_title", title);
		values.put("color", color);
		
		return values;
	}
	
	private void testItemToMap(String parent,TestItem item,long time)
	{
		List<TestItem> list = item.getChildren();
		String name = (parent==null? "":parent+"->")+item.getName();
		
		if(list!=null && !list.isEmpty())
		{
			for(TestItem ti:list)
				this.testItemToMap(name, ti, time);
		}
		else
		{
			Map<String,Object> values = new HashMap<String,Object>();
			
			values.put("task_time", time);
			values.put("task_menus_time", taskMenus.getTime());
Log.i("testItemToMap", ""+item.getId());

			values.put("test_item_id", item.getId());
			values.put("test_item_name", name);
			values.put("test_item_unit", item.getUnit());
			values.put("log_type", 0);
			
			values.put("log_value", "");
			
			this.logsValues.add(values);
			
			Log.i("testItemToMap add", name);
		}
	}

	private void setULGridView()
	{
		String[] eitems = this.getResources().getStringArray(R.array.ul_e_items);

		if(eitems==null || eitems.length==0)
			return;
		
		this.ulAdapter = new UserLogCheckBoxAdapter(this,eitems);
		this.ulGridView.setAdapter(this.ulAdapter);
	}
	
	private String getULContent()
	{
		StringBuffer sb = new StringBuffer("");
		
		if(this.ulAdapter!=null)
			sb.append(this.ulAdapter.getRet());
		
		sb.append(this.ulContent.getText().toString());
		
		return sb.toString();
	}
	
	private long getNow()
	{
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		
		return now.getTimeInMillis();
	}
	
	/*private void setUserMedicines()
	{
		if(!this.userMedicinesPathState)
		{
			this.userMedicinesPathState = true;
			ThreadPoolUtils.execute(new Runnable()
			{

				@Override
				public void run() 
				{
					medicines = httpService.findUserMedicineItemsByUserId(user.getId());
					if (medicines!=null) 
					{
						handler.sendEmptyMessage(UPATE_USER_MEDICINES_MESSAGE);
						return;
					}
					
					umlrList = httpService.findUserMedicineLogRecordAndUserAndTaskTime(user.getId(), taskMenus.getTime());
					if(umlrList!=null)
					{
						handler.sendEmptyMessage(UPATE_USER_MEDICINES_MESSAGE);
						return;
					}
					userMedicinesPathState = false;
				}
			});
		}
	}*/

	private void setOldDatas()
	{
		ThreadPoolUtils.execute(new Runnable()
		{
			@Override
			public void run() 
			{
				try {
					userLogData = httpService.findUserLogDataByUserIdAndTaskTime(user.getId(), taskMenus.getTime());
				} catch (Exception e) {
					showException(e);
				}
				handler.sendEmptyMessage(UPDATE_DATAS_MESSAGE);
			}
		});
	}
	
	private void setUserTestLogsValue()
	{
		if(this.userLogData==null)
			return;
		
		List<UserTestLog> logs = this.userLogData.getUTLogs();
		if(logs!=null && this.logsValues!=null && !this.logsValues.isEmpty())
		{
			for(Map<String,Object> map:this.logsValues)
			{
				Long id = (Long) map.get("test_item_id");
				for(UserTestLog log:logs)
				{	
					if(id!=null && id==log.getItem().getId())
						map.put("log_value", log.getValue());
				}
			}
		}
		
		if(this.utlAdapter!=null)
			this.utlAdapter.notifyDataSetChanged();
		else
			this.setLogsListView();
	}
	
	private void setUserMedicinesValue()
	{
		if(this.userLogData==null)
			return;
		
		this.umlrList = this.userLogData.getuMLRecords();
		this.setMLogsValues();
		
		if(this.umAdapter!=null)
			this.umAdapter.notifyDataSetChanged();
		else
			this.setMLogsListView();
	}
	
	private void setUserLogValue()
	{
		if(this.userLogData==null || this.userLogData.getULog()==null)
			return;
		
		String s = this.userLogData.getULog().getContent();
		if(s!=null && !"".equals(s))
			ulContent.setText(this.ulAdapter.setValues(s));
	}
	
	private void setUserStageSummaryValue()
	{
		if(this.userLogData==null)
			return;
		
		UserStageSummary uss = this.userLogData.getUSSummary();
		if(uss!=null)
		{
			this.dataEndContent.setText(uss.getContent());

			if (uss.getTeacherRatings()!=null && uss.getTeacherRatings().size()==3) 
			{
				this.rb01.setRating(uss.getTeacherRatings().get(0).getScore());
				this.rb02.setRating(uss.getTeacherRatings().get(1).getScore());
				this.rb03.setRating(uss.getTeacherRatings().get(2).getScore());
			}

		}
	}
}

