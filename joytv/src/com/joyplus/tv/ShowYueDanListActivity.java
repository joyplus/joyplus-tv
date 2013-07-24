package com.joyplus.tv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.joyplus.tv.Adapters.SearchAdapter;
import com.joyplus.tv.Adapters.ZongYiAdapter;
import com.joyplus.tv.entity.MovieItemData;
import com.joyplus.tv.ui.MyMovieGridView;
import com.joyplus.tv.utils.ItemStateUtils;
import com.joyplus.tv.utils.Log;
import com.joyplus.tv.utils.UtilTools;
import com.joyplus.tv.utils.URLUtils;
import com.umeng.analytics.MobclickAgent;

public class ShowYueDanListActivity extends AbstractShowActivity{

	private String TAG = "ShowYueDanListActivity";
	
	private static final int TOP = 4;
	
	private static final int DIALOG_WAITING = 0;

	private AQuery aq;
	private App app;

//	private EditText searchEt;
	private MyMovieGridView playGv;
	private LinearLayout topLinearLayout;
//	private View activeView;
	private int popWidth = 0, popHeight = 0;
	private boolean isGridViewUp = false;
	private int[] beforeFirstAndLastVible = { 0, 9 };
	private View beforeGvView = null;
	private ZongYiAdapter searchAdapter = null;
	private int beforepostion = 0;
	private int currentListIndex;
	private String search;
	private String filterSource;
	private PopupWindow popupWindow;
	
	private int activeRecordIndex = -1;
	
	private Button zuijinguankanBtn, zhuijushoucangBtn;
	private TextView yuedanListTv;
	private List<MovieItemData>[] lists = new List[4];
	private boolean[] isNextPagePossibles = new boolean[4];
	private int[] pageNums = new int[4];
	
	private boolean isCurrentKeyVertical = false;//水平方向移动
	private boolean isFirstActive = true;//是否界面初始化
	private SparseArray<View> mSparseArray = new SparseArray<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_yuedan_list);

		aq = new AQuery(this);
		app = (App) getApplication();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		String name = bundle.getString("NAME");
		String id = bundle.getString("ID");
		
		if(name != null && id != null && !name.equals("")
				&& !id.equals("")) {
			initView();
			
			initActivity();
			
			searchAdapter = new ZongYiAdapter(this,aq);
			
			showDialog(DIALOG_WAITING);
			yuedanListTv.setText(name);
			playGv.setAdapter(searchAdapter);
			
			playGv.requestFocus();
			playGv.setSelection(-1);
			getUnQuanbuData(URLUtils.getTopItemURL(TOP_ITEM_URL, id, 1 + "", 100 + ""));
			
		} else {
			
			finish();
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if(v.getId() == R.id.et_search) {
			
			if (hasFocus == true) {
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.showSoftInput(v, InputMethodManager.SHOW_FORCED);

			} else { // ie searchBoxEditText doesn't have focus
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(v.getWindowToken(), 0);

			}
		} else {
			
			if (hasFocus) {

				ItemStateUtils.viewToFocusState(getApplicationContext(), v);
			} else {

//				ItemStateUtils.viewToOutFocusState(getApplicationContext(), v,
//						activeView);
			}
		}
		
		if(!isCurrentKeyVertical) {
			
			int postion = playGv.getSelectedItemPosition();
			View view =mSparseArray.get(postion);
			
			if(view != null) {
				
				if (hasFocus) {// 如果gridview没有获取焦点，把item中高亮取消

					ItemStateUtils.viewOutAnimation(getApplicationContext(),
							view);
				} else {
					
					ItemStateUtils.viewInAnimation(getApplicationContext(), view);
					activeRecordIndex = postion;
				}
			}
		}

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		int action = event.getAction();

		if (action == KeyEvent.ACTION_DOWN) {

			switch (keyCode) {
			case KEY_UP:

//				isGridViewUp = true;
				isCurrentKeyVertical = true;
				break;
			case KEY_DOWN:

//				isGridViewUp = false;
				isCurrentKeyVertical = true;
				break;
			case KEY_LEFT:

				isCurrentKeyVertical = false;
				break;
			case KEY_RIGHT:

				isCurrentKeyVertical = false;
				break;

			default:
				break;
			}

		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (aq != null)
			aq.dismiss();
		
		clearLists();
		super.onDestroy();
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

	@Override
	protected void initViewListener() {
		// TODO Auto-generated method stub
		
		zuijinguankanBtn.setOnKeyListener(this);
		zhuijushoucangBtn.setOnKeyListener(this);
//		searchEt.setOnKeyListener(this);

		zuijinguankanBtn.setOnClickListener(this);
		zhuijushoucangBtn.setOnClickListener(this);
		
		zuijinguankanBtn.setOnFocusChangeListener(this);
		zhuijushoucangBtn.setOnFocusChangeListener(this);
//		searchEt.setOnFocusChangeListener(this);

		playGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				List<MovieItemData> list = searchAdapter.getMovieList();
				if (list != null && !list.isEmpty()) {
					String pro_type = list.get(position).getMovieProType();
					Log.i(TAG, "pro_type:" + pro_type);
					if (pro_type != null && !pro_type.equals("")) {
						Intent intent = new Intent();
						if (pro_type.equals("2")) {
							Log.i(TAG, "pro_type:" + pro_type + "   --->2");
							intent.setClass(ShowYueDanListActivity.this,
									ShowXiangqingTv.class);
							intent.putExtra("ID", list.get(position)
									.getMovieID());
						} else if (pro_type.equals("1")) {
							Log.i(TAG, "pro_type:" + pro_type + "   --->1");
							intent.setClass(ShowYueDanListActivity.this,
									ShowXiangqingMovie.class);
						} else if (pro_type.equals("131")) {

							intent.setClass(ShowYueDanListActivity.this,
									ShowXiangqingDongman.class);
						} else if (pro_type.equals("3")) {

							intent.setClass(ShowYueDanListActivity.this,
									ShowXiangqingZongYi.class);
						}

						intent.putExtra("ID", list.get(position).getMovieID());

						intent.putExtra("prod_url", list.get(position)
								.getMoviePicUrl());
						intent.putExtra("prod_name", list.get(position)
								.getMovieName());
						intent.putExtra("stars", list.get(position).getStars());
						intent.putExtra("directors", list.get(position)
								.getDirectors());
						intent.putExtra("summary", list.get(position)
								.getSummary());
						intent.putExtra("support_num", list.get(position)
								.getSupport_num());
						intent.putExtra("favority_num", list.get(position)
								.getFavority_num());
						intent.putExtra("definition", list.get(position)
								.getDefinition());
						intent.putExtra("score", list.get(position)
								.getMovieScore());
						startActivity(intent);

					}
				}
			}
		});

		playGv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, final View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// if (BuildConfig.DEBUG)
				Log.i(TAG, "Positon:" + position + " View:" + view + 
						" before: " + activeRecordIndex);

				if (view == null) {

					return;
				}else {
					
//					Log.i(TAG, "mSparseArray: " + mSparseArray.get(position));
//					if(mSparseArray.get(position) == null){
						
						mSparseArray.put(position,view);
//					}
				}

				final float y = view.getY();

				boolean isSmoonthScroll = false;

				boolean isSameContent = position >= beforeFirstAndLastVible[0]
						&& position <= beforeFirstAndLastVible[1];
//				if (position >= 5 && !isSameContent) {
//
//					if (beforepostion >= beforeFirstAndLastVible[0]
//							&& beforepostion <= beforeFirstAndLastVible[0] + 4) {
//
//						if (isGridViewUp) {
//
//							playGv.smoothScrollBy(-popHeight, 1000);
//							isSmoonthScroll = true;
//						}
//					} else {
//
//						if (!isGridViewUp) {
//
//							playGv.smoothScrollBy(popHeight, 1000 * 2);
//							isSmoonthScroll = true;
//
//						}
//					}
//
//				}

				if (mSparseArray.get(activeRecordIndex) != null && activeRecordIndex != position) {

					ItemStateUtils.viewOutAnimation(getApplicationContext(),
							mSparseArray.get(activeRecordIndex));
				}

				if (position != activeRecordIndex && isFirstActive) {

					ItemStateUtils.viewInAnimation(getApplicationContext(),
							view);
					activeRecordIndex = position;
				}
				
				if(!isFirstActive) {//如果不是初始化，那就设为true
					
					isFirstActive = true;
				}

				int[] firstAndLastVisible = new int[2];
				firstAndLastVisible[0] = playGv.getFirstVisiblePosition();
				firstAndLastVisible[1] = playGv.getLastVisiblePosition();

				if (y == 0 || y - popHeight == 0) {// 顶部没有渐影

					beforeFirstAndLastVible = ItemStateUtils
							.reCaculateFirstAndLastVisbile(
									beforeFirstAndLastVible,
									firstAndLastVisible, isSmoonthScroll, false);

				} else {// 顶部有渐影

					beforeFirstAndLastVible = ItemStateUtils
							.reCaculateFirstAndLastVisbile(
									beforeFirstAndLastVible,
									firstAndLastVisible, isSmoonthScroll, true);

				}

				beforepostion = position;

				// 缓存
				int size = searchAdapter.getMovieList().size();
				if (size - 1 - firstAndLastVisible[1] < URLUtils.CACHE_NUM) {

					if (isNextPagePossibles[currentListIndex]) {

						pageNums[currentListIndex]++;
						cachePlay(currentListIndex, pageNums[currentListIndex]);
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

//		searchEt.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				Editable editable = searchEt.getText();
//				String searchStr = editable.toString();
//				// searchEt.setText("");
//				playGv.setNextFocusForwardId(searchEt.getId());//
//
////				ItemStateUtils
////						.viewToNormal(getApplicationContext(), activeView);
////				activeView = searchEt;
//
//				if (searchStr != null && !searchStr.equals("")) {
//					resetGvActive();
//					showDialog(DIALOG_WAITING);
//					search = searchStr;
//					StatisticsUtils.clearList(lists[SEARCH]);
//					currentListIndex = SEARCH;
//					String url = StatisticsUtils.getSearch_FirstURL(searchStr);
//					getFilterData(url);
//				}
//
//			}
//		});
	}

	@Override
	protected void clearLists() {
		// TODO Auto-generated method stub

		for (int i = 0; i < lists.length; i++) {

			UtilTools.clearList(lists[i]);
		}
	}

	@Override
	protected void initLists() {
		// TODO Auto-generated method stub

		for (int i = 0; i < lists.length; i++) {

			lists[i] = new ArrayList<MovieItemData>();
			isNextPagePossibles[i] = false;// 认为所有的不能够翻页
			pageNums[i] = 0;
		}
	}

	@Override
	protected void notifyAdapter(List<MovieItemData> list) {
		// TODO Auto-generated method stub

		int height = searchAdapter.getHeight(), width = searchAdapter
				.getWidth();

		if (height != 0 && width != 0) {

			popWidth = width;
			popHeight = height;
		}

		if(currentListIndex != SEARCH &&
				currentListIndex != QUAN_FILTER) {
			
			searchAdapter.setList(list,true);
		}else {
			
			searchAdapter.setList(list,false);
		}
		
		if(searchAdapter.getItemId() == list.size()) {
			
			searchAdapter.setItemId(list.size() + 1);
		} else {
			
			searchAdapter.setItemId(list.size());
		}
		

		if (list.size() <= 0) {

			app.MyToast(getApplicationContext(),
					getString(R.string.toast_no_play));
		}

		if (list != null && !list.isEmpty() && QUANBUFENLEI != currentListIndex) {// 判断其能否向获取更多数据

			if (list.size() == URLUtils.FIRST_NUM) {

				isNextPagePossibles[currentListIndex] = true;
			} else if (list.size() < URLUtils.FIRST_NUM) {

				isNextPagePossibles[currentListIndex] = false;
			}
		}
		lists[currentListIndex] = list;

		playGv.setSelection(0);
		searchAdapter.notifyDataSetChanged();
		
		removeDialog(DIALOG_WAITING);
//		if(isFirstActive) {
//			
//			playGv.requestFocus();
//		}

	}

	@Override
	protected void filterVideoSource(String[] choice) {
		// TODO Auto-generated method stub

		String quanbu = getString(R.string.quanbu_name);
		String quanbufenlei = getString(R.string.quanbufenlei_name);
		String tempStr = URLUtils.getQuanBuFenLeiName(choice,
				quanbufenlei, quanbu);

		if (tempStr.equals(quanbufenlei)) {

			currentListIndex = QUANBUFENLEI;
			if (lists[QUANBUFENLEI] != null && !lists[QUANBUFENLEI].isEmpty()) {

				notifyAdapter(lists[QUANBUFENLEI]);
			}

			return;
		}

		showDialog(DIALOG_WAITING);
		UtilTools.clearList(lists[QUAN_FILTER]);
		currentListIndex = QUAN_FILTER;
		filterSource = URLUtils.getFileterURL3Param(choice, quanbu);
		String url = URLUtils.getFilter_DongmanFirstURL(filterSource);
		Log.i(TAG, "POP--->URL:" + url);
		getFilterData(url);
	}

	@Override
	protected void getQuan10Data(String url) {
		// TODO Auto-generated method stub

		showDialog(DIALOG_WAITING);
		getServiceData(url, "initQuan10ServiceData");
	}

	@Override
	protected void getQuanbuData(String url) {
		// TODO Auto-generated method stub

		getServiceData(url, "initQuanbuServiceData");
	}

	@Override
	protected void getUnQuanbuData(String url) {
		// TODO Auto-generated method stub

		getServiceData(url, "initUnQuanbuServiceData");
	}

	@Override
	protected void getFilterData(String url) {
		// TODO Auto-generated method stub

		getServiceData(url, "initFilerServiceData");
	}

	@Override
	protected void getServiceData(String url, String interfaceName) {
		// TODO Auto-generated method stub

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, interfaceName);

		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}

	@Override
	protected void refreshAdpter(List<MovieItemData> list) {
		// TODO Auto-generated method stub

		List<MovieItemData> srcList = searchAdapter.getMovieList();

		if (list != null && !list.isEmpty()) {

			for (MovieItemData movieItemData : list) {

				srcList.add(movieItemData);
			}
		}

		if (list.size() == URLUtils.CACHE_NUM) {

			isNextPagePossibles[currentListIndex] = true;
		} else {

			isNextPagePossibles[currentListIndex] = false;
		}

		searchAdapter.setList(srcList,true);
		lists[currentListIndex] = srcList;

		searchAdapter.notifyDataSetChanged();
	}



	@Override
	protected void getMoreFilterData(String url) {
		// TODO Auto-generated method stub
		getServiceData(url, "initMoreFilerServiceData");
	}

	@Override
	protected void getMoreBangDanData(String url) {
		// TODO Auto-generated method stub
		getServiceData(url, "initMoreBangDanServiceData");
	}

	@Override
	protected void filterPopWindowShow() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initMoreBangDanServiceData(String url, JSONObject json,
			AjaxStatus status) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initMoreFilerServiceData(String url, JSONObject json,
			AjaxStatus status) {
		// TODO Auto-generated method stub

		if (status.getCode() == AjaxStatus.NETWORK_ERROR) {

			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}

		try {
			
			if(json == null || json.equals("")) 
				return;
			
			Log.d(TAG, json.toString());

			refreshAdpter(UtilTools.returnFilterMovieSearch_TVJson(json
					.toString()));
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
	
	@Override
	protected void cachePlay(int index, int pageNum) {
		// TODO Auto-generated method stub
		
		switch (index) {
		case QUANBUFENLEI:

			break;
		case QUAN_TEN:

			break;
		case QUAN_FILTER:

			break;
		case SEARCH:

			getMoreFilterData(URLUtils.getSearch_CacheURL(pageNum,
					search));
			break;
		default:
			break;
		}
	}
	
	@Override
	public void initQuanbuServiceData(String url, JSONObject json,
			AjaxStatus status) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initUnQuanbuServiceData(String url, JSONObject json,
			AjaxStatus status) {
		// TODO Auto-generated method stub
		
		if (status.getCode() == AjaxStatus.NETWORK_ERROR) {

			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}
		try {
			
			if(json == null || json.equals("")) 
				return;
			
			Log.d(TAG, json.toString());
				
			notifyAdapter(UtilTools
					.returnTVBangDanList_YueDanListJson(json.toString()));
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
	
	@Override
	public void initFilerServiceData(String url, JSONObject json,
			AjaxStatus status) {
		// TODO Auto-generated method stub
		
		if (status.getCode() == AjaxStatus.NETWORK_ERROR) {

			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}
		
		try {
			
			if(json == null || json.equals("")) 
				return;
			
			Log.d(TAG, json.toString());
			notifyAdapter(UtilTools.returnFilterMovieSearch_TVJson(json
					.toString()));
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
	
	@Override
	public void initQuan10ServiceData(String url, JSONObject json,
			AjaxStatus status) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void initViewState() {
		// TODO Auto-generated method stub
		
		ItemStateUtils.setItemPadding(zuijinguankanBtn);
		ItemStateUtils.setItemPadding(zhuijushoucangBtn);
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
//		searchEt = (EditText) findViewById(R.id.et_search);
		playGv = (MyMovieGridView) findViewById(R.id.gv_movie_show);

		zuijinguankanBtn = (Button) findViewById(R.id.bt_zuijinguankan);
		zhuijushoucangBtn = (Button) findViewById(R.id.bt_zhuijushoucang);

		yuedanListTv = (TextView) findViewById(R.id.tv_yuedanlist_name);
		
		playGv.setNextFocusLeftId(R.id.bt_zuijinguankan);
		
		playGv.setNextFocusUpId(R.id.gv_movie_show);
		playGv.setNextFocusDownId(R.id.gv_movie_show);
		
//		activeView = zuijinguankanBtn;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("Yangzhg", "onClick");

//		if(v.getId() == R.id.bt_zuijinguankan) {
//			
//			startActivity(new Intent(this, HistoryActivity.class));
//			
//			return;
//		} else if( v.getId() == R.id.bt_zhuijushoucang) {
//			
//			startActivity(new Intent(this, ShowShoucangHistoryActivity.class));
//			return;
//		}

//		if (activeView.getId() == v.getId()) {
//
//			return;
//		}
		
		switch (v.getId()) {
		case R.id.bt_zuijinguankan:
			startActivity(new Intent(this, HistoryActivity.class));
			break;
		case R.id.bt_zhuijushoucang:
			startActivity(new Intent(this, ShowShoucangHistoryActivity.class));
			break;

		default:
			break;
		}

//		View tempView = ItemStateUtils.viewToActive(getApplicationContext(), v,
//				activeView);
//
//		if (tempView != null) {
//
//			activeView = tempView;
//		}

		playGv.setNextFocusLeftId(v.getId());
	}

	@Override
	protected void resetGvActive() {
		// TODO Auto-generated method stub
		mSparseArray.clear();
		activeRecordIndex = -1;
		isCurrentKeyVertical = false;
		isFirstActive = false;
	}
	
	protected void initFirstFloatView(int position,View view) {
		
	}

}
