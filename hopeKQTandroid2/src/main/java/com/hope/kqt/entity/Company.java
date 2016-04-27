package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Company implements Serializable 
{
	private long id;
	private String name;
	
	public Company(){}

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
	
	public JSONObject out() throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put("id", this.id);
		obj.put("name", this.name);
		
		return obj;
	}
	
	public static Company valueOf(JSONObject obj) throws JSONException
	{
		Company company = new Company();
		
		company.setId(obj.getLong("id"));
		company.setName(obj.getString("name"));
		
		return company;
	}
}
