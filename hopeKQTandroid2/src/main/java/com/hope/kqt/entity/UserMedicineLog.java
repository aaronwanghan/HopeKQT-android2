package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户用药记录
 * @author aaronwang
 *
 */
public class UserMedicineLog implements Serializable
{
	private static final long serialVersionUID = -2713924211619958258L;

	private long id;
	
	private long createDate;
	private User user;			//用户
	private Disease disease;	//所治疾病
	private Medicine medicine;	//药物
	
	/**
	 * 0 正在服用的药物
	 * 1停服的药物
	 */
	private int type;
	
	public UserMedicineLog(){}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}
	
	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public static UserMedicineLog valueOf(JSONObject obj) throws JSONException
	{
		UserMedicineLog l = new UserMedicineLog();
		
		l.setCreateDate(obj.getLong("createDate"));
		l.setId(obj.getLong("id"));
		l.setDisease(Disease.valueOf(obj.getJSONObject("disease")));
		l.setMedicine(Medicine.valueOf(obj.getJSONObject("medicine")));
		l.setType(obj.getInt("type"));
		
		return l;
	}
}
