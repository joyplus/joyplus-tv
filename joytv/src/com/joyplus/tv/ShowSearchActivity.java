package com.joyplus.tv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint.Join;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.joyplus.tv.Adapters.ZongYiAdapter;
import com.joyplus.tv.entity.MovieItemData;
import com.joyplus.tv.ui.KeyBoardView;
import com.joyplus.tv.ui.MyMovieGridView;
import com.joyplus.tv.ui.WaitingDialog;
import com.joyplus.tv.utils.ItemStateUtils;
import com.joyplus.tv.utils.Log;
import com.joyplus.tv.utils.URLUtils;
import com.joyplus.tv.utils.UtilTools;
import com.umeng.analytics.MobclickAgent;

public class ShowSearchActivity extends AbstractShowActivity {

	public static  String TAG = "ShowSearchActivity";
	
	private static final int MOVIE_LIST_INDEX = 4;
	private static final int TV_LIST_INDEX = 5;
	private static final int DONGMAN_LIST_INDEX = 6;
	private static final int ZONGYI_LIST_INDEX = 7;
	
	private static final int DIALOG_WAITING = 0;
	
	private static final int MOVIE_BUTTON_INDEX = 0;
	private static final int TV_BUTTON_INDEX = 1;
	private static final int DONGMAN_BUTTON_INDEX = 2;
	private static final int ZONGYI_BUTTON_INDEX = 3;
	
	
	private AQuery aq;
	private App app;

	private EditText searchEt;
	private MyMovieGridView playGv;
	private LinearLayout topLinearLayout;
	private View activeView;
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
	
	private List<MovieItemData>[] lists = new List[8];
	private boolean[] isNextPagePossibles = new boolean[8];
	private int[] pageNums = new int[8];

	private boolean isCurrentKeyVertical = false;//水平方向移动
	private boolean isFirstActive = true;//是否界面初始化
	private SparseArray<View> mSparseArray = new SparseArray<View>();
	
	private ImageView helpForSearch;
	
	private PopupWindow keyBoardWindow = null;
	private Button startSearchBtn;
	
	private boolean isLeft = false;
	
	private KeyBoardView keyBoardView;
	
	private LinearLayout searchLL;
	
	private Button play1Button,play2Button,play3Button,play4Button;
	private Button[] playButtons = new Button[4];
	
	private int[] showButtonCount = {0,0,0,0};//分别记录电影，电视剧，动漫，综艺
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_search);

		aq = new AQuery(this);
		app = (App) getApplication();
		
		initActivity();
		
		searchAdapter = new ZongYiAdapter(this,aq);
		searchAdapter.setList(lists[SEARCH], false);
		playGv.setAdapter(searchAdapter);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if(mSparseArray == null || mSparseArray.size() <= 0) {
			
			return ;
		}
		
		if(v.getId() == R.id.bt_search_click) {
			
//			if (hasFocus == true) {
//				Log.i(TAG, "et_search_onFocusChange--->hasFocus:" + hasFocus);
//				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//				.hideSoftInputFromWindow(v.getWindowToken(), 0);
//				KeyBoardView view = new KeyBoardView(ShowSearchActivity.this, searchEt, new KeyBoardView.OnKeyBoardResultListener() {
//					
//					@Override
//					public void onResult(boolean isSearch) {
//						// TODO Auto-generated method stub
//						if(keyBoardWindow!=null&&keyBoardWindow.isShowing()){
//							keyBoardWindow.dismiss();
//						}
//					}
//				});
//				
//				keyBoardWindow = new PopupWindow(view, searchEt.getRootView().getWidth(),
//						searchEt.getRootView().getHeight(), true);
//				keyBoardWindow.showAtLocation(searchEt.getRootView(), Gravity.BOTTOM, 0, 0);
//
//			} 
//			else { // ie searchBoxEditText doesn't have focus
//				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
//						.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//			}
			if(!hasFocus) {
				
				if(isLeft) {//如果是在搜索button上，并且向左移动，就当成垂直方向移动
					
					isCurrentKeyVertical = true;
				}
			} else {
				
				if(!isLeft) {//如果是从搜索button上，并且是从左边移动到button上，当成垂直方向移动
					
					isCurrentKeyVertical = true;
				}
			}

		} else {
			
			if (hasFocus) {

				ItemStateUtils.viewToFocusState(getApplicationContext(), v);
			} else {

				ItemStateUtils.viewToOutFocusState(getApplicationContext(), v,
						activeView);
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
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		
		Log.i(TAG, "dispatchKeyEvent--->" + event.getKeyCode());
		return super.dispatchKeyEvent(event);
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
				isLeft = false;
				break;
			case KEY_DOWN:

//				isGridViewUp = false;
				isCurrentKeyVertical = true;
				isLeft = false;
				break;
			case KEY_LEFT:

				isCurrentKeyVertical = false;
				isLeft = true;
				break;
			case KEY_RIGHT:

				isCurrentKeyVertical = false;
				isLeft = false;
				break;

			default:
				break;
			}

		}
		
		return super.onKeyDown(keyCode, event);
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(helpForSearch.getDrawable() != null) {
			
			UtilTools.recycleBitmap(((BitmapDrawable)helpForSearch.getDrawable()).getBitmap());
		}
		
		super.onDestroy();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_WAITING:
			WaitingDialog dlg = new WaitingDialog(this);
			dlg.show();
			dlg.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			dlg.setDialogWindowStyle();
			return dlg;
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	protected void initViewListener() {
		// TODO Auto-generated method stub
//		searchEt.setOnKeyListener(this);
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				searchLL.requestFocus();
				if(keyBoardWindow == null) {
					
					keyBoardWindow = new PopupWindow(keyBoardView, searchEt.getRootView().getWidth(),
							searchEt.getRootView().getHeight(), true);
					keyBoardWindow.setBackgroundDrawable(new BitmapDrawable());
					keyBoardWindow.setOutsideTouchable(true);
				}
				
				if(keyBoardWindow != null && !keyBoardWindow.isShowing()){
					
					keyBoardWindow.showAtLocation(searchEt.getRootView(), Gravity.BOTTOM, 0, 0);
				}
			}
		}, 300);
		
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
							intent.setClass(ShowSearchActivity.this,
									ShowXiangqingTv.class);
							intent.putExtra("ID", list.get(position)
									.getMovieID());
						} else if (pro_type.equals("1")) {
							Log.i(TAG, "pro_type:" + pro_type + "   --->1");
							intent.setClass(ShowSearchActivity.this,
									ShowXiangqingMovie.class);
						} else if (pro_type.equals("131")) {

							intent.setClass(ShowSearchActivity.this,
									ShowXiangqingDongman.class);
						} else if (pro_type.equals("3")) {

							intent.setClass(ShowSearchActivity.this,
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

		startSearchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				searchPlay();
			}
		});
		
		searchEt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Log.i(TAG, "searchLL.setOnClickListener");
				
				if(keyBoardWindow == null) {
					
					keyBoardWindow = new PopupWindow(keyBoardView, searchEt.getRootView().getWidth(),
							searchEt.getRootView().getHeight(), true);
					keyBoardWindow.setBackgroundDrawable(new BitmapDrawable());
					keyBoardWindow.setOutsideTouchable(true);
				}

				if(keyBoardWindow != null && !keyBoardWindow.isShowing()){
					
					keyBoardWindow.showAtLocation(searchEt.getRootView(), Gravity.BOTTOM, 0, 0);
				}
				
			}
		});
		
		searchLL.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Log.i(TAG, "searchLL.setOnClickListener");
				
				if(keyBoardWindow == null) {
					
					keyBoardWindow = new PopupWindow(keyBoardView, searchEt.getRootView().getWidth(),
							searchEt.getRootView().getHeight(), true);
					keyBoardWindow.setBackgroundDrawable(new BitmapDrawable());
					keyBoardWindow.setOutsideTouchable(true);
				}

				if(keyBoardWindow != null && !keyBoardWindow.isShowing()){
					
					keyBoardWindow.showAtLocation(searchEt.getRootView(), Gravity.BOTTOM, 0, 0);
				}
				
			}
		});
		
		searchLL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				
				if(hasFocus) {
					
					searchEt.setTextColor(getResources().getColor(R.color.black));
				} else {
					
					searchEt.setTextColor(getResources().getColor(R.color.white));
				}
			}
		});
		
		startSearchBtn.setOnFocusChangeListener(this);
	}
	
	/**
	 * 搜索过后，之前数据全部清空，内容重新显示
	 */
	private void searchPlay() {
		
		Editable editable = searchEt.getText();
		String searchStr = editable.toString();
		// searchEt.setText("");
		playGv.setNextFocusForwardId(startSearchBtn.getId());//

		ItemStateUtils
				.viewToNormal(getApplicationContext(), activeView);
		activeView = null;
		
		helpForSearch.setVisibility(View.GONE);

		if (searchStr != null && !searchStr.equals("")) {
			resetGvActive();
			isFirstActive = true;
			showDialog(DIALOG_WAITING);
			search = searchStr;
			clearLists();//清楚之前数据
			initLists();//重新初始化数据
			initButtons();//初始化buttons
//			currentListIndex = SEARCH;
			String url = URLUtils.getSearch_200URL(searchStr);
			getFilterData(url);
		}
	}
	
	private void initButtons() {
		
		play1Button.setVisibility(View.INVISIBLE);
		play2Button.setVisibility(View.INVISIBLE);
		play3Button.setVisibility(View.INVISIBLE);
		play4Button.setVisibility(View.INVISIBLE);
		
		for(int i=0;i<showButtonCount.length;i++) {
			
			showButtonCount[i] = 0;
		}
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

//		if (list != null && !list.isEmpty()&&QUANBUFENLEI != currentListIndex) {// 判断其能否向获取更多数据
//
//			if (list.size() == StatisticsUtils.FIRST_NUM) {
//
//				isNextPagePossibles[currentListIndex] = true;
//			} else if (list.size() < StatisticsUtils.FIRST_NUM) {
//
//				isNextPagePossibles[currentListIndex] = false;
//			}
//		}
//		lists[currentListIndex] = list;

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
	public void initMoreBangDanServiceData(String url, JSONObject json,
			AjaxStatus status) {
		// TODO Auto-generated method stub
		
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

			//因为获取200条数据，不进行下拉
//			getMoreFilterData(StatisticsUtils.getSearch_CacheURL(pageNum, search));
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
			
			if(json == null || json.equals("")) {
				
				resetData();
				return;
			}
				
			
			Log.d(TAG, json.toString());
			List<MovieItemData> tempList = UtilTools.returnFilterMovieSearch_TVJson(json
					.toString());
			
			if(tempList != null && tempList.size() > 0) {//把第一次获取到的list分配到各个集合中
				
				Log.i(TAG, "tempList size--->" + tempList.size());
				
				for(MovieItemData movieItemData:tempList) {
					
					String type = movieItemData.getMovieProType();
					
					if(type != null && !type.equals("")) {
						
						if(type.equals(MOVIE_TYPE)) {
							
							lists[MOVIE_LIST_INDEX].add(movieItemData);
						} else if(type.equals(TV_TYPE)) {
							
							lists[TV_LIST_INDEX].add(movieItemData);
						} else if(type.equals(DONGMAN_TYPE)) {
							
							lists[DONGMAN_LIST_INDEX].add(movieItemData);
						} else if(type.equals(ZONGYI_TYPE)) {
							
							lists[ZONGYI_LIST_INDEX].add(movieItemData);
						}
					}
				}
			} else {//如果没有获取任何数据
				
				resetData();
				return;
			}
			
			
			if(lists[MOVIE_LIST_INDEX]!= null && lists[MOVIE_LIST_INDEX].size() >0) {
				
				Log.i(TAG, "lists[MOVIE_LIST_INDEX].size()--->" + lists[MOVIE_LIST_INDEX].size());
				showButtonCount[MOVIE_BUTTON_INDEX] = 1;
			}
			
			if(lists[TV_LIST_INDEX]!= null && lists[TV_LIST_INDEX].size() >0) {
				
				Log.i(TAG, "lists[TV_LIST_INDEX].size()--->" + lists[TV_LIST_INDEX].size());
				showButtonCount[TV_BUTTON_INDEX] = 1;
			}
			
			if(lists[DONGMAN_LIST_INDEX]!= null && lists[DONGMAN_LIST_INDEX].size() >0) {
				
				Log.i(TAG, "lists[DONGMAN_LIST_INDEX].size()--->" + lists[DONGMAN_LIST_INDEX].size());
				showButtonCount[DONGMAN_BUTTON_INDEX] = 1;
			}
			
			if(lists[ZONGYI_LIST_INDEX]!= null && lists[ZONGYI_LIST_INDEX].size() >0) {
				
				Log.i(TAG, "lists[ZONGYI_LIST_INDEX].size()--->" + lists[ZONGYI_LIST_INDEX].size());
				showButtonCount[ZONGYI_BUTTON_INDEX] = 1;
			}
			
			int index = 0;
			
			for(int i=0;i<showButtonCount.length;i++) {
				
				if(showButtonCount[i] == 1) {
					
					Button button = playButtons[index];
					String str = null;
					int listIndex = -1;
					
					if(i == MOVIE_BUTTON_INDEX) {
						
						str = getString(R.string.dianying_name) + 
								"(" + lists[MOVIE_LIST_INDEX].size()+ ")"; 
						listIndex = MOVIE_LIST_INDEX;
								
					} else if(i == TV_BUTTON_INDEX) {
						
						str = getString(R.string.tv_play_name) + 
								"(" + lists[TV_LIST_INDEX].size()+ ")"; 
						listIndex = TV_LIST_INDEX;
					} else if(i == DONGMAN_BUTTON_INDEX) {
						
						str = getString(R.string.dongman_play_name) + 
								"(" + lists[DONGMAN_LIST_INDEX].size()+ ")"; 
						listIndex = DONGMAN_LIST_INDEX;
					} else if(i == ZONGYI_BUTTON_INDEX) {
						
						str = getString(R.string.zongyi_name) + 
								"(" + lists[ZONGYI_LIST_INDEX].size()+ ")"; 
						listIndex = ZONGYI_LIST_INDEX;
					}
					
					if(index == 0) {
						
						activeView = button;
						ItemStateUtils.buttonToActiveState(getApplicationContext(), button);
						playGv.setNextFocusLeftId(button.getId());
						if(listIndex != -1) {
							
							notifyAdapter(lists[listIndex]);
						}
						
					}
					
					button.setText(str);
					button.setVisibility(View.VISIBLE);
					ItemStateUtils.setItemPadding(button);
					button.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
							if ( activeView != null && activeView.getId() == v.getId()) {

								return;
							}
							
							String str = ((Button)v).getText().toString();
							
							if(str.contains(getString(R.string.dianying_name))) {
								
								notifyAdapter(lists[MOVIE_LIST_INDEX]);
							} else if(str.contains(getString(R.string.tv_play_name))){
								
								notifyAdapter(lists[TV_LIST_INDEX]);
							} else if(str.contains(getString(R.string.dongman_play_name))) {
								
								notifyAdapter(lists[DONGMAN_LIST_INDEX]);
							} else if(str.contains(getString(R.string.zongyi_name))) {
								
								notifyAdapter(lists[ZONGYI_LIST_INDEX]);
							}
							
							View tempView = ItemStateUtils.viewToActive(getApplicationContext(), v,
									activeView);

							if (tempView != null) {

								activeView = tempView;
							}

							playGv.setNextFocusLeftId(v.getId());
							activeView = v;
							
						}
					});
					button.setOnFocusChangeListener(this);
					
					index ++;//
				}
			}
			
//			notifyAdapter(tempList);
			
			playGv.requestFocus();
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
	//没有搜索到任何结果
	private void resetData() {
		
		app.MyToast(getApplicationContext(),
				getString(R.string.toast_no_play));
		
		resetGvActive();
		removeDialog(DIALOG_WAITING);
		clearLists();//清楚之前数据
		initLists();//重新初始化数据
		initButtons();//初始化buttons
		
		
		searchAdapter.setList(new ArrayList<MovieItemData>(), false);
		
		helpForSearch.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void initQuan10ServiceData(String url, JSONObject json,
			AjaxStatus status) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void initViewState() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
		searchEt = (EditText) findViewById(R.id.et_search);
		startSearchBtn = (Button) findViewById(R.id.bt_search_click);
		searchLL = (LinearLayout) findViewById(R.id.ll_search);
		playGv = (MyMovieGridView) findViewById(R.id.gv_movie_show);
		helpForSearch = (ImageView) findViewById(R.id.iv_help_for_search);
		
		keyBoardView = new KeyBoardView(ShowSearchActivity.this, searchEt, new KeyBoardView.OnKeyBoardResultListener() {
			
			@Override
			public void onResult(boolean isSearch) {
				// TODO Auto-generated method stub
				if(keyBoardWindow!=null&&keyBoardWindow.isShowing()){
					keyBoardWindow.dismiss();
				}
				if(isSearch) {
					
					searchPlay();
				}
				
			}
		});
		
		play1Button = (Button) findViewById(R.id.bt_play_1);
		play2Button = (Button) findViewById(R.id.bt_play_2);
		play3Button = (Button) findViewById(R.id.bt_play_3);
		play4Button = (Button) findViewById(R.id.bt_play_4);
		
		playButtons[0] = play1Button;
		playButtons[1] = play2Button;
		playButtons[2] = play3Button;
		playButtons[3] = play4Button;

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void resetGvActive() {
		// TODO Auto-generated method stub
		
		mSparseArray.clear();
		activeRecordIndex = -1;
		isCurrentKeyVertical = false;
		isFirstActive = false;
	}

	@Override
	protected void initFirstFloatView(int position, View view) {
		// TODO Auto-generated method stub
		
	}

}
