package com.joyplus.tv.Service.Return;

public class ReturnUserThirdPartyUsers {
	/*
	 * { "users": [ { "friend_id": joyplus系统的用户id "thirdpart_id": 第三方系统的用户id } ]
	 * }
	 */
	public Users[] users;

	public static class Users {

		public String friend_id;
		public String thirdpart_id;

	}
}
