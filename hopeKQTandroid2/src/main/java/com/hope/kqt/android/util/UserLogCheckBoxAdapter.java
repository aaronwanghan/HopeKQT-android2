package com.hope.kqt.android.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class UserLogCheckBoxAdapter extends BaseAdapter 
{
	private List<Map<String,Object>> ulValues;
	private Context context;
	
	public UserLogCheckBoxAdapter(Context context,String[] eitems)
	{
		this.context = context;
		
		this.ulValues = new ArrayList<Map<String,Object>>();
		for(String e:eitems)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", e);
			map.put("value", "");
			
			this.ulValues.add(map);
		}
	}
	
	public String getRet()
	{
		StringBuffer sb = new StringBuffer();
		
		for(Map<String,Object> values:this.ulValues)
			if(!"".equals(values.get("value")))
				sb.append((String)values.get("value")).append(",");
		
		return sb.toString();
	}
	
	public String setValues(String s)
	{
		StringBuffer content = new StringBuffer(s);
		
		for(Map<String,Object> map:this.ulValues)
		{
			String name = (String) map.get("name");
			int start = content.indexOf(name+",");
			if(start>-1)
			{
				map.put("value", name);
				content.delete(start, start+name.length()+1);
			}
			
		}
		this.notifyDataSetChanged();
		
		return content.toString();
	}
	
	@Override
	public int getCount() {
		return this.ulValues.size();
	}

	@Override
	public Object getItem(int position) {
		return this.ulValues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
			convertView = new CheckBox(context);
		
		CheckBox view = (CheckBox)convertView;
		
		final Map<String,Object> values = this.ulValues.get(position);
		
		view.setText((String)values.get("name"));
		
		if(!"".equals(values.get("value")))
			view.setChecked(true);
		else
			view.setChecked(false);
		
		view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) 
			{
				String s ="";
				
				if(isChecked)
					s = (String) values.get("name");
				
				values.put("value", s);
			}
		});
		return view;
	}

}
