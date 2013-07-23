package com.joyplus.tv.Service.Return;

/**
 * {
    "items": [         
        {
        "id": item id,
        "prod_id": 视频id,
        "prod_name": 视频名,
        "prod_type": 视频类别，3：综艺,
        "prod_pic_url": 海报url,
        "stars": 演员,
        "directors": 导演,
        "favority_num": 收藏数,
        "support_num": 支持数,
        "publish_date": 发布日期,
        "score": 豆瓣积分,
        "area": 地区,
        "cur_episode": 当前剧集名字
        "definition": 清晰度 5：超清，4：高清，3:尝鲜 其他的值为标清。
        "prod_summary":视频简介
        "duration": 时长

      },
       .....
     ]
   }
 */
public class ReturnMainHot {

	public HotItems[] items;

	public static class HotItems {

		public String id;
		public String prod_id;
		public String prod_name;
		public String prod_type;
		public String prod_pic_url;
		public String big_prod_pic_url;
		public String stars;
		public String directors;
		public String favority_num;
		public String support_num;
		public String publish_date;
		public String score;
		public String area;
		public String cur_episode;
		public String definition;
		public String prod_summary;
		public String duration;
		
		public PLAY_URLS[] play_urls;
		
		public String cur_item_url;
		public String max_episode;
		public String cur_item_name;
		
		
	}
	
	public static class PLAY_URLS{
		public String source;
		public URLS[] urls;
	}
	
	
	public static class URLS{
		public String type;
		public String url;
		public String file;
	}
}
