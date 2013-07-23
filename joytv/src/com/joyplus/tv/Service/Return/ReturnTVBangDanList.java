package com.joyplus.tv.Service.Return;

/**
 * TV版，数据返回
 * 
 * @author Administrator
 * 
 */
public class ReturnTVBangDanList {

	/**
	 * app_key required string 申请应用时分配的AppKey。 top_id required string 榜单id
	 * page_num = 需要请求的页码（可选），默认为1 page_size = 每一页包含的记录数（可选），默认为10
	 */

	/*
	 * { "items": [ { "id": 榜单项id, "prod_id": 视频id, "prod_name": 视频名称,
	 * "prod_type": 视频类别, "prod_pic_url": 视频图片 }, ..... ] }
	 */
	public Items[] items;

	public static class Items {
		public String id;
		public String prod_id;
		public String prod_name;
		public String definition;
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
		public String max_episode;
		public String cur_episode;
		public String duration;
	}
}
