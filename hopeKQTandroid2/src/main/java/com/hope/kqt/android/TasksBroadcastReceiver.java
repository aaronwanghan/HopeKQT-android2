package com.hope.kqt.android;


import com.hope.kqt.android.service.TasksService;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

@SuppressLint("InlinedApi")
public class TasksBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		//Toast.makeText(context,"TasksBroadcastReceiver onReceive", Toast.LENGTH_LONG).show();
		
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
		{
			Intent i = new Intent(context,TasksService.class);
			
			//i.setAction("android.intent.action.MAIN");
			//i.addCategory("android.intent.category.LAUNCHER");
			//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			//i.addFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
			
			//context.startActivity(i);
			context.startService(i);
		}
	}

}
