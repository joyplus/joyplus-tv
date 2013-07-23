package com.joyplus.tv.Service.Return;

/*
 *    {
 recommends: [
 {
 content_id: [INT] //节目id
 content_name: [STRING] //节目名称
 content_pic_url: [STRING] 节目的海报地址
 content_type: [INT] //节目类型 1：电影，2：电视剧，3：综艺节目
 recommendRate: [INT] //推荐度
 }
 ......
 ]
 }
 */
public class ReturnFriendRecommends {
	public Recommends[] recommends;

	public static class Recommends {

		public String content_id;
		public String content_name;
		public String content_pic_url;
		public String content_type;
		public String recommendRate;

	}
}
