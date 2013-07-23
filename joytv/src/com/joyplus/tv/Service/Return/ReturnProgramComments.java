package com.joyplus.tv.Service.Return;

/*
 *参数：

app_key required string 申请应用时分配的AppKey。
prod_id required string 节目id
page_num = 需要请求的页码（可选），默认为1
page_size = 每一页包含的记录数（可选），默认为10
返回值：

{
comments: [
    {
        owner_id: int 发表评论的用户id
        owner_name: string 发表评论用户名
        owner_pic_url: string 发表评论用户的头像
        id: int 评论id
        content: string 评论的内容
        create_date: date 评论时间
    }
    ......
  ]
}
 */
public class ReturnProgramComments {

	public Comments[] comments;

	public static class Comments {
		public String owner_id;//int 发表评论的用户id
		public String owner_name;//string 发表评论用户名
		public String owner_pic_url;//string 发表评论用户的头像
		public String id;// int 评论id
		public String content;//string 评论的内容
		public String create_date;//date 评论时间
	}

}
