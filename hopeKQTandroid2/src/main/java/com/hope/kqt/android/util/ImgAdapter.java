package com.hope.kqt.android.util;

import java.util.List;
import java.util.Map;

import com.hope.kqt.android.R;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImgAdapter extends BaseAdapter 
{
	private List<Map<String,Object>> imgsValues;
	
	private ReadImgListener listener;
	private Context context;
	public ImgAdapter(List<Map<String, Object>> imgsValues, ReadImgListener listener,Context context) {
		super();
		this.imgsValues = imgsValues;
		this.listener = listener;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.imgsValues.size();
	}

	@Override
	public Object getItem(int position) {
		return this.imgsValues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
			convertView = LayoutInflater.from(context).inflate(R.layout.img_on_load_item, parent,false);
		
		Map<String,Object> values = this.imgsValues.get(position);
		
		String name = (String) values.get("img_name");
		Long time = (Long) values.get("img_time");
		String file = (String) values.get("img_file");
		
		
		ImageView imgv = (ImageView) convertView.findViewById(R.id.img_file);
		TextView nametext = (TextView) convertView.findViewById(R.id.img_name);
		TextView timetext = (TextView) convertView.findViewById(R.id.img_time);
		
		if(time!=null)
			timetext.setText(Date.valueOf(time).toDate());
		nametext.setText(name);
		this.loadImage(imgv, file);
		return convertView;
	}

	private void loadImage(ImageView imgView,String fileName)
	{
		ImgAsyncTask task = new ImgAsyncTask(imgView);
		task.execute(fileName+"?icon=1");
	}
	
	private final class ImgAsyncTask extends AsyncTask<String,Integer,Uri>
	{
		private ImageView imgView;

		public ImgAsyncTask(ImageView imgView) {
			super();
			this.imgView = imgView;
		}

		@Override
		protected Uri doInBackground(String... params) 
		{
			try {
				return listener.getImage(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Uri result) 
		{
			super.onPostExecute(result);
			
			if(imgView!=null && result!=null)
				this.imgView.setImageURI(result);
		}
	}
	
	public interface ReadImgListener
	{
		public Uri getImage(String img) throws Exception;
	}
}
