package com.hope.kqt.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserImg 
{
	private long time;
	private String name;
	private String fileName;
	
	public UserImg() {
		super();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static UserImg valueOf(JSONObject obj) throws JSONException
	{
		if(obj==null)
			return null;
		
		UserImg ui = new UserImg();
		ui.setFileName(obj.getString("fileName"));
		ui.setName(obj.getString("title"));
		ui.setTime(obj.getLong("createDate"));
		
		return ui;
	}
}
