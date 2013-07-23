package com.joyplus.tv.Service.Return;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * 1：如果是电视剧

 {
 tv: {
 name: [STRING], // 节目的名字，255字节以内的字符串
 summary: [STRING], // 节目的简介，255字节以内的字符串
 poster: [URL], // 节目的海报图片地址，字符串格式符合http协议地址格式
 closed: [BOOL], // 表示节目是否已经完结，true或者false
 episodes_count: [NUM], // 节目的总集数，正整数
 sources: [STRING] // 节目的可用来源网站，以逗号分割的字符串，例如：优酷,土豆,...
 like_num: int //喜欢这个节目的用户数
 watch_num: int //观看过这个节目的用户数
 favority_num: int 收藏这个节目的用户数,
 score:  float 豆瓣分数，
 episodes: [
 {
 name: [STRING],
 video_urls: [
 {
 source: [STRING], // 节目的可用来源网站，例如：优酷,土豆
 url: [URL]
 },
 ...
 ]
 down_urls: [  //视频地址
 {
 source: [STRING], // 节目的可用来源网站，例如：优酷,土豆 
 urls: [
 {
 "type": "mp4", mp4:高清，flv：标清，hd2：超清
 "url": [URL]
 }
 ]
 }
 .........
 ]
 },
 ...
 ]
 },
 comments: [
 {
 owner_id: int 发表评论的用户id
 owner_name: string 发表评论用户名
 owner_pic_url: string 发表评论用户的头像
 id: int 评论id
 content: string 评论的内容
 create_date: date 评论时间
 }
 ......
 ]
 }
 2：如果是综艺节目

 {
 show: {
 name: [STRING], // 节目的名字，255字节以内的字符串
 summary: [STRING], // 节目的简介，255字节以内的字符串
 poster: [URL], // 节目的海报图片地址，字符串格式符合http协议地址格式
 closed: [BOOL], // 表示节目是否已经完结，true或者false
 episodes_count: [NUM], // 节目的总集数，正整数
 sources: [STRING] // 节目的可用来源网站，以逗号分割的字符串，例如：优酷,土豆,...
 like_num: int //喜欢这个节目的用户数
 watch_num: int //观看过这个节目的用户数
 favority_num: int 收藏这个节目的用户数,
 score:  float 豆瓣分数，
 episodes: [
 {
 name: [STRING],
 video_urls: [
 {
 source: [STRING], // 节目的可用来源网站，例如：优酷,土豆
 url: [URL]
 },
 ...
 ]
 down_urls: [  //视频地址
 {
 source: [STRING], // 节目的可用来源网站，例如：优酷,土豆 
 urls: [
 {
 "type": "mp4", mp4:高清，flv：标清，hd2：超清
 "url": [URL]
 }
 ]
 }
 .........
 ]
 },
 ...
 ]
 },
 comments: [
 {
 owner_id: int 发表评论的用户id
 owner_name: string 发表评论用户名
 owner_pic_url: string 发表评论用户的头像
 id: int 评论id
 content: string 评论的内容
 create_date: date 评论时间
 }
 ......
 ]
 }
 3：如果是电影

 {
 movie: {
 name: [STRING], // 节目的名字，255字节以内的字符串
 summary: [STRING], // 节目的简介，255字节以内的字符串
 poster: [URL], // 节目的海报图片地址，字符串格式符合http协议地址格式
 like_num: int //喜欢这个节目的用户数
 watch_num: int //观看过这个节目的用户数
 favority_num: int 收藏这个节目的用户数,
 score:  float 豆瓣分数，
 video_urls: [
 {
 source: [STRING], // 节目的可用来源网站，例如：优酷,土豆 
 url: [URL]
 }
 .........
 ]
 down_urls: [  //视频地址
 {
 source: [STRING], // 节目的可用来源网站，例如：优酷,土豆 
 urls: [
 {
 "type": "mp4", mp4:高清，flv：标清，hd2：超清
 "url": [URL]
 }
 ]
 }
 .........
 ]
 },
 comments: [
 {
 owner_id: int 发表评论的用户id
 owner_name: string 发表评论用户名
 owner_pic_url: string 发表评论用户的头像
 id: int 评论id
 content: string 评论的内容
 create_date: date 评论时间
 }
 ......
 ]
 }   
 4：如果是视频

 {
 video: {
 name: [STRING], // 节目的名字，255字节以内的字符串
 summary: [STRING], // 节目的简介，255字节以内的字符串
 poster: [URL], // 节目的海报图片地址，字符串格式符合http协议地址格式
 like_num: int //喜欢这个节目的用户数
 watch_num: int //观看过这个节目的用户数
 favority_num: int 收藏这个节目的用户数,
 score:  float 豆瓣分数，
 video_urls: [
 {
 source: [STRING], // 节目的可用来源网站，例如：优酷,土豆 
 url: [URL]
 }
 .........
 ]
 down_urls: [  //视频地址
 {
 source: [STRING], // 节目的可用来源网站，例如：优酷,土豆 
 urls: [
 {
 "type": "mp4", mp4:高清，flv：标清，hd2：超清
 "url": [URL]
 }
 ]
 }
 .........
 ]
 },
 comments: [
 {
 owner_id: int 发表评论的用户id
 owner_name: string 发表评论用户名
 owner_pic_url: string 发表评论用户的头像
 id: int 评论id
 content: string 评论的内容
 create_date: date 评论时间
 }
 ......
 ]
 }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnProgramView {

	public TV tv;
	public Show show;
	public Movie movie;
//	public Video video;
	public TOPICS[] topics;
	public COMMENTS[] comments;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class VIDEO_URLS {
		public String source;
		public String url;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TOPICS {
		public String t_name;
		public String t_id;
	}

	/*
	 * down_urls: [ //视频地址 { source: [STRING], // 节目的可用来源网站，例如：优酷,土豆 urls: [ {
	 * "type": "mp4", mp4:高清，flv：标清，hd2：超清 "url": [URL] } ] }
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DOWN_URLS {
		public String source;
		public URLS[] urls;
		public int index;

		public static class URLS {
			public String type;
			public String url;
			public String file;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class EPISODES {

		public String name;
		public VIDEO_URLS[] video_urls;
		public DOWN_URLS[] down_urls;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class COMMENTS {
		public String owner_id;
		public String owner_name;
		public String owner_pic_url;
		public String id;
		public String content;
		public String create_date;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TV {
		public String name;
		public String summary;
		public String poster;
		public String like_num;
		public String watch_num;
		public String favority_num;
		public String score;
		public String ipad_poster;
		public String support_num;
		public String publish_date;
		public String directors;
		public String episodes_count;
		public String cur_episode;
		public String max_episode;
		public String stars;
		public String id;
		public String area;
		public String total_comment_number;
		public String definition;
		public int current_play;

		public EPISODES[] episodes;

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Show {
		public String name;
		public String summary;
		public String poster;
		public String like_num;
		public String watch_num;
		public String favority_num;
		public String score;
		public String ipad_poster;
		public String support_num;
		public String publish_date;
		public String cur_episode;
		public String max_episode;
		public String directors;
		public String stars;
		public String id;
		public String area;
		public String total_comment_number;
		public String definition;
		public EPISODES[] episodes;

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Movie {
		public String name;
		public String summary;
		public String poster;
		public String like_num;
		public String watch_num;
		public String favority_num;
		public String score;
		public String ipad_poster;
		public String support_num;
		public String publish_date;
		public String directors;
		public String stars;
		public String id;
		public String area;
		public String total_comment_number;
		public String definition;
		public String duration;
		public EPISODES[] episodes;

	}

//	@JsonIgnoreProperties(ignoreUnknown = true)
//	public static class Video {
//		public String name;
//		public String summary;
//		public String poster;
//		public String sources;
//		public String like_num;
//		public String watch_num;
//		public String favority_num;
//		public String score;
//
//		public VIDEO_URLS[] video_urls;
//		public DOWN_URLS[] down_urls;
//
//	}
}
