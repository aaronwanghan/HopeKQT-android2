package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherRatingItem implements Serializable
{
	private long id;
	private String name;
	
	public TeacherRatingItem(){}

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
	
	public static TeacherRatingItem valueOf(JSONObject obj) throws JSONException
	{
		TeacherRatingItem tri = new TeacherRatingItem();
		
		tri.setId(obj.getLong("id"));
		tri.setName(obj.getString("name"));
		
		return tri;
	}
}
