package com.joyplus.tv.Service.Return;

public class ReturnUserView {
	/*
	 * { "id": 用户id "username": 用户名 "nickname": 用户昵称 "email": 电子邮件 "phone": 电话号码
	 * "pic_url": 头像 "bg_url": 背景图片 "like_num": 喜欢我发布节目的用户数 "follow_num":
	 * 我关注的用户数 "fan_num": 我的粉丝书 "isFollowed": 登陆用户是否关注过这个用户 true：已经关注，false为未关注
	 * }
	 */

	public String id;
	public String username;
	public String nickname;
	public String email;

	public String phone;
	public String pic_url;
	public String bg_url;
	public String like_num;
	public String follow_num;
	public String fan_num;
	public String isFollowed;

}
