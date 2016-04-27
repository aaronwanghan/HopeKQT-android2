package com.hope.kqt.entity;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class HelpMessage implements Serializable 
{
	private long id;
	private String title;
	private String content;
	
	public HelpMessage(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj==null) return false;
		if(obj==this) return true;
		if(obj instanceof Disease)
		{
			HelpMessage d = (HelpMessage)obj;
			if(d.getId()==this.getId())
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
	
	public static HelpMessage valueOf(JSONObject obj) throws JSONException
	{
		HelpMessage message = new HelpMessage();
		
		message.setId(obj.getLong("id"));
		message.setTitle(obj.getString("title"));
		message.setContent(obj.getString("content"));
		
		return message;
	}
}
