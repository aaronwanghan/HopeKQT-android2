package com.hope.kqt.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.entity.Medicine;
import com.hope.kqt.entity.UserMedicineLogsItem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class UserMedicinesActivity extends KqtActivity 
{
	private static final int UPDATE_USER_MEDICINES = 0x01;
	
	private ListView userMedicineListView;
	private List<Map<String,Object>> datas;
	
	private List<UserMedicineLogsItem> items;
	
	private Handler handler = new Handler()
	{
		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case UPDATE_USER_MEDICINES:
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
		this.setContentView(R.layout.user_medicines_menus);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);
		
		TextView title = (TextView) this.findViewById(R.id.kqt_text);
		//this.iconsLayout = (LinearLayout) this.findViewById(R.id.kqt_user_icons_layout);
		//this.setIcons();
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);
		
		title.setText("用药记录");
		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		this.userMedicineListView = (ListView) this.findViewById(R.id.user_medicines_menus_list);
		
		ThreadPoolUtils.execute(new Runnable()
		{
			@Override
			public void run() 
			{
				if(user==null)
					return;
				
				try {
					items = httpService.findUserMedicineLogsItemByUserId(user.getId());
				} catch (Exception e) {
					showException(e);
				}
				if(items!=null)
					handler.sendEmptyMessage(UPDATE_USER_MEDICINES);
			}
		});
	}

	private void setDatas()
	{
		if(items==null || items.isEmpty())
			return;
		
		this.datas = new ArrayList<Map<String,Object>>();
		
		for(UserMedicineLogsItem item:items)
		{
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("log_data", item.getMedicine());
			
			this.datas.add(data);
		}
	}
	
	private void setListView()
	{
		if(this.datas==null || this.datas.isEmpty())
			return;
		
		this.userMedicineListView.setAdapter(new UserMedicineItemAdapter(this,this.datas));
		this.userMedicineListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) 
			{
				UserMedicineLogsItem item = items.get(position);
				
				if(item.getLogs()==null || item.getLogs().isEmpty())
					return;
				
				Intent i = new Intent(view.getContext(),UserDataChartActivity.class);
				i.putExtra("user_data_type", 2);
				i.putExtra("user_data_datas", item);
				
				view.getContext().startActivity(i);
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
			Medicine m = (Medicine) data.get("log_data");
			
			nameview.setText(m.getName());
			
			return convertView;
		}
		
	}
}
