package com.hope.kqt.android.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hope.kqt.android.R;
import com.hope.kqt.android.UserLogDatasActivity;
import com.hope.kqt.entity.UserTask;
import com.hope.kqt.entity.UserTaskMenus;

public class TasksAdapter extends BaseAdapter 
{
	private List<Map<String,Object>> taskValues;
	private Context context;
	
	private UserTaskMenus taskMenus;
	
	public TasksAdapter(Context context, UserTaskMenus taskMenus) {
		super();
		this.context = context;
		this.taskMenus = taskMenus;
		setTaskValues();
	}

	private void setTaskValues()
	{
		if(this.taskMenus==null)
			return;
		
		List<UserTask> tasks = this.taskMenus.getTasks();
		
		this.taskValues = new ArrayList<Map<String, Object>>();
		
		if(tasks==null || tasks.isEmpty())
			return;
		
		for(UserTask task:tasks)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("task_name", task.getTitle());
			map.put("task_content", task.getContent());
			map.put("task_type", task.getType());
			map.put("task_start_time", task.getStartTime());
			map.put("task_end_time", task.getEndTime());
			map.put("task_call_time", task.getCallTime());
			
			
			
			this.taskValues.add(map);
		}
	}
	
	
	
	public void setTaskMenus(UserTaskMenus taskMenus) 
	{
		this.taskMenus = taskMenus;
		this.setTaskValues();
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.taskValues.size();
	}

	@Override
	public Object getItem(int position) {
		return this.taskValues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.tasks_item, parent, false);
		}
		
		final Map<String,Object> task = this.taskValues.get(position);
		
		if(task!=null)
		{
			TextView name = (TextView) convertView.findViewById(R.id.task_name);
			TextView content = (TextView) convertView.findViewById(R.id.task_content);
			TextView time = (TextView) convertView.findViewById(R.id.task_time);

			name.setText(task.get("task_name").toString());
			content.setText(task.get("task_content").toString());
			
			this.setTimeTextView(time,position);
			
			ImageView image = (ImageView) convertView.findViewById(R.id.task_image);
			Integer type = (Integer) task.get("task_type");
			
			switch(type)
			{
			case 0:
				image.setImageResource(R.drawable.type00);
				image.setOnClickListener(null);
				break;
			case 1:
				image.setImageResource(R.drawable.type01);
				image.setOnClickListener(null);
				break;
			case 2:
				image.setImageResource(R.drawable.type02);
				image.setOnClickListener(null);
				break;
			case 3:
				image.setImageResource(R.drawable.type03);
				image.setOnClickListener(null);
				break;
			case 4:
				image.setImageResource(R.drawable.type04);
				image.setOnClickListener(null);
				break;
			case 5:
				image.setImageResource(R.drawable.type05);
				image.setOnClickListener(new View.OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						
						if(taskMenus!=null)
						{
							Intent i = new Intent(context,UserLogDatasActivity.class);
							i.putExtra("taskMeuns", taskMenus);
							i.putExtra("current_tab", 0);
							KQTUtils.setStartAndEnd(i,(Long)task.get("task_start_time"));
							context.startActivity(i);
						}
					}
				});
				break;
			case 6:
				image.setImageResource(R.drawable.type05);
				image.setOnClickListener(new View.OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						
						if(taskMenus!=null)
						{
							Intent i = new Intent(context,UserLogDatasActivity.class);
							i.putExtra("taskMeuns", taskMenus);
							i.putExtra("current_tab", 1);
							context.startActivity(i);
						}
					}
				});
				break;
			case 7:
				image.setImageResource(R.drawable.type05);
				image.setOnClickListener(new View.OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						
						if(taskMenus!=null)
						{
							Intent i = new Intent(context,UserLogDatasActivity.class);
							i.putExtra("taskMeuns", taskMenus);
							i.putExtra("current_tab", 3);
							context.startActivity(i);
						}
					}
				});
				break;
			}
		}

		convertView.startAnimation(AnimationUtils.loadAnimation(parent.getContext(),R.anim.task_item));
		return convertView;
	}

	
	private void setTimeTextView(TextView time,int position)
	{
		Map<String,Object> task = this.taskValues.get(position);
		
		Long start = (Long) task.get("task_start_time");
		Long end = (Long) task.get("task_end_time");
		Long call = (Long) task.get("task_call_time");

		//Long nexttime = this.getNextTime(position,start);
		
		
		
		if(start!=null)
		{
			//setTimeTextViewBackgroundColor(time,start,end,call,null);
			time.setBackgroundColor(Color.parseColor(KQTUtils.taskTimeToColor(call,start,end)));

			StringBuffer sb = new StringBuffer();
			sb.append(Date.valueOf(start).toTime());
			
			if(end!=null && end!=0)
			{
				sb.append(" - ").append(Date.valueOf(end).toTime());
			}
			
			time.setText(sb.toString());
		}
	}
	
	/*private Long getNextTime(int position,long onstart)
	{
		Long nexttime = null;
		
		if(position<this.taskValues.size()-1)
		{
			Map<String,Object> task = this.taskValues.get(position+1);
			Long start = (Long) task.get("task_start_time");
			Long end = (Long) task.get("task_end_time");
			
			if(start == onstart)
				return onstart+30*60*1000;
			
			if(end!=0)
				nexttime = start;
			else
				return getNextTime(position+1,onstart);
		}
		
		return nexttime;
	}*/
	
	/*private void setTimeTextViewBackgroundColor(TextView time,long start,long end,long call,Long nexttime)
	{
		long now = System.currentTimeMillis();
		if((call!=start && now<call )|| now<start)
		{
			time.setBackgroundColor(Color.parseColor("#66CCFF"));
			return;
		}
		
		if((end!=0 && now>end) || now>start+30*60*1000)
		{
			time.setBackgroundColor(Color.parseColor("#999999"));
			return;
		}
		
		time.setBackgroundColor(Color.parseColor("#66CC33"));
	}*/
	
}
