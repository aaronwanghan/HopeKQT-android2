package com.hope.kqt.android.util;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hope.kqt.android.R;

public class UserDataItemAdapter extends BaseAdapter 
{
	private List<Map<String,Object>> udValues;
	private Context context;
	
	public UserDataItemAdapter(List<Map<String, Object>> udValues,
			Context context) {
		super();
		this.udValues = udValues;
		this.context = context;
	}

	@Override
	public int getCount() {
		
		return this.udValues.size();
	}

	@Override
	public Object getItem(int position) {
		return this.udValues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.user_data_item2, parent,false);
		
		final Map<String,Object> values = this.udValues.get(position);
		
		String name = (String) values.get("user_data_name");
		
		TextView text = (TextView) convertView.findViewById(R.id.user_data_item_name_text);
		
		text.setText(name);

		Animation anim = new TranslateAnimation(0,0,200*(int)(Math.random()*100%10),0);
		anim.setDuration(1000);
		convertView.startAnimation(anim);

		return convertView;
	}

}
