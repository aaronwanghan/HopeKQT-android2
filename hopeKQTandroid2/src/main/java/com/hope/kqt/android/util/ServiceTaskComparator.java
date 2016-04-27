package com.hope.kqt.android.util;

import java.util.Comparator;

import com.hope.kqt.android.service.ServiceTask;

public class ServiceTaskComparator implements Comparator<ServiceTask> {

	@Override
	public int compare(ServiceTask st1, ServiceTask st2) 
	{
		if(st1==null && st2==null)
			return 0;
		
		if(st1==null && st2!=null)
			return 1;
		
		if(st1!=null && st2==null)
			return -1;
		
		long time1 = st1.getTime();
		long time2 = st2.getTime();
		
		if(time1==time2)
			return 0;
		
		if(time1>time2)
			return 1;
		
		return -1;
	}

}
