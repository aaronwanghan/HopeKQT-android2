package com.hope.kqt.android;

import java.io.File;

import com.hope.kqt.android.util.UpdateAppHelper;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VersionActivity extends KqtActivity implements View.OnClickListener
{
	private TextView versionString;
	private Button updateButton;
	private ProgressBar progressBar;
	
	private UpdateAppHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.version);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kqt_titlebtn);
		TextView title = (TextView) this.findViewById(R.id.kqt_text);
		title.setText("关于1°健康人生");
		Button exit = (Button) this.findViewById(R.id.kqt_exit_button);
		//this.iconsLayout = (LinearLayout) this.findViewById(R.id.kqt_user_icons_layout);
		//this.setIcons();
		
		exit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		this.versionString = (TextView) this.findViewById(R.id.version_string);
		this.updateButton = (Button) this.findViewById(R.id.version_update_button);
		this.progressBar = (ProgressBar) this.findViewById(R.id.version_progress_bar);
		
		String cachePath = Environment.getExternalStorageDirectory()+Application.FILE_SAVE_PATH;
        
        File f = new File(cachePath);
        if(!f.exists())
        	f.mkdirs();
		
		try {
			this.helper = new UpdateAppHelper(this,this.httpService,super.handler,cachePath);
			this.helper.setProgressBar(progressBar);
		} catch (Exception e) {
			this.finish();
		}
		
		if(this.helper!=null)
		{
			this.versionString.setText(this.helper.getVersionString());
			this.updateButton.setOnClickListener(this);
		}
	}
	
	@Override
	public void onClick(View v) 
	{
		v.setEnabled(false);
		helper.update();
	}
}
