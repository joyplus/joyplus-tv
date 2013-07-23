package com.joyplus.tv.utils;

import com.joyplus.tv.Constant;

public interface BangDanConstant {

	/**
	 * 
	 * 4677 TV版网络首播 
	 * 140 本周网络首播 
	 * 141 热播动作片 
	 * 3472 热播科幻片 
	 * 142 热播伦理片 
	 * 143 热播喜剧片 
	 * 2704 热播爱情片 
	 * 145 热播悬疑片 
	 * 146 热播恐怖片 
	 * 144 热播动画片
	 * 
	 * 906 动漫剧场 
	 * 148 热播欧美剧 
	 * 147 热播大陆剧 
	 * 2250 热播港剧 
	 * 150 热播台剧 
	 * 918 热播韩剧 
	 * 149 热播日剧
	 * 
	 * 152 热播综艺
	 * 
	 * 5429 热播亲子动漫 
	 * 7112 热播热血动漫 
	 * 7176 热播后宫动漫 
	 * 7188 热播推理动漫 
	 * 7193 热播机战动漫 
	 * 7265 热播搞笑动漫
	 * 
	 * 7280 电影 for TV 
	 * 7282 电视剧 for TV 
	 * 7284 动漫 for TV
	 */
	
	String TV_WANGLUO_SHOUBO = "4677";//4677 TV版网络首播 
	String BEN_ZHOU_WANGLUO_SHOUBO = "140";//140 本周网络首播
	String REBO_DONGZUO_MOVIE = "141";//141 热播动作片 
	String REBO_KEHUAN_MOVIE = "3472";//3472 热播科幻片 
	String REBO_LUNLI_MOVIE = "142";//142 热播伦理片
	String REBO_XIJU_MOVIE = "143";//143 热播喜剧片
	String REBO_AIQING_MOVIE = "2704";//2704 热播爱情片 
	String REBO_XUANYI_MOVIE = "145";//145 热播悬疑片 
	String REBO_KONGBU_MOVIE = "146";//146 热播恐怖片 
	String REBO_DONGHUA_MOVIE = "144";//144 热播动画片
	
	String REBO_DONGMAN_DIANSHI = "906";//906 动漫剧场 
	String REBO_OUMEI_DIANSHI = "148";//148 热播欧美剧 
	String REBO_DALU_DIANSHI = "147";//147 热播大陆剧 
	String REBO_GANGJU_DIANSHI = "2250";//2250 热播港剧
	String REBO_TAIJU_DIANSHI = "150";//150 热播台剧 
	String REBO_HANJU_DIANSHI = "918";//918 热播韩剧 
	String REBO_RIJU_DIANSHI = "149";//149 热播日剧
	
	String REBO_ZONGYI = "152";//152 热播综艺
	
	String REBO_QINZI_DONGMAN = "5429";//5429 热播亲子动漫 
	String REBO_REXUE_DONGMAN = "7112";//7112 热播热血动漫 
	String REBO_HOUGONG_DONGMAN = "7176";//7176 热播后宫动漫 
	String REBO_TUILI_DONGMAN = "7188";//7188 热播推理动漫 
	String REBO_JIZHAN_DONGMAN = "7193";//7193 热播机战动漫 
	String REBO_GAOXIAO_DONGMAN = "7265";//7265 热播搞笑动漫
	
	String TV_DIANYING = "7280";//7280 电影 for TV 
	String TV_DIANSHIJU = "7282";//282 电视剧 for TV 
	String TV_DONGMAN = "7284";//7284 动漫 for TV
	
	String TOP_ITEM_URL = Constant.BASE_URL + "top_items";
	String TOP_URL = Constant.BASE_URL + "tops";
	String FILTER_URL = Constant.BASE_URL + "filter";
//	String SEARCH_URL = Constant.BASE_URL + "search";
	String FAV_URL = Constant.BASE_URL + "user/favorities";
	String HISTORY_URL = Constant.BASE_URL + "/user/playHistories";
	String YINGPING_URL = Constant.BASE_URL + "program/reviews";
	String SEARCH_CAPITAL_URL = Constant.BASE_URL + "search_capital";
	String SERISE_ALL_GROUP_URL = Constant.BASE_URL + "/program/RelatedGroup";
	
	String MOVIE_TYPE = "1";
	String TV_TYPE = "2";
	String ZONGYI_TYPE = "3";
	String DONGMAN_TYPE = "131";

	int CHANGXIAN = 6;//尝鲜
	int GAOQING = 7;//高清 普清
	int CHAOQING = 8;//超清
}
