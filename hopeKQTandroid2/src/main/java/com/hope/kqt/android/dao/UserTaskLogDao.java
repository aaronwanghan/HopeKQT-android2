package com.hope.kqt.android.dao;

import com.hope.kqt.entity.UserTaskLog;

public interface UserTaskLogDao 
{
	public void add(UserTaskLog log);
	
	public void delete();
	
	public UserTaskLog find(long tid,int type);
}
