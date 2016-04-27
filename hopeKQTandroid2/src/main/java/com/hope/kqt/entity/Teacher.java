package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Teacher implements Serializable 
{
	private static final long serialVersionUID = 5964050822982175649L;
	private long id;
	private String name;
	
	private Company company;
	private String img;
	
	private int score;
	private int allNumber;
	private int successNumber;
	
	private int type;
	private String phone;
	
	public Teacher(){}

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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getAllNumber() {
		return allNumber;
	}

	public void setAllNumber(int allNumber) {
		this.allNumber = allNumber;
	}

	public int getSuccessNumber() {
		return successNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setSuccessNumber(int successNumber) {
		this.successNumber = successNumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public JSONObject out() throws JSONException
	{
		JSONObject obj = new JSONObject();
		
		obj.put("id", this.id);
		obj.put("img", this.img);
		obj.put("name", this.name);
		obj.put("score", this.score);
		obj.put("successNumber", this.successNumber);
		obj.put("allNumber", this.allNumber);
		obj.put("type", this.type);
		obj.put("phone", this.phone);
		obj.put("company", (this.company==null? null:this.company.out()));
		return obj;
	}
	
	public static Teacher valueOf(JSONObject obj) throws JSONException
	{
		Teacher teacher = new Teacher();
		
		teacher.setAllNumber(obj.getInt("allNumber"));
		
		try {
			JSONObject c = obj.getJSONObject("company");
			if(c!=null && c.length()>0)
				teacher.setCompany(Company.valueOf(c));
		} catch (Exception e) {
		}
		
		teacher.setId(obj.getLong("id"));
		teacher.setImg(obj.getString("img"));
		teacher.setName(obj.getString("name"));
		teacher.setScore(obj.getInt("score"));
		teacher.setSuccessNumber(obj.getInt("successNumber"));
		teacher.setType(obj.getInt("type"));
		teacher.setPhone(obj.getString("phone"));
		
		return teacher;
	}
}
