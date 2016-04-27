package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.hope.kqt.entity.User;

public class UserLog implements Serializable 
{
	private long id;
	private User user;

	/**
	 * 1 康复反应
	 */
	private int type;
	
	private long createDate;
	
	private String content;
	
	public UserLog(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public static UserLog valueOf(JSONObject obj) throws JSONException
	{
		UserLog log = new UserLog();
		
		log.setContent(obj.getString("content"));
		log.setCreateDate(obj.getLong("createDate"));
		log.setId(obj.getLong("id"));
		log.setType(obj.getInt("type"));
		
		return log;
	}
}
