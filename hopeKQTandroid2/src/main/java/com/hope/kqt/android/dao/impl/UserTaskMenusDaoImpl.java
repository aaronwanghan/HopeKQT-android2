package com.hope.kqt.android.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hope.kqt.android.dao.UserTaskMenusDao;
import com.hope.kqt.entity.UserTaskMenus;

public class UserTaskMenusDaoImpl implements UserTaskMenusDao 
{
	public static final String CREATE_SQL =
			"CREATE TABLE user_task_menus ( _time INTEGER PRIMARY KEY NOT NULL,content VARCHAR )";
	
	private SQLiteDatabase db;
	
	public UserTaskMenusDaoImpl(SQLiteDatabase db) {
		super();
		this.db = db;
	}

	@Override
	public List<UserTaskMenus> findAll() 
	{
		List<UserTaskMenus> list = new ArrayList<UserTaskMenus>();
		
		Cursor c = this.db.rawQuery("SELECT *FROM user_task_menus ", null);
		
		while(c.moveToNext())
		{
			Log.i("findAll UserTaskMenus", "time="+c.getString(c.getColumnIndex("_time")));
			String content = c.getString(c.getColumnIndex("content"));
			if(content!=null && !"".equals(content))
				try {
					list.add(UserTaskMenus.valueOf(new JSONObject(content)));
				} catch (JSONException e) {
				}
		}
		
		c.close();
		
		return list;
	}

	@Override
	public int deleteAll() 
	{
		return this.db.delete("user_task_menus", null, null);
	}

	@Override
	public boolean update(List<UserTaskMenus> list) 
	{
		if(list==null || list.isEmpty())
			return true;
		
		int num = this.deleteAll();
		Log.i("delete UserTaskMenus", "num:"+num);
		
		for(UserTaskMenus utm:list)
		{
			Log.i("save UserTaskMenus", "_time="+utm.getTime());
			
			ContentValues cv = new ContentValues();
			cv.put("_time", utm.getTime());
			try {
				cv.put("content", utm.getJSONObject().toString());
				
			this.db.insert("user_task_menus", null, cv);
			} catch (JSONException e) {
				Log.w("save UserTaskMenus", "_time="+utm.getTime());
				return false;
			}
		}
		
		return true;
	}

	@Override
	public UserTaskMenus findUserTaskMenusByTime(long time) 
	{
		Log.i("findUserTaskMenusByTime", "time:"+time);
		List<UserTaskMenus> list = new ArrayList<UserTaskMenus>();
		
		Cursor c = this.db.rawQuery("SELECT *FROM user_task_menus WHERE _time=?", new String[]{time+""});
		
		while(c.moveToNext())
		{
			Log.i("findUserTaskMenusByTime UserTaskMenus", "time="+c.getString(c.getColumnIndex("_time")));
			String content = c.getString(c.getColumnIndex("content"));
			if(content!=null && !"".equals(content))
				try {
					list.add(UserTaskMenus.valueOf(new JSONObject(content)));
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
		
		c.close();
		
		if(!list.isEmpty())
			return list.get(0);
		
		Log.i("findUserTaskMenusByTime", "0");
		
		return null;
	}

}
