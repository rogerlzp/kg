package com.abc.utility;

public class CellSiteConstants {
	
	
	public static final String CELLSITE_CONFIG = "cellsite_config";
	public static final String USER_NAME = "username";
	public static final String PASSWORD = "password";
	public static final String USER_ID = "userId";
	public static final String USER_PORTARIT = "portrait";
	
	public static final String NORMAL_USER_MODE = "regUser";
	
	
	// RESULT
	public final static String LOGIN_RESULT = "login_result";
	public final static String FORGET_PASSWORD_RESULT = "forget_password_result";
	
	
	//RESULT 
	// login
	public final static int LOGIN_SUCC = 10000; // login succeed
	public final static int LOGIN_BAD_USERNAME = 10001; // bad username
	public final static int LOGIN_BAD_PASSWORD = 10002; // bad password
	public static final int UNKNOWN_ERROR = 10050;
	public static final int UNKNOWN_HOST_ERROR = 10051;
	
	// password 
	public final static int CHANGE_PASSWORD_SUCC = 10020;
	public final static int CHANGE_PASSWORD_FAIL = 10021;
	public final static int OLD_PASSWORD_WRONG = 10022;
	public final static int FORGET_PASSWORD_FAIL = 10023;
	public final static int LOGIN_NAME_WRONG = 10024;
	public final static int LOGIN_MAIL_WRONG = 10025;
	
	public final static int SEND_MAIL_SUC = 10026;
	public final static int SEND_MAIL_FAIL = 10027;
	public final static int NOT_AVAILABLE_MAIL  = 10028;
	

	

	
	// ÍøÂçÁ¬½Ó URL 
	public static final String HOST = "http://20120328.welives.sinaapp.com/";
	public static final String LOGIN_URL = HOST + "login.php"; 
	public static final String CHANGE_PASSWORD_URL = HOST + "changePassword.php";
	public static final String FORGET_PASSWORD_URL = HOST + "forgetPassword.php";
	
	
}
