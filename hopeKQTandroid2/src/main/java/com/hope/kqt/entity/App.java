package com.hope.kqt.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class App implements Serializable 
{
	private long id;
	private int version;
	private String versionString;
	private String url;
	private long createDate;
	
	public App(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getVersionString() {
		return versionString;
	}

	public void setVersionString(String versionString) {
		this.versionString = versionString;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	public static App valueOf(JSONObject obj) throws JSONException
	{
		App app = new App();
		
		app.setId(obj.getLong("id"));
		app.setCreateDate(obj.getLong("createDate"));
		app.setUrl(obj.getString("url"));
		app.setVersion(obj.getInt("version"));
		app.setVersionString(obj.getString("versionString"));
		
		return app;
	}
}
