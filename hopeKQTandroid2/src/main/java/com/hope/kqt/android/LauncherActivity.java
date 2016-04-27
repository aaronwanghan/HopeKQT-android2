package com.hope.kqt.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LauncherActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.launcher);
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() 
			{
				action();
			}
			
		}, 3000);
	}

	private void action()
	{
		Intent i = new Intent(this,MainActivity.class);
		this.startActivity(i);
		this.finish();
	}
}
