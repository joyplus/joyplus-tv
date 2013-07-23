package com.joyplus.tv.Service.Return;

/*
 * {
 "prestiges": [
 {
 "id": 用户id
 "nickname": 用户名
 "user_pic_url": 用户头像
 "is_follow":是否被当前用户关注 1：被关注，0：没有被关注
 },
 。。。。。。
 ]
 }
 */
public class ReturnUserPrestiges {
	public Prestiges[] prestiges;

	public static class Prestiges {

		public String id;
		public String nickname;
		public String user_pic_url;
		public String is_follow;

	}

}
