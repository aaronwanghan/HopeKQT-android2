package com.hope.kqt.android.http.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import com.hope.kqt.android.Application;
import com.hope.kqt.android.http.HttpService;
import com.hope.kqt.android.util.KQTUtils;
import com.hope.kqt.android.util.WebUtils;
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

public class HttpServiceImpl implements HttpService 
{
	@Override
	public User login(String username, String password) throws Exception
	{
		Map<String,String> values = new HashMap<String,String>();

		values.put("username", username);
		values.put("password", password);

		String r = WebUtils.post(Application.HTTP_USER_LOGIN_PATH, values);

		//Log.i("user login", r);

		if(!"{null}".equals(r))
		{
			JSONObject jo = new JSONObject(r);
			return User.valueOf(jo);
		}


		return null;
	}

	@Override
	public UserTreatment findUserTreatment(long uid) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findUserTreatmentVersion(long uid) throws Exception
	{

		String r = WebUtils.get(Application.HTTP_USER_TASKS_VERSION_PATH+"/"+uid);

		if(r!=null && !"".equals(r))
		{
			JSONObject values = new JSONObject(r);
			return values.getInt("version");
		}


		return -1;
	}

	@Override
	public List<UserTaskMenus> findUserTaskByUserId(long uid) throws Exception
	{
		List<UserTaskMenus> list = new ArrayList<UserTaskMenus>();


		String r = WebUtils.get(Application.HTTP_USER_TASKMENUSS_PATH+"/"+uid);
		JSONArray array = new JSONArray(r);
		for(int i=0;i<array.length();i++)
		{
			JSONObject jo = array.getJSONObject(i);
			list.add(UserTaskMenus.valueOf(jo));
		}

		return list;
	}

	@Override
	public List<UserMedicineLog> findUserMedicineItemsByUserId(long uid) throws Exception
	{
		List<UserMedicineLog> list = new ArrayList<UserMedicineLog>();


		String r = WebUtils.get(Application.HTTP_USER_MEDICINE_PATH+"/"+uid);

		JSONArray array = new JSONArray(r);
		for(int i=0;i<array.length();i++)
		{
			JSONObject jo = array.getJSONObject(i);
			list.add(UserMedicineLog.valueOf(jo));
		}



		return list;
	}

	@Override
	public List<UserImg> findUserImgByUserId(long uid) throws Exception
	{
		List<UserImg> list = new ArrayList<UserImg>();


		String r = WebUtils.get(Application.HTTP_USERIMGS_PATH+"/"+uid);

		JSONArray array = new JSONArray(r);
		for(int i=0;i<array.length();i++)
			list.add(UserImg.valueOf(array.getJSONObject(i)));



		return list;
	}

	@Override
	public Uri getImage(String path,String fileName, File cache) throws Exception
	{
		File file = new File(cache,"img_"+fileName.hashCode());

		//Log.i("file", file.getPath());

		if(file.exists())
			return Uri.fromFile(file);


		InputStream is = WebUtils.getInputStream(path+"/"+fileName);

		//Log.i("img", r);

		if(is!=null)
		{
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len = 0;

			while((len=is.read(buf))!=-1)
			{
				fos.write(buf, 0, len);
			}

			is.close();

			fos.close();

			return Uri.fromFile(file);
		}


		return null;
	}

	@Override
	public UserImg addUserImg(UserImg img,long uid) throws Exception
	{
		List<String> list = new ArrayList<String>();
		list.add(img.getFileName());


		Map<String,String> datas = new HashMap<String,String>();

		datas.put("img_user_id", uid+"");
		datas.put("img_title", KQTUtils.parseByte2HexStr(img.getName().getBytes()));

		String r = WebUtils.post(Application.HTTP_ADD_USER_IMG_PATH, list,datas);

		if(r!=null && !"".equals(r))
			return UserImg.valueOf(new JSONObject(r));


		return null;
	}

	@Override
	public boolean addUserTestLog(List<UserTestLog> logs) throws Exception
	{
		if(logs==null || logs.isEmpty())
			return true;

		JSONArray array = new JSONArray();


		for(UserTestLog log:logs)
			array.put(log.out());

		Map<String,String> data = new HashMap<String,String>();
		data.put("v", array.toString());

		String r = WebUtils.post(Application.HTTP_ADD_USER_TEST_LOG_PATH, data);
		if("{ok}".equals(r))
			return true;



		return false;
	}

	@Override
	public List<UserTestLog> findUserTestLogByUserIdAndTestItemId(long uid,
			long tid) throws Exception
			{
		StringBuffer path = new StringBuffer(Application.HTTP_USER_TEST_LOGS_PATH);

		path.append("/").append(uid).append("/").append(tid);


		String r = WebUtils.get(path.toString());
		JSONArray array = new JSONArray();
		if(array.length()>0)
		{
			List<UserTestLog> list = new ArrayList<UserTestLog>();
			for(int i=0;i<array.length();i++)
			{
				UserTestLog log = new UserTestLog();
				log.in(array.getJSONObject(i));
				list.add(log);
			}

			return list;
		}


		return null;
			}

	@Override
	public boolean addUserStageSummary(UserStageSummary uss) throws Exception
	{
		Map<String,String> datas = new HashMap<String,String>();
		datas.put("content", KQTUtils.parseByte2HexStr(uss.getContent().getBytes()));
		//datas.put("title", KQTUtils.parseByte2HexStr(uss.getTitle().getBytes()));
		datas.put("stage_num", uss.getStageNum()+"");
		datas.put("score", uss.getScore());
		datas.put("task_time", uss.getTaskTime()+"");
		datas.put("uid", uss.getUser().getId()+"");


		String r = WebUtils.post(Application.HTTP_ADD_USER_STAGE_SUMMARY_PATH, datas);

		if(r!=null && "{ok}".equals(r))
			return true;


		return false;
	}

	@Override
	public boolean addUserlog(UserLog log) throws Exception
	{
		Map<String,String> datas = new HashMap<String,String>();
		datas.put("uid", log.getUser().getId()+"");
		datas.put("content",KQTUtils.parseByte2HexStr(log.getContent().getBytes()));
		datas.put("type", log.getType()+"");
		datas.put("task_time", log.getCreateDate()+"");


		String r = WebUtils.post(Application.HTTP_ADD_USER_LOG_PATH, datas);

		if(r!=null && "{ok}".equals(r))
			return true;


		return false;
	}

	@Override
	public List<UserTestItem> findUserTestItemByUserId(long uid) throws Exception
	{
		List<UserTestItem> list = new ArrayList<UserTestItem>();



		String r = WebUtils.get(Application.HTTP_USER_TEST_ITMES_PATH+"/"+uid);

		if(r!=null && !"".equals(r))
		{
			JSONArray array = new JSONArray(r);
			if(array!=null && array.length()>0)
				for(int i=0;i<array.length();i++)
					list.add(UserTestItem.valueOf(array.getJSONObject(i)));

		}


		return list;
	}

	@Override
	public List<UserLog> findUserLogByUserid(long uid) throws Exception
	{
		List<UserLog> list = new ArrayList<UserLog>();



		String r = WebUtils.get(Application.HTTP_USER_LOGS_PATH+"/"+uid+"/1");

		if(r!=null && !"".equals(r))
		{
			JSONArray array = new JSONArray(r);
			if(array!=null && array.length()>0)
				for(int i=0;i<array.length();i++)
					list.add(UserLog.valueOf(array.getJSONObject(i)));

		}


		return list;
	}

	@Override
	public List<UserStageSummary> findUserStageSummaryByUserId(long uid) throws Exception
	{
		List<UserStageSummary> list = new ArrayList<UserStageSummary>();


		String r = WebUtils.get(Application.HTTP_USER_STAGE_SUMMARYS_PATH+"/"+uid);

		if(r!=null && !"".equals(r))
		{
			JSONArray array = new JSONArray(r);
			if(array!=null && array.length()>0)
				for(int i=0;i<array.length();i++)
					list.add(UserStageSummary.valueOf(array.getJSONObject(i)));

		}

		return list;
	}

	@Override
	public List<UserMLog> findUserMLogByUseridAndMedicineId(long uid, long mid) throws Exception
	{
		List<UserMLog> list = null;


		String r = WebUtils.get(Application.HTTP_USER_MLOG_PATH+"/"+uid+"/"+mid);

		if(r!=null && !"".equals(r))
		{
			JSONArray array = new JSONArray(r);
			if(array.length()==0)
				return list;

			list = new ArrayList<UserMLog>();

			for(int i=0;i<array.length();i++)
				list.add(UserMLog.valueOf(array.getJSONObject(i)));
		}


		return list;
	}

	@Override
	public List<UserMedicineLogRecord> findUserMedicineLogRecordAndUserAndTaskTime(
			long uid, long taskTime) throws Exception
			{
		List<UserMedicineLogRecord> list = null;


		String r = WebUtils.get(Application.HTTP_USER_MEDICINE_RECORDS_PATH+"/"+uid+"/"+taskTime);

		if(r!=null && !"".equals(r))
		{
			JSONArray array = new JSONArray(r);
			if(array.length()==0)
				return list;

			list= new ArrayList<UserMedicineLogRecord>();

			for(int i=0;i<array.length();i++)
				list.add(UserMedicineLogRecord.valueOf(array.getJSONObject(i)));
		}


		return list;
			}

	@Override
	public boolean addUserMedicineLogRecords(List<UserMedicineLogRecord> logs) throws Exception
	{
		JSONArray array = new JSONArray();


		for(UserMedicineLogRecord log:logs)
		{
			JSONObject obj = new JSONObject();
			obj.put("id", log.getId());
			obj.put("mid", log.getMedicine().getId());
			obj.put("uid", log.getUser().getId());
			obj.put("create_date", log.getCreateDate());
			obj.put("num", log.getNum());
			obj.put("task_time", log.getTaskTime());

			array.put(obj);
		}


		Map<String,String> datas = new HashMap<String,String>();

		datas.put("s", array.toString());

		String s = WebUtils.post(Application.HTTP_ADD_USER_MEDICINE_RECORDS_PATH, datas);

		if("{ok}".equals(s))
			return true;



		return false;
	}

	@Override
	public UserMedicineLogRecord addUserMedicineLogRecord(
			UserMedicineLogRecord l) throws Exception
			{
		Map<String,String> datas = new HashMap<String,String>();

		datas.put("id", l.getId()+"");
		datas.put("task_time", l.getTaskTime()+"");
		datas.put("create_date", l.getCreateDate()+"");
		datas.put("uid", l.getUser().getId()+"");
		datas.put("mid", l.getMedicine().getId()+"");
		datas.put("num", l.getNum()+"");


		String s = WebUtils.post(Application.HTTP_ADD_USER_MEDICINE_RECORD_PATH, datas);

		if(s!=null && !"".equals(s))
		{
			JSONObject obj = new JSONObject(s);
			l.setId(obj.getLong("id"));
			return l;
		}


		return null;
			}

	@Override
	public boolean deleteUserMedicineLogRecord(long lid) throws Exception
	{

		String s = WebUtils.get(Application.HTTP_DELETE_USER_MEDICINE_RECORD_PATH+"/"+lid);

		if(s!=null && !"".equals(s))
			return true;



		return false;
	}

	@Override
	public UserMedicineLog addUserMedicineLog(long uid, String mName,
			String dName,String unit) throws Exception
			{
		Map<String,String> datas = new HashMap<String,String>();

		datas.put("uid", uid+"");
		datas.put("m_name", KQTUtils.parseByte2HexStr(mName.getBytes()));
		datas.put("d_name", KQTUtils.parseByte2HexStr(dName.getBytes()));
		datas.put("unit", KQTUtils.parseByte2HexStr(unit.getBytes()));

		String s = WebUtils.post(Application.HTTP_ADD_USER_MEDICINE_PATH, datas);

		if(s!=null && !"".equals(s))
		{
			return UserMedicineLog.valueOf(new JSONObject(s));
		}



		return null;
			}

	@Override
	public boolean deleteUserImg(long uid, String fileName) throws Exception
	{

		String s = WebUtils.get(Application.HTTP_DELETE_USER_IMG_PATH+"/"+uid+"/"+fileName);

		if("{ok}".equals(s))
			return true;



		return false;
	}

	@Override
	public UserLogData findUserLogDataByUserIdAndTaskTime(long uid,
			long taskTime) throws Exception
			{


		String s = WebUtils.get(Application.HTTP_USER_LOG_DATA_PATH+"/"+uid+"/"+taskTime);

		if(s!=null && !"".equals(s))
			return UserLogData.valueOf(new JSONObject(s));


		return null;
			}

	@Override
	public List<UserMedicineLogsItem> findUserMedicineLogsItemByUserId(long uid) throws Exception
	{
		List<UserMedicineLogsItem> items = new ArrayList<UserMedicineLogsItem>();

		String s = WebUtils.get(Application.HTTP_USER_MLOG_PATH+"/"+uid);

		if(s!=null && !"[]".equals(s) && !"".equals(s))
		{
			JSONArray array = new JSONArray(s);
			for(int i=0;i<array.length();i++)
				items.add(UserMedicineLogsItem.valueOf(array.getJSONObject(i)));
		}

		return items;
	}

	@Override
	public int findUserVersion(long uid) throws Exception
	{
		String s = WebUtils.get(Application.HTTP_USER_VERSION_PATH+"/"+uid);
		
		if(s!=null && !"".equals(s))
		{
			try {
				JSONObject obj = new JSONObject(s);
				int version = obj.getInt("version");
				return version;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return -1;
	}

	@Override
	public App findAppLast() throws Exception 
	{
		String s = WebUtils.get(Application.HTTP_APP_LAST_PATH);
		
		if(s!=null && !"".equals(s) && !"{null}".equals(s))
		{
			try {
				JSONObject obj = new JSONObject(s);
				return App.valueOf(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	@Override
	public List<HelpMessage> findHelpMessageByUserId(long uid) throws Exception 
	{
		List<HelpMessage> list = null;
		
		String s = WebUtils.get(Application.HTTP_HELP_MESSAGES_PATH+"/"+uid);
		
		if(s!=null && !"".equals(s) && !"{null}".equals(s))
		{
			JSONArray array = new JSONArray(s);
			if(array.length()==0)
				return list;
			
			list = new ArrayList<HelpMessage>();
			for(int i=0;i<array.length();i++)
				list.add(HelpMessage.valueOf(array.getJSONObject(i)));
		}
		
		return list;
	}

	@Override
	public User updateUser(long uid, String key) throws Exception 
	{
		String s = WebUtils.get(Application.HTTP_USER_UPDATE_PATH+"/"+uid+"/"+key);
		
		if(s!=null && !"".equals(s) && !"{login}".equals(s))
		{
			Log.i("httpService", "updateUser:"+s);
			return User.valueOf(new JSONObject(s));
		}
			
		
		return null;
	}

}
