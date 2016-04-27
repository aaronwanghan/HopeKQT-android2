package com.hope.kqt.android.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hope.kqt.android.R;
import com.hope.kqt.entity.HelpMessage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HelpMessageAdapter extends BaseAdapter 
{
	private Context context;
	private List<Map<String,Object>> values;
	
	public HelpMessageAdapter(Context context,List<HelpMessage> messages) 
	{
		super();
		this.context = context;
		this.setValues(messages);
	}
	
	public void setValues(List<HelpMessage> messages)
	{
		if(this.values==null)
			this.values = new ArrayList<Map<String,Object>>();
		else
			this.values.clear();
		
		if(messages==null || messages.isEmpty())
			return;
		
		for(HelpMessage m:messages)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("title", m.getTitle());
			map.put("content", m.getContent());
			
			this.values.add(map);
		}
		
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		return this.values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.help_message_item, parent, false);
		
		Map<String,Object> value = this.values.get(position);
		
		TextView title = (TextView) convertView.findViewById(R.id.hm_item_title);
		WebView content = (WebView) convertView.findViewById(R.id.hm_item_content);
		
		title.setText((String)value.get("title"));
		Log.i("html", (String)value.get("content"));
		content.loadDataWithBaseURL(null,(String)value.get("content"), "text/html", "UTF-8", null);
		
		return convertView;
	}

}
