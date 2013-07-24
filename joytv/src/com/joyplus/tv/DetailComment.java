package com.joyplus.tv;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyplus.tv.Adapters.DetailCommentListData;
import com.joyplus.tv.Service.Return.ReturnProgramReviews;
import com.umeng.analytics.MobclickAgent;

public class DetailComment extends Activity implements
		android.widget.AdapterView.OnItemClickListener {
	private String TAG = "DetailComment";
	private AQuery aq;
	private App app;
	private ReturnProgramReviews m_ReturnProgramReviews = null;

	private String prod_id = null;
	private String prod_name = null;
	private String prod_dou = null;
	private String prod_url = null;

	private ArrayList dataStruct;
	private ListView ItemsListView;
	private DetailCommentListAdapter DetailCommentAdapter;
	private int isLastisNext = 1;
	private boolean isDetailComment = false;
	private int CurrentIndex = 0;
	private ScrollView scrollViewItemDetail;
	private TextView mtextViewItemDetail;
	private int CurrentDetailComment = 0;
	private int totalDetailCommentHeight = 0;
	private Animation fade_in;
//	private Animation fade_out;
	private int mListViewHeight = 172;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_comment);
		app = (App) getApplication();
		aq = new AQuery(this);
		
		dataStruct = new ArrayList();

		// 获取listview对象
		ItemsListView = (ListView) findViewById(R.id.listView1);
		
		
		ItemsListView.setNextFocusLeftId(R.id.listView1);
		
		
		// 设置listview的点击事件监听器
		ItemsListView.setOnItemClickListener(this);
//		ItemsListView.setOnKeyListener(new View.OnKeyListener() {
//
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				// TODO Auto-generated method stub
//				int action = event.getAction();
//				if (action == KeyEvent.ACTION_DOWN
//						&& keyCode == KeyEvent.KEYCODE_NUMPAD_5) {
//					aq.id(R.id.scrollViewItemDetail).invisible();
//					aq.id(R.id.listView1).visible();
//					aq.id(R.id.listView1).getView().requestFocus();
//				}
//				return false;
//			}
//		});
		ItemsListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 判断滚动到底部
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						isLastisNext++;
						GetServiceData(isLastisNext);
					}
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		fade_in = AnimationUtils.loadAnimation(this, R.anim.comment_fade_in);
//		fade_out = AnimationUtils.loadAnimation(this,
//				R.anim.comment_fade_out);
		 Intent intent = getIntent();
		 Bundle bundle = intent.getExtras();
		 prod_id = bundle.getString("prod_id");
		 prod_name = bundle.getString("prod_name");
		 prod_dou =bundle.getString("prod_dou");
		 prod_url  =bundle.getString("prod_url");

//		prod_id = "6524";
//		prod_name = "那年冬天,风在吹";
//		prod_dou = "3.5";
//		prod_url = "http://img3.douban.com/view/photo/photo/public/p1869602430.jpg";

		aq.id(R.id.textView_score).text(prod_dou);
		aq.id(R.id.textView1).text(prod_name);
		aq.id(R.id.image).image(prod_url, true, true, 0,
				R.drawable.post_normal);

		scrollViewItemDetail = (ScrollView) findViewById(R.id.scrollViewItemDetail);
		
//		scrollViewItemDetail.setScrollbarFadingEnabled(false);
		
		mtextViewItemDetail = (TextView) findViewById(R.id.textViewItemDetail);
		if (prod_id != null)
			CheckSaveData();
//		if (prod_dou != null)
//			updateScore(prod_dou);
		if (Float.parseFloat(prod_dou) > 0 && Float.parseFloat(prod_dou) <= 10)
			updateScore(prod_dou);
		// MobclickAgent.setDebugMode(true);
	}

//	public void InitDou() {
//		String m_j = null;
//		int i = 0;
//		Float f_dou = Float.parseFloat(prod_dou)/2;
//
//		for (i = 0; i < f_dou / 1; i++) {
//			m_j = Integer.toString(i + 2);
//			ImageView m_ImageView = (ImageView) this
//					.findViewById(getResources().getIdentifier(
//							"imageView" + m_j, "id", getPackageName()));
//			m_ImageView.setImageResource(R.drawable.star_on);
//		}
//		if (f_dou % i > 0) {
//			m_j = Integer.toString(i + 1);
//			ImageView m_ImageView = (ImageView) this
//					.findViewById(getResources().getIdentifier(
//							"imageView" + m_j, "id", getPackageName()));
//			m_ImageView.setImageResource(R.drawable.star_half);
//		}
//	}

	public void OnClickTab1TopLeft(View v) {

	}

	public void OnClickDownloadTopRight(View v) {

	}

	public void OnClickTab1TopRight(View v) {
	}

	@Override
	protected void onDestroy() {
		if (aq != null)
			aq.dismiss();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(app.getUserInfo()!=null){
			aq.id(R.id.iv_head_user_icon).image(
					app.getUserInfo().getUserAvatarUrl(), false, true, 0,
					R.drawable.avatar_defult);
			aq.id(R.id.tv_head_user_name).text(app.getUserInfo().getUserName());
		}
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void GetReviews() {
		String m_j = null;

		if (m_ReturnProgramReviews.reviews == null)
			return;

		aq.id(R.id.scrollViewItemDetail).gone();
		aq.id(R.id.listView1).visible();
//		aq.id(R.id.listView1).getView().requestFocus();
		

		if (isLastisNext > 1) {
			for (int i = 0; i < m_ReturnProgramReviews.reviews.length; i++) {
				DetailCommentListData m_DetailCommentListData = new DetailCommentListData();
				m_DetailCommentListData.Prod_ID = m_ReturnProgramReviews.reviews[i].review_id;
				m_DetailCommentListData.Prod_title = m_ReturnProgramReviews.reviews[i].title;
				m_DetailCommentListData.Prod_comments = m_ReturnProgramReviews.reviews[i].comments
						.replace("\r", "\r\n");
				dataStruct.add(m_DetailCommentListData);
				DetailCommentAdapter.notifyDataSetChanged();
			}
			return;

		} else {
			DetailCommentAdapter.notifyDataSetChanged();
		}

		for (int i = 0; i < m_ReturnProgramReviews.reviews.length; i++) {
			DetailCommentListData m_DetailCommentListData = new DetailCommentListData();
			m_DetailCommentListData.Prod_ID = m_ReturnProgramReviews.reviews[i].review_id;
			m_DetailCommentListData.Prod_title = m_ReturnProgramReviews.reviews[i].title;
			m_DetailCommentListData.Prod_comments = m_ReturnProgramReviews.reviews[i].comments
					.replace("\r", "\r\n");
			dataStruct.add(m_DetailCommentListData);
		}
		DetailCommentAdapter.notifyDataSetChanged();
		
		ItemsListView.requestFocus();
		ItemsListView.setSelection(0);
	}

	public void OnClickImageView(View v) {
		/*
		 * Intent intent = new Intent(this, BuChongGeRenZhiLiao.class);
		 * intent.putExtra("prod_id", m_prod_id); intent.putExtra("prod_type",
		 * m_prod_type); try { startActivity(intent); } catch
		 * (ActivityNotFoundException ex) { Log.e(TAG,
		 * "OnClickImageView failed", ex); }
		 */
	}

	// 初始化list数据函数
	public void InitListData(String url, JSONObject json, AjaxStatus status) {

		if (status.getCode() == AjaxStatus.NETWORK_ERROR) {
			// aq.id(R.id.ProgressText).invisible();
			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (isLastisNext > 1)
				m_ReturnProgramReviews = null;
			m_ReturnProgramReviews = mapper.readValue(json.toString(),
					ReturnProgramReviews.class);
			if (m_ReturnProgramReviews.reviews.length > 0)
				app.SaveServiceData(
						"tops" + prod_id + Integer.toString(isLastisNext),
						json.toString());

			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			mListViewHeight = (int) (display.getHeight() - (display.getHeight()
					* 0.8 / 6.8 + display.getHeight() / 7 - 20)) / 3;
			// 创建数据源对象
			GetReviews();

			if (dataStruct.size() > 0) {
				aq.id(R.id.textView3).text(
						"1/" + Integer.toString(dataStruct.size()));
				aq.id(R.id.textView3).visible();
			}
			// aq.id(R.id.ProgressText).invisible();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// listview的点击事件接口函数
	@Override
	public void onItemClick(AdapterView adapterview, View view, int i, long l) {
		CurrentIndex = i;
		DetailCommentListData m_DetailCommentListData = (DetailCommentListData) ItemsListView
				.getItemAtPosition(i);
		if (m_ReturnProgramReviews != null) {
			isDetailComment = true;
			CurrentDetailComment = 0;
//			 ItemsListView.startAnimation(fade_out);
			scrollViewItemDetail.startAnimation(fade_in);
			scrollViewItemDetail.setScrollY(0);
			aq.id(R.id.listViewItemTitle).text(
					m_DetailCommentListData.Prod_title);
			aq.id(R.id.textViewItemDetail).text(
					m_DetailCommentListData.Prod_comments);
			aq.id(R.id.listView1).gone();
			aq.id(R.id.scrollViewItemDetail).visible();
			aq.id(R.id.scrollViewItemDetail).getView().requestFocus();

//			scrollViewItemDetail.fullScroll(ScrollView.FOCUS_UP);
//
//			ViewTreeObserver vto = mtextViewItemDetail.getViewTreeObserver();
//			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//				@Override
//				public void onGlobalLayout() {
//					// TODO Auto-generated method stub
//					if(isDetailComment){
//						totalDetailCommentHeight = mtextViewItemDetail.getHeight() /scrollViewItemDetail.getHeight()+1;//scrollViewItemDetail.getMeasuredHeight();
//						aq.id(R.id.textView3).text(
//							Integer.toString(CurrentDetailComment + 1) + "/" + Integer.toString(totalDetailCommentHeight));
//					}
//				}
//			});

		} else {
			app.MyToast(this, "ReturnProgramReviews is empty.");
		}

	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
////		if (isDetailComment) {
////			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
////				if (event.getAction() == KeyEvent.ACTION_DOWN
////						&& event.getRepeatCount() == 0
////						&& scrollViewItemDetail.arrowScroll(View.FOCUS_DOWN)
////								&& CurrentDetailComment < totalDetailCommentHeight-1) {
//////					scrollViewItemDetail.startAnimation(fade_in);
////					scrollViewItemDetail.pageScroll(View.FOCUS_DOWN);
////					
////					CurrentDetailComment++;
//////					scrollViewItemDetail.smoothScrollBy(0, scrollViewItemDetail.getHeight());
////					return true;
////				}
////			} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
////				if (event.getAction() == KeyEvent.ACTION_DOWN
////						&& event.getRepeatCount() == 0
////						&& scrollViewItemDetail.arrowScroll(View.FOCUS_UP)
////						&& CurrentDetailComment >0) {
//////					scrollViewItemDetail.startAnimation(fade_in);
////					scrollViewItemDetail.pageScroll(View.FOCUS_UP);
////					CurrentDetailComment--;
//////					scrollViewItemDetail.smoothScrollBy(0, 0);
////
////					return true;
////				}
////			}
////		}
//
//		return super.dispatchKeyEvent(event);
//	}

	private void CheckSaveData() {
		String SaveData = null;
		ObjectMapper mapper = new ObjectMapper();
		SaveData = app.GetServiceData("DetailComment" + prod_id
				+ Integer.toString(isLastisNext));

		dataStruct = new ArrayList();
		DetailCommentAdapter = new DetailCommentListAdapter();
		ItemsListView.setAdapter(DetailCommentAdapter);
		if (SaveData == null) {
			GetServiceData(isLastisNext);
		} else {
			try {
				m_ReturnProgramReviews = mapper.readValue(SaveData,
						ReturnProgramReviews.class);
				// 创建数据源对象
				GetReviews();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// execute the task
						dataStruct = null;
						dataStruct = new ArrayList();
						GetServiceData(isLastisNext);
					}
				}, 100000);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void GetServiceData(int index) {
		String url = Constant.BASE_URL + "program/reviews" + "?prod_id="
				+ prod_id + "&page_num=" + Integer.toString(index)
				+ "&page_size=6";

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, "InitListData");

		cb.SetHeader(app.getHeaders());

		// aq.id(R.id.ProgressText).visible();
		aq.ajax(cb);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && isDetailComment) {
			ItemsListView.startAnimation(fade_in);
			aq.id(R.id.scrollViewItemDetail).gone();
			aq.id(R.id.listView1).visible();
			aq.id(R.id.listView1).getView().requestFocus();
			DetailCommentAdapter.notifyDataSetChanged();
			aq.id(R.id.textView3).text(
					Integer.toString(CurrentIndex + 1) + "/"
							+ Integer.toString(dataStruct.size()));
			isDetailComment = false;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void updateScore(String score){
		aq.id(R.id.textView_score).text(score);
		float f = Float.valueOf(score);
//		int i = Math.round(f);
		int i = (int) Math.ceil(f);
//		int i = (f%1>=0.5)?(int)(f/1):(int)(f/1+1);
		switch (i) {
		case 0:
			aq.id(R.id.start1).image(R.drawable.star_off);
			aq.id(R.id.start2).image(R.drawable.star_off);
			aq.id(R.id.start3).image(R.drawable.star_off);
			aq.id(R.id.start4).image(R.drawable.star_off);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 1:
			aq.id(R.id.start1).image(R.drawable.star_half);
			aq.id(R.id.start2).image(R.drawable.star_off);
			aq.id(R.id.start3).image(R.drawable.star_off);
			aq.id(R.id.start4).image(R.drawable.star_off);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 2:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_off);
			aq.id(R.id.start3).image(R.drawable.star_off);
			aq.id(R.id.start4).image(R.drawable.star_off);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 3:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_half);
			aq.id(R.id.start3).image(R.drawable.star_off);
			aq.id(R.id.start4).image(R.drawable.star_off);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 4:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_on);
			aq.id(R.id.start3).image(R.drawable.star_off);
			aq.id(R.id.start4).image(R.drawable.star_off);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 5:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_on);
			aq.id(R.id.start3).image(R.drawable.star_half);
			aq.id(R.id.start4).image(R.drawable.star_off);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 6:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_on);
			aq.id(R.id.start3).image(R.drawable.star_on);
			aq.id(R.id.start4).image(R.drawable.star_off);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 7:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_on);
			aq.id(R.id.start3).image(R.drawable.star_on);
			aq.id(R.id.start4).image(R.drawable.star_half);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 8:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_on);
			aq.id(R.id.start3).image(R.drawable.star_on);
			aq.id(R.id.start4).image(R.drawable.star_on);
			aq.id(R.id.start5).image(R.drawable.star_off);
			break;
		case 9:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_on);
			aq.id(R.id.start3).image(R.drawable.star_on);
			aq.id(R.id.start4).image(R.drawable.star_on);
			aq.id(R.id.start5).image(R.drawable.star_half);
			break;
		case 10:
			aq.id(R.id.start1).image(R.drawable.star_on);
			aq.id(R.id.start2).image(R.drawable.star_on);
			aq.id(R.id.start3).image(R.drawable.star_on);
			aq.id(R.id.start4).image(R.drawable.star_on);
			aq.id(R.id.start5).image(R.drawable.star_on);
			break;
		default:
			break;
		}
	}
	static class ViewHolder {
		ImageView imageView1;
		TextView textView01;
		TextView textView02;
	}

	public class DetailCommentListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataStruct.size();
		}

		@Override
		public DetailCommentListData getItem(int position) {
			return (DetailCommentListData) dataStruct.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {

			DetailCommentListData m_DetailCommentListData = (DetailCommentListData) getItem(position);
			ViewHolder holder;

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.detail_comment_list, viewGroup, false);

				holder = new ViewHolder();

				holder.imageView1 = (ImageView) convertView
						.findViewById(R.id.imageView1);

				holder.textView01 = (TextView) convertView
						.findViewById(R.id.TextView01);

				holder.textView02 = (TextView) convertView
						.findViewById(R.id.TextView02);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
//			if (CurrentIndex == position) {
//				holder.imageView1.setVisibility(View.VISIBLE);
//				convertView.setBackgroundResource(R.drawable.bg_teat_repeat);
//			} else {
//				holder.imageView1.setVisibility(View.GONE);
//				convertView.setBackgroundResource(0);
//			}
//			holder.imageView1.setMinimumHeight(mListViewHeight);

			holder.textView01.setText(m_DetailCommentListData.Prod_title);
			holder.textView02.setText(m_DetailCommentListData.Prod_comments);

			return convertView;
		}
	}
}
