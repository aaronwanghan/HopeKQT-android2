package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserLogData implements Serializable 
{
	private List<UserTestLog> uTLogs;
	private List<UserMedicineLogRecord> uMLRecords;
	private UserLog uLog;
	private UserStageSummary uSSummary;
	
	public UserLogData(){}

	public List<UserTestLog> getUTLogs() {
		return uTLogs;
	}

	public void setUTLogs(List<UserTestLog> uTLogs) {
		this.uTLogs = uTLogs;
	}

	public List<UserMedicineLogRecord> getuMLRecords() {
		return uMLRecords;
	}

	public void setUMLRecords(List<UserMedicineLogRecord> uMLRecords) {
		this.uMLRecords = uMLRecords;
	}

	public UserLog getULog() {
		return uLog;
	}

	public void setULog(UserLog uLog) {
		this.uLog = uLog;
	}

	public UserStageSummary getUSSummary() {
		return uSSummary;
	}

	public void setUSSummary(UserStageSummary uSSummary) {
		this.uSSummary = uSSummary;
	}
	
	public static UserLogData valueOf(JSONObject obj) throws JSONException
	{
		UserLogData data = new UserLogData();
		
		data.setUTLogs(obj);
		data.setULog(obj);
		data.setUMLRecords(obj);
		data.setUSSummary(obj);
		
		return data;
	}
	
	private void setUTLogs(JSONObject obj) throws JSONException
	{
		if(obj.isNull("uTLogs"))
			return;
		
		JSONArray array = obj.getJSONArray("uTLogs");
		
		if(array==null || array.length()==0)
			return;
		
		this.uTLogs = new ArrayList<UserTestLog>();
		for(int i=0;i<array.length();i++)
		{
			UserTestLog utl = new UserTestLog();
			utl.in(array.getJSONObject(i));
			
			this.uTLogs.add(utl);
		}
	}
	
	private void setUMLRecords(JSONObject obj) throws JSONException
	{
		if(obj.isNull("uMLRecords"))
			return;
		
		JSONArray array = obj.getJSONArray("uMLRecords");
		
		if(array==null || array.length()==0)
			return;
		
		this.uMLRecords = new ArrayList<UserMedicineLogRecord>();
		for(int i=0;i<array.length();i++)
		{
			this.uMLRecords.add(UserMedicineLogRecord.valueOf(array.getJSONObject(i)));
		}
	}
	
	private void setULog(JSONObject obj) throws JSONException
	{
		if(obj.isNull("uLog"))
			return;
		
		this.uLog = UserLog.valueOf(obj.getJSONObject("uLog"));
	}
	
	private void setUSSummary(JSONObject obj) throws JSONException
	{
		if(obj.isNull("uSSummary"))
			return;
		
		this.uSSummary = UserStageSummary.valueOf(obj.getJSONObject("uSSummary"));
	}
}
