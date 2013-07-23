package com.joyplus.tv.Adapters;

public class MovieBangDanData {

	private String Pic_ID;
	private String Pic_url;
	private String Pic_name;
	private String[] Pic_lists;
	public String[] getPic_lists() {
		return Pic_lists;
	}

	public void setPic_lists(String[] pic_lists) {
		Pic_lists = pic_lists;
	}

	private String right;

	public String getPic_ID() {
		return Pic_ID;
	}

	public void setPic_ID(String pic_ID) {
		Pic_ID = pic_ID;
	}

	public String getPic_url() {
		return Pic_url;
	}

	public void setPic_url(String pic_url) {
		Pic_url = pic_url;
	}

	public String getPic_name() {
		return Pic_name;
	}

	public void setPic_name(String pic_name) {
		Pic_name = pic_name;
	}
	
	

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

}
