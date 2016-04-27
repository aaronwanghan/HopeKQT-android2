package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Disease implements Serializable 
{
	private long id;
	private String name;
	private long createDate;
	
	public Disease(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public JSONObject out() throws JSONException
	{
		JSONObject obj = new JSONObject();
		
		obj.put("createDate", this.createDate);
		obj.put("id", this.id);
		obj.put("name", name);
		
		return obj;
	}
	
	public static Disease valueOf(JSONObject obj) throws JSONException
	{
		Disease d = new Disease();
		
		d.setCreateDate(obj.getLong("createDate"));
		d.setId(obj.getLong("id"));
		d.setName(obj.getString("name"));
		
		return d;
		
	}
}
