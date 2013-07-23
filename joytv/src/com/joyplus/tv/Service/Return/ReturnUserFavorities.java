package com.joyplus.tv.Service.Return;

/*
 *{"favorities":[{"content_pic_url":"http:\/\/kansha.sae.sinacdn.com\/.app-stor\/image\/2012\/04\/18\/1334751040CnDCbdAMXZYS6bq.jpg",
 *"area":"台湾",
 *"favority_num":"8"
 *,"content_id":"6672",
 *"publish_date":"2012"
 *,"create_date":"2012-12-03 14:49:30",
 *"stars":"柯震东 陈妍希 敖犬 郝邵文 蔡昌宪"
 *,"score":"8.2",
 *"support_num":"4"
 *,"directors":"九把刀,",
 *"content_type":"1"
 *,"content_name":"那些年，我们一起追的女孩"}]}
 */
public class ReturnUserFavorities {
	public Favorities[] favorities;

	public static class Favorities {

		public String content_id;
		public String content_name;
		public String content_pic_url;
		public String big_content_pic_url;
		public String content_type;
		public String directors;
		public String area;
		public String score;
		public String stars;
		public String favority_num;
		public String publish_date;
		public String support_num;
		public String create_date;
		
		public String max_episode;
		public String cur_episode;
		public String duration;

	}
}
