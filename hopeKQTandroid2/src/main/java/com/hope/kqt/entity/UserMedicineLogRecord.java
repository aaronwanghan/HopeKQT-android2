package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserMedicineLogRecord implements Serializable
{
	private static final long serialVersionUID = -6616373057306487749L;

	private long id;
	
	private long taskTime;
	private long createDate;
	private User user;			//用户
	private Medicine medicine;	//药物
	
	private double num;

	public UserMedicineLogRecord(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(long taskTime) {
		this.taskTime = taskTime;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public static UserMedicineLogRecord valueOf(JSONObject obj) throws JSONException
	{
		UserMedicineLogRecord l = new UserMedicineLogRecord();
		
		l.setCreateDate(obj.getLong("createDate"));
		l.setId(obj.getLong("id"));
		l.setMedicine(Medicine.valueOf(obj.getJSONObject("medicine")));
		l.setNum(obj.getDouble("num"));
		l.setTaskTime(obj.getLong("taskTime"));
		
		return l;
	}

	@Override
	public boolean equals(Object o) 
	{
		
		
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	
}
