package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hope.kqt.entity.User;

public class UserStageSummary implements Serializable 
{
	private static final long serialVersionUID = -5328612221959400456L;
	
	private long id;
	private User user;
	private Teacher teacher;
	private int stageNum;
	private String content;
	
	private List<TeacherRating> teacherRatings;
	
	private long taskTime;
	private long createDate;
	
	public UserStageSummary(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(long taskTime) {
		this.taskTime = taskTime;
	}

	public int getStageNum() {
		return stageNum;
	}

	public void setStageNum(int stageNum) {
		this.stageNum = stageNum;
	}

	public List<TeacherRating> getTeacherRatings() {
		return teacherRatings;
	}

	public void setTeacherRatings(List<TeacherRating> teacherRatings) {
		this.teacherRatings = teacherRatings;
	}

	public static UserStageSummary valueOf(JSONObject obj) throws JSONException
	{
		UserStageSummary uss = new UserStageSummary();
		
		uss.setContent(obj.getString("content"));
		uss.setCreateDate(obj.getLong("createDate"));
		uss.setId(obj.getLong("id"));
		
		List<TeacherRating> trs = new ArrayList<TeacherRating>();
		JSONArray array = obj.getJSONArray("teacherRatings");
		for(int i=0;i<array.length();i++)
		{
			TeacherRating tr = TeacherRating.valueOf(array.getJSONObject(i));
			trs.add(tr);
		}
		
		uss.setTeacherRatings(trs);
		uss.setTaskTime(obj.getLong("taskTime"));
		uss.setStageNum(obj.getInt("stageNum"));
		JSONObject t;
		try {
			t = obj.getJSONObject("teacher");
			uss.setTeacher(Teacher.valueOf(t));
		} catch (Exception e) {
		}

		return uss;
	}
	
	private String score;

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
}
