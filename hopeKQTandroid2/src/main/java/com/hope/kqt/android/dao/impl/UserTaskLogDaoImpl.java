package com.hope.kqt.android.dao.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hope.kqt.android.dao.UserTaskLogDao;
import com.hope.kqt.entity.UserTaskLog;

public class UserTaskLogDaoImpl implements UserTaskLogDao 
{
	public static final String CREATE_SQL = "CREATE TABLE _utlog"
			+ "( _id INTEGER PRIMARY KEY NOT NULL,"
			+ " _tid INTEGER NOT NULL,"
			+ "_type INTEGER NOT NULL )";
	
	private SQLiteDatabase db;
	
	public UserTaskLogDaoImpl(SQLiteDatabase db) {
		super();
		this.db = db;
	}

	@Override
	public void add(UserTaskLog log) 
	{
		ContentValues cv = new ContentValues();
		cv.put("_tid", log.getTid());
		cv.put("_type", log.getType());
		
		this.db.insert("_utlog", null, cv);
	}

	@Override
	public void delete() {
		this.db.delete("_utlog", null, null);
	}

	@Override
	public UserTaskLog find(long tid, int type) 
	{
		Log.i("UserTaskLog", "find");
		UserTaskLog log = null;
		Cursor c = this.db.rawQuery("SELECT *FROM _utlog WHERE _tid=? AND _type=?", new String[]{
				tid+"",type+""
		});
		
		if(c.moveToFirst())
		{
			Log.i("UserTaskLog", "not null :"+tid);
			log = new UserTaskLog();
			log.setId(c.getLong(c.getColumnIndex("_id")));
			log.setTid(c.getLong(c.getColumnIndex("_tid")));
			log.setType(c.getInt(c.getColumnIndex("_type")));
		}
		
		c.close();
		
		return log;
	}

}
