package com.joyplus.tv.Service.Return;

public class ReturnProgramViewRecommend {

	public TV tv;
	public Show show;
	public Movie movie;
	public Video video;
	public Comments[] comments;

	public static class VIDEO_URLS {
		public String source;
		public String url;
	}

	public static class DOWN_URLS {
		public String source;
		public String url;
	}

	public static class EPISODES {
		public String name;
		public VIDEO_URLS[] video_urls;
		public DOWN_URLS[] down_urls;
	}

	public static class Comments {
		public String owner_id;
		public String owner_name;
		public String owner_pic_url;
		public String id;
		public String content;
		public String create_date;
		public String replies;
	}

	/*
	 * "dynamics": [ { "type":"favority" //收藏 "user_id": 用户id "user_name": 用户名
	 * "user_pic_url": 用户头像 "create_date": 操作时间 },
	 */
	public static class Dynamics {
		public String type;
		public String user_id;
		public String user_name;
		public String user_pic_url;
		public String create_date;
		public String share_where_type;
		public String reason;
		public String content;
	}

	public class Show {
		public String name;
		public String summary;
		public String poster;
		public String closed;
		public String episodes_count;
		public String sources;
		public String like_num;
		public String watch_num;

		public String favority_num;
		public String score;

		public EPISODES[] episodes;

	}

	public class Movie {
		public String name;
		public String summary;
		public String poster;
		public String closed;
		public String episodes_count;
		public String sources;
		public String like_num;
		public String watch_num;

		public String favority_num;
		public String score;

		public EPISODES[] episodes;
		public Comments[] comments;
		public Dynamics[] dynamics;

	}

	public class Video {
		public String name;
		public String summary;
		public String poster;
		public String closed;
		public String episodes_count;
		public String sources;
		public String like_num;
		public String watch_num;

		public String favority_num;
		public String score;

		public EPISODES[] episodes;
		public Comments[] comments;
		public Dynamics[] dynamics;
	}

	public class TV {
		public String name;
		public String summary;
		public String poster;
		public String closed;
		public String episodes_count;
		public String sources;
		public String like_num;
		public String watch_num;

		public String favority_num;
		public String score;

		public EPISODES[] episodes;

		public Comments[] comments;
		public Dynamics[] dynamics;

	}

}
