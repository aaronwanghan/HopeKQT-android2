package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherRating implements Serializable 
{
	private long id;
	private TeacherRatingItem item;
	private Integer score;
	
	public TeacherRating(){}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TeacherRatingItem getItem() {
		return item;
	}

	public void setItem(TeacherRatingItem item) {
		this.item = item;
	}

	public static TeacherRating valueOf(JSONObject obj) throws JSONException
	{
		TeacherRating tr = new TeacherRating();
		
		tr.setId(obj.getLong("id"));
		tr.setItem(TeacherRatingItem.valueOf(obj.getJSONObject("item")));
		tr.setScore(obj.getInt("score"));
		
		return tr;
	}
}
