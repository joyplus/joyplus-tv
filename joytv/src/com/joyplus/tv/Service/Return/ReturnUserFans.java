package com.joyplus.tv.Service.Return;

/*
 *  {
 "fans": [
 {
 "id": 用户id
 "nickname": 用户名
 "user_pic_url": 用户头像
 },
 .......
 ]
 }
 */
public class ReturnUserFans {
	public Fans[] fans;

	public static class Fans {

		public String id;
		public String nickname;
		public String user_pic_url;

	}
}
