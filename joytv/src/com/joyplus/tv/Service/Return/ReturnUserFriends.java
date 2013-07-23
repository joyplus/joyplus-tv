package com.joyplus.tv.Service.Return;

/*
 *{
 "friends": [
 {
 "id": 用户id
 "nickname": 用户名
 "user_pic_url": 用户头像
 },
 。。。。。。
 ]
 }
 */
public class ReturnUserFriends {

	public Friends[] friends;

	public static class Friends {

		public String id;
		public String nickname;
		public String user_pic_url;

	}

}
