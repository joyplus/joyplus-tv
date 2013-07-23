package com.joyplus.tv.entity;

import com.joyplus.tv.Service.Return.ReturnMainHot.PLAY_URLS;
import com.joyplus.tv.Service.Return.ReturnMainHot.URLS;

public class HotItemInfo{
	
		public int type; //0 历史 ； 1正常
	
		public String id;
		public String prod_id;
		public String prod_name; 
		public String prod_type; //1，电影; 2，电视剧; 3，综艺; 131，动漫;
		public String prod_pic_url;
		public String stars;
		public String directors;
		public String favority_num;
		public String support_num;
		public String publish_date;
		public String score;
		public String area;
		public String cur_episode;
		public String definition;
		public String prod_summary;
		public String duration;
		
		public String playback_time;
		public String max_episode;
		public String video_url;
		public String source;//来源
		public String prod_subname;
		public String play_type;
		
		
		public PLAY_URLS[] play_urls;
	}