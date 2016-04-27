package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserTaskMenus implements Serializable 
{
	private long time;
	
	/**
	 * 0 蛋白日
	 * 1常规日
	 * 2平台期
	 * 3巩固期
	 * 4高尿酸
	 * 5排毒
	 */
	private String name;
	private int stageNum;
	private int num;
	private List<UserTask> tasks;
	
	public UserTaskMenus(){}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<UserTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<UserTask> tasks) {
		this.tasks = tasks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getStageNum() {
		return stageNum;
	}

	public void setStageNum(int stageNum) {
		this.stageNum = stageNum;
	}

	
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public static UserTaskMenus valueOf(JSONObject jo) throws JSONException
	{
		if(jo==null)
			return null;
		
		UserTaskMenus utm = new UserTaskMenus();
		
		utm.setTime(jo.getLong("time"));
		utm.setName(jo.getString("name"));
		utm.setStageNum(jo.getInt("stageNum"));
		utm.setNum(jo.getInt("num"));
		
		JSONArray array = jo.getJSONArray("tasks");
		
		if(array!=null && array.length()>0)
		{
			List<UserTask> tasks = new ArrayList<UserTask>();
			
			for(int i=0;i<array.length();i++)
			{
				UserTask ut = UserTask.valueOf(array.getJSONObject(i));
				if(ut!=null)
					tasks.add(ut);
			}
			
			utm.setTasks(tasks);
		}
		
		return utm;
		
	}
	
	public JSONObject getJSONObject() throws JSONException
	{
		JSONObject obj = new JSONObject();
		
		obj.put("time", this.time);
		obj.put("name", this.name);
		obj.put("stageNum", this.stageNum);
		obj.put("num", this.num);
		
		JSONArray array = new JSONArray();
		
		if(this.tasks!=null && !this.tasks.isEmpty())
			for(UserTask ut:this.tasks)
				array.put(ut.getJSONObject());
		
		obj.put("tasks", array);
		return obj;
	}
}
