package com.hope.kqt.android.dao;

public interface SystemValueDao 
{
	public void set(String key,String value);
	
	public String load(String key);
}
