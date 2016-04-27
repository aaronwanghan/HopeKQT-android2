package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserTask implements Serializable 
{
	private long id;
	
	private long startTime;
	private long endTime;
	
	private long callTime;
	
	private String title;
	private String content;
	
	/**
	 * 0 提示
	 * 1用药记录
	 * 2电话
	 */
	private int type;
	
	private List<TestItem> testItems;
	
	public UserTask(){}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getCallTime() {
		return callTime;
	}

	public void setCallTime(long callTime) {
		this.callTime = callTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public List<TestItem> getTestItems() {
		return testItems;
	}

	public void setTestItems(List<TestItem> testItems) {
		this.testItems = testItems;
	}

	public static UserTask valueOf(JSONObject jo) throws JSONException
	{
		if(jo==null)
			return null;
		UserTask ut = new UserTask();
		
		ut.setId(jo.getLong("id"));
		ut.setCallTime(jo.getLong("callTime"));
		ut.setContent(jo.getString("content"));
		ut.setEndTime(jo.getLong("endTime"));
		ut.setStartTime(jo.getLong("startTime"));
		ut.setTitle(jo.getString("title"));
		ut.setType(jo.getInt("type"));
		
		if(ut.getType()==5 && !jo.isNull("testItems"))
		{
			JSONArray array = jo.getJSONArray("testItems");
			if(array!=null &&array.length()>0)
			{
				List<TestItem> testItems = new ArrayList<TestItem>();
				for(int i=0;i<array.length();i++)
				{
					TestItem item = new TestItem();
					item.in(array.getJSONObject(i));
					testItems.add(item);
				}
				
				ut.setTestItems(testItems);
			}
		}
		
		return ut;
	}
	
	public JSONObject getJSONObject() throws JSONException
	{
		JSONObject obj = new JSONObject();
		
		obj.put("callTime", this.callTime);
		obj.put("content", this.content);
		obj.put("endTime", this.endTime);
		obj.put("startTime", this.startTime);
		obj.put("title", this.title);
		obj.put("type", this.type);
		obj.put("id", this.id);
		
		JSONArray array = new JSONArray();
		if(this.testItems!=null && !this.testItems.isEmpty())
			for(TestItem item:this.testItems)
				array.put(item.out());
		
		obj.put("testItems", array);
		return obj;
	}
}
