package com.joyplus.tv.Service.Return;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 *  {
 "dynamics": [
 {   //回复评论
 "type": "reply"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 "thread_id": 评论id
 "thread_comment": 评论的内容
 "content": 回复的内容
 },
 {  //取消好友关注
 "type": "destory"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "friend_name": 好友名
 "friend_pic_url": 好友头像
 "friend_id": 好友id
 },
 {  //收藏
 "type": "favority"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 },
 { //观看节目
 "type": "watch"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 },
 { //好友关注
 "type": "follow"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "friend_name": 好友名
 "friend_pic_url": 好友头像
 "friend_id": 好友id
 },
 { //用户喜欢你
 "type": "like_person"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "friend_name": 好友名
 "friend_pic_url": 好友头像
 "friend_id": 好友id
 },
 { //分享节目
 "type": "share"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 "share_where_type": 分享到哪儿，1:新浪，2：腾讯，3：人人网，4：豆瓣
 },
 { //推荐节目
 "type": "recommend"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 "reason":推荐原因
 },
 { //评论节目
 "type": "comment"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 "content": 评论内容
 },
 { //喜欢节目
 "type": "like"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 },
 { //发布节目
 "type": "publish"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 },

 { //取消收藏节目
 "type": "unfavority"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类型 1：电影，2：电视剧，3：综艺节目
 "prod_name": 节目名称
 "prod_poster": 节目海报
 "prod_id": 节目id
 },
 。。。。。。。
 ]
 } 
 */
@JsonIgnoreProperties
public class ReturnFriendAndMeDynamics {
	public Dynamics[] dynamics;

	public static class Dynamics {

		public String type;
		public String user_id;
		public String user_name;
		public String user_pic_url;

		public String create_date;

		public String prod_id;
		public String prod_name;
		public String prod_type;
		public String prod_poster;

		public String friend_id;
		public String friend_name;
		public String friend_pic_url;

		public String thread_id;
		public String content;
		public String thread_comment;
		public String share_to_where;

		public String share_where_type;
		public String reason;

	}
}
