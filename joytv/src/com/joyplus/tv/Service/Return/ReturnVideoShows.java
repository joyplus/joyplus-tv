package com.joyplus.tv.Service.Return;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * {
 "show": [
 {
 "prod_id": 节目id
 "prod_name": 节目名
 "prod_type": 节目类型
 "prod_pic_url": 海报
 },
 ]
 } 
 */
@JsonIgnoreProperties
public class ReturnVideoShows {

	public Show[] show;

	public static class Show {

		public String prod_id;
		public String prod_name;
		public String prod_type;
		public String prod_pic_url;
		public String score;
	}

}
