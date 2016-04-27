package com.hope.kqt.android.util;

import java.util.List;

import com.hope.kqt.android.Application;
import com.hope.kqt.android.dao.DbHelper;
import com.hope.kqt.android.dao.SystemValueDao;
import com.hope.kqt.android.dao.UserTaskLogDao;
import com.hope.kqt.android.dao.UserTaskMenusDao;
import com.hope.kqt.android.dao.impl.SystemValueFileDaoImpl;
import com.hope.kqt.android.dao.impl.UserTaskLogDaoImpl;
import com.hope.kqt.android.dao.impl.UserTaskMenusDaoImpl;
import com.hope.kqt.android.http.HttpService;
import com.hope.kqt.android.http.impl.HttpServiceImpl;
import com.hope.kqt.entity.User;
import com.hope.kqt.entity.UserTaskMenus;

import android.content.Context;
import android.util.Log;

public class UserTaskMenusDatasHelper 
{
	private Context context;
	private UserTaskLogDao logDao;
	private UserTaskMenusDao utmDao;
	private SystemValueDao systemValueDao;
	private HttpService httpService = new HttpServiceImpl();
	
	private User user;
	private List<UserTaskMenus> taskMeunss;
	private DbHelper db;
	
	public UserTaskMenusDatasHelper(Context context,User user) 
	{
		super();
		this.context = context;
		this.user = user;
		this.initDao();
		
	}
	
	private void initDao()
	{
		if(this.context!=null)
		{
			this.systemValueDao = new SystemValueFileDaoImpl(this.context);
			this.db = new DbHelper(this.context, Application.SQL_DB, null);
			this.utmDao = new UserTaskMenusDaoImpl(db.getWritableDatabase());
			this.logDao = new UserTaskLogDaoImpl(db.getWritableDatabase());
		}
	}
	
	public boolean updateDatas()
	{
		if(user==null)
			return false;
		
		int version = 0;
		try {
			version = httpService.findUserTreatmentVersion(user.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("updateDatas", "read version:"+version);
		String old = systemValueDao.load(Application.USER_TREATMENT_VERSION_SYSTEM_KEY);
		
		if((version+"").equals(old) || version==-1)
		{
			Log.i("updateDatas", "read sql");
			readDB();
		}
		else
		{
			Log.i("updateDatas", "update tasks");
			
			try {
				taskMeunss = httpService.findUserTaskByUserId(user.getId());
				saveDB(taskMeunss);
				systemValueDao.set(Application.USER_TREATMENT_VERSION_SYSTEM_KEY, version+"");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public void close()
	{
		this.utmDao = null;
		this.db.close();
	}
	
	public List<UserTaskMenus> findAll()
	{
		//this.updateDatas();
		if(this.utmDao!=null)
			return this.utmDao.findAll();
		
		return null;
	}
	
	public UserTaskMenus getNow()
	{
		if(this.utmDao==null)
			return null;
		//this.updateDatas();
		return this.utmDao.findUserTaskMenusByTime(KQTUtils.getNowTime());

	}
	
	private void readDB()
	{
		if(this.utmDao!=null)
		{
			this.taskMeunss = this.utmDao.findAll();
			if(this.taskMeunss!=null)
				Log.i("taskMeunss", taskMeunss.size()+"");
		}
		
	}
	
	private void saveDB(List<UserTaskMenus> list)
	{
		this.utmDao.update(list);
		this.logDao.delete();
	}
	
	public void clearTasks()
	{
		this.utmDao.deleteAll();
	}
}
