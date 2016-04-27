package com.hope.kqt.android;

/**
 * 系统参数
 * @author aaronwang
 *
 */
public class Application
{
	//服务器地址 http://219.238.241.88
	public static final String SERVER_IP = "http://219.238.241.86";

	//请求超时时间
	public static final int TIME_OUT_CONNECTION = 5000;
	
	public static final String HTTP_USER_LOGIN_PATH = "/kqt/r/user/login";
	public static final String HTTP_USER_UPDATE_PATH = "/kqt/r/user/update";
	public static final String HTTP_USER_VERSION_PATH = "/kqt/r/user/version";
	
	public static final String HTTP_USER_TASKMENUSS_PATH = "/kqt/r/task/usertasks";
	public static final String HTTP_USER_TASKS_VERSION_PATH = "/kqt/r/task/version";
	
	public static final String HTTP_USER_MEDICINE_PATH = "/kqt/r/user/medicines";
	public static final String HTTP_ADD_USER_MEDICINE_PATH = "/kqt/r/user/medicine/add";
	public static final String HTTP_USER_MEDICINE_RECORDS_PATH = "/kqt/r/user/medicine/records";
	public static final String HTTP_ADD_USER_MEDICINE_RECORDS_PATH = "/kqt/r/user/medicine/record/adds";
	public static final String HTTP_ADD_USER_MEDICINE_RECORD_PATH = "/kqt/r/user/medicine/record/add";
	public static final String HTTP_DELETE_USER_MEDICINE_RECORD_PATH = "/kqt/r/user/medicine/record/delete";
	
	public static final String HTTP_USER_MLOG_PATH ="/kqt/r/user/medicine/logs";
	
	public static final String HTTP_USERIMGS_PATH = "/kqt/r/img/userimgs";
	
	public static final String HTTP_USER_IMG_FILE_PATH = "/kqt/r/img/user/f";
	public static final String HTTP_USER_IMG_PATH = "/kqt/r/img/user";
	public static final String HTTP_TEACHER_IMG_PATH = "/kqt/r/img/teacher";
	
	public static final String HTTP_ADD_USER_IMG_PATH = "/kqt/r/img/userimg/add";
	public static final String HTTP_DELETE_USER_IMG_PATH = "/kqt/r/img/userimg/delete";
	
	public static final String HTTP_ADD_USER_TEST_LOG_PATH = "/kqt/r/user/usertestlog/add";
	public static final String HTTP_USER_TEST_LOGS_PATH = "/kqt/r/user/usertestlogs";
	
	public static final String HTTP_ADD_USER_STAGE_SUMMARY_PATH = "/kqt/r/user/stagesummary/add";
	public static final String HTTP_USER_STAGE_SUMMARYS_PATH = "/kqt/r/user/stagesummarys";
	
	public static final String HTTP_ADD_USER_LOG_PATH = "/kqt/r/user/userlog/add";
	public static final String HTTP_USER_LOGS_PATH = "/kqt/r/user/userlogs";
	
	public static final String HTTP_USER_TEST_ITMES_PATH = "/kqt/r/user/usertestitems";
	
	public static final String HTTP_USER_LOG_DATA_PATH = "/kqt/r/user/userdata";
	
	public static final String HTTP_APP_LAST_PATH = "/kqt/r/app/user/last";
	
	public static final String HTTP_HELP_MESSAGES_PATH = "/kqt/r/help/messages";
	
	public static final String FIRST_OPEN_SYSTEM_KEY ="first_open";
	
	public static final String USER_TREATMENT_VERSION_SYSTEM_KEY = "user_treatment_version";
	public static final String USER_SYSTEM_KEY = "user";
	public static final String USER_ALERT_SETTINGS_KEY = "user_alert_settings";
	
	public static final String SQL_DB = "kqt_db";

	public static final String SERVICE_ACTION = "tasks_service_action";
	
	public static final String FILE_SAVE_PATH = "/download";
	
	public static final int SQL_VERSION = 2;
}
