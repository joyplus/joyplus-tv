package com.joyplus.tv.utils;

import java.net.URLEncoder;

public class URLUtils implements JieMianConstant, BangDanConstant{
	
	private static final String TAG = "URLUtils";
	
	public static String getTopItemURL(String url, String top_id,
			String page_num, String page_size) {

		return url + "?top_id=" + top_id + "&page_num=" + page_num
				+ "&page_size=" + page_size;
	}

	public static String getTopURL(String url, String page_num,
			String page_size, String topic_type) {

		return url + "?page_num=" + page_num + "&page_size=" + page_size
				+ "&topic_type=" + topic_type;
	}
	
	public static String getGroupURL(String url,String prod_id) {
		
		return url + "?prod_id=" + prod_id;
	}
	
	public static String getGroupSeries(String prod_id) {
		
		return getGroupURL(BangDanConstant.SERISE_ALL_GROUP_URL, prod_id);
	}

	/**
	 * type required 视频的类别，节目类型，1：电影，2：电视剧，3：综艺节目，131：动漫
	 * 
	 * @param url
	 * @param page_num
	 * @param page_size
	 * @param type
	 * @return
	 */
	public static String getFilterURL(String url, String page_num,
			String page_size, String type) {

		return url + "?page_num=" + page_num + "&page_size=" + page_size
				+ "&type=" + type;
	}

	public static String getSearchURL(String url, String page_num,
			String page_size, String keyword) {

		return url + "?page_num=" + page_num + "&page_size=" + page_size
				+ "&keyword=" + keyword;
	}

	public static String getUserFavURL(String url, String page_num,
			String page_size, String vod_type, String userId) {

		return url + "?page_num=" + page_num + "&page_size=" + page_size
				+ "&vod_type=" + vod_type + "&userid=" + userId;
	}
	
	public static String getYingPinURL(String url,String page_num,
			String page_size,String prod_id) {
		
		return url + "?page_num=" + page_num + "&page_size=" + page_size
				+ "&prod_id=" + prod_id ;
	}

	public static final int CACHE_NUM = 20;
	public static final int FIRST_NUM = 30;

	// TV 全部分类 10
	public static String getTV_Quan10URL() {

		return getTopItemURL(TOP_ITEM_URL, TV_DIANSHIJU,
				1 + "", CACHE_NUM + "");
	}

	// TV 全部分类
	public static String getTV_QuanAllFirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", TV_TYPE);
	}

	// TV 全部分类
	public static String getTV_QuanAllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", TV_TYPE);
	}

	// TV 大陆剧
	public static String getTV_DalujuFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_DALU_DIANSHI,
				1 + "", FIRST_NUM + "");
	}

	// TV 大陆剧
	public static String getTV_DalujuCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_QINZI_DONGMAN,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取内地电视剧filter first
	public static String getTV_Daluju_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", TV_TYPE) + AREA 
				+ URLEncoder.encode("内地");
	}
	
	//获取内地电视剧filter cache
	public static String getTV_Daluju_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", TV_TYPE)+ AREA 
				+ URLEncoder.encode("内地");
	}

	// 港剧
	public static String getTV_GangjuFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_GANGJU_DIANSHI,
				1 + "", FIRST_NUM + "");
	}

	// 港剧
	public static String getTV_GangjuCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_GANGJU_DIANSHI,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取香港电视剧filter first
	public static String getTV_Gangju_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", TV_TYPE) + AREA 
				+ URLEncoder.encode("香港");
	}
	
	//获取香港电视剧filter cache
	public static String getTV_Gangju_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", TV_TYPE)+ AREA 
				+ URLEncoder.encode("香港");
	}

	public static String getTV_TaijuFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_TAIJU_DIANSHI,
				1 + "", FIRST_NUM + "");
	}

	public static String getTV_TaijuCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_TAIJU_DIANSHI,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取台湾电视剧filter first
	public static String getTV_Taiju_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", TV_TYPE) + AREA 
				+ URLEncoder.encode("台湾");
	}
	
	//获取台湾电视剧filter cache
	public static String getTV_Taiju_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", TV_TYPE)+ AREA 
				+ URLEncoder.encode("台湾");
	}

	public static String getTV_HanjuFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_HANJU_DIANSHI,
				1 + "", FIRST_NUM + "");
	}

	public static String getTV_HanjuCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_HANJU_DIANSHI,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取韩国电视剧filter first
	public static String getTV_Hanju_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", TV_TYPE) + AREA 
				+ URLEncoder.encode("韩国");
	}
	
	//获取韩国电视剧filter cache
	public static String getTV_Hanju_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", TV_TYPE)+ AREA 
				+ URLEncoder.encode("韩国");
	}

	public static String getTV_MeijuFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_OUMEI_DIANSHI,
				1 + "", FIRST_NUM + "");
	}

	public static String getTV_MeijuCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_OUMEI_DIANSHI,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取美国电视剧filter first
	public static String getTV_Meiju_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", TV_TYPE) + AREA 
				+ URLEncoder.encode("美国");
	}
	
	//获取美国电视剧filter cache
	public static String getTV_Meiju_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", TV_TYPE)+ AREA 
				+ URLEncoder.encode("美国");
	}

	public static String getTV_RijuFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_RIJU_DIANSHI,
				1 + "", FIRST_NUM + "");
	}

	public static String getTV_RijuCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_RIJU_DIANSHI,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取日本电视剧filter first
	public static String getTV_Riju_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", TV_TYPE) + AREA 
				+ URLEncoder.encode("日本");
	}
	
	//获取日本电视剧filter cache
	public static String getTV_Riju_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", TV_TYPE)+ AREA 
				+ URLEncoder.encode("日本");
	}

	// Movie

	public static String getMovie_Quan10URL() {

		return getTopItemURL(TOP_ITEM_URL, TV_DIANYING, 1 + "",
				CACHE_NUM + "");
	}

	public static String getMovie_QuanAllFirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE);
	}

	public static String getMovie_QuanAllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE);
	}

	public static String getMovie_DongzuoFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_DONGZUO_MOVIE,
				1 + "", FIRST_NUM + "");
	}

	public static String getMovie_DongzuoCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_DONGZUO_MOVIE,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取动作电影filter first
	public static String getMovie_Dongzuo_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("动作");
	}
	
	//获取动作电影filter cache
	public static String getMovie_Dongzuo_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("动作");
	}

	public static String getMovie_KehuanFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_KEHUAN_MOVIE,
				1 + "", FIRST_NUM + "");
	}

	public static String getMovie_KehuanCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_KEHUAN_MOVIE,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取科幻电影filter first
	public static String gettMovie_Kehuan_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("科幻");
	}
	
	//获取科幻电影filter cache
	public static String gettMovie_Kehuan_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("科幻");
	}

	public static String getMovie_LunliFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_LUNLI_MOVIE,
				1 + "", FIRST_NUM + "");
	}

	public static String getMovie_LunliCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_LUNLI_MOVIE,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取伦理电影filter first
	public static String gettMovie_Lunli_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("伦理");
	}
	
	//获取伦理电影filter cache
	public static String gettMovie_Lunli_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("伦理");
	}

	public static String getMovie_XijuFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_XIJU_MOVIE,
				1 + "", FIRST_NUM + "");
	}

	public static String getMovie_XijuCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_XIJU_MOVIE,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取喜剧电影filter first
	public static String gettMovie_Xiju_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("喜剧");
	}
	
	//获取喜剧电影filter cache
	public static String gettMovie_Xiju_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("喜剧");
	}

	public static String getMovie_AiqingFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_AIQING_MOVIE,
				1 + "", FIRST_NUM + "");
	}

	public static String getMovie_AiqingCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_AIQING_MOVIE,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取爱情电影filter first
	public static String gettMovie_Aiqing_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("爱情");
	}
	
	//获取爱情电影filter cache
	public static String gettMovie_Aiqing_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("爱情");
	}

	public static String getMovie_XuanyiFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_XUANYI_MOVIE,
				1 + "", FIRST_NUM + "");
	}

	public static String getMovie_XuanyiCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_XUANYI_MOVIE,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取悬疑电影filter first
	public static String gettMovie_Xuanyi_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("悬疑");
	}
	
	//获取悬疑电影filter cache
	public static String gettMovie_Xuanyi_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("悬疑");
	}

	public static String getMovie_KongbuFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_KONGBU_MOVIE,
				1 + "", FIRST_NUM + "");
	}

	public static String getMovie_KongbuCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_KONGBU_MOVIE,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取恐怖电影filter first
	public static String gettMovie_Kongbu_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("恐怖");
	}
	
	//获取恐怖电影filter cache
	public static String gettMovie_Kongbu_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("恐怖");
	}

	public static String getMovie_DonghuaFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_DONGHUA_MOVIE,
				1 + "", FIRST_NUM + "");
	}

	public static String getMovie_DonghuaCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_DONGHUA_MOVIE,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取动画电影filter first
	public static String gettMovie_Donghua_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", MOVIE_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("动画");
	}
	
	//获取动画电影filter cache
	public static String gettMovie_Donghua_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("动画");
	}

	// 动漫
	public static String getDongman_Quan10URL() {

		return getTopItemURL(TOP_ITEM_URL, TV_DONGMAN, 1 + "",
				CACHE_NUM + "");
	}

	public static String getDongman_QuanAllFirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", DONGMAN_TYPE);
	}

	public static String getDongman_QuanAllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", DONGMAN_TYPE);
	}

	public static String getDongman_QinziFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_QINZI_DONGMAN,
				1 + "", FIRST_NUM + "");
	}
	

	public static String getDongman_QinziCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_QINZI_DONGMAN,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取亲子动漫filter first
	public static String getDongman_Qinzi_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", DONGMAN_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("亲子");
	}
	
	//获取亲子动漫filter cache
	public static String getDongman_Qinzi_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", DONGMAN_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("亲子");
	}

	public static String getDongman_RexueFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_REXUE_DONGMAN,
				1 + "", FIRST_NUM + "");
	}
	

	public static String getDongman_RexueCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_REXUE_DONGMAN,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取热血动漫filter first
	public static String getDongman_Rexue_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", DONGMAN_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("热血");
	}
	
	//获取热血动漫filter cache
	public static String getDongman_Rexue_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", DONGMAN_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("热血");
	}

	public static String getDongman_HougongFirstURL() {

		return getTopItemURL(TOP_ITEM_URL,
				REBO_HOUGONG_DONGMAN, 1 + "", FIRST_NUM + "");
	}

	public static String getDongman_HougongCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL,
				REBO_HOUGONG_DONGMAN, (pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取校园动漫filter first
	public static String getDongman_Hougong_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", DONGMAN_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("校园");
	}
	
	//获取校园动漫filter cache
	public static String getDongman_Hougong_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", DONGMAN_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("校园");
	}

	public static String getDongman_TuiliFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_TUILI_DONGMAN,
				1 + "", FIRST_NUM + "");
	}

	public static String getDongman_TuiliCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_TUILI_DONGMAN,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取推理动漫filter first
	public static String getDongman_Tuili_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", DONGMAN_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("推理");
	}
	
	//获取推理动漫filter cache
	public static String getDongman_Tuili_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", DONGMAN_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("推理");
	}

	public static String getDongman_JizhanFirstURL() {

		return getTopItemURL(TOP_ITEM_URL, REBO_JIZHAN_DONGMAN,
				1 + "", FIRST_NUM + "");
	}

	public static String getDongman_JizhanCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL, REBO_JIZHAN_DONGMAN,
				(pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取机战动漫filter first
	public static String getDongman_Jizhan_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", DONGMAN_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("机战");
	}
	
	//获取机战动漫filter cache
	public static String getDongman_Jizhan_Quan_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", DONGMAN_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("机战");
	}

	public static String getDongman_GaoxiaoFirstURL() {

		return getTopItemURL(TOP_ITEM_URL,
				REBO_GAOXIAO_DONGMAN, 1 + "", CACHE_NUM + "");
	}

	public static String getDongman_GaoxiaoCacheURL(int pageNum) {

		return getTopItemURL(TOP_ITEM_URL,
				REBO_GAOXIAO_DONGMAN, (pageNum + 1) + "", CACHE_NUM + "");
	}
	
	//获取搞笑动漫filter first
	public static String getDongman_Gaoxiao_Quan_FirstURL() {

		return getFilterURL(FILTER_URL, 1 + "",
				(FIRST_NUM - 10) + "", DONGMAN_TYPE) + SUB_TYPE 
				+ URLEncoder.encode("搞笑");
	}
	
	//获取搞笑动漫filter cache
	public static String getDongman_Gaoxiao_AllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", DONGMAN_TYPE)+ SUB_TYPE 
				+ URLEncoder.encode("搞笑");
	}

	// 悦单

	public static String getYueDan_DianyingFirstURL() {

		return getTopURL(TOP_URL, 1 + "", FIRST_NUM + "",
				MOVIE_TYPE + "");
	}

	public static String getYueDan_DianyingCacheURL(int pageNum) {

		return getTopURL(TOP_URL, (pageNum + 1) + "", CACHE_NUM
				+ "", MOVIE_TYPE + "");
	}

	public static String getYueDan_DianshiFirstURL() {

		return getTopURL(TOP_URL, 1 + "", FIRST_NUM + "",
				TV_TYPE + "");
	}

	public static String getYueDan_DianshiCacheURL(int pageNum) {

		return getTopURL(TOP_URL, (pageNum + 1) + "", CACHE_NUM
				+ "", TV_TYPE + "");
	}

	public static String getZongyi_QuanAllFirstURL() {

		return getFilterURL(FILTER_URL, 1 + "", (FIRST_NUM)
				+ "", ZONGYI_TYPE);
	}

	public static String getZongyi_QuanAllCacheURL(int pageNum) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", ZONGYI_TYPE);
	}

	// search

	public static String getSearch_FirstURL(String search) {
//		Log.i(TAG, "getSearch_FirstURL-->" + getSearchURL(SEARCH_URL, 1 + "", FIRST_NUM + "",
//				search));

		return getSearchURL(SEARCH_CAPITAL_URL, 1 + "", FIRST_NUM + "",
				search);
	}

	public static String getSearch_CacheURL(int pageNum, String search) {

		return getSearchURL(SEARCH_CAPITAL_URL, (pageNum + 1) + "",
				CACHE_NUM + "", search);
	}
	
	//获取200条数据
	public static String getSearch_200URL(String search) {
//		Log.i(TAG, "getSearch_FirstURL-->" + getSearchURL(SEARCH_URL, 1 + "", FIRST_NUM + "",
//				search));

		return getSearchURL(SEARCH_CAPITAL_URL, 1 + "", 200 + "",
				search);
	}
	
	//电影搜索 search
	public static String getSearch_Movie_FirstURL(String search) {
//		Log.i(TAG, "getSearch_FirstURL-->" + getSearchURL(SEARCH_URL, 1 + "", FIRST_NUM + "",
//				search));

		return getSearchURL(SEARCH_CAPITAL_URL, 1 + "", FIRST_NUM + "",
				search) + "&type=" + MOVIE_TYPE;
	}

	public static String getSearch_Movie_CacheURL(int pageNum, String search) {

		return getSearchURL(SEARCH_CAPITAL_URL, (pageNum + 1) + "",
				CACHE_NUM + "", search) + "&type=" + MOVIE_TYPE;
	}
	
	//电视剧搜索 search
	public static String getSearch_TV_FirstURL(String search) {
//		Log.i(TAG, "getSearch_FirstURL-->" + getSearchURL(SEARCH_URL, 1 + "", FIRST_NUM + "",
//				search));

		return getSearchURL(SEARCH_CAPITAL_URL, 1 + "", FIRST_NUM + "",
				search) + "&type=" + TV_TYPE;
	}

	public static String getSearch_TV_CacheURL(int pageNum, String search) {

		return getSearchURL(SEARCH_CAPITAL_URL, (pageNum + 1) + "",
				CACHE_NUM + "", search) + "&type=" + TV_TYPE;
	}
	
	//综艺搜索 search
	public static String getSearch_Zongyi_FirstURL(String search) {
//		Log.i(TAG, "getSearch_FirstURL-->" + getSearchURL(SEARCH_URL, 1 + "", FIRST_NUM + "",
//				search));

		return getSearchURL(SEARCH_CAPITAL_URL, 1 + "", FIRST_NUM + "",
				search) + "&type=" + ZONGYI_TYPE;
	}

	public static String getSearch_Zongyi_CacheURL(int pageNum, String search) {

		return getSearchURL(SEARCH_CAPITAL_URL, (pageNum + 1) + "",
				CACHE_NUM + "", search) + "&type=" + ZONGYI_TYPE;
	}
	
	//动漫搜索 search
	public static String getSearch_Dongman_FirstURL(String search) {
//		Log.i(TAG, "getSearch_FirstURL-->" + getSearchURL(SEARCH_URL, 1 + "", FIRST_NUM + "",
//				search));

		return getSearchURL(SEARCH_CAPITAL_URL, 1 + "", FIRST_NUM + "",
				search) + "&type=" + DONGMAN_TYPE;
	}

	public static String getSearch_Dongman_CacheURL(int pageNum, String search) {

		return getSearchURL(SEARCH_CAPITAL_URL, (pageNum + 1) + "",
				CACHE_NUM + "", search) + "&type=" + DONGMAN_TYPE;
	}

	// 动漫filter
	public static String getFilter_DongmanFirstURL(String filterSource) {

		return getFilterURL(FILTER_URL, 1 + "", FIRST_NUM + "",
				DONGMAN_TYPE) + filterSource;
	}

	public static String getFilter_DongmanCacheURL(int pageNum,
			String filterSource) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", DONGMAN_TYPE) + filterSource;
	}

	// 电影filter
	public static String getFilter_DianyingFirstURL(String filterSource) {
		
		Log.i(TAG, "getFilter_DianyingFirstURL--->" + getFilterURL(FILTER_URL, 1 + "", FIRST_NUM + "",
				MOVIE_TYPE) + filterSource);

		return getFilterURL(FILTER_URL, 1 + "", FIRST_NUM + "",
				MOVIE_TYPE) + filterSource;
	}

	public static String getFilter_DianyingCacheURL(int pageNum,
			String filterSource) {
		
		Log.i(TAG, "getFilter_DianyingFirstURL--->" + getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE) + filterSource);

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", MOVIE_TYPE) + filterSource;
	}

	// 电视剧filter
	public static String getFilter_DianshijuFirstURL(String filterSource) {

		return getFilterURL(FILTER_URL, 1 + "", FIRST_NUM + "",
				TV_TYPE) + filterSource;
	}

	public static String getFilter_DianshijuCacheURL(int pageNum,
			String filterSource) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", TV_TYPE) + filterSource;
	}

	// 综艺filter
	public static String getFilter_ZongyiFirstURL(String filterSource) {

		return getFilterURL(FILTER_URL, 1 + "", FIRST_NUM + "",
				ZONGYI_TYPE) + filterSource;
	}

	public static String getFilter_ZongyiCacheURL(int pageNum,
			String filterSource) {

		return getFilterURL(FILTER_URL, (pageNum + 1) + "",
				CACHE_NUM + "", ZONGYI_TYPE) + filterSource;
	}

	public static final int SHOUCANG_NUM = 100;

	// 收藏 取100条数据 为全部类型
	public static String getShoucangURL(String userId) {

		return getUserFavURL(FAV_URL, 1 + "", SHOUCANG_NUM + "", "", userId);
	}
	
	// 历史 取100条数据 为全部类型
	public static String getHistoryURL(String userId) {

		return getUserFavURL(HISTORY_URL, 1 + "", SHOUCANG_NUM + "", "", userId);
	}
	
	//影评
	public static String getYingPin_1_URL(String prod_id) {
		Log.i(TAG, "getYingPin_1_URL--->" + getYingPinURL(YINGPING_URL, 1+ "", 1 + "", prod_id));
		
		return getYingPinURL(YINGPING_URL, 1+ "", 1 + "", prod_id);
	}
	
	public static final String YEAR = "&year=";
	public static final String AREA = "&area=";
	public static final String SUB_TYPE = "&sub_type=";
	public static final String[] PARAMS_3 = { AREA, SUB_TYPE, YEAR };

	public static String getFileterURL3Param(String[] choices,
			String defaultItemName) {

		if (choices.length < 3 && choices.length != PARAMS_3.length) {

			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < choices.length; i++) {

			if (!choices[i].equals(defaultItemName)) {
				sb.append(PARAMS_3[i]);
				String encode;
				if (i != 2) {

					encode = URLEncoder.encode(choices[i]);
				} else {
					encode = choices[i];
				}
				sb.append(encode);
			}
		}

		return sb.toString();
	}

	public static String getQuanBuFenLeiName(String[] choices,
			String defaultQuanbufenlei, String defaultItemName) {

		if (choices.length < 3) {

			return defaultQuanbufenlei;
		}

		if (choices[0].equals(defaultItemName)
				&& choices[1].equals(defaultItemName)
				&& choices[2].equals(defaultItemName)) {

			return defaultQuanbufenlei;
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < choices.length; i++) {

				if (!choices[i].equals(defaultItemName)) {
					sb.append(choices[i] + "/");
				}
			}

			sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		}

	}


}
