package com.joyplus.tv.entity;

import java.util.ArrayList;

public class YueDanInfo {
	
//	public int type; 
	public String id;
	public String name;
	public String prod_type;//0- 连接  1 - 电影  2- 电视剧 
	public String pic_url;
	public String num;
	public String content;
	public ArrayList<ShiPinInfoParcelable> shiPinList;
}
