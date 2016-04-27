package com.hope.kqt.entity;

import java.io.Serializable;

public class UserData implements Serializable 
{
	private long time;
	private double value;
	
	private double progress;
	
	public UserData(){}
	
	public UserData(long time, double value) {
		super();
		this.time = time;
		this.value = value;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	
}
