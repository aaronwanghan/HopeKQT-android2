package com.hope.kqt.android;

import com.hope.kqt.android.service.TasksService;
import com.hope.kqt.android.util.AlertSettingsAdapter;
import com.hope.kqt.android.util.KQTUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsActivity extends KqtActivity 
{
	private Button versionButton;
	
	private ListView listView;
	private AlertSettingsAdapter adapter;

	private static final String[] ALERT_SETTINGS_VALUES = {"关闭","提示信息","闹钟"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.settings);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);
		TextView title = (TextView) this.findViewById(R.id.kqt_text);
		title.setText("设置");
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);
		//this.iconsLayout = (LinearLayout) this.findViewById(R.id.kqt_user_icons_layout);
		//this.setIcons();
		
		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		this.listView = (ListView) this.findViewById(R.id.settings_message_list_view);
		
		this.versionButton = (Button) this.findViewById(R.id.settings_version_button);
		
		this.versionButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(v.getContext(),VersionActivity.class);
				v.getContext().startActivity(i);
			}
		});
		
		initMessageListView();
	}

	private void initMessageListView()
	{
		int select = 2;
		String selectedString = systemValueDao.load(Application.USER_ALERT_SETTINGS_KEY);
		if(selectedString!=null)
		{
			try {
				select = Integer.parseInt(selectedString);
			} catch (NumberFormatException e) {
			}
		}
		
		this.adapter = new AlertSettingsAdapter(ALERT_SETTINGS_VALUES,this,select);
		this.listView.setAdapter(adapter);
		this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> a, View view,
					int position, long id) 
			{
				if(adapter!=null)
				{
					adapter.setSelected(position);
					adapter.notifyDataSetChanged();
					systemValueDao.set(Application.USER_ALERT_SETTINGS_KEY, position+"");
					startService(position);
				}
			}
		});
		
		KQTUtils.setListViewHeightBasedOnChildren(listView);
	}
	
	private void startService(int selectd)
	{
		Intent i = new Intent(this,TasksService.class);
		i.putExtra("alert_setting_selected", selectd);
		i.setAction(Application.SERVICE_ACTION);
		this.startService(i);
	}
}
