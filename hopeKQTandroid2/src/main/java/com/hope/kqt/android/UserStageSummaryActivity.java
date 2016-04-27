package com.hope.kqt.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.android.util.UserStageSummaryAdapter;
import com.hope.kqt.entity.UserStageSummary;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class UserStageSummaryActivity extends ListViewActivity 
{
	private List<UserStageSummary> ussList;
	
	private Handler handler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case UPDATE_LIST_VIEW_MESSAGE:
				setListView();
				break;
			}
		}};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		ThreadPoolUtils.execute(new Runnable()
		{
			@Override
			public void run() 
			{
				try {
					ussList = httpService.findUserStageSummaryByUserId(user.getId());
				} catch (Exception e) {
					showException(e);
				}
				
				if(ussList!=null)
					Log.i("ussList", ussList.size()+"");
				
				if(ussList!=null)
					handler.sendEmptyMessage(UPDATE_LIST_VIEW_MESSAGE);
			}
		});
	}

	@Override
	protected String getTextTitle() {
		return "小结";
	}

	@Override
	protected BaseAdapter getAdapter() 
	{
		if(this.values!=null)
			return new UserStageSummaryAdapter(this.values,this);
		
		return null;
	}

	@Override
	protected void setValues()
	{
		if(this.ussList==null || this.ussList.isEmpty())
			return;
		
		this.values = new ArrayList<Map<String, Object>>();
		
		for(UserStageSummary uss:this.ussList)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("value", uss);
			
			this.values.add(map);
		}
	}

}
