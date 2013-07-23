package com.joyplus.tv.Service.Return;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class ReturnComments {

	public Comments[] comments;

	/*
	 * { comments: [ { owner_id: int 发表评论的用户id owner_name: string 发表评论用户名
	 * owner_pic_url: string 发表评论用户的头像 id: int 评论id content: string 评论的内容
	 * create_date: date 评论时间 } ...... ] }
	 */
	public static class Comments {
		public String owner_id;
		public String owner_name;
		public String owner_pic_url;
		public String id;
		public String content;
		public String create_date;

	}
}
