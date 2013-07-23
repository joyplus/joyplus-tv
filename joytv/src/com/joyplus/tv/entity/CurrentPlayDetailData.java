package com.joyplus.tv.entity;

public class CurrentPlayDetailData {
//	public String prod_id;
//	public String prod_name;//视频名称
//	public String prod_sub_name;//视频子名称
//	public String prod_url;//播放地址
//	public String prod_src;//来源
//	public long prod_time;//开始播放时间, 秒*1000 ,单位是毫秒
//	public int prod_qua;//清晰度
//	public boolean prod_favority;//是否收藏
//	
//	public int prod_type;//视频类别 1：电影，2：电视剧，3：综艺，131动漫
//	public int CurrentIndex = 0;//电视剧和综艺用，index序号
//	public int CurrentSource = 0;//视频来源,比如乐视风行的index序号
//	public int CurrentQuality = 0;//视频清晰度,比如高清，普清，超清的index序号
	
	public String prod_id = "-1";//如果未知则为-1
	public String prod_name;//视频名称
	public String prod_sub_name;//当前集数对应的name
	public String prod_url;//播放地址
	public String prod_src;//来源
	public long prod_time = 0;//开始播放时间, 秒*1000 ,单位是毫秒
	public int prod_qua = 8;//清晰度 6为尝鲜，7为普清，8为高清
	public boolean prod_favority = false;//是否收藏
	public int prod_type  = 1;//视频类别 1：电影，2：电视剧，3：综艺，131动漫
	public boolean hasReturnData = false;//是否有详情信息
}
