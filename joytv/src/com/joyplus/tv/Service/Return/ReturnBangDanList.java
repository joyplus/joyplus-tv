package com.joyplus.tv.Service.Return;

public class ReturnBangDanList {
	/*
	 * { "items": [ { "id": 榜单项id, "prod_id": 视频id, "prod_name": 视频名称,
	 * "prod_type": 视频类别, "prod_pic_url": 视频图片 }, ..... ] }
	 */
	public Items[] items;

	public static class Items {
		public String id;
		public String prod_id;
		public String prod_name;
		public String prod_type;
		public String prod_pic_url;
	}
}
