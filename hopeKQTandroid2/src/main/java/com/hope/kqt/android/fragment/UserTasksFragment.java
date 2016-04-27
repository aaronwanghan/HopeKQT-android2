package com.hope.kqt.android.fragment;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.hope.kqt.android.R;
import com.hope.kqt.android.UserLogDatasActivity;
import com.hope.kqt.android.util.Date;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.android.util.ProgressDialogAsyncTask;
import com.hope.kqt.android.util.TasksAdapter;
import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.entity.UserTask;
import com.hope.kqt.entity.UserTaskMenus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class UserTasksFragment extends Fragment 
{
	private static final int UPDATE_PARE = 0x01;
	
	private static final int ON_REFRESH_COMPLETE = 0x02;
	
	private Context context;
	
	private List<UserTaskMenus> taskMenuss;
	
	private UserTasksListener listener;
	
	private PullToRefreshListView listView;
	private TasksAdapter adapter;
	private TextView dateTextView;
	
	private View onButton;
	private View nextButton;
	
	private int page = 0;
	
	private Handler handler = new Handler()
	{
		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case UPDATE_PARE:
				updatePage();
				listView.onRefreshComplete();
				break;
			case ON_REFRESH_COMPLETE:
				listView.onRefreshComplete();
				break;
			}
			
			super.dispatchMessage(msg);
		}
		
	};
	
	/*public static UserTasksFragment newInstance(Bundle bundle)
	{
		UserTasksFragment fragment = new UserTasksFragment();
		fragment.setArguments(bundle);
		return fragment;
	}*/

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (UserTasksListener) activity;
		this.context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ProgressDialogAsyncTask task = new ProgressDialogAsyncTask(this.context,
				new Runnable()
		{
			@Override
			public void run() {
				taskMenuss = listener.getUserTaskMenuss();
				setDefaultPage();
				handler.sendEmptyMessage(UPDATE_PARE);
			}

		},"提示","数据加载中...");
		task.execute();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.user_tasks_fragment, container, false);
		
		this.listView = (PullToRefreshListView) v.findViewById(R.id.utf_list_view);
		this.dateTextView = (TextView) v.findViewById(R.id.utf_date);
		
		this.onButton = v.findViewById(R.id.utf_on_button);
		this.nextButton = v.findViewById(R.id.utf_next_button);
		
		this.onButton.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				onButton.setEnabled(false);
				nextButton.setEnabled(false);
				page --;
				handler.sendEmptyMessage(UPDATE_PARE);
			}
		});
		
		this.nextButton.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				onButton.setEnabled(false);
				nextButton.setEnabled(false);
				page ++;
				handler.sendEmptyMessage(UPDATE_PARE);
			}
		});
		
		this.setDefaultPage();
		this.updatePage();
		
		return v;
	}

	private void updatePage()
	{
		if(this.taskMenuss==null || page>=this.taskMenuss.size())
			return;
		
		UserTaskMenus utm = this.taskMenuss.get(page);
		
			Date date = Date.valueOf(utm.getTime());
			StringBuffer sb = new StringBuffer(date.toDate());
			
			if(utm.getStageNum()>=0)
			{
				sb.append(" 第").append(utm.getStageNum()+1).append("阶段");
				sb.append(" 第").append(utm.getNum()+1).append("天");
			}
			
			this.dateTextView.setText(sb.toString());

		
		this.initListView(utm);
		
		this.listView.getRefreshableView().setSelection(this.getDefaultItem(utm));
		
		setTitleButtonVisibility();
	}
	
	private void initListView(final UserTaskMenus utm)
	{
		this.adapter = new TasksAdapter(this.context,utm);
		this.listView.setAdapter(this.adapter);

		this.listView.setOnRefreshListener(new OnRefreshListener<ListView>()
		{
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) 
				{
					ThreadPoolUtils.execute(new Runnable()
					{
						@Override
						public void run() {
							List<UserTaskMenus> r = listener.updateUserTasks();
							if(r!=null)
							{
								taskMenuss = r;
								handler.sendEmptyMessage(UPDATE_PARE);
							}
							else
								handler.sendEmptyMessage(ON_REFRESH_COMPLETE);
						}
						
					});
				}
		});
		
		this.listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) 
			{
				if(position==0)
					return;
				
				UserTask task = utm.getTasks().get(position-1);
				Intent i = null;

				switch(task.getType())
				{
				case 5:
					i = new Intent(view.getContext(),UserLogDatasActivity.class);
					i.putExtra("taskMeuns", utm);
					i.putExtra("current_tab", 0);
					KQTUtils.setStartAndEnd(i,task.getStartTime());
					view.getContext().startActivity(i);
					break;
					
				case 6:
					i = new Intent(view.getContext(),UserLogDatasActivity.class);
					i.putExtra("taskMeuns", utm);
					i.putExtra("current_tab", 1);
					view.getContext().startActivity(i);
					break;
					
				case 7:
					i = new Intent(view.getContext(),UserLogDatasActivity.class);
					i.putExtra("taskMeuns", utm);
					i.putExtra("current_tab", 3);
					view.getContext().startActivity(i);
					break;
					
				}
			}
		});
	}
	
	private void setDefaultPage()
	{
		if(this.taskMenuss==null)
			return;
		
		long time = KQTUtils.getNowTime();
		
		for(int i=0;i<this.taskMenuss.size();i++)
		{
			UserTaskMenus utm = this.taskMenuss.get(i);
			
			if((i==0 && time<utm.getTime())
					|| time==utm.getTime()
					|| (i==this.taskMenuss.size()-1 && time>utm.getTime()))
			{
				page = i;
				break;
			}
		}
	}
	
	private int getDefaultItem(UserTaskMenus utm)
	{
		List<UserTask> tasks = utm.getTasks();
		
		long now = System.currentTimeMillis();
		
		for(int i=0;i<tasks.size();i++)
		{
			UserTask task = tasks.get(i);
			long time = (task.getCallTime()==0? task.getStartTime():task.getCallTime());
			if(now < time + 10*60*1000)
				return i;
		}
		
		return 0;
	}
	
	private void setTitleButtonVisibility()
	{
		if(this.taskMenuss==null || this.taskMenuss.isEmpty())
		{
			this.onButton.setVisibility(View.INVISIBLE);
			this.nextButton.setVisibility(View.INVISIBLE);
			return;
		}
		
		if(page<=0)
			this.onButton.setVisibility(View.INVISIBLE);
		else
			this.onButton.setVisibility(View.VISIBLE);
		if(page>=this.taskMenuss.size()-1)
			this.nextButton.setVisibility(View.INVISIBLE);
		else
			this.nextButton.setVisibility(View.VISIBLE);
		
		this.onButton.setEnabled(true);
		this.nextButton.setEnabled(true);
	}
	
	public interface UserTasksListener
	{
		public List<UserTaskMenus> getUserTaskMenuss();
		
		public List<UserTaskMenus> updateUserTasks();
	}
}
