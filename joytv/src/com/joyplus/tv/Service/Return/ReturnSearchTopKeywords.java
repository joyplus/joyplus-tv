package com.joyplus.tv.Service.Return;

public class ReturnSearchTopKeywords {

	public TopKeywords[] topKeywords;

	/*
	 * { "topKeywords": [ { "content": 关键词 "search_count": 搜索次数
	 * "last_search_date": 最新搜索时间 } ] }
	 */
	public static class TopKeywords {
		public String content;
		public String search_count;
		public String last_search_date;

	}
}
