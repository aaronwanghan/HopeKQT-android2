package com.hope.kqt.android;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hope.kqt.android.fragment.UserDatasFragment;
import com.hope.kqt.android.fragment.UserDatasFragment.UserDatasListener;
import com.hope.kqt.android.fragment.UserImgsFragment;
import com.hope.kqt.android.fragment.UserImgsFragment.UserImgsListener;
import com.hope.kqt.android.fragment.UserTasksFragment;
import com.hope.kqt.android.fragment.UserTasksFragment.UserTasksListener;
import com.hope.kqt.android.service.TasksService;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.android.util.UpdateAppHelper;
import com.hope.kqt.android.util.UserTaskMenusDatasHelper;
import com.hope.kqt.android.view.MainResideMenu;
import com.hope.kqt.android.view.MainResideMenu.MainResideMenuListener;
import com.hope.kqt.entity.User;
import com.hope.kqt.entity.UserImg;
import com.hope.kqt.entity.UserTaskMenus;
import com.hope.kqt.entity.UserTestItem;
import com.special.ResideMenu.ResideMenu;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends KqtActivity 
	implements UserTasksListener,MainResideMenuListener,UserImgsListener,UserDatasListener
{	
	private static final int CLEAR_USER_VALUES = 0x02;
	
	private MainResideMenu menu;
	
	private View userTasksMenuButton;
	private View userDatasMenuButton;
	private View userImgsMenuButton;
	
	//private ImageView userImg;

	private UserTaskMenusDatasHelper dataHelper;
	private UpdateAppHelper updateAppHelper;
	
	private Handler handler = new Handler()
	{
		@Override
		public void dispatchMessage(Message msg) 
		{
			Intent i =null;
			switch(msg.what)
			{
			case CLEAR_USER_VALUES:
				clearUser();
				loginAgain();
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.activity_main);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_titlebtn);
		
		this.userTasksMenuButton = this.findViewById(R.id.main_foot_user_tasks_button);
		this.userDatasMenuButton = this.findViewById(R.id.main_foot_user_datas_button);
		this.userImgsMenuButton = this.findViewById(R.id.main_foot_user_imgs_button);
		//this.userImg = (ImageView) this.findViewById(R.id.main_titlebtn_img);
		this.initUser();
		this.startService();
		
		this.dataHelper = new UserTaskMenusDatasHelper(this,user);
		
		String cachePath = Environment.getExternalStorageDirectory()+Application.FILE_SAVE_PATH;
        
        File f = new File(cachePath);
        if(!f.exists())
        	f.mkdirs();
		
		try {
			this.updateAppHelper = new UpdateAppHelper(this,httpService,super.handler,cachePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.menu = new MainResideMenu(this,this.user);
		this.initTitlebtn();
		this.init();
		
		if(user!=null)
		{
			this.updateAppHelper.setShowLastVersionDialog(false);
			this.updateAppHelper.update();
		}
	}
	
	private void initUser()
	{
		String s = systemValueDao.load(Application.USER_SYSTEM_KEY);
		
		Log.i("initUser s",(s==null? "null":s));
		
		if(s!=null && !"".equals(s))
		{
			try {
				
				Log.i("systemValueDao user", new String(KQTUtils.parseHexStr2Byte(s)));
				
				user = User.valueOf(new JSONObject(new String(KQTUtils.parseHexStr2Byte(s))));
				
				ThreadPoolUtils.execute(new Runnable()
				{
					@Override
					public void run() 
					{
						try {
							int version = httpService.findUserVersion(user.getId());
							if(version!=-1 && version!=user.getVersion())
							{
								user = httpService.updateUser(user.getId(), KQTUtils.getUserUpdateKey(user));
								
								if(user==null)
									handler.sendEmptyMessage(CLEAR_USER_VALUES);
								else
								{
									systemValueDao.set(Application.USER_SYSTEM_KEY, KQTUtils.parseByte2HexStr(user.out().toString().getBytes()));
									systemValueDao.set(Application.USER_TREATMENT_VERSION_SYSTEM_KEY, "-1");
									dataHelper.updateDatas();
									
									startService();
								}
								
							}
						} catch (Exception e) {
							//handler.sendEmptyMessage(SHOW_USER_TASKS);
						}
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else
		{
			this.login();
		}
	}
	
	private void startService()
	{
		Intent i = new Intent(this,TasksService.class);
		i.putExtra("user", user);
		this.startService(i);
	}
	
	private void initTitlebtn()
	{
		ImageView imgview = (ImageView) this.findViewById(R.id.main_titlebtn_img);
		TextView title = (TextView) this.findViewById(R.id.main_titlebtn_title);
		
		if(user!=null)
		{
			title.setText(user.getName());
			final String img = this.user.getImg();
			if(img!=null && !"".equals(img))
			{
				ImgAsyncTask task = new ImgAsyncTask(this.cache,imgview);
				task.execute(Application.HTTP_USER_IMG_PATH,img);
			}
		}
		
		imgview.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				menu.getResideMenu().openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
	}

	@SuppressLint("NewApi")
	private void init()
	{
		FragmentTransaction bt = this.getFragmentManager().beginTransaction();
		//f = UserTasksFragment.newInstance(bundle);
		bt.replace(R.id.main_fragment, new UserTasksFragment());
		bt.commit();
		
		this.userTasksMenuButton.setEnabled(false);
	}
	
	@SuppressLint("NewApi")
	public void layoutOnClickMethod(View layout) 
	{
		this.resetMenuButtons();
		layout.setEnabled(false);

		FragmentTransaction bt = this.getFragmentManager().beginTransaction();

		switch(layout.getId())
		{
		case R.id.main_foot_user_tasks_button:
			bt.replace(R.id.main_fragment, new UserTasksFragment());
			break;
		case R.id.main_foot_user_datas_button:
			bt.replace(R.id.main_fragment, new UserDatasFragment());
			break;
		case R.id.main_foot_user_imgs_button:
			bt.replace(R.id.main_fragment, new UserImgsFragment());
			break;
		}
		
		bt.commit();
	}
	
	private void resetMenuButtons()
	{
		this.userDatasMenuButton.setEnabled(true);
		this.userImgsMenuButton.setEnabled(true);
		this.userTasksMenuButton.setEnabled(true);
	}

	@Override
	public List<UserTaskMenus> getUserTaskMenuss() 
	{
		if(this.dataHelper!=null)
		{
			this.dataHelper.updateDatas();
			return dataHelper.findAll();
		}
		
		return null;
	}

	@Override
	public void logout() 
	{
		clearUser();

		Intent i = new Intent(this,TasksService.class);
		this.stopService(i);
		this.login();
		finish();
	}

	@Override
	public void updateApp() 
	{
		if(this.updateAppHelper!=null)
			this.updateAppHelper.update();
	}

	@Override
	public String getAppVersion() 
	{
		if(this.updateAppHelper!=null)
			return this.updateAppHelper.getVersionString();
		
		return null;
	}

	@Override
	public void setProgressBar(ProgressBar pbar) {
		if(this.updateAppHelper!=null)
			this.updateAppHelper.setProgressBar(pbar);
	}

	@Override
	public void showSettings() 
	{
		Intent i = new Intent(this,SettingsActivity.class);
		this.startActivity(i);
	}

	@Override
	public UserTaskMenus getNowUserTaskMenus() 
	{
		if(this.dataHelper!=null)
			return this.dataHelper.getNow();
		return null;
	}

	@Override
	public List<UserImg> getUserImgs() {
		try {
			return httpService.findUserImgByUserId(user.getId());
		} catch (Exception e) {
			this.showException(e);
		}
		
		return null;
	}

	@Override
	public Uri getImage(String path, String fileName) throws Exception {
		return httpService.getImage(path, fileName, cache);
	}

	@Override
	public UserImg onLoadImg(UserImg ui) 
	{
		try {
			return httpService.addUserImg(ui, user.getId());
		} catch (Exception e) {
			this.showException(e);
		}
		
		return null;
	}

	@Override
	public List<UserTestItem> getUserTestItems() {
		try {
			return this.httpService.findUserTestItemByUserId(user.getId());
		} catch (Exception e) {
			this.showException(e);
		}
		
		return null;
	}

	@Override
	public List<UserTaskMenus> updateUserTasks() 
	{
		if(this.dataHelper.updateDatas())
		{
			startService();
			return this.getUserTaskMenuss();
		}
		
		return null;
	}
}
