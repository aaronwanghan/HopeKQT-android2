package com.hope.kqt.android.util;

import com.hope.kqt.android.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlertSettingsAdapter extends BaseAdapter 
{
	private String[] values;
	private Context context;
	
	private int selected;

	public AlertSettingsAdapter(String[] values, Context context, int selected) {
		super();
		this.values = values;
		this.context = context;
		this.selected = selected;
	}

	@Override
	public int getCount() {
		return values.length;
	}

	@Override
	public Object getItem(int position) {
		return values[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.alert_settings_item, parent, false);
		
		TextView nametext = (TextView) convertView.findViewById(R.id.alert_settings_item_name);
		nametext.setText(values[position]);
		
		if(position==selected)
		{
			convertView.setBackgroundColor(Color.BLACK);
			nametext.setTextColor(Color.WHITE);
		}
		else
		{
			convertView.setBackgroundColor(Color.WHITE);
			nametext.setTextColor(Color.BLACK);
		}
		
		return convertView;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

}
