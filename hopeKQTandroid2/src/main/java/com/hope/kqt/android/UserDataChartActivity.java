package com.hope.kqt.android;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hope.kqt.android.util.TestItemChartHelper;
import com.hope.kqt.android.util.UserDataAdapter;
import com.hope.kqt.android.util.UserDateChartHelper;
import com.hope.kqt.entity.UserData;
import com.hope.kqt.entity.UserDatasItem;
import com.hope.kqt.entity.UserMLog;
import com.hope.kqt.entity.UserMedicineLogsItem;
import com.hope.kqt.entity.UserTestItem;
import com.hope.kqt.entity.UserTestLog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("InlinedApi")
public class UserDataChartActivity extends KqtActivity 
{
	private TextView title;
	
	private RelativeLayout layout;
	
	private LinearLayout titleLayout;
	
	private ListView userDataListView;
	private UserDataAdapter userDataAdapter;
	
	private List<UserDatasItem> datas;
	
	private UserTestItem uti;
	
	private UserMedicineLogsItem umli;
	
	private int type;
	
	private List<Button> buttons = new ArrayList<Button>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.user_data_chart);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);
		
		this.title = (TextView) this.findViewById(R.id.kqt_text);
		
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);
		
		//this.iconsLayout = (LinearLayout) this.findViewById(R.id.kqt_user_icons_layout);
		//this.setIcons();
		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		
		this.layout = (RelativeLayout) this.findViewById(R.id.user_data_chart_layout);
		this.userDataListView = (ListView) this.findViewById(R.id.user_data_chart_listview);
		this.titleLayout = (LinearLayout) this.findViewById(R.id.user_data_chart_values_titles);
		//this.setTestUserDatas();
		
		this.type = this.getIntent().getIntExtra("user_data_type", 0);
		this.setView();
		//this.uti = (UserTestItem) this.getIntent().getSerializableExtra("user_data_datas");
		
		/*if(this.uti!=null)
		{
			title.setText(this.uti.getName());
			
			this.datas = new ArrayList<UserDatasItem>();
			this.userTestItemToUserDatasItems(uti, datas);
			
			UserDateChartHelper helper = new UserDateChartHelper(this.datas,this.uti.getName(),this.uti.getUnit());
			
			this.layout.setBackgroundColor(Color.parseColor("#000000"));
			this.layout.addView(helper.getView(this), 0, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 
					LinearLayout.LayoutParams.WRAP_CONTENT));
			
			this.setUserDataListView();
			this.setListTitle();
		}*/
	}

	private void setView()
	{
		
		this.datas = new ArrayList<UserDatasItem>();
		String name = null;
		String unit = null;
		View chart = null;
		switch(this.type)
		{
		case 0:
			break;
		case 1:
			this.uti = (UserTestItem) this.getIntent().getSerializableExtra("user_data_datas");
			if(this.uti!=null)
			{
				this.title.setText(this.uti.getName());
				this.userTestItemToUserDatasItems(uti, datas);
				//name = this.uti.getName();
				//unit = this.uti.getUnit();
				TestItemChartHelper helper = new TestItemChartHelper(this.uti);
				chart = helper.getChartView(this);
			}
			break;
		case 2:
			this.umli = (UserMedicineLogsItem) this.getIntent().getSerializableExtra("user_data_datas");
			if(this.umli!=null)
			{
				this.title.setText(this.umli.getMedicine().getName());
				this.userMedicineLogsItemToUserDatasItems(umli, datas);
				name = this.umli.getMedicine().getName();
				unit = this.umli.getMedicine().getUnit();
				UserDateChartHelper helper = new UserDateChartHelper(this.datas,name,unit);
				chart = helper.getBarView(this);
			}
			break;
		}
		
		if(!this.datas.isEmpty())
		{
			
			this.layout.setBackgroundColor(Color.parseColor("#000000"));
			this.layout.addView(chart, 0, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 
					LinearLayout.LayoutParams.WRAP_CONTENT));
			this.setUserDataListView();
			this.setListTitle();
		}
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
	{
		super.onWindowFocusChanged(hasFocus);
		
		if(!this.buttons.isEmpty())
		{
			for(Button button:this.buttons)
			{
				button.setWidth(this.titleLayout.getWidth()/this.buttons.size());
			}
		}
	}

	private void setListTitle()
	{
		LinearLayout parent = (LinearLayout) this.findViewById(R.id.user_data_chart_parent_layout);
		
		View v = LayoutInflater.from(this).inflate(R.layout.user_data_chart_item, parent, false);
		
		TextView timetext = (TextView) v.findViewById(R.id.udc_item_text01);
		TextView valuetext = (TextView) v.findViewById(R.id.udc_item_text02);
		TextView ptext = (TextView) v.findViewById(R.id.udc_item_text03);
		
		timetext.setText("时间");
		valuetext.setText("数据");
		ptext.setText("增减量");
		
		parent.addView(v, 2);
	}
	
	private void clearButtonState()
	{
		if(this.buttons.isEmpty())
			return;
		for(Button button:this.buttons)
			button.setEnabled(true);
	}
	
	private void setUserDataListView()
	{
		if(this.datas!=null && !this.datas.isEmpty())
		{
			for(final UserDatasItem item:this.datas)
			{
				Button button = new Button(this);
				button.setBackgroundResource(R.drawable.chart_button_style);
				button.setText(item.getName());
				button.setOnClickListener(new View.OnClickListener() 
				{	
					@Override
					public void onClick(View v) 
					{
						clearButtonState();
						v.setEnabled(false);
						
						if(userDataAdapter==null)
						{
							userDataAdapter = new UserDataAdapter(item.getDatas(),v.getContext());
							userDataListView.setAdapter(userDataAdapter);
						}
						else
							userDataAdapter.setLogs(item.getDatas());
						userDataAdapter.notifyDataSetChanged();
					}
				});
				this.buttons.add(button);
				this.titleLayout.addView(button,new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, 
						LinearLayout.LayoutParams.WRAP_CONTENT));
			}
			
			this.buttons.get(0).setEnabled(false);
			this.userDataAdapter = new UserDataAdapter(this.datas.get(0).getDatas(),this);
			this.userDataListView.setAdapter(this.userDataAdapter);
			
			if(this.datas.size()==1)
				this.titleLayout.setVisibility(View.GONE);
		}
	}
	
	/*private void setTestUserDatas()
	{
		this.datas = new ArrayList<UserData>();
		
		Calendar now = Calendar.getInstance();
		
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		
		now.add(Calendar.DAY_OF_YEAR, -20);
		
		int value = 100;
		for(int i=0;i<20;i++)
		{

			now.add(Calendar.DAY_OF_YEAR, i);
			long time = now.getTimeInMillis();
			
			UserData ud = new UserData();
			ud.setTime(time);
			
			ud.setValue(value += i%2*(-1)*i);
			
			ud.setProgress(i%2*(-1)*i);
			
			this.datas.add(ud);
		}
	}*/
	private List<UserData> userMLogToUserData(List<UserMLog> logs)
	{
		if(logs==null || logs.isEmpty())
			return null;
		
		List<UserData> list = new ArrayList<UserData>();
		
		for(int i=0;i<logs.size();i++)
		{
			UserMLog log = logs.get(i);
			UserMLog on = null;
			
			if(i>0)
				on = logs.get(i-1);
			
			UserData data = new UserData();
			data.setTime(log.getTaskTime());
			data.setValue(log.getNum());
			
			if(on!=null)
				data.setProgress(log.getNum()-on.getNum());
			
			list.add(data);
		}
			
		return list;
	}
	
	private List<UserData> userTestLogToUserData(List<UserTestLog> logs)
	{
		if(logs==null || logs.isEmpty())
			return null;
		
		List<UserData> list = new ArrayList<UserData>();
		
		for(int i=0;i<logs.size();i++)
		{
			UserTestLog log = logs.get(i);
		
			if(log.getValue()==null || "".equals(log.getValue()))
				continue;
			
			UserTestLog on = this.getOnData(logs, i);
			
			UserData data = new UserData();
			data.setTime(log.getTaskTime());
			data.setValue(Double.valueOf(log.getValue()));
			
			if(on!=null)
				data.setProgress(getProress(data.getValue(),Double.valueOf(on.getValue())));
			
			list.add(data);
		}
		
		return list;
	}
	
	private UserTestLog getOnData(List<UserTestLog> logs,int i)
	{
		if(i>0)
		{
			UserTestLog log = logs.get(i-1);
			if(log.getValue()==null || "".equals(log.getValue()))
				return this.getOnData(logs, i-1);
			
			return log;
		}
		
		return null;
	}

	private void userMedicineLogsItemToUserDatasItems(UserMedicineLogsItem item,List<UserDatasItem> list)
	{
		UserDatasItem uitem = new UserDatasItem();
		uitem.setName(item.getMedicine().getName());
		uitem.setUnit(item.getMedicine().getUnit());
		uitem.setDatas(this.userMLogToUserData(item.getLogs()));
		
		list.add(uitem);
	}
	
	private void userTestItemToUserDatasItems(UserTestItem item,List<UserDatasItem> list)
	{
		List<UserTestItem> clist = item.getChildren();
		
		if(clist!=null && !clist.isEmpty())
		{
			for(UserTestItem citem:clist)
			{
				this.userTestItemToUserDatasItems(citem, list);
			}
		}
		else
		{
			UserDatasItem uitem = new UserDatasItem();
			uitem.setName(item.getName());
			uitem.setUnit(item.getUnit());
			uitem.setDatas(this.userTestLogToUserData(item.getLogs()));
			
			list.add(uitem);
		}
	}
	
	private Double getProress(Double d1,Double d2)
	{
		BigDecimal bd1 = new BigDecimal(d1);
		BigDecimal bd2 = new BigDecimal(d2);
		
		return bd1.subtract(bd2).doubleValue();
	}
}
