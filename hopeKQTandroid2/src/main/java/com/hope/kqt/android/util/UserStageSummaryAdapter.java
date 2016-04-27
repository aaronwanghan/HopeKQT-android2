package com.hope.kqt.android.util;

import java.util.List;
import java.util.Map;

import com.hope.kqt.android.R;
import com.hope.kqt.entity.TeacherRating;
import com.hope.kqt.entity.UserStageSummary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class UserStageSummaryAdapter extends BaseAdapter 
{
	private List<Map<String,Object>> values;
	private Context context;

	public UserStageSummaryAdapter(List<Map<String, Object>> values,
			Context context) {
		super();
		this.values = values;
		this.context = context;
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		return values.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.user_stage_summary_item, parent,false);
		
		UserStageSummary uss = (UserStageSummary) this.values.get(position).get("value");
		
		TextView title = (TextView) convertView.findViewById(R.id.uss_title_text);
		TextView content = (TextView) convertView.findViewById(R.id.uss_content_text);
		LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.uss_ratings_list);
		
		title.setText("第"+(uss.getStageNum()+1)+"阶段小结");
		content.setText(uss.getContent());
		
		layout.removeAllViewsInLayout();
		
		for(TeacherRating tr:uss.getTeacherRatings())
		{
			Log.i("tr", tr.getItem().getName());
			View rview = LayoutInflater.from(context).inflate(R.layout.teacher_rating, layout,false);
			TextView name = (TextView) rview.findViewById(R.id.tr_text);
			RatingBar ratingbar = (RatingBar) rview.findViewById(R.id.tr_ratingBar);
			
			name.setText(tr.getItem().getName());
			ratingbar.setRating(tr.getScore());
			layout.addView(rview);
		}
		
		return convertView;
	}

}
