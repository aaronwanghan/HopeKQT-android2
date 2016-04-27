package com.hope.kqt.android;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hope.kqt.android.util.HelpMessageAdapter;
import com.hope.kqt.android.util.HelpMessagesHelper;
import com.hope.kqt.android.util.ProgressDialogAsyncTask;
import com.hope.kqt.android.util.ThreadPoolUtils;
import com.hope.kqt.entity.HelpMessage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class HelpMessagesActivity extends KqtActivity implements HelpMessagesHelper.UpdateListener
{
	private static final int UPDATE_PAGE = 0x01;
	private static final int UPDATE_MESSAGES_ERROR = 0x02;
	
	private PullToRefreshListView listView;
	private HelpMessageAdapter adapter;
	
	private HelpMessagesHelper helper;
	private List<HelpMessage> messages;
	
	private Handler handler = new Handler()
	{

		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case UPDATE_PAGE:
				adapter.setValues(messages);
				listView.onRefreshComplete();
				break;
			case UPDATE_MESSAGES_ERROR:
				listView.onRefreshComplete();
				break;
			}
			super.dispatchMessage(msg);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.help_messages);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);
		TextView title = (TextView) this.findViewById(R.id.kqt_text);
		
		title.setText(this.getResources().getString(R.string.food_help_messages));
		
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);
		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		this.listView = (PullToRefreshListView) this.findViewById(R.id.hm_list_view);
		
		if(this.user!=null)
		{
			this.helper = new HelpMessagesHelper(this,this.user);
			this.helper.setOnUpdateListener(this);
			
			this.messages = this.helper.findAll();
			if(this.messages==null || this.messages.isEmpty())
				this.initMessages();

			this.setListView();
		}
	}
	
	private void initMessages()
	{
		ProgressDialogAsyncTask task = new ProgressDialogAsyncTask(this,
				new Runnable()
		{
			@Override
			public void run() 
			{
				try {
					helper.updateDatas();
				} catch (Exception e) {
					showException(e);
				}
			}
			
		},"提示","数据加载中...");
		task.execute();
	}

	@Override
	public void action(List<HelpMessage> messages) 
	{
		this.messages = messages;
		handler.sendEmptyMessage(UPDATE_PAGE);
	}
	
	private void setListView()
	{
		this.adapter = new HelpMessageAdapter(this,this.messages);
		this.listView.setAdapter(adapter);
		this.listView.setOnRefreshListener(new OnRefreshListener<ListView>()
		{
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) 
			{
				ThreadPoolUtils.execute(new Runnable(){

					@Override
					public void run() {
						try {
							helper.updateDatas();
						} catch (Exception e) {
							showException(e);
							handler.sendEmptyMessage(UPDATE_MESSAGES_ERROR);
						}
					}
					
				});
			}
			
		});
	}
}
