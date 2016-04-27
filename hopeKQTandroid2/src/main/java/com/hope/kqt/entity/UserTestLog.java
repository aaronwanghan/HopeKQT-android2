package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.hope.kqt.entity.User;


public class UserTestLog implements Serializable 
{
	private static final String JSON_NAME_USER_ID = "user_id";
	private static final String JSON_NAME_TEST_ITEM_ID = "test_item_id";
	private static final String JSON_NAME_VALUE = "value";
	private static final String JSON_NAME_TASK_TIME = "time";
	
	private long id;
	private long createDate;
	
	private long taskTime;
	
	private User user;
	private TestItem item;
	
	private String value;
	
	public UserTestLog(){}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TestItem getItem() {
		return item;
	}

	public void setItem(TestItem item) {
		this.item = item;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
		obj.put(JSON_NAME_TASK_TIME, this.taskTime);
		obj.put(JSON_NAME_VALUE, this.value);
		obj.put(JSON_NAME_USER_ID, this.user.getId());
		obj.put(JSON_NAME_TEST_ITEM_ID, this.item.getId());
		
		return obj;
	}
	
	public void in(JSONObject obj) throws JSONException
	{
		this.taskTime = obj.getLong("taskTime");
		this.createDate = obj.getLong("createDate");
		this.id = obj.getLong("id");
		this.value = obj.getString("value");
		
		this.item = new TestItem();
		this.item.in(obj.getJSONObject("item"));
	}
	
	public static UserTestLog valueOf(JSONObject obj) throws JSONException
	{
		if(obj==null)
			return null;
		
		UserTestLog log = new UserTestLog();
		
		log.setId(obj.getLong("id"));
		log.setCreateDate(obj.getLong("createDate"));
		log.setTaskTime(obj.getLong("taskTime"));
		log.setValue(obj.getString("value"));
		
		TestItem item = new TestItem();
		item.in(obj.getJSONObject("item"));
		
		log.setItem(item);
		
		return log;
	}
}
