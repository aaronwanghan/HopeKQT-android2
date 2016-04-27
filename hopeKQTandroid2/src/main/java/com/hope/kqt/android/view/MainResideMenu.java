package com.hope.kqt.android.view;

import com.hope.kqt.android.HelpMessagesActivity;
import com.hope.kqt.android.R;
import com.hope.kqt.android.UserLogDatasActivity;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.android.util.UpdateAppHelper;
import com.hope.kqt.entity.User;
import com.hope.kqt.entity.UserTaskMenus;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainResideMenu 
{
	private final static int[] TITLES = {R.string.edit_datas,
		R.string.food_help_messages,
		R.string.teacher_phone,
		R.string.system_settings,
		R.string.logout};
	
	private final static int[] ICONS = {R.drawable.menu_edit,
		R.drawable.menu_food_help,
		R.drawable.menu_phone,
		R.drawable.menu_settings,
		R.drawable.menu_logout};
	
	private View.OnClickListener[] listeners = 
	{
			new View.OnClickListener() 
			{
				
				@Override
				public void onClick(View v) {
					showUserTestLogs();
				}
			},
			new View.OnClickListener() 
			{
				
				@Override
				public void onClick(View v) {
					showHelpMessages();
				}
			},
			new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) {
					talk();
				}
			},
			new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) {
					listener.showSettings();
				}
			},
			new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					logout();
				}
			}
	};
	
	private Context context;
	private ResideMenu resideMenu; 
	
	//private UpdateAppHelper updateAppHelper;
	
	private User user;
	
	private MainResideMenuListener listener;
	
	public MainResideMenu(Activity activity,User user)
	{
		this.context = activity;
		this.listener = (MainResideMenuListener) activity;
		this.user = user;
		this.resideMenu = new ResideMenu(this.context);
		this.resideMenu.setBackground(R.drawable.menu_background);
		this.resideMenu.attachToActivity(activity);
		
		this.resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		
		Button updatebutton = (Button) this.resideMenu.findViewById(R.id.rmenu_update_button);
		TextView versionview = (TextView) this.resideMenu.findViewById(R.id.rmenu_version);
		ProgressBar pbar = (ProgressBar) this.resideMenu.findViewById(R.id.rmenu_progress_bar);
		
		versionview.setText(this.listener.getAppVersion());
		this.listener.setProgressBar(pbar);
		updatebutton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				listener.updateApp();
			}
		});
		
		this.setMenuItems();
	}

	public ResideMenu getResideMenu() {
		return resideMenu;
	}
	
	private void setMenuItems()
	{
		for(int i=0;i<TITLES.length;i++)
		{
			ResideMenuItem item = new ResideMenuItem(this.context,ICONS[i],
					this.context.getResources().getString(TITLES[i]));
			
			if(this.listener!=null)
				item.setOnClickListener(this.listeners[i]);
			
			this.resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT);
			
			if(i==2 && this.user!=null && this.user.getTeacher()!=null)
				item.setTitle(this.user.getTeacher().getName());
		}
	}
	
	private void showUserTestLogs()
	{
		UserTaskMenus utm = this.listener.getNowUserTaskMenus();
		if(utm!=null)
		{
			Intent i = new Intent(this.context,UserLogDatasActivity.class);
			i.putExtra("taskMeuns", utm);
			i.putExtra("current_tab", 0);
			this.context.startActivity(i);
		}
	}
	
	private void logout()
	{
		new AlertDialog.Builder(this.context).setTitle("提示")
		.setMessage("点击确认完成注销.")
		.setPositiveButton("确认", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				listener.logout();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.show();
	}
	
	private void talk()
	{
		if(user!=null && user.getTeacher()!=null)
		{

			final String phone = user.getTeacher().getPhone();
			if(phone!=null && !"".equals(phone))
			{
				new AlertDialog.Builder(this.context).setTitle("提示")
				.setMessage("点击确认 联系教练["+phone+"].")
				.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent i = new Intent("android.intent.action.CALL",Uri.parse("tel:"+phone));
						context.startActivity(i);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
			}
			
		}
	}
	
	private void showHelpMessages()
	{
		Intent i = new Intent(this.context,HelpMessagesActivity.class);
		this.context.startActivity(i);
	}
	
	public interface MainResideMenuListener
	{
		public UserTaskMenus getNowUserTaskMenus();
		
		public void showSettings();
		
		public void logout();
		
		public void updateApp();
		
		public String getAppVersion();
		
		public void setProgressBar(ProgressBar pbar);
	}
}
