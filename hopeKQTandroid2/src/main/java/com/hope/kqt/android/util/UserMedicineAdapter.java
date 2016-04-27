package com.hope.kqt.android.util;

import java.util.List;
import java.util.Map;

import com.hope.kqt.android.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class UserMedicineAdapter extends BaseAdapter 
{

	private List<Map<String,Object>> mValues;
	private Context context;

	public UserMedicineAdapter(List<Map<String, Object>> mValues,
			Context context) {
		super();
		this.mValues = mValues;
		this.context = context;
	}

	@Override
	public int getCount() 
	{
		return this.mValues.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return this.mValues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.umlr_item, parent, false);
		
		final Map<String,Object> values = this.mValues.get(position);
		
		String name = (String) values.get("m_item_name");
		String unit = (String) values.get("m_item_unit");
		String value = (String) values.get("m_value");
		
		Log.i("value", value);
		
		TextView text = (TextView) convertView.findViewById(R.id.umlr_name_text);
		TextView valueText = (TextView) convertView.findViewById(R.id.umlr_value_edit);
		TextView unitText = (TextView) convertView.findViewById(R.id.umlr_unit_text);
		TextView timeText = (TextView) convertView.findViewById(R.id.umlr_time_text);
		
		if(values.get("create_date")!=null)
		{
			Date d = Date.valueOf((Long)values.get("create_date"));
			timeText.setText(d.toTime());
		}
		
		text.setText(name);
		unitText.setText(unit);
		
		valueText.setText(value); 
		
		return convertView;
	}

}
