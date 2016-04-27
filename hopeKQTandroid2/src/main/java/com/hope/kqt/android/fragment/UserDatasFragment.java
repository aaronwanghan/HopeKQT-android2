package com.hope.kqt.android.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hope.kqt.android.R;
import com.hope.kqt.android.UserDataChartActivity;
import com.hope.kqt.android.UserLogsActivity;
import com.hope.kqt.android.UserMedicinesActivity;
import com.hope.kqt.android.UserStageSummaryActivity;
import com.hope.kqt.android.util.ProgressDialogAsyncTask;
import com.hope.kqt.android.util.UserDataItemAdapter;
import com.hope.kqt.entity.UserTestItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

@SuppressLint("NewApi")
public class UserDatasFragment extends Fragment 
{
	private static final String[] ITEMS_NAME = {"用药","反应","小结"};
	private static final int[] ITEMS_TYPE = {1,2,3};
	
	private final static int UPDATE_PAGE = 0x01;
	
	private Context context;
	private UserDatasListener listener;
	
	private GridView listView;
	private List<Map<String,Object>> udValues;
	private List<UserTestItem> userTestItems;
	
	private Handler handler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case UPDATE_PAGE:
				setListView();
				break;
			}
			super.dispatchMessage(msg);
		}
		
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
		this.listener = (UserDatasListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		ProgressDialogAsyncTask task = new ProgressDialogAsyncTask(this.context,
				new Runnable()
		{
			@Override
			public void run() 
			{
				userTestItems = listener.getUserTestItems();
				handler.sendEmptyMessage(UPDATE_PAGE);
			}
			
		},"提示","数据加载中...");
		task.execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.user_data_menus2, container, false);
		
		this.listView = (GridView) v.findViewById(R.id.user_data_listview);
		
		return v;
	}
	
	private void setListView()
	{
		if(this.userTestItems==null || this.userTestItems.isEmpty())
			return;
		
		setUDValues();
		UserDataItemAdapter adapter = new UserDataItemAdapter(this.udValues,this.context);
		
		this.listView.setAdapter(adapter);
		this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) 
			{
				//Intent i = new Intent(view.getContext(),UserDataChartActivity.class);
				//view.getContext().startActivity(i);
				
				Map<String,Object> map = udValues.get(position);
				Intent i = null;
				int type = (Integer) map.get("user_data_type");
				switch(type)
				{
				case 0:
					i = new Intent(view.getContext(),UserDataChartActivity.class);
					i.putExtra("user_data_type", 1);
					i.putExtra("user_data_datas", userTestItems.get(position));
					break;
				case 1:
					i = new Intent(view.getContext(),UserMedicinesActivity.class);
					break;
				case 2:
					i = new Intent(view.getContext(),UserLogsActivity.class);
					break;
				case 3:
					i = new Intent(view.getContext(),UserStageSummaryActivity.class);
					break;
				}
				
				if(i!=null)
					view.getContext().startActivity(i);
			}
		});
	}
	
	private void setUDValues()
	{
		this.udValues = new ArrayList<Map<String,Object>>();
		
		for(UserTestItem uti:this.userTestItems)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			
			map.put("user_data_name", uti.getName());
			map.put("user_date_unit", uti.getUnit());
			//map.put("user_data_datas", uti.getLogs());

           // if(uti.getUserMedicineLogItems()!=null)
            //    map.put("user_data_umls",uti.getUserMedicineLogItems());

			map.put("user_data_type", 0);
			
			this.udValues.add(map);
		}
		
		for(int i=0;i<ITEMS_NAME.length;i++)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user_data_name", ITEMS_NAME[i]);
			map.put("user_data_type", ITEMS_TYPE[i]);
			
			this.udValues.add(map);
		}
	}
	
	public interface UserDatasListener
	{
		public List<UserTestItem> getUserTestItems();
	}
}
