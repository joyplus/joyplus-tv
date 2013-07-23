package com.joyplus.tv.Service.Return;

/*
 * {
 watchs: [
 {
 "content_id": 节目id
 "content_name": 节目名
 "content_pic_url": 节目的海报
 "content_type": 节目的类型 1：电影，2：电视剧，3：综艺节目
 "reason"：  推荐原因
 "create_date"：  推荐时间
 },
 .......
 ]
 }
 */
public class ReturnUserRecommends {
	public Recommends[] recommends;

	public static class Recommends {

		public String content_id;
		public String content_name;
		public String content_pic_url;
		public String content_type;
		public String reason;
		public String create_date;

	}
}
