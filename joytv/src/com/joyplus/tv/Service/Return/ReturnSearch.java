package com.joyplus.tv.Service.Return;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ReturnSearch {

	public Results[] results;

	/*
	 * { "results": [ { "prod_id": 节目id "prod_name": 节目名 "prod_type":
	 * 节目类型，1：电影，2：电视剧，3：综艺节目 "prod_pic_url": 海报地址 "prod_sumary": 介绍 "star": 主演
	 * "director": 导演 "favority_num": 收藏数, "support_num": 顶数 "publish_date":
	 * 上映日期, "score": 豆瓣积分, "area": 地区 }, ] }
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Results {

		public String prod_id;
		public String prod_name;
		public String prod_type;
		public String prod_pic_url;
		public String prod_sumary;
		public String star;
		public String director;
		public String favority_num;
		public String support_num;
		public String publish_date;
		public String area;
		public String score;

	}
}
