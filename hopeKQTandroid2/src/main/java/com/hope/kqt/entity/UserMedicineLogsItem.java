package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserMedicineLogsItem implements Serializable 
{
	private Medicine medicine;
	private List<UserMLog> logs;
	
	public UserMedicineLogsItem(){}

	public UserMedicineLogsItem(Medicine medicine, List<UserMLog> logs) {
		super();
		this.medicine = medicine;
		this.logs = logs;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public List<UserMLog> getLogs() {
		return logs;
	}

	public void setLogs(List<UserMLog> logs) {
		this.logs = logs;
	}
	
	public static UserMedicineLogsItem valueOf(JSONObject obj) throws JSONException
	{
		UserMedicineLogsItem item = new UserMedicineLogsItem();
		
		item.setMedicine(Medicine.valueOf(obj.getJSONObject("medicine")));
		
		if(!obj.isNull("logs"))
		{
			JSONArray array = obj.getJSONArray("logs");
			if(array.length()>0)
			{
				List<UserMLog> logs = new ArrayList<UserMLog>();
				for(int i=0;i<array.length();i++)
					logs.add(UserMLog.valueOf(array.getJSONObject(i)));
				
				item.setLogs(logs);
			}
		}
		return item;
	}
}
