package com.joyplus.tv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.joyplus.tv.Adapters.ZongYiAdapter;
import com.joyplus.tv.entity.MovieItemData;
import com.joyplus.tv.ui.KeyBoardView;
import com.joyplus.tv.ui.MyMovieGridView;
import com.joyplus.tv.ui.NavigateView;
import com.joyplus.tv.ui.NavigateView.OnResultListener;
import com.joyplus.tv.ui.WaitingDialog;
import com.joyplus.tv.utils.DBUtils;
import com.joyplus.tv.utils.ItemStateUtils;
import com.joyplus.tv.utils.Log;
import com.joyplus.tv.utils.URLUtils;
import com.joyplus.tv.utils.UtilTools;
import com.umeng.analytics.MobclickAgent;

public class ShowZongYiActivity extends AbstractShowActivity {

	public static final String TAG = "ShowZongYiActivity";

	private static final int DIALOG_WAITING = 0;

	private AQuery aq;
	private App app;

	private EditText searchEt;
	private MyMovieGridView playGv;
	private LinearLayout topLinearLayout;
	private View activeView;
	private int popWidth = 0, popHeight = 0;
	// private boolean isGridViewUp = false;
	private int[] beforeFirstAndLastVible = { 0, 9 };
	private ZongYiAdapter searchAdapter = null;
	private int beforepostion = 0;
	private int currentListIndex;
	private String search;
	private String filterSource;
	private PopupWindow popupWindow;

	private int activeRecordIndex = -1;

	private List<MovieItemData>[] lists = new List[4];
	private boolean[] isNextPagePossibles = new boolean[4];
	private int[] pageNums = new int[4];
	private Button zuijinguankanBtn, zhuijushoucangBtn, mFenLeiBtn;

	private boolean isCurrentKeyVertical = false;// 水平方向移动
	private boolean isFirstActive = true;// 是否界面初始化
	private SparseArray<View> mSparseArray = new SparseArray<View>();

	private List<MovieItemData> shoucangList = new ArrayList<MovieItemData>();
	private boolean isShowShoucang = false;

	private LinearLayout shoucangTitlleLL;
	private int qitaNextPoistion = -1;
	private TextView shoucangTv;

	private boolean isGridViewUp = false;

	private PopupWindow keyBoardWindow = null;
	private Button startSearchBtn;

	private boolean isLeft = false;

	private KeyBoardView keyBoardView;
	private LinearLayout searchLL;

	private boolean isDragGridView = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_zongyi);

		app = (App) getApplication();
		aq = new AQuery(this);

		// 本地收藏，有没有更新
		String userId = null;
		if (app.getUserInfo() != null) {

			if (app.getUserInfo().getUserId() != null) {

				userId = app.getUserInfo().getUserId();
			}
		} else {

			userId = UtilTools.getCurrentUserId(getApplicationContext());
		}

		if (userId != null) {

			shoucangList = DBUtils.getList4DB(getApplicationContext(),
					UtilTools.getCurrentUserId(getApplicationContext()),
					ZONGYI_TYPE);
		}

		if (shoucangList != null && !shoucangList.isEmpty()) {

			if (shoucangList.size() > 0) {

				Log.i(TAG, "shoucangList--->:" + shoucangList.size());

				isShowShoucang = true;
			}
		}

		initActivity();

		searchAdapter = new ZongYiAdapter(this, aq);

		if (isShowShoucang) {

			searchAdapter.setShouCangCount(shoucangList.size());
			searchAdapter
					.setQita_name(getString(R.string.qitadongman_play_name));
			searchAdapter.setShoucangShow(true);
		}

		playGv.setAdapter(searchAdapter);
		playGv.requestFocus();
		playGv.setSelection(-1);
		showDialog(DIALOG_WAITING);
		getQuanbuData(URLUtils.getZongyi_QuanAllFirstURL());
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub

		if (mSparseArray == null || mSparseArray.size() <= 0) {

			return;
		}

		if (v.getId() == R.id.bt_search_click) {

			// if (hasFocus == true) {
			// Log.i(TAG, "et_search_onFocusChange--->hasFocus:" + hasFocus);
			// ((InputMethodManager)
			// getSystemService(Context.INPUT_METHOD_SERVICE))
			// .hideSoftInputFromWindow(v.getWindowToken(), 0);
			// KeyBoardView view = new KeyBoardView(ShowSearchActivity.this,
			// searchEt, new KeyBoardView.OnKeyBoardResultListener() {
			//
			// @Override
			// public void onResult(boolean isSearch) {
			// // TODO Auto-generated method stub
			// if(keyBoardWindow!=null&&keyBoardWindow.isShowing()){
			// keyBoardWindow.dismiss();
			// }
			// }
			// });
			//
			// keyBoardWindow = new PopupWindow(view,
			// searchEt.getRootView().getWidth(),
			// searchEt.getRootView().getHeight(), true);
			// keyBoardWindow.showAtLocation(searchEt.getRootView(),
			// Gravity.BOTTOM, 0, 0);
			//
			// }
			// else { // ie searchBoxEditText doesn't have focus
			// ((InputMethodManager)
			// getSystemService(Context.INPUT_METHOD_SERVICE))
			// .hideSoftInputFromWindow(v.getWindowToken(), 0);
			//
			// }
			if (!hasFocus) {

				if (isLeft) {// 如果是在搜索button上，并且向左移动，就当成垂直方向移动

					isCurrentKeyVertical = true;
				}
			} else {

				if (!isLeft) {// 如果是从搜索button上，并且是从左边移动到button上，当成垂直方向移动

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

		if (!isCurrentKeyVertical) {

			int postion = playGv.getSelectedItemPosition();
			View view = mSparseArray.get(postion);

			if (view != null) {

				if (hasFocus) {// 如果gridview没有获取焦点，把item中高亮取消

					ItemStateUtils.viewOutAnimation(getApplicationContext(),
							view);
				} else {

					ItemStateUtils.viewInAnimation(getApplicationContext(),
							view);
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

		isDragGridView = false;// 不是拖动

		if (action == KeyEvent.ACTION_DOWN) {

			switch (keyCode) {
			case KEY_UP:

				// isGridViewUp = true;
				isCurrentKeyVertical = true;
				isLeft = false;
				break;
			case KEY_DOWN:

				// isGridViewUp = false;
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

		if (app.getUserInfo() != null) {
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

		zuijinguankanBtn.setOnKeyListener(this);
		zhuijushoucangBtn.setOnKeyListener(this);
		mFenLeiBtn.setOnKeyListener(this);
		// searchEt.setOnKeyListener(this);

		playGv.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				int action = event.getAction();

				isDragGridView = false;// 不是拖动

				if (action == KeyEvent.ACTION_DOWN) {
					Log.i(TAG, "onKeyDown--->" + keyCode);

					switch (keyCode) {
					case KEY_UP:

						isGridViewUp = true;
						// isCurrentKeyVertical = true;
						Log.i(TAG, "onKeyDown--->KEY_UP" + keyCode + " "
								+ isGridViewUp);
						break;
					case KEY_DOWN:

						isGridViewUp = false;
						// isCurrentKeyVertical = true;
						Log.i(TAG, "onKeyDown--->KEY_DOWN" + keyCode + " "
								+ isGridViewUp);
						break;
					case KEY_LEFT:

						// isCurrentKeyVertical = false;
						break;
					case KEY_RIGHT:

						// isCurrentKeyVertical = false;
						break;

					default:
						break;
					}

				}

				return false;
			}
		});

		zuijinguankanBtn.setOnClickListener(this);
		zhuijushoucangBtn.setOnClickListener(this);
		mFenLeiBtn.setOnClickListener(this);

		zuijinguankanBtn.setOnFocusChangeListener(this);
		zhuijushoucangBtn.setOnFocusChangeListener(this);
		mFenLeiBtn.setOnFocusChangeListener(this);
		// searchEt.setOnFocusChangeListener(this);

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
							intent.setClass(ShowZongYiActivity.this,
									ShowXiangqingTv.class);
							intent.putExtra("ID", list.get(position)
									.getMovieID());
						} else if (pro_type.equals("1")) {
							intent.setClass(ShowZongYiActivity.this,
									ShowXiangqingMovie.class);
						} else if (pro_type.equals("131")) {

							intent.setClass(ShowZongYiActivity.this,
									ShowXiangqingDongman.class);
						} else if (pro_type.equals("3")) {

							intent.setClass(ShowZongYiActivity.this,
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
				Log.i(TAG, "Positon:" + position + " View:" + view
						+ " before: " + activeRecordIndex);

				if (view == null) {

					return;
				} else {

					// Log.i(TAG, "mSparseArray: " +
					// mSparseArray.get(position));
					if (view.getTag() != null) {

						mSparseArray.put(position, view);
					} else {

						mSparseArray.delete(position);
					}
				}

				final float y = view.getY();

				boolean isSmoonthScroll = false;

				boolean isSameContent = position >= beforeFirstAndLastVible[0]
						&& position <= beforeFirstAndLastVible[1];
				// if (position >= 5 && !isSameContent) {
				//
				// if (beforepostion >= beforeFirstAndLastVible[0]
				// && beforepostion <= beforeFirstAndLastVible[0] + 4) {
				//
				// if (isGridViewUp) {
				//
				// playGv.smoothScrollBy(-popHeight, 1000);
				// isSmoonthScroll = true;
				// }
				// } else {
				//
				// if (!isGridViewUp) {
				//
				// playGv.smoothScrollBy(popHeight, 1000 * 2);
				// isSmoonthScroll = true;
				//
				// }
				// }
				//
				// }

				if (isShowShoucang) {

					int shoucangNum = shoucangList.size();
					if (!UtilTools.isPostionEmpty(position, shoucangNum)) {

						if (UtilTools.isPositionShowQitaTitle(position,
								shoucangNum)) {
							Log.i(TAG, "Position:--->" + position
									+ " isGridViewUp--->" + isGridViewUp);
							if (isGridViewUp) {

								playGv.setSelection(position - 5);
								
								// playGv.smoothScrollBy(35, -1);
							} else {

								// playGv.setSelection(position + 5);
								qitaNextPoistion = position + 5;
								playGv.smoothScrollBy(35 + popHeight, -1);
								playGv.setSelection(position + 5);
								shoucangTv
								.setText(R.string.qitadongman_play_name);
							}
						} else {

							if (!isGridViewUp
									&& qitaNextPoistion + 5 == position) {

								// playGv.smoothScrollBy(35, -1);
								// int scrolly = playGv.getScrollY();

								// playGv.scrollTo(0, (int)view.getY());

//								shoucangTv
//										.setText(R.string.qitadongman_play_name);

							} else if (isGridViewUp
									&& qitaNextPoistion - 10 == position) {

								shoucangTv
										.setText(R.string.shoucang_update_name);
							}

							if (mSparseArray.get(activeRecordIndex) != null
									&& activeRecordIndex != position
									&& !isDragGridView) {
								Log.i(TAG,
										"setOnItemSelectedListener position != activeRecordIndex--->viewOutAnimation");
								ItemStateUtils.viewOutAnimation(
										getApplicationContext(),
										mSparseArray.get(activeRecordIndex));
							}

							Log.i(TAG, "position-->" + position
									+ " activeRecordIndex--->"
									+ activeRecordIndex + " isFirstActive--->"
									+ isFirstActive + "isDragGridView"
									+ isDragGridView);

							if (position != activeRecordIndex && isFirstActive
									&& !isDragGridView) {

								Log.i(TAG,
										"setOnItemSelectedListener position != activeRecordIndex--->viewInAnimation");
								ItemStateUtils.viewInAnimation(
										getApplicationContext(), view);
								activeRecordIndex = position;
							}

							if (!isFirstActive) {// 如果不是初始化，那就设为true

								isFirstActive = true;
							}
						}
					} else {
						// 当前位置为空的组件
						if (isGridViewUp) {// 向上

							playGv.setSelection(UtilTools
									.stepToFirstInThisRow(position));
						} else {// 向下

							playGv.setSelection(UtilTools
									.stepToFirstInThisRow(position));
						}

					}
				} else {

					if (mSparseArray.get(activeRecordIndex) != null
							&& activeRecordIndex != position && !isDragGridView) {
						Log.i(TAG,
								"setOnItemSelectedListener position != activeRecordIndex--->viewOutAnimation");
						ItemStateUtils.viewOutAnimation(
								getApplicationContext(),
								mSparseArray.get(activeRecordIndex));
					}

					if (position != activeRecordIndex && isFirstActive
							&& !isDragGridView) {
						Log.i(TAG,
								"setOnItemSelectedListener position != activeRecordIndex--->viewOutAnimation");

						ItemStateUtils.viewInAnimation(getApplicationContext(),
								view);
						activeRecordIndex = position;
					}

					if (!isFirstActive) {// 如果不是初始化，那就设为true

						isFirstActive = true;
					}
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

				// beforepostion = position;

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

		playGv.setOnScrollListener(new AbsListView.OnScrollListener() {

			int tempfirstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					Log.i(TAG, "playGv--->SCROLL_STATE_IDLE"
							+ " tempfirstVisibleItem--->"
							+ tempfirstVisibleItem);

					// 缓存
					// playGv.setSelection(tempfirstVisibleItem);
					if (searchAdapter != null) {

						if (searchAdapter.getMovieList() != null) {

							int size = searchAdapter.getMovieList().size();

							if (size > 0) {

								if (size - 1 - (tempfirstVisibleItem + 9) < URLUtils.CACHE_NUM) {

									if (isNextPagePossibles[currentListIndex]) {

										pageNums[currentListIndex]++;
										cachePlay(currentListIndex,
												pageNums[currentListIndex]);
									}
								}
							}
						}
					}
					
					if(shoucangList != null && shoucangList.size() > 0) {
						
						if(tempfirstVisibleItem > UtilTools.getFirstPositionQitaTitle(shoucangList.size())) {
							
							shoucangTv
							.setText(R.string.qitadongman_play_name);
						} else {
							
							shoucangTv
							.setText(R.string.shoucang_update_name);
						}
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					Log.i(TAG, "playGv--->SCROLL_STATE_TOUCH_SCROLL");
					isDragGridView = true;

					if (activeRecordIndex >= 0
							&& mSparseArray.get(activeRecordIndex) != null) {

						ItemStateUtils.viewOutAnimation(
								getApplicationContext(),
								mSparseArray.get(activeRecordIndex));

						activeRecordIndex = -1;
					}

					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					Log.i(TAG, "playGv--->SCROLL_STATE_FLING");

					// isDragGridView = false;
					break;
				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				tempfirstVisibleItem = firstVisibleItem;
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

				if (keyBoardWindow == null) {

					keyBoardWindow = new PopupWindow(keyBoardView, searchEt
							.getRootView().getWidth(), searchEt.getRootView()
							.getHeight(), true);
					keyBoardWindow.setBackgroundDrawable(new BitmapDrawable());
					keyBoardWindow.setOutsideTouchable(true);
				}

				if (keyBoardWindow != null && !keyBoardWindow.isShowing()) {

					keyBoardWindow.showAtLocation(searchEt.getRootView(),
							Gravity.BOTTOM, 0, 0);
				}

			}
		});

		searchLL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.i(TAG, "searchLL.setOnClickListener");

				if (keyBoardWindow == null) {

					keyBoardWindow = new PopupWindow(keyBoardView, searchEt
							.getRootView().getWidth(), searchEt.getRootView()
							.getHeight(), true);
					keyBoardWindow.setBackgroundDrawable(new BitmapDrawable());
					keyBoardWindow.setOutsideTouchable(true);
				}

				if (keyBoardWindow != null && !keyBoardWindow.isShowing()) {

					keyBoardWindow.showAtLocation(searchEt.getRootView(),
							Gravity.BOTTOM, 0, 0);
				}

			}
		});

		searchLL.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				if (hasFocus) {

					searchEt.setTextColor(getResources()
							.getColor(R.color.black));
				} else {

					searchEt.setTextColor(getResources()
							.getColor(R.color.white));
				}
			}
		});

		startSearchBtn.setOnFocusChangeListener(this);
	}

	private void searchPlay() {

		Editable editable = searchEt.getText();
		String searchStr = editable.toString();
		// searchEt.setText("");
		playGv.setNextFocusLeftId(startSearchBtn.getId());//

		ItemStateUtils.viewToNormal(getApplicationContext(), activeView);
		activeView = null;

		if (searchStr != null && !searchStr.equals("")) {
			resetGvActive();
			showDialog(DIALOG_WAITING);
			search = searchStr;
			UtilTools.clearList(lists[SEARCH]);
			currentListIndex = SEARCH;
			// String url = StatisticsUtils.getSearch_FirstURL(searchStr);
			String url = URLUtils.getSearch_Zongyi_FirstURL(searchStr);
			getFilterData(url);
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

		if (currentListIndex != SEARCH && currentListIndex != QUAN_FILTER) {

			searchAdapter.setList(list, true);
		} else {

			searchAdapter.setList(list, false);
		}

		playGv.setAdapter(searchAdapter);

		if (searchAdapter.getItemId() == list.size()) {

			searchAdapter.setItemId(list.size() + 1);
		} else {

			searchAdapter.setItemId(list.size());
		}

		if (list.size() <= 0) {

			app.MyToast(getApplicationContext(),
					getString(R.string.toast_no_play));
		}

		if (list != null && !list.isEmpty() && QUANBUFENLEI != currentListIndex) {// 判断其能否向获取更多数据

			if (SEARCH == currentListIndex || QUAN_FILTER == currentListIndex) {// 只有搜索和连续两次点击出现筛界面下拉才在这判断

				if (list.size() == URLUtils.FIRST_NUM) {

					isNextPagePossibles[currentListIndex] = true;
				} else if (list.size() < URLUtils.FIRST_NUM) {

					isNextPagePossibles[currentListIndex] = false;
				}
			}

		}
		lists[currentListIndex] = list;

		playGv.setSelection(0);
		searchAdapter.notifyDataSetChanged();
		removeDialog(DIALOG_WAITING);

		if (currentListIndex == SEARCH) {// 搜索高亮在gridview第一个元素

			isFirstActive = true;
			playGv.requestFocus();
		}

	}

	@Override
	protected void filterVideoSource(String[] choice) {
		// TODO Auto-generated method stub

		String quanbu = getString(R.string.quanbu_name);
		String quanbufenlei = getString(R.string.quanbufenlei_name);
		String tempStr = URLUtils.getQuanBuFenLeiName(choice, quanbufenlei,
				quanbu);
		mFenLeiBtn.setText(tempStr);

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
		resetGvActive();
		filterSource = URLUtils.getFileterURL3Param(choice, quanbu);
		String url = URLUtils.getFilter_ZongyiFirstURL(filterSource);
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

		currentListIndex = QUANBUFENLEI;
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

		searchAdapter.setList(srcList, true);
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

		if (popupWindow == null) {
			NavigateView view = new NavigateView(this);
			int[] location = new int[2];
			mFenLeiBtn.getLocationOnScreen(location);
			view.Init(
					getResources().getStringArray(R.array.diqu_zongyi_fenlei),
					getResources()
							.getStringArray(R.array.leixing_zongyi_fenlei),
					getResources().getStringArray(
							R.array.shijian_dianying_fenlei), location[0],
					location[1], mFenLeiBtn.getWidth(), mFenLeiBtn.getHeight(),
					new OnResultListener() {

						@Override
						public void onResult(View v, boolean isBack,
								String[] choice) {
							// TODO Auto-generated method stub
							if (isBack) {
								popupWindow.dismiss();
							} else {
								if (popupWindow.isShowing()) {
									popupWindow.dismiss();
									// Toast.makeText(ShowZongYiActivity.this,
									// "selected is " + choice[0] +
									// ","+choice[1]+","+choice[2],
									// Toast.LENGTH_LONG).show();
									filterVideoSource(choice);

								}
							}
						}
					});
			view.setLayoutParams(new LayoutParams(0, 0));
			// popupWindow = new PopupWindow(view,
			// getWindowManager().getDefaultDisplay().getWidth(),
			// getWindowManager().getDefaultDisplay().getHeight(), true);
			int width = topLinearLayout.getWidth();
			int height = topLinearLayout.getHeight();
			popupWindow = new PopupWindow(view, width, height, true);
		}
		popupWindow.showAtLocation(mFenLeiBtn.getRootView(), Gravity.LEFT
				| Gravity.BOTTOM, 0, 0);
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
			if (json == null || json.equals(""))
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
			// getFilterData(StatisticsUtils.getTV_QuanAllCacheURL(pageNum));
			getMoreFilterData(URLUtils.getZongyi_QuanAllCacheURL(pageNum));
			break;
		case QUAN_TEN:

			break;
		case QUAN_FILTER:
			getMoreFilterData(URLUtils.getFilter_ZongyiCacheURL(pageNum,
					filterSource));
			break;
		case SEARCH:
			// getMoreFilterData(StatisticsUtils.getSearch_CacheURL(pageNum,
			// search));
			getMoreFilterData(URLUtils.getSearch_Zongyi_CacheURL(pageNum,
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

		if (status.getCode() == AjaxStatus.NETWORK_ERROR) {

			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}
		try {

			if (json == null || json.equals(""))
				return;

			Log.d(TAG, json.toString());

			List<MovieItemData> temp10List = new ArrayList<MovieItemData>();
			List<MovieItemData> tempList = new ArrayList<MovieItemData>();
			tempList = UtilTools
					.returnFilterMovieSearch_TVJson(json.toString());

			for (MovieItemData movieItemData : tempList) {

				boolean isSame = false;
				String proId = movieItemData.getMovieID();

				if (shoucangList != null && shoucangList.size() > 0) {

					for (MovieItemData movieItemData2 : shoucangList) {

						if (movieItemData2.getMovieID().equals(proId)) {

							isSame = true;
							break;// // 符合条件跳出本次循环
						}
					}
				}
				if (!isSame) {

					temp10List.add(movieItemData);
				}
			}

			Log.i(TAG, "Temp size:" + tempList.size());
			if (tempList.size() == URLUtils.CACHE_NUM) {

				isNextPagePossibles[currentListIndex] = true;
			}

			if (isShowShoucang) {

				int tianchongEmpty = UtilTools.tianchongEmptyItem(shoucangList
						.size());

				Log.i(TAG, "tianchongEmpty--->" + tianchongEmpty
						+ " temp10List" + temp10List.size());

				List<MovieItemData> tempList2 = new ArrayList<MovieItemData>(
						shoucangList);

				for (int i = 0; i < tianchongEmpty; i++) {

					MovieItemData item = new MovieItemData();
					tempList2.add(item);

				}

				Log.i(TAG, "tempList2 start--->" + tempList2.size());

				tempList2.addAll(temp10List);

				Log.i(TAG, "tempList2 end--->" + tempList2.size());

				notifyAdapter(tempList2);
			} else {

				notifyAdapter(temp10List);
			}

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

			if (json == null || json.equals(""))
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

		activeView = mFenLeiBtn;

		ItemStateUtils.buttonToActiveState(getApplicationContext(), mFenLeiBtn);

		ItemStateUtils.setItemPadding(zuijinguankanBtn);
		ItemStateUtils.setItemPadding(zhuijushoucangBtn);
		ItemStateUtils.setItemPadding(mFenLeiBtn);

		if (isShowShoucang) {

			shoucangTitlleLL.setVisibility(View.VISIBLE);
		} else {

			shoucangTitlleLL.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

		searchEt = (EditText) findViewById(R.id.et_search);

		startSearchBtn = (Button) findViewById(R.id.bt_search_click);
		searchLL = (LinearLayout) findViewById(R.id.ll_search);

		mFenLeiBtn = (Button) findViewById(R.id.bt_quanbufenlei);
		playGv = (MyMovieGridView) findViewById(R.id.gv_movie_show);

		zuijinguankanBtn = (Button) findViewById(R.id.bt_zuijinguankan);
		zhuijushoucangBtn = (Button) findViewById(R.id.bt_zhuijushoucang);

		topLinearLayout = (LinearLayout) findViewById(R.id.ll_show_movie_top);

		shoucangTitlleLL = (LinearLayout) findViewById(R.id.ll_shoucanggengxin);
		shoucangTv = (TextView) findViewById(R.id.tv_shoucanggengxin_name);

		keyBoardView = new KeyBoardView(this, searchEt,
				new KeyBoardView.OnKeyBoardResultListener() {

					@Override
					public void onResult(boolean isSearch) {
						// TODO Auto-generated method stub
						if (keyBoardWindow != null
								&& keyBoardWindow.isShowing()) {
							keyBoardWindow.dismiss();
						}

						if (isSearch) {

							searchPlay();
						}
					}
				});

		playGv.setNextFocusLeftId(R.id.bt_quanbufenlei);
		playGv.setNextFocusUpId(R.id.gv_movie_show);
		playGv.setNextFocusDownId(R.id.gv_movie_show);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("Yangzhg", "onClick");

		if (v.getId() == R.id.bt_quanbufenlei && activeView != null
				&& activeView.getId() == R.id.bt_quanbufenlei) {

			filterPopWindowShow();
		}

		if (v.getId() == R.id.bt_zuijinguankan) {

			startActivity(new Intent(this, HistoryActivity.class));

			return;
		} else if (v.getId() == R.id.bt_zhuijushoucang) {

			startActivity(new Intent(this, ShowShoucangHistoryActivity.class));
			return;
		}

		if (activeView != null && activeView.getId() == v.getId()) {

			return;
		}

		switch (v.getId()) {
		case R.id.bt_quanbufenlei:
			currentListIndex = QUANBUFENLEI;
			resetGvActive();
			if (lists[currentListIndex] != null
					&& !lists[currentListIndex].isEmpty()) {

				notifyAdapter(lists[currentListIndex]);
			}
			break;
		case R.id.bt_zuijinguankan:
			startActivity(new Intent(this, HistoryActivity.class));
			break;
		case R.id.bt_zhuijushoucang:
			startActivity(new Intent(this, ShowShoucangHistoryActivity.class));
			break;

		default:
			break;
		}

		View tempView = ItemStateUtils.viewToActive(getApplicationContext(), v,
				activeView);

		if (tempView != null) {

			activeView = tempView;
		}

		playGv.setNextFocusLeftId(v.getId());
	}

	@Override
	protected void resetGvActive() {
		// TODO Auto-generated method stub

		if (currentListIndex == QUANBUFENLEI) {

			searchAdapter.setShoucangShow(true);
			shoucangTitlleLL.setVisibility(View.VISIBLE);

			if (shoucangList != null && !shoucangList.isEmpty()) {

				if (shoucangList.size() > 0) {

					Log.i(TAG, "shoucangList--->:" + shoucangList.size());

					isShowShoucang = true;
				}
			} else {

				isShowShoucang = false;
				searchAdapter.setShoucangShow(false);
				shoucangTitlleLL.setVisibility(View.GONE);
			}
		} else {

			searchAdapter.setShoucangShow(false);
			shoucangTitlleLL.setVisibility(View.GONE);
			isShowShoucang = false;
		}

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
