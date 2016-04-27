package com.hope.kqt.entity;

import java.util.List;

public class UserDatasItem 
{
	private String name;
	private String unit;
	
	private List<UserData> datas;
	
	public UserDatasItem(){}

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

	public List<UserData> getDatas() {
		return datas;
	}

	public void setDatas(List<UserData> datas) {
		this.datas = datas;
	}
	
	
}
