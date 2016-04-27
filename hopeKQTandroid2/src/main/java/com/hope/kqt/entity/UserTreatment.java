package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.List;

import com.hope.kqt.entity.User;

public class UserTreatment implements Serializable
{
	private long id;
	private long createDate;
	
	private User user;
	
	private String content;
	
	private List<UserTaskMenus> taskMenuss;
	
	private int version;
	
	public UserTreatment(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<UserTaskMenus> getTaskMenuss() {
		return taskMenuss;
	}

	public void setTaskMenuss(List<UserTaskMenus> taskMenuss) {
		this.taskMenuss = taskMenuss;
	}
	
}
