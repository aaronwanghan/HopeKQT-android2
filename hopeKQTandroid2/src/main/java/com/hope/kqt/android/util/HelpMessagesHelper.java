package com.hope.kqt.android.util;

import java.util.List;

import com.hope.kqt.android.Application;
import com.hope.kqt.android.dao.DbHelper;
import com.hope.kqt.android.dao.HelpMessageDao;
import com.hope.kqt.android.dao.impl.HelpMessageDaoImpl;
import com.hope.kqt.android.http.HttpService;
import com.hope.kqt.android.http.impl.HttpServiceImpl;
import com.hope.kqt.entity.HelpMessage;
import com.hope.kqt.entity.User;

import android.content.Context;

public class HelpMessagesHelper 
{
	private Context context;
	private HelpMessageDao dao;
	private HttpService httpService = new HttpServiceImpl();
	
	private User user;
	
	private UpdateListener onUpdateListener;
	
	public HelpMessagesHelper(Context context,User user) 
	{
		super();
		this.context = context;
		this.user = user;
		DbHelper db = new DbHelper(this.context, Application.SQL_DB, null);
		this.dao = new HelpMessageDaoImpl(db.getWritableDatabase());
	}
	
	public void updateDatas() throws Exception
	{
		if(user==null)
			return;
		
		List<HelpMessage> list = httpService.findHelpMessageByUserId(user.getId());
		
		if(list!=null)
		{
			this.dao.update(list);
			
			if(this.onUpdateListener!=null)
				this.onUpdateListener.action(list);
		}
	}
	
	public void setOnUpdateListener(UpdateListener onUpdateListener) {
		this.onUpdateListener = onUpdateListener;
	}

	public List<HelpMessage> findAll()
	{
		return this.dao.findAll();
	}
	
	public interface UpdateListener
	{
		public void action(List<HelpMessage> messages);
	}
}
