package com.hope.kqt.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.android.util.UserLogAdapter;
import com.hope.kqt.entity.UserLog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

public class UserLogsActivity extends ListViewActivity 
{
	private List<UserLog> userLogList;
	
	private Handler handler = new Handler()
	{
		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case UPDATE_LIST_VIEW_MESSAGE:
				setListView();
				break;
			}
		}
	};
	
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
					userLogList = httpService.findUserLogByUserid(user.getId());
				} catch (Exception e) {
					showException(e);
				}
				
				if(userLogList!=null)
					handler.sendEmptyMessage(UPDATE_LIST_VIEW_MESSAGE);
			}
		});
	}

	@Override
	protected String getTextTitle() 
	{
		return "康复反应";
	}

	@Override
	protected BaseAdapter getAdapter() 
	{
		if(this.values!= null)
			return new UserLogAdapter(this.values,this);
		
		return null;
	}

	@Override
	protected void setValues() 
	{
		if(this.userLogList==null || this.userLogList.isEmpty())
			return;
		
		this.values = new ArrayList<Map<String, Object>>();
		
		for(UserLog log:this.userLogList)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("value", log);
			
			this.values.add(map);
		}
	}

}
