package com.hope.kqt.android.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.hope.kqt.android.util.ServiceTaskComparator;
import com.hope.kqt.entity.UserTask;

public class ServiceTask implements Serializable 
{
	private long time;
	private List<UserTask> tasks;
	
	public ServiceTask(){}

	public ServiceTask(long time, List<UserTask> tasks) {
		super();
		this.time = time;
		this.tasks = tasks;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<UserTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<UserTask> tasks) {
		this.tasks = tasks;
	}

	public static List<ServiceTask> userTasksToServiceTasks(List<UserTask> tasks)
	{
		List<ServiceTask> list = new ArrayList<ServiceTask>();
		
		if(tasks!=null && !tasks.isEmpty())
		{
			List<UserTask> utlist = new ArrayList<UserTask>();
			long oldtime = 0;
			for(int i=0;i<tasks.size();i++)
			{
				UserTask task = tasks.get(i);
				long time = (task.getCallTime()==0? task.getStartTime():task.getCallTime());
				
				if(oldtime!=time && !utlist.isEmpty())
				{
					list.add(new ServiceTask(oldtime,utlist));
					utlist = new ArrayList<UserTask>();
				}
				
				utlist.add(task);
				oldtime = time;
				
				if(i==tasks.size()-1)
				{
					list.add(new ServiceTask(time,utlist));
					break;
				}
			}
		}
		
		Collections.sort(list, new ServiceTaskComparator());
		
		Log.i("ServiceTask", list.size()+"");
		
		return list; 
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("time:").append(time);
		
		sb.append(" tasks:");
		for(UserTask ut:this.tasks)
		{
			sb.append(" ").append(ut.getTitle());
		}
		
		return sb.toString();
	}
	
	
}
