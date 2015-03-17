package com.abc.huoyun.utility;


public class CellSiteConstants {

	public static final String CELLSITE_CONFIG = "cellsite_config";
	public static final String USER_NAME = "username";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	
	public static final String USER = "user";
	public static final String PROFILE = "profile";
	public static final String MOBILE = "mobile";
	public static final String USER_ID = "user_id";
	public static final String USER_PORTARIT = "portrait";
	public static final String PROFILE_IMAGE_URL = "profile_image_url";
	public static final String DRIVER_LICENSE_URL = "driver_license_image_url";
//	public static final String DRIVER_LICENSE_URL = "driver_license_image_url";
	
	public static final String NORMAL_USER_MODE = "regUser";
	public static final String VERIFY_CODE = "verify_code";

	public static final String CARGO_TYPE = "ctype";
	public static final String CARGO_WEIGHT = "cweight";
	public static final String CARGO_VOLUME = "cvolume";
	
	// 
	public static final String ID = "id";
	public static final String TRUCK = "truck";
	public static final String TRUCK_ID = "truck_id";
	public static final String TRUCK_TYPE = "ttype_id";
	public static final String TRUCK_LENGTH = "tlength_id";
	public static final String TRUCK_WEIGHT = "tweight_id";
	public static final String TRUCK_STATUS = "tstatus_id";
	public static final String TRUCK_AUDIT_STATUS = "taudit_status_id";
	public static final String TRUCK_LICENSE_URL = "tl_image_url";
	public static final String TRUCK_MOBILE_NUM = "tmobile_num";
	public static final String TRUCK_IMAGE_URL = "tphoto_image_url";
	public static final String TRUCK_LICENSE = "tlicense";

	
	public static final String SHIPPER_ADDRESS_CODE = "sa_code";
	public static final String SHIPPER_DATE = "shipper_date";
	public static final String SHIPPER_USERNAME = "shipper_username";
	public static final String CONSIGNEE_ADDRESS_CODE = "ca_code";
	public static final String HORDER_DESCRIPTION = "horder_desc";

	// RESULT
	public final static String LOGIN_RESULT = "login_result";
	public final static String FORGET_PASSWORD_RESULT = "forget_password_result";

	// RESULT
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
	public final static int NOT_AVAILABLE_MAIL = 10028;

	// result info
	public final static int TAKE_PICTURE = 1001;
	public final static int PICK_PICTURE = 1002;
	public final static int CROP_PICTURE = 1010;
	
// image corp size
	public static final int IMAGE_WIDTH = 180;
	public static final int IMAGE_HEIGHT = 180;
	
	// down horder
	public static final int NORMAL_OPERATION = 0; 
	public static final int MORE_OPERATION = 1; 
	// horder status 
	public static final String HORDER_STATUS = "horder_status"; 
	public static final int HORDER_WAITING = 0; 
	public static final int HORDER_SENT = 1; 
	public static final int HORDER_COMPLEMENTED = 2; 
	// horder page count
	public static final int PAGE_COUNT = 3;
	

	// �������� URL
	// public static final String HOST = "http://20120328.welives.sinaapp.com/";
	 public static final String HOST = "http://115.28.175.222:9091/";
	// public static final String HOST = "http://192.168.2.106:9003/";
	public static final String LOGIN_URL = HOST + "login/fmobile";
	public static final String CHANGE_PASSWORD_URL = HOST
			+ "changePassword.php";
	public static final String FORGET_PASSWORD_URL = HOST
			+ "forgetPassword.php";

	// image url for user
	public final static String UPDATE_DRIVER_LICENSE_URL = HOST
			+ "updateDriverLicense/fmobile";
	public final static String UPDATE_USER_PORTRAIT_URL = HOST
			+ "updateUserPortrait/fmobile";
	public final static String UPDATE_USERNAME_URL = HOST
			+ "updateUsername/fmobile";
	
	

	// get my horder url
	public static final String GET_MY_HORDER_URL = HOST + "getMyHorder/fmobile";
	public static final String GET_HORDER_URL = HOST + "getHorder/fmobile";
	
	public final static String USER_IMAGE_URL = HOST + "";// TOOD
	
	public final static String USER_QUERY_URL = HOST + "getUserProfile/fmobile";
	// 用户注册
	 public static final String REGISTER_URL = HOST + "register/fmobile";

	// driver     注册
	// public static final String REGISTER_URL = HOST + "registerDriver/fmobile";
	public static final String VERIFY_URL = HOST + "verify/fmobile";
	// public static final String REGISTER_URL =
	// "http://115.28.175.222:9091/test1.php" ;
	// HOST + "test1.php";
	public static final String GET_TRUCK_URL = HOST + "getTruck/fmobile";
	
	
	public static final String GET_TRUCKS_URL = HOST + "getTrucks/fmobile";
	
	public static final String CREATE_HORDER_URL = HOST
			+ "createHorder/fmobile";
	// public final static String REGISTER_RESULT = "register_result";

	// TRUCK
	public static final String UPDATE_TRUCK_URL = HOST + "updateTruck/fmobile";

	public final static int REGISTER_SUCC = 10000; // login succeed
	public final static int REGISTER_FAIL = 10001; // bad username
	public final static int REGISTER_USER_EXISTS = 10002; // bad username
	public final static int REGISTER_INTEGRATION_ERROR = 10003; // bad username

	public static final String VERIFY_PHONE_NUMBE_URL = "http://m.youhubst.com/phone_cz.php?phone=";

	public final static String RESULT_CODE = "result_code";
	public final static int RESULT_SUC = 0;
	public final static String RESULT = "result";

	// Truck 类型
	public final static String[] TruckTypes = { "厢式车", "平板车", "高低板车", "高栏车",
			"中栏车", "低栏车", "罐式车", "冷藏车", "保温车", "危险品车", "铁笼车", "集装箱", "自卸货车",
			"其他车型" };
	
	public final static String[] TruckLengths = { "4.2米", "5.2米", "5.8米", "6.2米",
		"6.5米", "6.8米", "7.2米", "8米", "9.6米", "12米", "13米", "13.5米", "15米",
		"16.5米", "17.5米" , "18.5米" , "20米" , "22米", "24米"   };
	

}
