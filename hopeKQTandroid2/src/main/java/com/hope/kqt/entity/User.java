package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User implements Serializable 
{
	private long id;
	private String username;
	//private String password;
	
	private long birthday;
	private String address;
	private String telephone;
	private String phone;
	private String email;
	private String name;
	private String idcard;
	private String img;
	
	private Teacher teacher;
	
	private long createDate;
	
	private List<Disease> diseases;
	
	private int version;
	
	public User(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public List<Disease> getDiseases() {
		return diseases;
	}

	public void setDiseases(List<Disease> diseases) {
		this.diseases = diseases;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public JSONObject out() throws JSONException
	{
		JSONObject obj = new JSONObject();
		
		obj.put("id", this.id);
		obj.put("username", this.username);
		obj.put("address", this.address);
		obj.put("birthday", this.birthday);
		obj.put("createDate", this.createDate);
		obj.put("email", this.email);
		obj.put("idcard", this.idcard);
		obj.put("name", this.name);
		obj.put("phone", this.phone);
		obj.put("teacher", (this.teacher==null? null:this.teacher.out()));
		obj.put("version", this.version);
		obj.put("img", this.img);
		
		JSONArray array = new JSONArray();
		
		if(this.diseases!=null && !this.diseases.isEmpty())
		{
			for(Disease d:this.diseases)
			{
				array.put(d.out());
			}
		}
		
		obj.put("diseases", array);
		
		return obj;
	}
	
	public static User valueOf(JSONObject jo) throws JSONException
	{
		User user = new User();
		
		user.setId(jo.getLong("id"));
		user.setUsername(jo.getString("username"));
		//user.setPassword(jo.getString("password"));
		user.setAddress(jo.getString("address"));
		user.setBirthday(jo.getLong("birthday"));
		user.setCreateDate(jo.getLong("createDate"));
		user.setEmail(jo.getString("email"));
		user.setIdcard(jo.getString("idcard"));
		user.setName(jo.getString("name"));
		user.setPhone(jo.getString("phone"));
		user.setVersion(jo.getInt("version"));
		user.setImg(jo.getString("img"));
		
		try {
				user.setTeacher(Teacher.valueOf(jo.getJSONObject("teacher")));
		} catch (Exception e) {
			Log.i("user setTeacher", "Exception:"+e.getMessage());
		}
	
		JSONArray array = jo.getJSONArray("diseases");
		if(array!=null && array.length()>0)
		{
			List<Disease> list = new ArrayList<Disease>();
			for(int i=0;i<array.length();i++)
			{
				list.add(Disease.valueOf(array.getJSONObject(i)));
			}
			
			user.setDiseases(list);
		}
		
		return user;
	}
}
