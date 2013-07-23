package com.joyplus.tv.Service.Return;

/*{
	   histories: [
	      {
	          "prod_type": 视频类别 1：电影，2：电视剧，3：综艺，4：视频
	          "prod_name": 视频名字,
	          "prod_subname": 视频的集数,
	          "prod_id": 视频id,
	          "create_date": 播放时间
	          "play_type": 播放的类别  1: 视频地址播放 2:webview播放
	          "playback_time": 上次播放时间，单位：秒,
	          "video_url": 视频地址,
	          "duration": 视频时长， 单位：秒
	          "id" : 播放记录id
	          "prod_pic_url": 视频图片,
	          "big_prod_pic_url": 视频图片（tv版）,
	          "definition": 清晰度，5：超清，4：高清,3：超鲜
	          "prod_summary": 视频描述,
	          "stars": 演员,
	          "directors": 导演,
	          "favority_num": 收藏数,
	          "support_num": 顶数,
	          "publish_date": 上线日期,
	          "score": 豆瓣积分,
	          "area": 地区,
	          "max_episode": 最大集数,
	          "cur_episode": 当前集数
	      },
	     .......
	   ]
	 }*/
public class ReturnUserPlayHistories {
	public Histories[] histories;

	public static class Histories {

		public String prod_type;
		public String prod_name;
		public String prod_subname;
		public String prod_id;
		public String create_date;
		public String play_type;
		public String playback_time;
		public String video_url;
		public String duration;
		public String id;
		public String prod_pic_url;
		public String big_prod_pic_url;
		public String definition;
		public String prod_summary;
		public String stars;
		public String directors;
		public String favority_num;
		public String support_num;
		public String publish_date;
		public String score;
		public String area;
		public String max_episode;
		public String cur_episode;

	}
}
