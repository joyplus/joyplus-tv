package com.joyplus.tv.utils;

public class DataBaseItems {
	
	public static final String ORDER_BY_TIME_DESC = "integer_1 desc";
	public static final String ORDER_BY_TIME_ASC = "integer_1 asc";
	
	public static final String ORDER_BY_ID_DESC = "id desc";
	public static final String ORDER_BY_ID_ASC = "id asc";
	
	public static interface SQLite3_DataType {
		
		
		String INTEGER = " INTEGER ";
		String NULL = " NULL ";
		String REAL = " REAL ";
		String TEXT = " TEXT ";
		String BLOB = " BLOB ";
		
		String INTEGER_DOT = " INTEGER, ";
		String NULL_DOT = " NULL, ";
		String REAL_DOT = " REAL, ";
		String TEXT_DOT = " TEXT, ";
		String BLOB_DOT = " BLOB, ";
	}
	
	public static final int NEW = 1;
	public static final int OLD = 0;

	public static interface UserShouCang {

		String ID = "id";// 自增id key 不同的用户收藏相同的影片

		String USER_ID = "user_id";// 用户id 
		
		String PRO_ID = "prod_id";// 影片id
		String NAME = "name";// 影片名字
		String SCORE = "score";// 影片评分
		String PRO_TYPE = "pro_type";// 影片类型
		String PIC_URL = "pic_url";// 图片url
		String DURATION = "duration";// 时间
		String CUR_EPISODE = "cur_episode";// 当前集数
		String MAX_EPISODE = "max_episode";// 最大集数
		
		String STARS = "stars";//主演
		String DIRECTORS = "directors";//导演
		
		String IS_NEW = "is_new";//同步标记字段 1为new 0为old
		
		//追剧中，用户收藏过，并且有最新更新。
		//更新规则为 当首页首次启动时候，检查网络数据是否有最新集数
		//如果有那就设为true 1，否则一直为false 0
		String IS_UPDATE = "is_update";
		// String NUM = "num";
		
		String TEXT_1 = "text_1";
		String TEXT_2 = "text_2";
		String TEXT_3 = "text_3";
		String TEXT_4 = "text_4";
		
		String INTEGER_1 = "integer_1";//用来存储当前插入的时间
		String INTEGER_2 = "integer_2";
		String INTEGER_3 = "integer_3";
		String INTEGER_4 = "integer_4";
	}
	
	public static interface UserHistory {
		
		String ID = "id";// 自增id
		
		String USER_ID = "user_id";// 用户id
		String PROD_TYPE = "prod_type"; //视频类别 1：电影，2：电视剧，3：综艺，4：视频
		String PROD_NAME= "prod_name";//视频名字,
		String PROD_SUBNAME = "prod_subname";//视频的集数,
		String PRO_ID = "prod_id";//视频id,
//		String CREATE_DATE = "create_date";//播放时间
		String PLAY_TYPE = "play_type";//播放的类别  1: 视频地址播放 2:webview播放
		String PLAYBACK_TIME = "playback_time";//上次播放时间，单位：秒,
		String VIDEO_URL = "video_url";//视频地址,
		String DURATION = "duration";//视频时长， 单位：秒
		String BOFANG_ID = "bofang_id";//播放记录id
		String PROD_PIC_URL = "prod_pic_url";//视频图片,
//		String BIG_PROD_PIC_URL="big_prod_pic_url";//视频图片（tv版）,
		String DEFINITION = "definition";//清晰度，5：超清，4：高清,3：超鲜
//		String PROD_SUMMARY = "prod_summary";//视频描述,
		String STARS = "stars";//演员
		String DIRECTORS = "directors";//导演,
		String FAVORITY_NUM = "favority_num";//收藏数,
		String SUPPORT_NUM = "support_num";//顶数
		String PUBLISH_DATE = "publish_date";
		String SCORE = "score";//豆瓣积分,
		String AREA = "area";//地区,
		String MAX_EPISODE = "max_episode";//最大集数,
		String CUR_EPISODE = "cur_episode";//当前集数
		
		String IS_NEW = "is_new";//同步标记字段 1为new 0为old
		
		String TEXT_1 = "text_1";
		String TEXT_2 = "text_2";
		String TEXT_3 = "text_3";
		String TEXT_4 = "text_4";
		
		String INTEGER_1 = "integer_1";//用来存储当前插入的时间
		String INTEGER_2 = "integer_2";
		String INTEGER_3 = "integer_3";
		String INTEGER_4 = "integer_4";

	}

}
