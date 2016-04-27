package com.hope.kqt.android.dao;

import com.hope.kqt.android.Application;
import com.hope.kqt.android.dao.impl.HelpMessageDaoImpl;
import com.hope.kqt.android.dao.impl.UserTaskLogDaoImpl;
import com.hope.kqt.android.dao.impl.UserTaskMenusDaoImpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context, String name, CursorFactory factory) {
		super(context, name, factory, Application.SQL_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(UserTaskMenusDaoImpl.CREATE_SQL);
		db.execSQL(UserTaskLogDaoImpl.CREATE_SQL);
		db.execSQL(HelpMessageDaoImpl.CREATE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL(HelpMessageDaoImpl.CREATE_SQL);
	}

}
