package com.joyplus.tv;

import android.os.Environment;

public class Constant {
	
	public static final boolean cacheMode = true;
	public static final boolean ANIMATION = true;
	
	public static String LOADING_ADV_PUBLISHERID = "42906cd89";
	public static String MAIN_ADV_PUBLISHERID = "2e8e2bdbed";
	public static String PLAYER_ADV_PUBLISHERID = "ad51ed4ef2";

	
	public static String BASE_URL = "localhost/joyplus-social/index.php/";
	public static String APPKEY = "72dbdcec3b85d5d21c6777c696bc6aa3";

	public static final String VIDEOPLAYERCMD = "com.joyplus.tv.videoservicecommand";

	
	
	public static final String[] video_dont_support_extensions = { ".m3u",".m3u8" };
	public static final String[] video_index = { "wangpan", "le_tv_fee",
			"letv", "fengxing", "qiyi", "youku", "sinahd", "sohu", "56", "qq","pptv", "m1905" };
	public static final String BAIDU_WANGPAN = "baidu_wangpan";

	public static final String[] player_quality_index = { "hd2", "mp4", "3gp","flv" };

	//模拟firefox发送请求
	public static final String USER_AGENT_IOS = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7";
	public static final String USER_AGENT_ANDROID = "Mozilla/5.0 (Linux; U; Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	public static final String USER_AGENT_FIRFOX = "	Mozilla/5.0 (Windows NT 6.1; rv:19.0) Gecko/20100101 Firefox/19.0";
	
	//图片路径
//	public static String PATH = Environment.getExternalStorageDirectory()
//			+ "/joy/image_cache/";
	//用此目录用户清除程序时，可以删掉缓存信息
	public static String PATH = Environment.getExternalStorageDirectory()
	+ "/Android/data/com.joyplus.tv/image_cache";
	public static String PATH_BIG_IMAGE = Environment.getExternalStorageDirectory()
	+ "/Android/data/com.joyplus.tv/bg_image_cache";
	public static String PATH_HEAD = Environment.getExternalStorageDirectory()
			+ "/joy/admin/";

}
