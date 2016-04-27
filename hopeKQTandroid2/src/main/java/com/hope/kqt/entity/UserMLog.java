package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.hope.kqt.entity.User;

public class UserMLog implements Serializable
{
	private long taskTime;
	private User user;			//用户
	private Disease disease;	//所治疾病
	private Medicine medicine;	//药物
	
	private double num;
	
	public UserMLog(){}

	public UserMLog(long taskTime, User user, Disease disease,
			Medicine medicine, Object num) {
		super();
		this.taskTime = taskTime;
		this.user = user;
		this.disease = disease;
		this.medicine = medicine;
		this.num = (Long)num;
	}

	public long getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(long taskTime) {
		this.taskTime = taskTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public double getNum() {
		return num;
	}

	public void setNum(double num) {
		this.num = num;
	}
	
	public static UserMLog valueOf(JSONObject obj) throws JSONException
	{
		UserMLog log = new UserMLog();
		
		//log.setDisease(Disease.valueOf(obj.getJSONObject("disease")));
		log.setMedicine(Medicine.valueOf(obj.getJSONObject("medicine")));
		log.setNum(obj.getDouble("num"));
		log.setTaskTime(obj.getLong("taskTime"));
		
		return log;
	}
}
