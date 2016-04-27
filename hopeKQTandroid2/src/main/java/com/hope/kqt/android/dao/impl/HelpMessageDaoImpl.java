package com.hope.kqt.android.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hope.kqt.android.dao.HelpMessageDao;
import com.hope.kqt.entity.HelpMessage;

public class HelpMessageDaoImpl implements HelpMessageDao 
{
	public static final String CREATE_SQL = "CREATE TABLE help_message"
			+"( _id INTEGER NOT NULL,"
			+" _title VARCHAR,"
			+ " _content VARCHAR)";
	
	private SQLiteDatabase db;
	
	public HelpMessageDaoImpl(SQLiteDatabase db) {
		super();
		this.db = db;
	}

	@Override
	public List<HelpMessage> findAll() 
	{
		List<HelpMessage> list = new ArrayList<HelpMessage>();
		
		Cursor c = this.db.rawQuery("SELECT *FROM help_message ", null);
		
		while(c.moveToNext())
		{
			HelpMessage m = new HelpMessage();
			m.setId(c.getLong(c.getColumnIndex("_id")));
			m.setTitle(c.getString(c.getColumnIndex("_title")));
			m.setContent(c.getString(c.getColumnIndex("_content")));
			
			list.add(m);
		}
		
		c.close();
		
		return list;
	}

	@Override
	public void deleteAll() {
		this.db.delete("help_message", null, null);
	}

	@Override
	public boolean update(List<HelpMessage> list) 
	{
		if(list==null || list.isEmpty())
			return true;
		
		this.deleteAll();
		for(HelpMessage m:list)
		{
			ContentValues cv = new ContentValues();
			cv.put("_id", m.getId());
			cv.put("_title", m.getTitle());
			cv.put("_content", m.getContent());
			
			try {
				this.db.insert("help_message", null, cv);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

}
