package com.hope.kqt.android.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hope.kqt.android.Application;
import com.hope.kqt.android.R;
import com.hope.kqt.android.util.ImgAdapter;
import com.hope.kqt.android.util.ImgUtils;
import com.hope.kqt.android.util.ImgAdapter.ReadImgListener;
import com.hope.kqt.android.util.ProgressDialogAsyncTask;
import com.hope.kqt.entity.UserImg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class UserImgsFragment extends Fragment implements ReadImgListener
{
	private static final int UPDATE_IMGS_MESSAGE = 0x01;
	
	private static final int TASK_PHOTO_MESSAGE = 0x02;
	private static final int PICK_PHOTO_MESSAGE = 0x03;
	
	private static final int INPUT_IMG_TITLE_MESSAGE = 0x04;
	
	private GridView gridView;
	private ImgAdapter adapter;
	private List<Map<String,Object>> imgsValues;
	private List<UserImg> imgs;
	
	private Uri photoUri;
	private String imgPath;
	
	private Context context;
	private UserImgsListener listener;
	
	private Handler handler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) 
		{
			switch(msg.what)
			{
			case UPDATE_IMGS_MESSAGE:
				setGridView();
				break;
			case INPUT_IMG_TITLE_MESSAGE:
				onLoadImg((String)msg.obj);
				break;
			}
			super.dispatchMessage(msg);
		}
		
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (UserImgsListener) activity;
		this.context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		ProgressDialogAsyncTask task = new ProgressDialogAsyncTask(this.context,
				new Runnable()
		{
			@Override
			public void run() 
			{
				imgs = listener.getUserImgs();
				handler.sendEmptyMessage(UPDATE_IMGS_MESSAGE);
			}
			
		},"提示","数据加载中...");
		task.execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.img_on_load, container, false);
		
		this.gridView = (GridView) v.findViewById(R.id.img_grid_view);
		View addbutton = v.findViewById(R.id.img_add_button);
		addbutton.setOnClickListener(new View.OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				showAddImgDialog();
			}
		});
		
		return v;
	}
	
	private void setGridView()
	{
		if(this.imgs==null || this.imgs.isEmpty())
			return;
		
		this.setImgsValues();
		this.adapter = new ImgAdapter(this.imgsValues,this,this.context);
		this.gridView.setAdapter(adapter);
		this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) 
			{
				Map<String,Object> values = imgsValues.get(position);
				ImgIntentAsyncTask task = new ImgIntentAsyncTask(view.getContext());
				task.execute(Application.HTTP_USER_IMG_FILE_PATH,(String)values.get("img_file"));
			}
		});
	}
	
	private void setImgsValues()
	{
		if(this.imgs==null || this.imgs.isEmpty())
			return;
		
		this.imgsValues = new ArrayList<Map<String,Object>>();
		
		for(UserImg ui:this.imgs)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			
			map.put("img_name", ui.getName());
			map.put("img_time", ui.getTime());
			map.put("img_file", ui.getFileName());
			
			this.imgsValues.add(map);
		}
	}
	
	private void showAddImgDialog()
	{
		new AlertDialog.Builder(this.context).setTitle("添加图片").setMessage("请选择添加方式")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("拍照", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				photoUri = context.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
					new ContentValues());
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,photoUri);
				startActivityForResult(intent, TASK_PHOTO_MESSAGE);
			}
		})
		.setNeutralButton("本地图片",  new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent,PICK_PHOTO_MESSAGE);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		})
		.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(resultCode!=Activity.RESULT_OK)
			return;
		
		switch(requestCode)
		{
		case TASK_PHOTO_MESSAGE:
			setImg();
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = 4;
			File file = new File(imgPath);
			
			int degree = ImgUtils.readPictureDegree(file.getAbsolutePath());
			Bitmap cameraBitmap = BitmapFactory.decodeFile(imgPath, bitmapOptions);
			Bitmap bitmap = ImgUtils.rotaingImageView(degree, cameraBitmap);
			ImgUtils.saveBitmap(imgPath, bitmap);
			setTitle();
			break;
		case PICK_PHOTO_MESSAGE:
			if(data!=null)
			{
				photoUri = data.getData();
				setImg();
				setTitle();
			}
			
			break;
		}
	}
	
	private void setImg()
	{
		if(photoUri!=null)
		{
			String[] pojo = {MediaStore.Images.Media.DATA};
			Cursor cursor = this.context.getContentResolver().query(this.photoUri, pojo, null, null, null);
			if(cursor!=null && cursor.moveToFirst())
			{
				int ci = cursor.getColumnIndexOrThrow(pojo[0]);
				imgPath = cursor.getString(ci);
				
				Log.i("img", imgPath);
				cursor.close();
			}
			else
			{
				if(cursor==null)
					Log.i("cursor", "null");
				else
					Log.i("cursor", "moveToFirst is false");
			}
		}
		else
			Log.i("photoUri", "null");
	}

	private void setTitle()
	{
		if(imgPath!=null)
			this.showDialog();
	}
	
	private void showDialog()
	{
		View v = LayoutInflater.from(this.context).inflate(R.layout.input_img_title, null, false);
		
		final EditText titleEdit = (EditText) v.findViewById(R.id.input_it_edit);
		ImageView imgView = (ImageView) v.findViewById(R.id.input_it_imageview);
		
		this.setImageBitmap(imgView);
		
		new AlertDialog.Builder(this.context).setTitle("请输入图片标题")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(v)
			.setPositiveButton("确认", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String s = titleEdit.getText().toString();
					
					if(s==null || "".equals(s))
						return;
					
					Message msg = new Message();
					msg.what = INPUT_IMG_TITLE_MESSAGE;
					msg.obj = s;
					
					handler.sendMessage(msg);
					
					Log.i("titleEdit", s);
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) {
					photoUri=null;
					imgPath = null;
				}
			})
			.show();
	}
	
	private void setImageBitmap(ImageView imgView)
	{
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.imgPath);
		} catch (FileNotFoundException e) {
			Log.i("seTitle", "FileNotFoundException");
			return;
		}
		imgView.setImageBitmap(ImgUtils.readBitMap(fis));
	}
	
	private void onLoadImg(final String title)
	{
		ProgressDialogAsyncTask task = new ProgressDialogAsyncTask(this.context,
				new Runnable()
		{
			@Override
			public void run() {
				UserImg ui = new UserImg();
				ui.setFileName(imgPath);
				ui.setName(title);
				
				ui = listener.onLoadImg(ui);
				
				if(ui!=null)
				{
					imgs.add(0, ui);
					handler.sendEmptyMessage(UPDATE_IMGS_MESSAGE);
				}
					
			}

		},"提示","图片上传中...");
		task.execute();
	
	}

	private final class ImgIntentAsyncTask extends AsyncTask<String,Integer,Uri>
	{
		private Context context;
		private ProgressDialog progressBar;
		
		public ImgIntentAsyncTask(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected Uri doInBackground(String... params) {
			try {
				return listener.getImage(params[0], params[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPreExecute() {
			this.progressBar = new ProgressDialog(this.context);
			this.progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.progressBar.setTitle("提示");
			this.progressBar.setMessage("读取文件中...");
			this.progressBar.setIndeterminate(false);
			this.progressBar.setCancelable(true);
			
			this.progressBar.show();
		}

		@Override
		protected void onPostExecute(Uri result) 
		{
			super.onPostExecute(result);
			if(result!=null)
			{
				if(this.progressBar!=null)
					this.progressBar.dismiss();
				
				Intent i = new Intent("android.intent.action.VIEW");
				i.addCategory("android.intent.category.DEFAULT");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				i.setDataAndType(result, "image/*");
				
				this.context.startActivity(i);
			}
		}
	}

	@Override
	public Uri getImage(String img) throws Exception{
		return listener.getImage(Application.HTTP_USER_IMG_FILE_PATH, img);
	}
	
	public interface UserImgsListener
	{
		public List<UserImg> getUserImgs();

		public Uri getImage(String path,String fileName) throws Exception;
		
		public UserImg onLoadImg(UserImg ui);
	}
}
