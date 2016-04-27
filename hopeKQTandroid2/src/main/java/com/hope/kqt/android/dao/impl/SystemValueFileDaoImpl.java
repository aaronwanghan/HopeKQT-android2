package com.hope.kqt.android.dao.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

import com.hope.kqt.android.dao.SystemValueDao;

public class SystemValueFileDaoImpl implements SystemValueDao 
{
	private Properties prop = null;
	
	private final String FILE_NAME = "kqt.properties";
	private Context context;
	
	public SystemValueFileDaoImpl(Context context)
	{
		this.context = context;
		if(prop==null)
			setProp();
	}
	
	private void setProp()
	{
		prop = new Properties();
		try {
			FileInputStream in = this.context.openFileInput(FILE_NAME);
			prop.load(in);
			
			in.close();
		} catch (FileNotFoundException e) {
			Log.i("SystemValueFileDaoImpl", "setProp:FileNotFoundException");
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void set(String key, String value)
	{
		if(prop!=null)
			prop.setProperty(key, value);
		save();
	}
	
	private boolean save() 
	{
		try {
			FileOutputStream out = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			prop.store(out, "");
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			Log.i("SystemValueFileDaoImpl", "save:FileNotFoundException");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public String load(String key) 
	{
		setProp();
		if(prop==null)
			return "";
		
		return prop.getProperty(key,"");
	}
}
