package com.hope.kqt.android.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppValues 
{
	private int version;
	private String versionString;
	
	public AppValues(PackageManager manager,String packagename) throws NameNotFoundException
	{
		PackageInfo pkInfo = manager.getPackageInfo(packagename, 0);
		if(pkInfo!=null)
		{
			this.version = pkInfo.versionCode;
			this.versionString = pkInfo.versionName;
		}
	}

	public int getVersion() {
		return version;
	}

	public String getVersionString() {
		return versionString;
	}
	
	
}
