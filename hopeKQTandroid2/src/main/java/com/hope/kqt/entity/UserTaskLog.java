package com.hope.kqt.entity;

import java.io.Serializable;

public class UserTaskLog implements Serializable 
{
	private long id;
	private long tid;
	private int type;
	
	public UserTaskLog(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTid() {
		return tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
