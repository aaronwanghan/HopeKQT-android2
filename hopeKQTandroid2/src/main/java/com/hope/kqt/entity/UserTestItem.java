package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserTestItem implements Serializable 
{
	private String name;
	private String unit;
	private long id;
	
	private List<UserTestLog> logs;
	
	private List<UserTestItem> children;

	private List<UserMedicineLogItem> userMedicineLogItems;

	public UserTestItem(){}

	public UserTestItem(TestItem item,List<UserTestLog> logs)
	{
		this.name = item.getName();
		this.unit = item.getUnit();
		this.id = item.getId();
		this.logs = logs;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<UserTestLog> getLogs() {
		return logs;
	}

	public void setLogs(List<UserTestLog> logs) {
		this.logs = logs;
	}

	public List<UserTestItem> getChildren() {
		return children;
	}

	public void setChildren(List<UserTestItem> children) {
		this.children = children;
	}

	public List<UserMedicineLogItem> getUserMedicineLogItems() {
		return userMedicineLogItems;
	}

	public void setUserMedicineLogItems(List<UserMedicineLogItem> userMedicineLogItems) {
		this.userMedicineLogItems = userMedicineLogItems;
	}

	public static UserTestItem valueOf(JSONObject obj) throws JSONException
	{
		if(obj==null)
			return null;
		
		UserTestItem item = new UserTestItem();
		item.setName(obj.getString("name"));
		item.setUnit(obj.getString("unit"));
		item.setId(obj.getLong("testItemId"));
		
		if (!obj.isNull("logs"))
		{
			JSONArray array = obj.getJSONArray("logs");
			List<UserTestLog> logs = new ArrayList<UserTestLog>();
			item.setLogs(logs);
			
			if(array!=null && array.length()>0)
				for(int i=0;i<array.length();i++)
					logs.add(UserTestLog.valueOf(array.getJSONObject(i)));
		}
		
		if (!obj.isNull("children")) 
		{
			JSONArray carray = obj.getJSONArray("children");
			if (carray != null && carray.length() > 0) 
			{
				List<UserTestItem> citemlist = new ArrayList<UserTestItem>();
				for (int i = 0; i < carray.length(); i++)
					citemlist.add(UserTestItem.valueOf(carray.getJSONObject(i)));

				item.setChildren(citemlist);
			}
		}

		if(!obj.isNull("medicineLogItems"))
		{
			JSONArray array = obj.getJSONArray("medicineLogItems");
			if(array!=null && array.length()>0)
			{
				List<UserMedicineLogItem> items = new ArrayList<UserMedicineLogItem>();
				for(int i = 0;i<array.length();i++)
					items.add(UserMedicineLogItem.valueOf(array.getJSONObject(i)));

				item.setUserMedicineLogItems(items);
			}
		}

		return item;
	}
}
