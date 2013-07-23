package com.joyplus.tv.Service.Return;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ReturnTops implements Serializable {

	public Tops[] tops;

	/*
	 * { "tops": [ { "id": 榜单id, "name": 榜单名, "prod_type": 榜单中视频类别
	 * 1：电影，2：电视剧，3：综艺，4：视频 "pic_url": 榜单图片 "content": 榜单介绍 "items": [ { "id":
	 * "id", "prod_id": 视频id, "prod_name": 视频名称, "prod_type": 视频类别
	 * 1：电影，2：电视剧，3：综艺，4：视频 "prod_pic_url": 视频图片 "stars": 主演, "directors": 导演,
	 * "favority_num": 收藏数, "support_num": 顶数 "publish_date": 上映日期, "score":
	 * 豆瓣积分, "area": 地区
	 * 
	 * }, ] } ] }
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Tops {

		public String id;
		public String name;
		public String prod_type;
		public String pic_url;
		public String content;
		public String big_pic_url;
		public String num;
		public Items[] items;

		public static class Items {
			public String id;
			public String prod_id;
			public String definition;
			public String prod_name;
			public String prod_type;
			public String prod_pic_url;
			public String big_prod_pic_url;
//			// 综艺多了个 : 最新剧集
//			public String cur_item_name;
			public String stars;
			public String directors;
			public String favority_num;
			public String support_num;
			public String publish_date;
			public String score;
			public String area;
			public String max_episode;
			public String cur_episode;
			public String duration;
		}

	}

}
