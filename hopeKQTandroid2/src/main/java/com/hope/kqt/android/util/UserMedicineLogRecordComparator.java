package com.hope.kqt.android.util;

import java.util.Comparator;

import com.hope.kqt.entity.UserMedicineLogRecord;

public class UserMedicineLogRecordComparator implements
		Comparator<UserMedicineLogRecord> {

	@Override
	public int compare(UserMedicineLogRecord l1, UserMedicineLogRecord l2) 
	{
		if(l1==null && l2==null)
			return 0;
		
		if(l1==null && l2!=null)
			return 1;
		
		if(l1!=null && l2==null)
			return -1;
		
		long time1 = l1.getCreateDate();
		long time2 = l2.getCreateDate();
		
		if(time1==time2)
			return 0;
		
		if(time1>time2)
			return 1;
		
		return -1;
	}

}
