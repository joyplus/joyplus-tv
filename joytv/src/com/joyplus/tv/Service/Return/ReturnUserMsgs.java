package com.joyplus.tv.Service.Return;

/*
 *  {
 "msgs": [
 {
 "type":  "reply"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类别  1：电影，2：电视剧，3：综艺节目
 "prod_id":   节目id
 "prod_name": 节目名
 "thread_id": 评论id
 "content":回复的评论
 "thread_comment": 评论内容
 },
 {
 "type":  "comment"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类别  1：电影，2：电视剧，3：综艺节目
 "prod_id":   节目id
 "prod_name": 节目名
 "content": 评论内容
 },
 {
 "type":  "like"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类别  1：电影，2：电视剧，3：综艺节目
 "prod_id":   节目id
 "prod_name": 节目名
 },
 {
 "type":  "watch"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类别  1：电影，2：电视剧，3：综艺节目
 "prod_id":   节目id
 "prod_name": 节目名
 },
 {
 "type":  "share"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类别  1：电影，2：电视剧，3：综艺节目
 "prod_id":   节目id
 "prod_name": 节目名
 "share_to_where": 分享到哪儿，1:新浪，2：腾讯，3：人人网，4：豆瓣
 },
 {
 "type":  "favority"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类别  1：电影，2：电视剧，3：综艺节目
 "prod_id":   节目id
 "prod_name": 节目名
 },
 {
 "type":  "unfavority"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 "prod_type": 节目类别  1：电影，2：电视剧，3：综艺节目
 "prod_id":   节目id
 "prod_name": 节目名
 },
 {
 "type":  "follow"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 },
 {
 "type":  "destory"
 "user_id": 用户id
 "user_name": 用户名
 "user_pic_url": 用户头像
 "create_date": 时间
 }
 .........
 ]
 }
 */
public class ReturnUserMsgs {

	public Msgs[] msgs;

	public static class Msgs {

		public String type;
		public String user_id;
		public String user_name;
		public String user_pic_url;

		public String create_date;

		public String prod_id;
		public String prod_name;
		public String prod_type;

		public String thread_id;
		public String content;
		public String thread_comment;
		public String share_to_where;

	}
}
