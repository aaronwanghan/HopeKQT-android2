package com.hope.kqt.android.util;

import java.util.List;

import com.hope.kqt.android.R;
import com.hope.kqt.entity.UserData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserDataAdapter extends BaseAdapter
{
	//private List<Map<String,Object>> udValues;
	private List<UserData> logs;
	private Context context;
	
	public UserDataAdapter(List<UserData> logs, Context context) {
		super();
		this.logs = logs;
		this.context = context;
	}

	@Override
	public int getCount() {
		
		if(this.logs!=null)
			return this.logs.size();
		
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(this.logs!=null)
			return this.logs.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.user_data_chart_item, parent, false);
		
		UserData data = this.logs.get(position);
		
		//BigDecimal bd = new BigDecimal(data.getProgress());
		
		TextView timetext = (TextView) convertView.findViewById(R.id.udc_item_text01);
		timetext.setText(Date.valueOf(data.getTime()).toDate());
		
		TextView valuetext = (TextView) convertView.findViewById(R.id.udc_item_text02);
		valuetext.setText(data.getValue()+"");
		
		TextView ptext = (TextView) convertView.findViewById(R.id.udc_item_text03);
		
		ptext.setText((data.getProgress()>0? "+":"")+ KQTUtils.round(data.getProgress(), 1));
		//ptext.setText((progress>0? "+":"")+progress);
		
		return convertView;
	}

	public void setLogs(List<UserData> logs) {
		this.logs = logs;
	}

}
