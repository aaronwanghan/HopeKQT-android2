package com.hope.kqt.android.http;

import java.io.File;
import java.util.List;

import android.net.Uri;

import com.hope.kqt.entity.App;
import com.hope.kqt.entity.HelpMessage;
import com.hope.kqt.entity.User;
import com.hope.kqt.entity.UserImg;
import com.hope.kqt.entity.UserLog;
import com.hope.kqt.entity.UserLogData;
import com.hope.kqt.entity.UserMLog;
import com.hope.kqt.entity.UserMedicineLog;
import com.hope.kqt.entity.UserMedicineLogRecord;
import com.hope.kqt.entity.UserMedicineLogsItem;
import com.hope.kqt.entity.UserStageSummary;
import com.hope.kqt.entity.UserTaskMenus;
import com.hope.kqt.entity.UserTestItem;
import com.hope.kqt.entity.UserTestLog;
import com.hope.kqt.entity.UserTreatment;



public interface HttpService 
{
	public User login(String username,String password) throws Exception;
	
	public UserTreatment findUserTreatment(long uid) throws Exception;
	
	public int findUserTreatmentVersion(long utid) throws Exception;
	
	public List<UserTaskMenus> findUserTaskByUserId(long uid) throws Exception;
	
	public List<UserMedicineLog> findUserMedicineItemsByUserId(long uid) throws Exception;
	
	public UserMedicineLog addUserMedicineLog(long uid,String mName,String dName,String unit) throws Exception;
	
	public List<UserMLog> findUserMLogByUseridAndMedicineId(long uid,long mid) throws Exception;
	
	public List<UserMedicineLogRecord> findUserMedicineLogRecordAndUserAndTaskTime(long uid,long taskTime) throws Exception;
	
	public UserMedicineLogRecord addUserMedicineLogRecord(UserMedicineLogRecord l) throws Exception;
	
	public boolean deleteUserMedicineLogRecord(long lid) throws Exception;
	
	public boolean addUserMedicineLogRecords(List<UserMedicineLogRecord> logs) throws Exception;
	
	public List<UserImg> findUserImgByUserId(long uid) throws Exception;
	
	public Uri getImage(String path,String fileName, File cache) throws Exception;
	
	public UserImg addUserImg(UserImg img,long uid) throws Exception;
	
	public boolean deleteUserImg(long uid,String fileName) throws Exception;
	
	public boolean addUserTestLog(List<UserTestLog> logs) throws Exception;
	
	public List<UserTestLog> findUserTestLogByUserIdAndTestItemId(long uid,long tid) throws Exception;
	
	public boolean addUserStageSummary(UserStageSummary uss) throws Exception;
	
	public boolean addUserlog(UserLog log) throws Exception;
	
	public List<UserTestItem> findUserTestItemByUserId(long uid) throws Exception;
	
	public List<UserLog> findUserLogByUserid(long uid) throws Exception;
	
	public List<UserStageSummary> findUserStageSummaryByUserId(long uid) throws Exception;
	
	public UserLogData findUserLogDataByUserIdAndTaskTime(long uid,long taskTime) throws Exception;
	
	public List<UserMedicineLogsItem> findUserMedicineLogsItemByUserId(long uid) throws Exception;
	
	public int findUserVersion(long uid) throws Exception;
	
	public App findAppLast() throws Exception;
	
	public List<HelpMessage> findHelpMessageByUserId(long uid) throws Exception;
	
	public User updateUser(long uid,String key) throws Exception;
}
