package com.hope.kqt.android.dao;

import java.util.List;

import com.hope.kqt.entity.HelpMessage;

public interface HelpMessageDao 
{
	public List<HelpMessage> findAll();
	
	public void deleteAll();
	
	public boolean update(List<HelpMessage> list);
}
