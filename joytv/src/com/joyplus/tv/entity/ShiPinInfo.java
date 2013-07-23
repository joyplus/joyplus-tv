package com.joyplus.tv.entity;


/**
 * id: "15845",
	prod_id: "1009837",
	prod_name: "PRICELESS",
	definition: "0",
	prod_type: "2",
	prod_pic_url: "http://img3.douban.com/mpic/s22711338.jpg",
	big_prod_pic_url: "http://img3.douban.com/view/photo/photo/public/p1724422946.jpg",
	stars: "木村拓哉 / 中井贵一 / 香里奈 / 藤谷太辅 / 莲佛美沙子 / 升毅 / 前田旺志郎 / 田中奏生 / 中村敦夫 / イッセー尾形 / 夏木真理 / 藤木直人 / 小嶋阳菜",
	directors: "铃木雅之 / 平野真",
	favority_num: "83",
	support_num: "21",
	publish_date: "2012",
	score: "7.7",
	area: "日本",
	max_episode: "",
	cur_episode: "10",
	duration: ""
 *
 */
public class ShiPinInfo {
	
	private String id;
	private String prod_id;
	private String definition;
	private String prod_name;
	private String prod_type;
	private String prod_pic_url;
	private String big_prod_pic_url;
//	// 综艺多了个 : 最新剧集
	private String cur_item_name;
	private String stars;
	private String directors;
	private String favority_num;
	private String support_num;
	private String publish_date;
	private String score;
	private String area;
	private String max_episode;
	private String cur_episode;
	private String duration;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getProd_type() {
		return prod_type;
	}
	public void setProd_type(String prod_type) {
		this.prod_type = prod_type;
	}
	public String getProd_pic_url() {
		return prod_pic_url;
	}
	public void setProd_pic_url(String prod_pic_url) {
		this.prod_pic_url = prod_pic_url;
	}
	public String getBig_prod_pic_url() {
		return big_prod_pic_url;
	}
	public void setBig_prod_pic_url(String big_prod_pic_url) {
		this.big_prod_pic_url = big_prod_pic_url;
	}
	public String getCur_item_name() {
		return cur_item_name;
	}
	public void setCur_item_name(String cur_item_name) {
		this.cur_item_name = cur_item_name;
	}
	public String getStars() {
		return stars;
	}
	public void setStars(String stars) {
		this.stars = stars;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public String getFavority_num() {
		return favority_num;
	}
	public void setFavority_num(String favority_num) {
		this.favority_num = favority_num;
	}
	public String getSupport_num() {
		return support_num;
	}
	public void setSupport_num(String support_num) {
		this.support_num = support_num;
	}
	public String getPublish_date() {
		return publish_date;
	}
	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getMax_episode() {
		return max_episode;
	}
	public void setMax_episode(String max_episode) {
		this.max_episode = max_episode;
	}
	public String getCur_episode() {
		return cur_episode;
	}
	public void setCur_episode(String cur_episode) {
		this.cur_episode = cur_episode;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
}
