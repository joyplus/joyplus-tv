package com.joyplus.tv.Adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.joyplus.tv.R;
import com.joyplus.tv.entity.YueDanInfo;

public class MainYueDanItemAdapter extends BaseAdapter {
	
	private List<YueDanInfo> yuedan_list;
	private Context c;
	private android.widget.Gallery.LayoutParams layoutParam;
	private int displayWith;
	private AQuery aq;

	public MainYueDanItemAdapter(Context c, List<YueDanInfo> yuedan_list) {
		// TODO Auto-generated constructor stub
		this.c = c;
		this.yuedan_list = yuedan_list;
		displayWith = ((Activity)c).getWindowManager().getDefaultDisplay().getWidth();
		layoutParam = new android.widget.Gallery.LayoutParams((displayWith-40)/6,2*displayWith/9);
		aq = new AQuery(c);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return yuedan_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(c).inflate(R.layout.item_layout_gallery, null);
			holder = new ViewHolder();
			holder.firstTitle = (TextView) convertView.findViewById(R.id.first_title);
			holder.secondTitle = (TextView) convertView.findViewById(R.id.second_title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.definition = (ImageView) convertView.findViewById(R.id.definition);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.bottom_frame);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		int type = Integer.valueOf(yuedan_list.get(position).prod_type);
		switch (type) {
		case 0://连接
//			holder.firstTitle.setVisibility(View.GONE);
			holder.layout.setVisibility(View.GONE);
			holder.definition.setVisibility(View.GONE);
			if("-1".equals(yuedan_list.get(position).id)){
				holder.image.setImageResource(R.drawable.more_movie);
			}else if("-2".equals(yuedan_list.get(position).id)){
				holder.image.setImageResource(R.drawable.more_episode);
			}
			break;
		case 1://电影
			holder.layout.setVisibility(View.VISIBLE);
			holder.definition.setVisibility(View.VISIBLE);
			holder.image.setTag(yuedan_list.get(position).pic_url);
			aq.id(holder.image).image(yuedan_list.get(position).pic_url,true,true,0,R.drawable.post_normal);
			holder.secondTitle.setText(yuedan_list.get(position).name);
			holder.content.setText(c.getString(R.string.before_num_text) + 
					yuedan_list.get(position).num+ c.getString(R.string.after_num_text));
//			holder.content.setText(yuedan_list.get(position).content);
			holder.definition.setImageResource(R.drawable.icon_movie);
			break;//电视剧
		case 2:
			holder.layout.setVisibility(View.VISIBLE);
			holder.definition.setVisibility(View.VISIBLE);
			holder.image.setTag(yuedan_list.get(position).pic_url);
			aq.id(holder.image).image(yuedan_list.get(position).pic_url,true,true,0,R.drawable.post_normal);
			holder.secondTitle.setText(yuedan_list.get(position).name);
			holder.content.setText(c.getString(R.string.before_num_text) + 
					yuedan_list.get(position).num+ c.getString(R.string.after_num_text));
//			holder.content.setText(yuedan_list.get(position).content);
			holder.definition.setImageResource(R.drawable.icon_episode);
			break;
		default:
			break;
		}
		
//		aq = new AQuery(convertView);
//		holder.image.setTag(yuedan_list.get(position).prod_pic_url);
////		holder.image.setImageResource(R.drawable.test1);
//		aq.id(holder.image).image(yuedan_list.get(position).prod_pic_url,true,true);
//		if(yuedan_list.get(position).type == 0){
//			holder.firstTitle.setVisibility(View.VISIBLE);
//		}else{
//			holder.firstTitle.setVisibility(View.GONE);
//		}
//		holder.secondTitle.setText(yuedan_list.get(position).prod_name);
//		holder.score.setVisibility(View.GONE);
//		switch (Integer.valueOf(yuedan_list.get(position).definition)) {
//		case 5:
//			holder.definition.setImageResource(R.drawable.icon_bd);
//			break;
//		case 4:
//			holder.definition.setImageResource(R.drawable.icon_hd);
//			break;
//		case 3:
//			holder.definition.setImageResource(R.drawable.icon_ts);
//			break;
//		default:
//			holder.definition.setVisibility(View.GONE);
//			break;
//		}
		convertView.setPadding(15, 10, 15, 10);
		convertView.setLayoutParams(layoutParam);
		return convertView;
	}
	
	class ViewHolder{
		TextView firstTitle;
		TextView secondTitle;
		TextView content;
		TextView score;
		ImageView image;
		ImageView definition;
		LinearLayout layout;
	}

}
