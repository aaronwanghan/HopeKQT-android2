package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestItem implements Serializable 
{
	public static final String JSON_NAME_ID = "id";
	public static final String JSON_NAME_NAME = "name";
	public static final String JSON_NAME_UNIT = "unit";
	public static final String JSON_NAME_CHILDREN = "children";
	
	private long id;
	private String name;
	private String unit;
	
	private List<TestItem> children;
	
	public TestItem(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public List<TestItem> getChildren() {
		return children;
	}

	public void setChildren(List<TestItem> children) {
		this.children = children;
	}

	public void in(JSONObject jo) throws JSONException
	{
		this.id = jo.getLong(JSON_NAME_ID);
		this.name = jo.getString(JSON_NAME_NAME);
		this.unit = jo.getString(JSON_NAME_UNIT);
		

		if (!jo.isNull(JSON_NAME_CHILDREN)) 
		{
			JSONArray array = jo.getJSONArray(JSON_NAME_CHILDREN);
			if (array.length() > 0) 
			{
				this.children = new ArrayList<TestItem>();
				for (int i = 0; i < array.length(); i++) 
				{
					TestItem item = new TestItem();
					item.in(array.getJSONObject(i));
					this.children.add(item);
				}
			}
		}

	}
	
	public JSONObject out() throws JSONException
	{
		JSONObject jo = new JSONObject();
		jo.put(JSON_NAME_ID, id);
		jo.put(JSON_NAME_NAME, name);
		jo.put(JSON_NAME_UNIT, unit);
		
		JSONArray array = new JSONArray();
		
		if(this.children!=null && !this.children.isEmpty())
		{
			for(TestItem item:this.children)
			{
				array.put(item.out());
			}
		}
		
		System.out.println(this.name + " "+ (this.children==null? 0:this.children.size()));
		
		jo.put(JSON_NAME_CHILDREN, array);
		
		return jo;
	}
	
	@Override
	public int hashCode() {
		return (new Long(id)).hashCode();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj==null) return false;
		if(obj==this) return true;
		if(obj instanceof TestItem)
		{
			TestItem t = (TestItem)obj;
			
			if(t.getId()==this.getId())
				return true;
		}
		
		return super.equals(obj);
	}
	
	
}
