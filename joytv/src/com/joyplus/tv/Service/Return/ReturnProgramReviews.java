package com.joyplus.tv.Service.Return;

/*
 * {
  reviews: [
    {
        review_id: int 影评id
        title: string 影评标题
        comments: string 影评
        douban_review_id: int 豆瓣影评id
        create_date: date 评论时间
    }
    ......
  ]
}
 */
public class ReturnProgramReviews {
	public Reviews[] reviews;

	public static class Reviews {
		public String review_id;
		public String title;
		public String comments;
		public String douban_review_id;
		public String create_date;
	}

}
