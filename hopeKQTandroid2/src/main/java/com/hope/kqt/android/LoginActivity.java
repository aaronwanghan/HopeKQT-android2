package com.hope.kqt.android;

import org.json.JSONException;

import com.hope.kqt.android.R;
import com.hope.kqt.android.dao.SystemValueDao;
import com.hope.kqt.android.dao.impl.SystemValueFileDaoImpl;
import com.hope.kqt.android.service.TasksService;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.android.util.ProgressDialogAsyncTask;
import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.entity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends KqtActivity 
{
	private static final int USER_LOGIN_FINISH_MESSAGE = 0x01;
	private static final int USER_LOGIN_ERROR_MESSAGE = 0x02;
	
	private EditText username;
	private EditText password;
	
	private Button loginButton;
	
	private boolean state = true;
	private SystemValueDao systemValueDao;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler()
	{
		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case USER_LOGIN_FINISH_MESSAGE:
				save();
				break;
			case USER_LOGIN_ERROR_MESSAGE:
				password.setText("");
				getMessage("用户名或密码错误!");
				break;
			}
		}
		
	};
	
	private void save(User user)
	{
		try {
			this.systemValueDao.set(Application.USER_SYSTEM_KEY, KQTUtils.parseByte2HexStr(user.out().toString().getBytes()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.login);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);

		View view = this.findViewById(R.id.login_page);
		view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.login));

		TextView title = (TextView) this.findViewById(R.id.kqt_text);
		title.setText("用户登录");
		
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);
		
		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		this.username = (EditText) this.findViewById(R.id.login_username_text);
		this.password = (EditText) this.findViewById(R.id.login_password_text);

		//Animation inAnim = new TranslateAnimation(1000,0,0,0);
		//inAnim.setDuration(1000);

		//this.username.startAnimation(inAnim);
		//this.password.startAnimation(inAnim);

		//this.username.setText("110108198410305738");
		//this.password.setText("123456");
		
		this.loginButton = (Button) this.findViewById(R.id.login_button);

		//Animation buAnim = new AlphaAnimation(0.1f,1);
		//buAnim.setDuration(1000);

		//this.loginButton.startAnimation(buAnim);

		this.systemValueDao = new SystemValueFileDaoImpl(this);
		this.setListener();
	}
	
	private void setListener()
	{
		this.loginButton.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				final String u = username.getText().toString();
				final String p = password.getText().toString();
				
				if(u==null || "".equals(u))
				{
					getMessage("用户名不能为空！");
					return;
				}
				
				if(p==null || "".equals(p))
				{
					getMessage("密码不能为空！");
					return;
				}
				
				if(state)
				{
					state = false;

					ProgressDialogAsyncTask task = new ProgressDialogAsyncTask(v.getContext(),
							new Runnable()
					{
						@Override
						public void run() 
						{
							try {
								user = httpService.login(u, p);
							} catch (Exception e) {
								showException(e);
							}
							
							if(user!=null)
							{
								handler.sendEmptyMessage(USER_LOGIN_FINISH_MESSAGE);
								return;
							}
							else
								handler.sendEmptyMessage(USER_LOGIN_ERROR_MESSAGE);
							
							state = true;
						}
						
					},"提示","登录中...");
					task.execute();
				}
				else
					getMessage("登陆中...");
				
				/*if("admin".equals(username.getText()) && "admin".equals(password.getText()))
				{
					user = new User();
					user.setPassword("admin");
					user.setUsername("admin");
					
					Intent i = new Intent(v.getContext(),MainActivity.class);
					v.getContext().startActivity(i);
					LoginActivity.this.finish();
				}*/
			}
		});
	}
	
	private void save()
	{
		save(user);
		Intent i = new Intent(this,MainActivity.class);
		this.startActivity(i);
		this.finish();
	}
}
