package com.hope.kqt.android.util;

import java.util.List;
import java.util.Map;

import com.hope.kqt.android.R;
import com.hope.kqt.entity.UserLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserLogAdapter extends BaseAdapter 
{
	private List<Map<String,Object>> values;
	private Context context;
	
	
	
	public UserLogAdapter(List<Map<String, Object>> values, Context context) {
		super();
		this.values = values;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		return values.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)  
	{
		if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.user_log_item, parent,false);
		
		UserLog log = (UserLog) this.values.get(position).get("value");
		
		TextView title = (TextView) convertView.findViewById(R.id.ul_title_text);
		TextView content = (TextView) convertView.findViewById(R.id.ul_content_text);
		
		title.setText(Date.valueOf(log.getCreateDate()).toDate());
		content.setText(log.getContent());
		
		return convertView;
	}

}
