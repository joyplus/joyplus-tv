package com.joyplus.tv.Adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.IntArraySerializer;
import com.joyplus.tv.R;
import com.joyplus.tv.entity.HotItemInfo;
import com.joyplus.tv.utils.UtilTools;

public class MainHotItemAdapter extends BaseAdapter {

	private List<HotItemInfo> hot_list;
	private AQuery aq;
	private Context c;
	private int displayWith;
	private android.widget.Gallery.LayoutParams layoutParam;
	
	public MainHotItemAdapter(Context c,List<HotItemInfo> list){
		super();
		this.hot_list = list;
		this.c = c;
		displayWith = ((Activity)c).getWindowManager().getDefaultDisplay().getWidth();
		layoutParam = new android.widget.Gallery.LayoutParams((displayWith-40)/6,2*displayWith/9);
		aq = new AQuery(c);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return hot_list.size();
//		if(hot_list.size()!=0){
//			return 2;
//		}else{
//			return hot_list.size();
//		}
	}

	@Override
	public Object getItem(int position) {
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
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
//		aq = new AQuery(convertView);
		holder.image.setTag(hot_list.get(position).prod_pic_url);
		holder.score.setText(UtilTools.formateScore(hot_list.get(position).score));
//		holder.image.setImageResource(R.drawable.test1);
		aq.id(holder.image).image(hot_list.get(position).prod_pic_url,true,true,0,R.drawable.post_normal);
		if(hot_list.get(position).type == 0){
			holder.firstTitle.setVisibility(View.VISIBLE);
			if(hot_list.get(position).playback_time!=null&&!"".equals(hot_list.get(position).playback_time)){
				holder.content.setText("观看到:" +UtilTools.formatDuration1(Long.valueOf(hot_list.get(position).playback_time)));
			}else{
				holder.content.setText("");
			}
		}else{
			holder.firstTitle.setVisibility(View.GONE);
			int type = Integer.valueOf(hot_list.get(position).prod_type);
			switch (type) {
			case 1:
				if("".equals(hot_list.get(position).duration)){
					holder.content.setText("");
//					holder.content.setText("时长未知");
				}else{
//					holder.content.setText("时长："+hot_list.get(position).duration);
//					holder.content.setText("时长:"+hot_list.get(position).duration.replace("：00", "分钟"));
					holder.content.setText(UtilTools.formatMovieDuration(hot_list.get(position).duration));
				}
				break;
			case 2:
				if("".equals(hot_list.get(position).max_episode)){
					
					if("".equals(hot_list.get(position).cur_episode)||"0".equals(hot_list.get(position).cur_episode)) {
						
						holder.content.setText("");
					} else {
						
//						int curEpisode = Integer.valueOf(hot_list.get(position).cur_episode);
						holder.content.setText("更新到第" + hot_list.get(position).cur_episode+"集");
					}
				}else{
					if("".equals(hot_list.get(position).cur_episode)||"0".equals(hot_list.get(position).cur_episode)){
						holder.content.setText(hot_list.get(position).max_episode + "集全");
					}else{
						int curEpisode = Integer.valueOf(hot_list.get(position).cur_episode);
						int maxEpisode = Integer.valueOf(hot_list.get(position).max_episode);
						if(curEpisode>=maxEpisode){
							holder.content.setText(hot_list.get(position).max_episode + "集全");
						}else{
							holder.content.setText("更新到第" + hot_list.get(position).cur_episode+"集");
						}
					}
				}
				break;
			case 3:
				holder.score.setText("");
				holder.content.setText(UtilTools.formateZongyi(hot_list.get(position).cur_episode, c));
				break;
			case 131:
				if("".equals(hot_list.get(position).max_episode)){
					holder.content.setText("更新到第" + hot_list.get(position).cur_episode+"集");
				}else if(!hot_list.get(position).cur_episode.equals(hot_list.get(position).max_episode)){
					if("".equals(hot_list.get(position).cur_episode)||"0".equals(hot_list.get(position).cur_episode)){
						holder.content.setText(hot_list.get(position).max_episode + "集全");
					}else{
						holder.content.setText("更新到第" + hot_list.get(position).cur_episode+"集");
					}
				}else {
					holder.content.setText(hot_list.get(position).max_episode + "集全");
				}
				break;
			default:
				holder.content.setVisibility(View.INVISIBLE);
				break;
			}
		}
		holder.secondTitle.setText(hot_list.get(position).prod_name);
		
		switch (Integer.valueOf(hot_list.get(position).definition)) {
		case 8:
			holder.definition.setImageResource(R.drawable.icon_bd);
			break;
		case 7:
			holder.definition.setImageResource(R.drawable.icon_hd);
			break;
		case 6:
			holder.definition.setImageResource(R.drawable.icon_ts);
			break;
		default:
			holder.definition.setImageDrawable(null);
			break;
		}
		convertView.setPadding(15, 10, 15, 10);
		convertView.setLayoutParams(layoutParam);
		
//		ImageView img = (ImageView) view.findViewById(R.id.image);
//		
//		img.setImageResource(resouces[position]);
//		Log.d(TAG, hot_list.get(position).prod_pic_url);
//		aq = new AQuery(view);
//		aq.id(R.id.image).image(hot_list.get(position).prod_pic_url);
		return convertView;
	}
	
	
	
	class ViewHolder{
		TextView firstTitle;
		TextView secondTitle;
		TextView content;
		TextView score;
		ImageView image;
		ImageView definition;
	}
}


