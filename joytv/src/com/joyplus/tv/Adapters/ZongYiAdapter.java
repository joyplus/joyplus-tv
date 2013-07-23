package com.joyplus.tv.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.joyplus.tv.R;
import com.joyplus.tv.entity.GridViewItemHodler;
import com.joyplus.tv.entity.MovieItemData;
import com.joyplus.tv.utils.AsyncImageLoader;
import com.joyplus.tv.utils.BangDanConstant;
import com.joyplus.tv.utils.JieMianConstant;
import com.joyplus.tv.utils.Log;
import com.joyplus.tv.utils.OnImageLoadListener;
import com.joyplus.tv.utils.UtilTools;

public class ZongYiAdapter extends BaseAdapter implements JieMianConstant{
	private static final String TAG = "ZongYiAdapter";

	private List<MovieItemData> movieList = new ArrayList<MovieItemData>();

	private int popWidth,popHeight;
	
	private Context context;
	private AQuery aq;
	
	private boolean isPreLoad = true;
	private long itemId = 0;
//	private AsyncImageLoader loader;
	private SparseArray<View> arrays = new SparseArray<View>();
//	private Resources resources;
	
	private boolean isShoucangShow = false;
	private String qita_name = "";//其他类型
	private int shouCangCount = 0;

	public ZongYiAdapter(Context context,AQuery aq) {
		
		this.context = context;
		this.aq = aq;
//		this.resources = context.getResources();
//		loader = new AsyncImageLoader(new OnImageLoadListener() {
//			
//			@Override
//			public void ImageLoadFinished(Bitmap bitmap, String imageUrl, int position) {
//				// TODO Auto-generated method stub
//				View view = arrays.get(position);
//				GridViewItemHodler gvGridViewItemHodler = (GridViewItemHodler) view.getTag();
//				if(gvGridViewItemHodler.haibaoIv.getTag().equals(imageUrl)) {
//					
////					gvGridViewItemHodler.haibaoIv.setBackgroundDrawable(new BitmapDrawable(resources, bitmap));
//					gvGridViewItemHodler.haibaoIv.setImageBitmap(bitmap);
//				}
//			}
//		});
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (movieList.size() <= 0 && isPreLoad) {

			return DEFAULT_ITEM_NUM;
		}
		return movieList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (movieList.size() <= 0 && isPreLoad) {

			return null;
		}
		return movieList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		
		return itemId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GridViewItemHodler viewItemHodler = null;
		
		
		int width = parent.getWidth() / 5;
		int height = (int) (width / 1.0f / STANDARD_PIC_WIDTH * STANDARD_PIC_HEIGHT);

		if (convertView == null) {
			viewItemHodler = new GridViewItemHodler();
			convertView = ((Activity)context).getLayoutInflater().inflate(
					R.layout.show_item_layout_dianying, null);
			viewItemHodler.nameTv = (TextView) convertView
					.findViewById(R.id.tv_item_layout_name);
			viewItemHodler.scoreTv = (TextView) convertView
					.findViewById(R.id.tv_item_layout_score);
			viewItemHodler.otherInfo = (TextView) convertView
					.findViewById(R.id.tv_item_layout_other_info);
			viewItemHodler.haibaoIv = (ImageView) convertView
					.findViewById(R.id.iv_item_layout_haibao);
			viewItemHodler.definition = (ImageView) convertView.findViewById(R.id.iv_item_layout_gaoqing_logo);
			viewItemHodler.isActive = false;
			convertView.setTag(viewItemHodler);
			
			convertView.setPadding(GRIDVIEW_ITEM_PADDING_LEFT,
					GRIDVIEW_ITEM_PADDING, GRIDVIEW_ITEM_PADDING_LEFT,
					GRIDVIEW_ITEM_PADDING);


		} else {

			viewItemHodler = (GridViewItemHodler) convertView.getTag();
			
			if(viewItemHodler == null) {
				viewItemHodler = new GridViewItemHodler();
				convertView = ((Activity)context).getLayoutInflater().inflate(
						R.layout.show_item_layout_dianying, null);
				viewItemHodler.nameTv = (TextView) convertView
						.findViewById(R.id.tv_item_layout_name);
				viewItemHodler.scoreTv = (TextView) convertView
						.findViewById(R.id.tv_item_layout_score);
				viewItemHodler.otherInfo = (TextView) convertView
						.findViewById(R.id.tv_item_layout_other_info);
				viewItemHodler.haibaoIv = (ImageView) convertView
						.findViewById(R.id.iv_item_layout_haibao);
				viewItemHodler.definition = (ImageView) convertView.findViewById(R.id.iv_item_layout_gaoqing_logo);
				viewItemHodler.isActive = false;
				convertView.setTag(viewItemHodler);
				
				convertView.setPadding(GRIDVIEW_ITEM_PADDING_LEFT,
						GRIDVIEW_ITEM_PADDING, GRIDVIEW_ITEM_PADDING_LEFT,
						GRIDVIEW_ITEM_PADDING);
			}
		}
		
		if(arrays.get(position) != convertView) {
			
			arrays.put(position, convertView);
		}
		
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				width, height);
		convertView.setLayoutParams(params);
		convertView.setVisibility(View.VISIBLE);
		
		if(width != 0) {
			
			popHeight = height;
			popWidth = width;
		}


		if (movieList.size() <= 0 && isPreLoad) {

			return convertView;
		}
		
		//如果要显示收藏
		if(isShoucangShow) {
			
			//首先拿到收藏数 如果收藏数不能被5整除，凑满，整除
			if(UtilTools.isPostionEmpty(position, shouCangCount)) {
				
				convertView.setVisibility(View.INVISIBLE);
				
				convertView.setTag(null);
				
				return convertView;
			}
			
			//显示其他
			if(UtilTools.isPositionShowQitaTitle(position, shouCangCount)) {
				
				AbsListView.LayoutParams paramsTv = new AbsListView.LayoutParams(
						width, 31);
				TextView tv = new TextView(context);
				tv.setBackgroundColor(context.getResources().getColor(R.color.menu_bg));
				
				//显示文字
				if(UtilTools.isPostionShowText(position, shouCangCount)) {
					
					tv.setText(qita_name);
					
					tv.setGravity(Gravity.CENTER|Gravity.LEFT);
					tv.setTextSize(20);
					tv.setPadding(23, 0, 0, 0);
					tv.setTextColor(context.getResources().getColor(R.color.text_normal));
				}
				tv.setLayoutParams(paramsTv);
				tv.setEnabled(false);
				tv.setFocusable(false);
				
				convertView = tv;
				convertView.setTag(null);
				
				return convertView;
			}
		}
		
		viewItemHodler.nameTv.setText("");
		viewItemHodler.otherInfo.setText("");
//		viewItemHodler.haibaoIv.setBackgroundResource(R.drawable.post_normal);	
		
		if(viewItemHodler.haibaoIv.getTag()==null || 
				!viewItemHodler.haibaoIv.getTag().equals(movieList.get(position).getMoviePicUrl())){
		
			viewItemHodler.haibaoIv.setImageBitmap(null);
		}

		viewItemHodler.nameTv.setText(movieList.get(position).getMovieName());
		
		String definition = movieList.get(position).getDefinition();
		
		Log.i(TAG, "position:" + position + " definition:" + definition);
		if(definition != null && !definition.equals("")) {
			viewItemHodler.definition.setVisibility(View.VISIBLE);
			switch (Integer.valueOf(definition)) {
			case 8:
				viewItemHodler.definition.setImageResource(R.drawable.icon_bd);
				break;
			case 7:
				viewItemHodler.definition.setImageResource(R.drawable.icon_hd);
				break;
			case 6:
				viewItemHodler.definition.setImageResource(R.drawable.icon_ts);
				break;
			default:
				viewItemHodler.definition.setVisibility(View.GONE);
				break;
			}
		}

		
		String proType = movieList.get(position).getMovieProType();
		
		if(proType != null && !proType.equals("")) {
			
			if(proType.equals("1")) {
				
				viewItemHodler.scoreTv.setText(UtilTools.formateScore(movieList.get(position).getMovieScore()));
				String duration = movieList.get(position).getMovieDuration();
				if(duration != null && !duration.equals("")) {
					
					viewItemHodler.otherInfo.setText(UtilTools.formatMovieDuration(duration));
				}
				
			} else if(proType.equals("2") || proType.equals("131")){
				
				viewItemHodler.scoreTv.setText(UtilTools.formateScore(movieList.get(position).getMovieScore()));
				String curEpisode = movieList.get(position).getMovieCurEpisode();
				String maxEpisode = movieList.get(position).getMovieMaxEpisode();
				
				if(maxEpisode != null && !maxEpisode.equals("")
						&& !maxEpisode.equals("0")) {

					if(curEpisode == null || curEpisode.equals("") 
							|| curEpisode.equals("0")) {
						
						viewItemHodler.otherInfo.setText(
								maxEpisode + context.getString(R.string.dianshiju_jiquan));
						} else{

							int max = 0;
							int min = 0;
							try {
								min = Integer.valueOf(curEpisode);
								max = Integer.valueOf(maxEpisode);
								
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							if(max != 0 && min != 0) {
								
								if(min >= max) {
									
									viewItemHodler.otherInfo.setText(
											maxEpisode + context.getString(R.string.dianshiju_jiquan));
								} else {
									
									viewItemHodler.otherInfo.setText(context.getString(R.string.zongyi_gengxinzhi) + 
											curEpisode);
								}
							}

					}
				} else {
					
					Log.i(TAG, "curEpisode--->" + curEpisode);
					
					if(curEpisode != null &&  !curEpisode.equals("") 
							&& !curEpisode.equals("0")) {
						
						int cur = 0;
						
						try {
							cur = Integer.valueOf(curEpisode);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(cur != 0) {
							
							viewItemHodler.otherInfo.setText(context.getString(R.string.zongyi_gengxinzhi) + 
									curEpisode);
						}
					}
				}

			} else if(proType.equals("3")) {
				
				String curEpisode = movieList.get(position).getMovieCurEpisode();
				if(curEpisode != null && !curEpisode.equals("")) {
					
					viewItemHodler.otherInfo.setText(UtilTools.formateZongyi(curEpisode,context));
				}
			}
		}

		viewItemHodler.haibaoIv.setTag(movieList.get(position).getMoviePicUrl());
		
//		loader.loadBitmap(movieList.get(position).getMoviePicUrl(), position);
		
		aq.id(viewItemHodler.haibaoIv).image(movieList.get(position).getMoviePicUrl(), 
				false, true,0, R.drawable.post_normal);
		return convertView;
	}

	public void setList(List<MovieItemData> list,boolean isPreLoadPic) {
		this.movieList = list;
		this.isPreLoad = isPreLoadPic;
	}
	
	public List<MovieItemData> getMovieList() {
		return movieList;
	}
	
	public int getWidth() {
		return popWidth;
	}

	public int getHeight() {
		return popHeight;
	}
	
	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	
	public String getQita_name() {
		return qita_name;
	}

	public void setQita_name(String qita_name) {
		this.qita_name = qita_name;
	}

	public int getShouCangCount() {
		return shouCangCount;
	}

	public void setShouCangCount(int shouCangCount) {
		this.shouCangCount = shouCangCount;
	}
	
	public boolean isShoucangShow() {
		return isShoucangShow;
	}

	public void setShoucangShow(boolean isShoucangShow) {
		this.isShoucangShow = isShoucangShow;
	}

}
