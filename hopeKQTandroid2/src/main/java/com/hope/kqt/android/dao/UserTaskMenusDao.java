package com.hope.kqt.android.dao;

import java.util.List;

import com.hope.kqt.entity.UserTaskMenus;

public interface UserTaskMenusDao 
{
	public List<UserTaskMenus> findAll();
	
	public int deleteAll();
	
	public boolean update(List<UserTaskMenus> list);
	
	public UserTaskMenus findUserTaskMenusByTime(long time);
}
