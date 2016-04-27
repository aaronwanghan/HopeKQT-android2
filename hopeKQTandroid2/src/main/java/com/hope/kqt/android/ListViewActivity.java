package com.hope.kqt.android;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ListViewActivity extends KqtActivity 
{
	protected ListView listView;
	
	protected List<Map<String,Object>> values;
	
	protected static final int UPDATE_LIST_VIEW_MESSAGE = 0x01;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		
		this.listView = new ListView(this);
		this.listView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
		
		this.setContentView(this.listView);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);
		
		TextView title = (TextView) this.findViewById(R.id.kqt_text);
		title.setText(getTextTitle());
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);
		
		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
	}
	
	protected abstract String getTextTitle();
	
	protected abstract BaseAdapter getAdapter();
	
	protected void setListView()
	{
		this.setValues();
		if(this.values==null || this.values.isEmpty())
			return;
		
		this.listView.setAdapter(getAdapter());
	}
	
	protected abstract void setValues();
}
