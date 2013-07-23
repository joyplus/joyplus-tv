package com.joyplus.tv.Service.Return;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 {
 comment: {
 owner_id: [INT] //用户的id 
 owner_name: [STRING] 用户名
 owner_pic_url: [STRING] // 用户头像
 id: [INT] //评论id,
 content: [STRING] 评论的内容
 create_date: [DATE] 发表评论的时间
 replies: [
 {
 owner_id: [INT] //用户的id 
 owner_name: [STRING] 用户名
 owner_pic_url: [STRING] // 用户头像
 id: [INT] //回复评论id,
 content: [STRING] 回复的内容
 create_date: [DATE] 回复评论的时间
 }
 .......
 ]
 }
 */
@JsonIgnoreProperties
public class ReturnCommentView {

	public String owner_id;
	public String owner_name;
	public String owner_pic_url;
	public String id;
	public String content;
	public String create_date;

	public Replies[] replies;

	public static class Replies {

		public String owner_id;
		public String owner_name;
		public String owner_pic_url;
		public String id;
		public String content;
		public String create_date;

	}
}
