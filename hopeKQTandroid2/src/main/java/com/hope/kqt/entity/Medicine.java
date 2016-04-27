package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 药品
 * @author aaronwang
 *
 */

public class Medicine implements Serializable 
{
	private static final long serialVersionUID = -8289642809405194862L;
	
	private long id;
	private long createDate;
	private String name;		//名称
	
	private String unit;		//药量
	
	public Medicine(){}

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

	@Override
	public boolean equals(Object obj) 
	{
		if(obj==null) return false;
		if(obj==this) return true;
		if(obj instanceof Medicine)
		{
			Medicine m = (Medicine)obj;
			if(m.getId()==this.getId())
				return true;
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() 
	{
		Long l = new Long(this.getId());
		return l.hashCode();
	}
	
	public static Medicine valueOf(JSONObject jo) throws JSONException
	{
		Medicine medicine = new Medicine();
		
		medicine.setCreateDate(jo.getLong("createDate"));
		medicine.setId(jo.getLong("id"));
		medicine.setName(jo.getString("name"));
		medicine.setUnit(jo.getString("unit"));
		return medicine;
	}
}
