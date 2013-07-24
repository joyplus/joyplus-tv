package com.joyplus.tv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyplus.tv.HistoryActivity.HistortyAdapter;
import com.joyplus.tv.Service.Return.ReturnUserFavorities;
import com.joyplus.tv.entity.HotItemInfo;
import com.joyplus.tv.utils.DBUtils;
import com.joyplus.tv.utils.ItemStateUtils;
import com.joyplus.tv.utils.Log;
import com.joyplus.tv.utils.UtilTools;
import com.umeng.analytics.MobclickAgent;

public class ShowShoucangHistoryActivity extends Activity implements OnClickListener, OnItemSelectedListener,OnFocusChangeListener {
	private static final String TAG = "ShowShoucangHistoryActivity";
	private Button btn_fenlei_all;
	private Button btn_fenlei_movie;
	private Button btn_fenlei_tv;
	private Button btn_fenlei_dongman;
	private Button btn_fenlei_zongyi;
	private Button selectedButton;
	private Button delBtn;
	private int index = 0;
	ObjectMapper mapper = new ObjectMapper();
	private List<HotItemInfo> allHistoryList;
	private List<HotItemInfo> movieHistoryList;
	private List<HotItemInfo> tvHistoryList;
	private List<HotItemInfo> dongmanHistoryList;
	private List<HotItemInfo> zongyiHistoryList;
	
	private View  selectedView;
	
	private ListView listView;
	private App app;
	private AQuery aq;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoucang);
		app = (App) getApplication();
		aq = new AQuery(this);
		
		listView = (ListView) findViewById(R.id.history_list);
		btn_fenlei_all = (Button) findViewById(R.id.fenlei_all);
		btn_fenlei_movie = (Button) findViewById(R.id.fenlei_movie);
		btn_fenlei_tv = (Button) findViewById(R.id.fenlei_tv);
		btn_fenlei_dongman = (Button) findViewById(R.id.fenlei_dongman);
		btn_fenlei_zongyi = (Button) findViewById(R.id.fenlei_zongyi);
		delBtn = (Button) findViewById(R.id.delete_btn);
		delBtn.setOnClickListener(this);
		btn_fenlei_all.setOnClickListener(this);
		btn_fenlei_movie.setOnClickListener(this);
		btn_fenlei_tv.setOnClickListener(this);
		btn_fenlei_dongman.setOnClickListener(this);
		btn_fenlei_zongyi.setOnClickListener(this);
		
		
		btn_fenlei_all.setOnFocusChangeListener(this);
		btn_fenlei_movie.setOnFocusChangeListener(this);
		btn_fenlei_tv.setOnFocusChangeListener(this);
		btn_fenlei_dongman.setOnFocusChangeListener(this);
		btn_fenlei_zongyi.setOnFocusChangeListener(this);
		
		
		btn_fenlei_all.setPadding(0, 0, 5, 0);
		btn_fenlei_movie.setPadding(0, 0, 5, 0);
		btn_fenlei_tv.setPadding(0, 0, 5, 0);
		btn_fenlei_dongman.setPadding(0, 0, 5, 0);
		btn_fenlei_zongyi.setPadding(0, 0, 5, 0);
		btn_fenlei_all.setTextColor(getResources().getColor(R.color.common_title_selected));
		btn_fenlei_all.setBackgroundResource(R.drawable.menubg);
		
		selectedButton = btn_fenlei_all;
		
		listView.setNextFocusLeftId(R.id.fenlei_all);
		selectedButton.setPadding(0, 0, 5, 0);
//		listView.setAdapter(new MovieAdapter());
		listView.setOnItemSelectedListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2>=((HistortyAdapter)listView.getAdapter()).data.size()){
					setResult(Activity.RESULT_OK);
					finish();
					return;
				}
//				Toast.makeText(ShowShoucangHistoryActivity.this, "seleced index = " + arg2, 100).show();
				final Dialog dialog = new AlertDialog.Builder(ShowShoucangHistoryActivity.this).create();
				dialog.show();
				LayoutInflater inflater = LayoutInflater.from(ShowShoucangHistoryActivity.this);
				View view = inflater.inflate(R.layout.layout_favourite_dialog, null);
				TextView nameText = (TextView) view.findViewById(R.id.dialog_title);
//				Button playButton = (Button) view.findViewById(R.id.history_play);
//				playButton.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//						CurrentPlayData playDate = new CurrentPlayData();
//						Intent intent = new Intent(ShowShoucangHistoryActivity.this,VideoPlayerActivity.class);
//						playDate.prod_id = ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_id;
//						playDate.prod_type = Integer.valueOf(((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_type);
//						playDate.prod_name = ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_name;
//						playDate.prod_url = ((HistortyAdapter)listView.getAdapter()).data.get(arg2).video_url;
////						playDate.prod_src = "";
//						if(!"".equals(((HistortyAdapter)listView.getAdapter()).data.get(arg2).playback_time)){
//							playDate.prod_time = Long.valueOf(((HistortyAdapter)listView.getAdapter()).data.get(arg2).playback_time);
//						}
////						playDate.prod_qua = Integer.valueOf(info.definition);
//						app.setCurrentPlayData(playDate);
//						startActivity(intent);
//					}
//				});
				Button viewDetailButton = (Button) view.findViewById(R.id.history_view);
				viewDetailButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						int  prod_type = Integer.valueOf(((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_type);
						Intent intent = null;
						switch (prod_type) {
						case 1:
							intent = new Intent(ShowShoucangHistoryActivity.this,ShowXiangqingMovie.class);
							break;
						case 2:
							intent = new Intent(ShowShoucangHistoryActivity.this,ShowXiangqingTv.class);
							break;
						case 3:
							intent = new Intent(ShowShoucangHistoryActivity.this,ShowXiangqingZongYi.class);
							break;
						case 131:
							intent = new Intent(ShowShoucangHistoryActivity.this,ShowXiangqingDongman.class);
							break;
						}
						if(intent == null){ 
							return; 
						}else{
							intent.putExtra("ID", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_id);
							intent.putExtra("prod_name", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_name);
							intent.putExtra("prod_url", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_pic_url);
							intent.putExtra("directors", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).directors);
							intent.putExtra("stars", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).stars);
							intent.putExtra("summary", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_summary);
							intent.putExtra("support_num", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).support_num);
							intent.putExtra("favority_num", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).favority_num);
							intent.putExtra("definition", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).definition);
							intent.putExtra("score", ((HistortyAdapter)listView.getAdapter()).data.get(arg2).score);
							startActivity(intent);
						}
					}
				});
				Button delButton = (Button) view.findViewById(R.id.history_del);
				delButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Log.i(TAG, "delButton.setOnClickListener--->" + ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_id);
						
						String prod_id = ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_id;
						deleteShoucang(true, ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_id);
//						((HistortyAdapter)listView.getAdapter()).data.remove(arg2);
						if(allHistoryList!=null){
							for(int i=0; i<allHistoryList.size(); i++){
								if(allHistoryList.get(i).prod_id.equals(prod_id)){
									allHistoryList.remove(i);
								}
							}
						}
						
						if(movieHistoryList!=null){
							for(int i=0; i<movieHistoryList.size(); i++){
								if(movieHistoryList.get(i).prod_id.equals(prod_id)){
									movieHistoryList.remove(i);
								}
							}
						}
						
						if(tvHistoryList!=null){
							for(int i=0; i<tvHistoryList.size(); i++){
								if(tvHistoryList.get(i).prod_id.equals(prod_id)){
									tvHistoryList.remove(i);
								}
							}
						}
						if(zongyiHistoryList!=null){
							for(int i=0; i<zongyiHistoryList.size(); i++){
								if(zongyiHistoryList.get(i).prod_id.equals(prod_id)){
									zongyiHistoryList.remove(i);
								}
							}
						}
						if(dongmanHistoryList!=null){
							for(int i=0; i<dongmanHistoryList.size(); i++){
								if(dongmanHistoryList.get(i).prod_id.equals(prod_id)){
									dongmanHistoryList.remove(i);
								}
							}
						}
						((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
						dialog.dismiss();

						//删除逐条数据时，同时删除数据库里面的数据
						DBUtils.deleteData4ProId(getApplicationContext(),
								UtilTools.getCurrentUserId(getApplicationContext()), 
								prod_id);
					}
				});
				nameText.setText(((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_name);
				dialog.setContentView(view);
			}
				
				
				
//				Toast.makeText(ShowShoucangHistoryActivity.this, "seleced index = " + arg2, 100).show();
//				
//				Dialog dialog = new AlertDialog.Builder(ShowShoucangHistoryActivity.this).create();
//				dialog.show();
//				LayoutInflater inflater = LayoutInflater.from(ShowShoucangHistoryActivity.this);
//				View view = inflater.inflate(R.layout.layout_history_dialog, null);
//				TextView name = (TextView) view.findViewById(R.id.dialog_title);
//				name.setText(allHistoryList.get(arg2).prod_name);
//				dialog.setContentView(view);
//			}
		});
		listView.setSelected(true);
		listView.setSelection(0);
		listView.requestFocus();
		listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
//				if(!hasFocus){
//					if(selectedView!=null){
//						selectedView.setBackgroundResource(R.drawable.bg_teat_repeat);
//					}
//				}else{
//					if(selectedView!=null){
//						selectedView.setBackgroundResource(R.drawable.historty_listitem_drawable_selector);
//					}
//				}
			}
		});
		
		getHistoryData(0);
	}
	
	class HistortyAdapter extends BaseAdapter{
		private List<HotItemInfo> data;
		
		public HistortyAdapter(List<HotItemInfo> data){
			this.data = data;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(data.size()==0){
				return 1;
			}else{
				return data.size();
			}
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
			if(convertView ==null){
				convertView = LayoutInflater.from(ShowShoucangHistoryActivity.this).inflate(R.layout.item_history_list,null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.img = (ImageView) convertView.findViewById(R.id.image);
				holder.directors = (TextView) convertView.findViewById(R.id.directors);
				holder.stars = (TextView) convertView.findViewById(R.id.stars);
				holder.directors_notice = (TextView) convertView.findViewById(R.id.directors_notice);
				holder.stars_notice = (TextView) convertView.findViewById(R.id.stars_notice);
				holder.content = (TextView) convertView.findViewById(R.id.content);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			if(data.size()>0){
				if("3".equals(data.get(position).prod_type)){
					holder.directors_notice.setText(R.string.xiangqing_zongyi_zhuchi);
					holder.stars_notice.setText(R.string.xiangqing_zongyi_shoubo);
					holder.directors.setText(data.get(position).stars);
					holder.stars.setText(data.get(position).directors);
				}else{
					holder.directors.setText(data.get(position).directors);
					holder.stars.setText(data.get(position).stars);
					holder.directors_notice.setText(R.string.xiangqing_daoyan_name);
					holder.stars_notice.setText(R.string.xiangqing_zhuyan_name);
				}
				holder.title.setText(data.get(position).prod_name);
				holder.content.setVisibility(View.GONE);
//				int prod_type = Integer.valueOf(data.get(position).prod_type);
//				String playBack_time = formatDuration(Integer.valueOf(data.get(position).playback_time));
//				String playBack_time = data.get(position).playback_time;
//				switch (prod_type) {
//				case 1:
//					holder.content.setText("上次观看到：" + playBack_time);
//					break;
//				case 2:
//					holder.content.setText("上次观看到：第" + data.get(position).cur_episode+"集"+playBack_time);
//					break;
//				case 3:
//					holder.content.setText("上次观看到：第" + data.get(position).cur_episode+"期"+playBack_time);
//					break;
//				case 131:
//					holder.content.setText("上次观看到：第" + data.get(position).cur_episode+"集"+playBack_time);
//					break;
//				}
				aq.id(holder.img).image(data.get(position).prod_pic_url);
			}else{
				holder.img.setImageResource(R.drawable.post_normal);
				holder.title.setText("您还未收藏过任何影片。去热播看看最近流行什么吧^_^~");
				holder.stars.setVisibility(View.GONE);
				holder.directors.setVisibility(View.GONE);
				holder.content.setVisibility(View.GONE);
				holder.stars_notice.setVisibility(View.GONE);
				holder.directors_notice.setVisibility(View.GONE);
			}
			convertView.setBackgroundResource(R.drawable.historty_listitem_drawable_selector);
			return convertView;
		}
		
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(selectedButton.equals(v)){
			return ;
		}
		
		switch (v.getId()) { 
		case R.id.fenlei_all:
			selectedButton.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
			selectedButton.setBackgroundResource(R.drawable.text_drawable_selector);
//			btn_fenlei_all.setTextColor(getResources().getColor(R.color.common_title_selected));
//			btn_fenlei_all.setBackgroundResource(R.drawable.menubg);
			selectedButton = btn_fenlei_all;
			selectedButton.setPadding(0, 0, 5, 0);
			index = 0;
			if(allHistoryList==null){
				listView.setAdapter(null);
				getHistoryData(0);
			}else{
				listView.setAdapter(new HistortyAdapter(allHistoryList));
			}
			listView.setNextFocusLeftId(R.id.fenlei_all);
			
			break;
		case R.id.fenlei_movie:
			selectedButton.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
			selectedButton.setBackgroundResource(R.drawable.text_drawable_selector);
//			btn_fenlei_movie.setTextColor(getResources().getColor(R.color.common_title_selected));
//			btn_fenlei_movie.setBackgroundResource(R.drawable.menubg);
			selectedButton = btn_fenlei_movie;
			selectedButton.setPadding(0, 0, 5, 0);
			index = 1;
			if(movieHistoryList==null){
				listView.setAdapter(null);
				getHistoryData(1);
			}else{
				listView.setAdapter(new HistortyAdapter(movieHistoryList));
			}
			
			listView.setNextFocusLeftId(R.id.fenlei_movie);
			break;
		case R.id.fenlei_tv:
			selectedButton.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
			selectedButton.setBackgroundResource(R.drawable.text_drawable_selector);
//			btn_fenlei_tv.setTextColor(getResources().getColor(R.color.common_title_selected));
//			btn_fenlei_tv.setBackgroundResource(R.drawable.menubg);
			selectedButton = btn_fenlei_tv;
			selectedButton.setPadding(0, 0, 5, 0);
			index = 2;
			if(tvHistoryList==null){
				listView.setAdapter(null);
				getHistoryData(2);
			}else{
				listView.setAdapter(new HistortyAdapter(tvHistoryList));
			}
			
			listView.setNextFocusLeftId(R.id.fenlei_tv);
			
			break;
		case R.id.fenlei_dongman:
			selectedButton.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
			selectedButton.setBackgroundResource(R.drawable.text_drawable_selector);
//			btn_fenlei_dongman.setTextColor(getResources().getColor(R.color.common_title_selected));
//			btn_fenlei_dongman.setBackgroundResource(R.drawable.menubg);
			selectedButton = btn_fenlei_dongman;
			selectedButton.setPadding(0, 0, 5, 0);
			index = 131;
			if(dongmanHistoryList==null){
				listView.setAdapter(null);
				getHistoryData(131);
			}else{
				listView.setAdapter(new HistortyAdapter(dongmanHistoryList));
			}
			listView.setNextFocusLeftId(R.id.fenlei_dongman);
			break;
		case R.id.fenlei_zongyi:
			selectedButton.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
			selectedButton.setBackgroundResource(R.drawable.text_drawable_selector);
//			btn_fenlei_zongyi.setTextColor(getResources().getColor(R.color.common_title_selected));
//			btn_fenlei_zongyi.setBackgroundResource(R.drawable.menubg);
			selectedButton = btn_fenlei_zongyi;
			selectedButton.setPadding(0, 0, 5, 0);
			index = 3;
			if(zongyiHistoryList==null){
				listView.setAdapter(null);
				getHistoryData(3);
			}else{
				listView.setAdapter(new HistortyAdapter(zongyiHistoryList));
			}
			
			listView.setNextFocusLeftId(R.id.fenlei_zongyi);
			break;
		case R.id.delete_btn:
			final Dialog dialog = new AlertDialog.Builder(ShowShoucangHistoryActivity.this).create();
			dialog.show();
			LayoutInflater inflater = LayoutInflater.from(ShowShoucangHistoryActivity.this);
			View view = inflater.inflate(R.layout.layout_deleteall_confirm_dialog, null);
			TextView nameText = (TextView) view.findViewById(R.id.dialog_title);
			Button cancelButton = (Button) view.findViewById(R.id.history_cancel);
			Button delButton = (Button) view.findViewById(R.id.history_del);
			dialog.setContentView(view);
			switch (index) {
			case 0:
				nameText.setText("是否清空所有记录？");
				break;
			case 1:
				nameText.setText("是否清空所有电影？");
				break;
			case 2:
				nameText.setText("是否清空所有电视剧？");
				break;
			case 3:
				nameText.setText("是否清空所有综艺？");
				break;
			case 131:
				nameText.setText("是否清空所有动漫？");
				break;
			}
			cancelButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			delButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					deleteShoucang(false, "");
					
					List<HotItemInfo> list = ((HistortyAdapter)listView.getAdapter()).data;
					for(int i=0;i< list.size();i++) {
						
						DBUtils.deleteData4ProId(getApplicationContext(),
								UtilTools.getCurrentUserId(getApplicationContext()), list.get(i).prod_id);
					}
					
//					((HistortyAdapter)listView.getAdapter()).data.clear();
					((HistortyAdapter)listView.getAdapter()).data.clear();
					if(allHistoryList!=null&&allHistoryList.size()>0){
						allHistoryList.clear();
						allHistoryList = null;
					}
					if(movieHistoryList!=null&&movieHistoryList.size()>0){
						movieHistoryList.clear();
						movieHistoryList = null;
					}
					if(tvHistoryList!=null&&tvHistoryList.size()>0){
						tvHistoryList.clear();
						tvHistoryList = null;
					}
					if(zongyiHistoryList!=null&&zongyiHistoryList.size()>0){
						zongyiHistoryList.clear();
						zongyiHistoryList = null;
					}
					if(dongmanHistoryList!=null&&dongmanHistoryList.size()>0){
						dongmanHistoryList.clear();
						dongmanHistoryList = null;
					}
					
					((HistortyAdapter)listView.getAdapter()).notifyDataSetChanged();
				}
			});
		default:
			break;
		}
	}
	
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		
		if(!hasFocus) {
			
			if(selectedButton.getId() == v.getId()) {
				Button button = (Button) v;
				ItemStateUtils.buttonToActiveState(getApplicationContext(), button);
			}
		} else {
			
			if(selectedButton.getId() == v.getId()) {
				
				Button button = (Button) v;
				ItemStateUtils.buttonToPTState(getApplicationContext(), button);
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		selectedView = arg1;
		if(((HistortyAdapter)listView.getAdapter()).data.size()-listView.getLastVisiblePosition()==3){
			getHistoryData(index);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void getHistoryData(int type){
		int index;
		if(listView.getAdapter()==null || ((HistortyAdapter)listView.getAdapter()).data==null){
			index = 1;
		}else{
			index = ((HistortyAdapter)listView.getAdapter()).data.size()/10 +1;
		}
		String url = Constant.BASE_URL + "user/favorities" +"?page_size=10&page_num=" + index;
		if(type!=0){
			url = url + "&vod_type=" + type;
		}
//		String url = Constant.BASE_URL + "user/playHistories" +"?page_num=1&page_size=10&userid=4742";

//		String url = Constant.BASE_URL;
		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		switch (type) {
		case 0:
			cb.url(url).type(JSONObject.class).weakHandler(this, "initAllHistoryData");
			break;
		case 1:
			cb.url(url).type(JSONObject.class).weakHandler(this, "initMovieHistoryData");
			break;
		case 2:
			cb.url(url).type(JSONObject.class).weakHandler(this, "initTvHistoryData");
			break;
		case 3:
			cb.url(url).type(JSONObject.class).weakHandler(this, "initZhongyiHistoryData");
			break;
		case 131:
			cb.url(url).type(JSONObject.class).weakHandler(this, "initDongmanHistoryData");
			break;
		default:
			break;
		}
		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}
	
	
	public void initAllHistoryData(String url, JSONObject json, AjaxStatus status){
		initHistoryData(url, json, status, 0);
	}
	public void initMovieHistoryData(String url, JSONObject json, AjaxStatus status){
		initHistoryData(url, json, status, 1);
	}
	public void initTvHistoryData(String url, JSONObject json, AjaxStatus status){
		initHistoryData(url, json, status, 2);
	}
	public void initZhongyiHistoryData(String url, JSONObject json, AjaxStatus status){
		initHistoryData(url, json, status, 3);
	}
	public void initDongmanHistoryData(String url, JSONObject json, AjaxStatus status){
		initHistoryData(url, json, status, 131);
	}
	
	
	public void initHistoryData(String url, JSONObject json, AjaxStatus status,int type){
		if (status.getCode() == AjaxStatus.NETWORK_ERROR)  {
//			aq.id(R.id.ProgressText).invisible();
			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}
		
		if(json == null || json.equals("")) 
			return;
		
		Log.d(TAG, "url--->>>>" + url);
		Log.d(TAG, "favourate data = " + json.toString());
		try {
			ReturnUserFavorities result  = mapper.readValue(json.toString(), ReturnUserFavorities.class);
			List<HotItemInfo> list = new ArrayList<HotItemInfo>();
			for(int i=0; i<result.favorities.length; i++){
				HotItemInfo item  =  new HotItemInfo();
//				item.id = result.favorities[i].id;
				item.prod_id = result.favorities[i].content_id;
				item.prod_name = result.favorities[i].content_name;
				item.prod_type = result.favorities[i].content_type;
//				item.prod_pic_url = result.favorities[i].big_content_pic_url;
				String bigPicUrl = result.favorities[i].big_content_pic_url;
				if(bigPicUrl == null || bigPicUrl.equals("")
						||bigPicUrl.equals(UtilTools.EMPTY)) {
					
					bigPicUrl = result.favorities[i].content_pic_url;
				}
				item.prod_pic_url = bigPicUrl;
				item.stars = result.favorities[i].stars;
				item.directors = result.favorities[i].directors;
				item.favority_num = result.favorities[i].favority_num;
				item.support_num = result.favorities[i].support_num;
				item.publish_date = result.favorities[i].publish_date;
				item.score = result.favorities[i].score;
				item.area = result.favorities[i].area;
				list.add(item);
			}
			switch (type) {
			case 0:
				if(allHistoryList==null){
					allHistoryList = list;
					listView.setAdapter(new HistortyAdapter(allHistoryList));
					listView.requestFocus();
				}else{
					allHistoryList.addAll(list);
					((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
				}
				break;
			case 1:
				if(movieHistoryList==null){
					movieHistoryList = list;
					listView.setAdapter(new HistortyAdapter(movieHistoryList));
				}else{
					movieHistoryList.addAll(list);
					((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
				}
				break;
			case 2:
				if(tvHistoryList==null){
					tvHistoryList = list;
					listView.setAdapter(new HistortyAdapter(tvHistoryList));
				}else{
					tvHistoryList.addAll(list);
					((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
				}
				break;
			case 3:
				if(zongyiHistoryList==null){
					zongyiHistoryList = list;
					listView.setAdapter(new HistortyAdapter(zongyiHistoryList));
				}else{
					zongyiHistoryList.addAll(list);
					((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
				}
				break;
			case 131:
				if(dongmanHistoryList==null){
					dongmanHistoryList = list;
					listView.setAdapter(new HistortyAdapter(dongmanHistoryList));
				}else{
					dongmanHistoryList.addAll(list);
					((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
				}
				break;
			}
//			listView.setSelection(0);
//			listView.requestFocus();
//			if(isFirstTime){
//				listView.requestFocus();
//				isFirstTime = false;
//			}else{
//				selectedButton.requestFocus();
//			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void deleteShoucang(boolean isSingle,String id){
		String url = "";
		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.SetHeader(app.getHeaders());
		if(isSingle){
			url = Constant.BASE_URL + "program/unfavority" ;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("prod_id", id);
			cb.params(params).url(url).type(JSONObject.class)
			.weakHandler(this, "deleteResult");
		}else{
			if(index == 0){
				url = Constant.BASE_URL + "user/clearFavorities";
				Map<String, Object> params = new HashMap<String, Object>();
				cb.params(params).url(url).type(JSONObject.class)
				.weakHandler(this, "deleteResult");
			}else{
				url = Constant.BASE_URL + "user/clearFavorities";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("vod_type", index);
				cb.params(params).url(url).type(JSONObject.class)
				.weakHandler(this, "deleteResult");
			}
			
		}
		aq.ajax(cb);
//		
//		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
//		Log.d("del", url);
//		cb.url(url).type(JSONObject.class).weakHandler(this, "deleteResult");
//		cb.SetHeader(app.getHeaders());
//		aq.ajax(cb);
	}
	
	public void deleteResult(String url, JSONObject json, AjaxStatus status){
		if (status.getCode() == AjaxStatus.NETWORK_ERROR)  {
//			aq.id(R.id.ProgressText).invisible();
			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}
		
		if(json == null || json.equals("")) 
			return;
		
		Log.d(TAG, json.toString());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		MobclickAgent.onResume(this);
		
		if(app.getUserInfo()!=null){
			aq.id(R.id.iv_head_user_icon).image(
					app.getUserInfo().getUserAvatarUrl(), false, true, 0,
					R.drawable.avatar_defult);
			aq.id(R.id.tv_head_user_name).text(app.getUserInfo().getUserName());
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		MobclickAgent.onPause(this);
	}
	
	class ViewHolder{
		TextView title;
		TextView stars;
		TextView stars_notice;
		TextView directors;
		TextView directors_notice;
		TextView content;
		ImageView img;
	}
	
//	class HistoryData{
//		public String id;
//		public String prod_id;
//		public String prod_name;
//		public String prod_type; //1，电影; 2，电视剧; 3，综艺; 4，动漫;
//		public String prod_pic_url;
//		public String stars;
//		public String directors;
//		public String favority_num;
//		public String support_num;
//		public String publish_date;
//		public String score;
//		public String area;
//		public String cur_episode;
//		public String max_episode;
//		public String definition;
//		public String prod_summary;
//		public String duration;
//		public String video_url;
//		public String playback_time;
//		public String prod_subname;
//		public String play_type;
//	}
}
