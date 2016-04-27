package com.hope.kqt.android.util;

import java.util.List;
import java.util.Map;

import com.hope.kqt.android.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class UserTestLogAdapter extends BaseAdapter 
{
	private List<Map<String,Object>> logsValues;
	private Context context;
	
	public UserTestLogAdapter(Context context,List<Map<String,Object>> logsValues)
	{
		super();
		this.logsValues = logsValues;
		this.context = context;
	}
	
	@Override
	public int getCount() 
	{
		return this.logsValues.size();
	}

	@Override
	public Object getItem(int position) {
		return this.logsValues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Map<String,Object> values = this.logsValues.get(position);
		
		Integer type = (Integer) values.get("log_type");
		
		switch(type)
		{
		case 0:
			convertView = LayoutInflater.from(context).inflate(R.layout.utl_item, parent,false);
			setLogItemValue(convertView,values);
			Long time = (Long) values.get("task_time");
			if(time!=null)
				convertView.setBackgroundColor(this.getViewBackgroundColor(time));
			break;
		case 1:
			convertView = LayoutInflater.from(context).inflate(R.layout.utl_time_title, parent,false);
			setTimeTitleItemValue(convertView,values);
			break;
		default:
			break;
		}
		
		return convertView;
	}
	
	private int getViewBackgroundColor(long time)
	{
		int h = Date.valueOf(time).getHour();
		
		if(h<12)
			return Color.parseColor("#FFFF99");
		else if(h>=12 && h<18)
			return Color.parseColor("#FFCC99");
		
		return Color.parseColor("#99CCFF");
	}

	private void setLogItemValue(View convertView,final Map<String,Object> values)
	{
		String name = (String) values.get("test_item_name");
		String unit = (String) values.get("test_item_unit");
		String logvalue = (String) values.get("log_value");
		
		TextView text = (TextView) convertView.findViewById(R.id.utl_name_text);
		EditText edit = (EditText) convertView.findViewById(R.id.utl_value_edit);
		//TextView unitText = (TextView) convertView.findViewById(R.id.utl_unit_text);

		text.setText(name+"["+unit+"]");
		//unitText.setText(unit);
		
		edit.setText(logvalue);
		edit.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) 
			{
				values.put("log_value", s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	private void setTimeTitleItemValue(View convertView,Map<String,Object> values)
	{
		String title = (String) values.get("time_title");
		String color = (String) values.get("color");
		
		TextView text = (TextView) convertView.findViewById(R.id.utl_time_title_text);
		
		if(color!=null && !"".equals(color))
			convertView.setBackgroundColor(Color.parseColor(color));
		
		text.setText(title);
	}
}
