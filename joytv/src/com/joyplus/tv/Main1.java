package com.joyplus.tv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.banner.AdView;
import com.joyplus.tv.Adapters.MainHotItemAdapter;
import com.joyplus.tv.Adapters.MainLibAdapter;
import com.joyplus.tv.Adapters.MainYueDanItemAdapter;
import com.joyplus.tv.Service.Return.ReturnLogInfo;
import com.joyplus.tv.Service.Return.ReturnMainHot;
import com.joyplus.tv.Service.Return.ReturnProgramView;
import com.joyplus.tv.Service.Return.ReturnTops;
import com.joyplus.tv.Service.Return.ReturnUserPlayHistories;
import com.joyplus.tv.database.TvDatabaseHelper;
import com.joyplus.tv.entity.CurrentPlayDetailData;
import com.joyplus.tv.entity.HotItemInfo;
import com.joyplus.tv.entity.ShiPinInfoParcelable;
import com.joyplus.tv.entity.YueDanInfo;
import com.joyplus.tv.ui.CustomGallery;
import com.joyplus.tv.ui.MyScrollLayout;
import com.joyplus.tv.ui.MyScrollLayout.OnViewChangeListener;
import com.joyplus.tv.ui.UserInfo;
import com.joyplus.tv.ui.WaitingDialog;
import com.joyplus.tv.utils.BangDanConstant;
import com.joyplus.tv.utils.DBUtils;
import com.joyplus.tv.utils.DataBaseItems;
import com.joyplus.tv.utils.DataBaseItems.UserHistory;
import com.joyplus.tv.utils.DataBaseItems.UserShouCang;
import com.joyplus.tv.utils.Log;
import com.joyplus.tv.utils.URLUtils;
import com.joyplus.tv.utils.UtilTools;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class Main1 extends Activity implements OnItemSelectedListener,
		OnItemClickListener , AdListener{
	private String TAG = "Main";
	public static final String ACTION_USERUPDATE = "user_update";
	
	private static final int DIALOG_WAITING = 0;

	private static final int MESSAGE_STEP1_SUCESS = 100;
	private static final int MESSAGE_STEP2_SUCESS = MESSAGE_STEP1_SUCESS + 1;

	private static final int MESSAGE_START_TIMEOUT = MESSAGE_STEP2_SUCESS + 1;
	private static final int MESSAGE_UPDATEUSER = MESSAGE_START_TIMEOUT + 1;

	private static final int MESSAGE_UPDATEUSER_HISTORY_SUCEESS = MESSAGE_UPDATEUSER + 1;
	private static final int MESSAGE_30S_TIMEOUT = MESSAGE_UPDATEUSER_HISTORY_SUCEESS + 1;

	private static final long LOADING_PIC_TIME = 10 * 1000;
	private static final long LOADING_TIME_OUT = 30 * 1000;
	
	private int[] resouces_lib_nomal = { R.drawable.movie_normal,
			R.drawable.episode_normal, R.drawable.cartoon_normal,
			R.drawable.variety_normal, R.drawable.search_normal };
	private int[] resouces_lib_active = { R.drawable.movie_active,
			R.drawable.episode_active, R.drawable.cartoon_active,
			R.drawable.variety_active, R.drawable.search_active, };

	// private int [] resouces_my_nomal = {
	// R.drawable.follow_normal,
	// R.drawable.recent_normal,
	// R.drawable.down_normal,
	// R.drawable.system_normal
	// };
	private int[] resouces_my_nomal = { R.drawable.follow_normal,
			R.drawable.recent_normal, R.drawable.system_normal };
	// private int [] resouces_my_active = {
	// R.drawable.follow_active,
	// R.drawable.recent_active,
	// R.drawable.down_active,
	// R.drawable.system_active,
	// };
	private int[] resouces_my_active = { R.drawable.follow_active,
			R.drawable.recent_active, R.drawable.system_active, };

	
	private App app;
	private AQuery aq; 
	ObjectMapper mapper = new ObjectMapper();

	private int initStep = 0;
	private long exitTime = 0;

	private ImageView startingImageView;
	private RelativeLayout rootLayout;
	private Map<String, String> headers;


	private SparseArray<View> hot_contentViews = new SparseArray<View>();
	private List<View> yuedan_contentViews = new ArrayList<View>();

	private CustomGallery gallery1;
	private float density;
	private int displayWith;
	private MyScrollLayout titleGroup;
	private FrameLayout itemFram;
	private ImageView highlightImageView_1;
	private ImageView highlightImageView_2;
	private ImageView highlightImageView_3;
	private ImageView highlightImageView_4;
	private ImageView playIcon;
	private ImageView definitionIcon;

	private ImageView image_bar1;
	private ImageView image_bar2;
	private ImageView image_bar3;

	private LinearLayout contentLayout;
	private TextView noticeView;

	// private View hotView;
	// private View yeuDanView;
	private View kuView;
	private View myView;
	private TextView lastBandTimeView;
	// private TextView hot_name_tv;
	// private TextView hot_score_tv;
	// private TextView hot_directors_tv;
	// private TextView hot_starts_tv;
	// private TextView hot_introduce_tv;

	private Map<Integer, Integer> indexCaces = new HashMap<Integer, Integer>();

	private Animation alpha_appear;
	private Animation alpha_disappear;

	private TranslateAnimation leftTranslateAnimationStep1;
	private TranslateAnimation leftTranslateAnimationStep2;
	private TranslateAnimation rightTranslateAnimationStep1;
	private TranslateAnimation rightTranslateAnimationStep2;

	// private FayeClient mClient;
//	private String macAddress;

	private static final int DIALOG_NETWORK_ERROR = DIALOG_WAITING + 1;
	private static final int DIALOG_NETWORK_SLOW = DIALOG_NETWORK_ERROR + 1;

	private boolean isNetWorkFine = true;// 记录网络是否正常
	private boolean isWifiReset = false;// wifi网络是否重新设置
	
	private List<HotItemInfo> hot_list = new ArrayList<HotItemInfo>();//用户数据，有可能去过重
	private List<HotItemInfo> netWorkHotList = new ArrayList<HotItemInfo>();//网络获取数据，不改变
	private List<YueDanInfo> yuedan_list = new ArrayList<YueDanInfo>();
	private int isHotLoadedFlag = 0;
	private int isYueDanLoadedFlag = 0;
	
	private Button upScrollBt,downScrollBt;
	private ImageButton leftCustomIv,rightCustomIv;
	
	/*
	 * adkey varies
	 * @author
	 */
	private RelativeLayout layout;
	private AdView mAdView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		app = (App) getApplicationContext();
		aq = new AQuery(this);
		
//		MobclickAgent.onError(this);
		

		startingImageView = (ImageView) findViewById(R.id.image_starting);
		
		if(!UtilTools.getIsShowAd(getApplicationContext())) {
			
			BitmapFactory.Options opt = new BitmapFactory.Options();
//			  opt.inPreferredConfig = Bitmap.Config.RGB_565; // Each pixel is stored 2 bytes
		  // opt.inPreferredConfig = Bitmap.Config.ARGB_8888; //Each pixel is stored 4 bytes
	//
			opt.inTempStorage = new byte[16 * 1024];
//			opt.inPurgeable = true;
//			opt.inInputShareable = true;
//			
			try {
				startingImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.starting, opt));
			} catch (OutOfMemoryError e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			
			showDialog(DIALOG_WAITING);
		}
		
		rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
		gallery1 = (CustomGallery) findViewById(R.id.gallery);
		contentLayout = (LinearLayout) findViewById(R.id.contentlayout);
		noticeView = (TextView) findViewById(R.id.notice_text);
		titleGroup = (MyScrollLayout) findViewById(R.id.group);
		titleGroup.setFocusable(false);
		titleGroup.setFocusableInTouchMode(false);

		image_bar1 = (ImageView) findViewById(R.id.img_navagatorbar1);
		image_bar2 = (ImageView) findViewById(R.id.img_navagatorbar2);
		image_bar3 = (ImageView) findViewById(R.id.img_navagatorbar3);
		image_bar1.setVisibility(View.INVISIBLE);
		image_bar2.setVisibility(View.INVISIBLE);
		image_bar3.setVisibility(View.INVISIBLE);
		
		upScrollBt = (Button) findViewById(R.id.bt_up_scrolllayout);
		
		upScrollBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "upScrollBt-->setOnClickListener");
				titleGroup.selectPreTitle();
			}
		});
		
		downScrollBt = (Button) findViewById(R.id.bt_down_scrolllayout);
		
		downScrollBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "downScrollBt-->setOnClickListener");
				titleGroup.selectNextTitle();
			}
		});
		
		leftCustomIv = (ImageButton) findViewById(R.id.icon_arrow_left);
		rightCustomIv = (ImageButton) findViewById(R.id.icon_arrow_right);
		leftCustomIv.setFocusable(false);
		leftCustomIv.setFocusableInTouchMode(false);
		rightCustomIv.setFocusable(false);
		rightCustomIv.setFocusableInTouchMode(false);
		leftCustomIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				gallery1.showPre();
			}
		});
		
		rightCustomIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				gallery1.showNext();
			}
		});

		kuView = LayoutInflater.from(Main1.this).inflate(R.layout.layout_lib,
				null);
		myView = LayoutInflater.from(Main1.this).inflate(R.layout.layout_my,
				null);
		lastBandTimeView = (TextView) myView.findViewById(R.id.lastBandTime);
		
		/*
		 * banner ad
		 */
		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
		// 一开始判断有没有网络
		if (!app.isNetworkAvailable()) {// 如果没有网络，弹出提示dialog

			isNetWorkFine = false;// 网络不正常
			showDialog(DIALOG_NETWORK_ERROR);

		}

		titleGroup.SetOnViewChangeListener(new OnViewChangeListener() {

			@Override
			public void OnViewChange(int index) {
				// TODO Auto-generated method stub
				handler.removeCallbacks(null, null);
				switch (index) {
				case 1:
					image_bar1.clearAnimation();
					image_bar2.clearAnimation();
					image_bar3.clearAnimation();
					image_bar2.setVisibility(View.INVISIBLE);
					image_bar3.setVisibility(View.INVISIBLE);
					if (image_bar1.getVisibility() == View.VISIBLE) {
						image_bar1.startAnimation(alpha_disappear);
						image_bar1.setVisibility(View.INVISIBLE);
					}
					highlightImageView_1.setVisibility(View.VISIBLE);
					highlightImageView_2.setVisibility(View.GONE);
					highlightImageView_3.setVisibility(View.GONE);
					highlightImageView_4.setVisibility(View.GONE);
					Log.d("AAAAA", "highlightImageView_1 visibale = "
							+ highlightImageView_1.getVisibility());
					Log.d("AAAAA", "highlightImageView_2 visibale = "
							+ highlightImageView_2.getVisibility());
					Log.d("AAAAA", "highlightImageView_3 visibale = "
							+ highlightImageView_3.getVisibility());
					Log.d("AAAAA", "highlightImageView_4 visibale = "
							+ highlightImageView_4.getVisibility());
					aq = new AQuery(Main1.this);
					if (isHotLoadedFlag == 2) {
						itemFram.setVisibility(View.VISIBLE);
						contentLayout.removeAllViews();
						if (indexCaces.get(index) != null
								&& indexCaces.get(index) < hot_contentViews
										.size() - 1) {
							View hotView = hot_contentViews.get(indexCaces
									.get(index));
							hotView.setLayoutParams(new LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
							contentLayout.startAnimation(alpha_appear);
							if(hot_list != null && hot_list.get(indexCaces
									.get(index))!= null) {
								
								HotItemInfo tempInfo = hot_list.get(indexCaces
										.get(index));
								
								Log.i(TAG, "tempInfo.duration--->" + tempInfo.duration);
								
								if(tempInfo.type == 1 && tempInfo.prod_type.equals("1")) {
									
									initOverTime(hotView, tempInfo.duration);
								}
							}
							contentLayout.addView(hotView);
						}
						gallery1.setAdapter(new MainHotItemAdapter(Main1.this,
								hot_list));
						if (indexCaces.get(index) == null) {
							gallery1.setSelection(0);
						} else {
							gallery1.setSelection(indexCaces.get(index));
						}
						// changeContent(0);
						int seletedindex = gallery1.getSelectedItemPosition();
						ImageView img = null;
						if (seletedindex >= 0
								&& seletedindex < hot_list.size() - 1) {
							img = (ImageView) gallery1.findViewWithTag(hot_list
									.get(gallery1.getSelectedItemPosition()).prod_pic_url);
						}
						if (img != null) {
							if (img.getDrawable() != null) {
								highlightImageView_1.setImageDrawable(img
										.getDrawable());
							} else {
								highlightImageView_1.setImageDrawable(null);
								aq.id(highlightImageView_1)
										.image(hot_list.get(gallery1
												.getSelectedItemPosition()).prod_pic_url,
												true, true);
							}
						} else {
							if (seletedindex >= 0
									&& seletedindex < hot_list.size() - 1) {
								aq.id(highlightImageView_1)
										.image(hot_list.get(gallery1
												.getSelectedItemPosition()).prod_pic_url,
												true, true);
							}
						}
						switch (Integer.valueOf(hot_list.get(gallery1
								.getSelectedItemPosition()).definition)) {
						case 8:
							definitionIcon.setImageResource(R.drawable.icon_bd);
							break;
						case 7:
							definitionIcon.setImageResource(R.drawable.icon_hd);
							break;
						case 6:
							definitionIcon.setImageResource(R.drawable.icon_ts);
							break;
						default:
							definitionIcon.setImageDrawable(null);
							break;
						}
						// aq.id(highlightImageView).image(hot_list.get(gallery1.getSelectedItemPosition()).prod_pic_url);
						// noticeView.setText(gallery1.getSelectedItemPosition()+1
						// + "/" + hot_list.size());
//						gallery1.setAdapter(new MainHotItemAdapter(Main.this,
//								hot_list));
//						if (indexCaces.get(index) == null) {
//							gallery1.setSelection(0);
//						} else {
//							gallery1.setSelection(indexCaces.get(index));
//						}

					} else {
						definitionIcon.setImageDrawable(null);
						itemFram.setVisibility(View.INVISIBLE);
						hot_list.clear();
						hot_contentViews.clear();
						gallery1.setAdapter(null);
						contentLayout.removeAllViews();
						Log.i(TAG, "SetOnViewChangeListener--->isHotLoadedFlag != 2");
						getHistoryServiceData();
						getHotServiceData();
					}
					playIcon.setVisibility(View.VISIBLE);
					break;
				case 2:
					image_bar1.clearAnimation();
					image_bar2.clearAnimation();
					image_bar3.clearAnimation();
					image_bar3.setVisibility(View.INVISIBLE);
					if (image_bar2.getVisibility() == View.VISIBLE) {
						image_bar2.startAnimation(alpha_disappear);
						image_bar2.setVisibility(View.INVISIBLE);
					} else {
						image_bar1.startAnimation(alpha_appear);
						image_bar1.setVisibility(View.VISIBLE);
					}
					aq = new AQuery(Main1.this);
					highlightImageView_1.setVisibility(View.GONE);
					highlightImageView_2.setVisibility(View.VISIBLE);
					highlightImageView_3.setVisibility(View.GONE);
					highlightImageView_4.setVisibility(View.GONE);
					Log.d("AAAAA", "highlightImageView_1 visibale = "
							+ highlightImageView_1.getVisibility());
					Log.d("AAAAA", "highlightImageView_2 visibale = "
							+ highlightImageView_2.getVisibility());
					Log.d("AAAAA", "highlightImageView_3 visibale = "
							+ highlightImageView_3.getVisibility());
					Log.d("AAAAA", "highlightImageView_4 visibale = "
							+ highlightImageView_4.getVisibility());
					playIcon.setVisibility(View.INVISIBLE);
					if (isYueDanLoadedFlag == 2) {
						itemFram.setVisibility(View.VISIBLE);
						contentLayout.removeAllViews();
						if (indexCaces.get(index) != null
								&& indexCaces.get(index) < yuedan_contentViews
										.size() - 1) {
							View yeuDanView = yuedan_contentViews
									.get(indexCaces.get(index));
							yeuDanView.setLayoutParams(new LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
							contentLayout.startAnimation(alpha_appear);
							contentLayout.addView(yeuDanView);
						}
						gallery1.setAdapter(new MainYueDanItemAdapter(
								Main1.this, yuedan_list));
						if (indexCaces.get(index) == null) {
							gallery1.setSelection(0);
						} else {
							gallery1.setSelection(indexCaces.get(index));
						}
						if (gallery1.getSelectedItemPosition() == yuedan_list
								.size() - 2) {
							highlightImageView_2
									.setImageResource(R.drawable.more_movie_active);
						} else if (gallery1.getSelectedItemPosition() == yuedan_list
								.size() - 1) {
							highlightImageView_2
									.setImageResource(R.drawable.more_episode_active);
						} else {
							ImageView img2 = (ImageView) gallery1.findViewWithTag(yuedan_list
									.get(gallery1.getSelectedItemPosition()).pic_url);
							if (img2 != null) {
								if (img2.getDrawable() != null) {
									highlightImageView_2.setImageDrawable(img2
											.getDrawable());
								} else {
									highlightImageView_2.setImageDrawable(null);
									aq.id(highlightImageView_2)
											.image(yuedan_list.get(gallery1
													.getSelectedItemPosition()).pic_url,
													true, true);
								}
							} else {
								aq.id(highlightImageView_2)
										.image(yuedan_list.get(gallery1
												.getSelectedItemPosition()).pic_url,
												true, true);
							}
							if ("1".equals(yuedan_list.get(gallery1
									.getSelectedItemPosition()).prod_type)) {
								definitionIcon
										.setImageResource(R.drawable.icon_movie);
							} else if ("2".equals(yuedan_list.get(gallery1
									.getSelectedItemPosition()).prod_type)) {
								definitionIcon
										.setImageResource(R.drawable.icon_episode);
							}
						}
						noticeView.setText(gallery1.getSelectedItemPosition()
								+ 1 + "/" + yuedan_list.size());
						
//						gallery1.setAdapter(new MainYueDanItemAdapter(Main.this,
//								yuedan_list));
//						if (indexCaces.get(index) == null) {
//							gallery1.setSelection(0);
//						} else {
//							gallery1.setSelection(indexCaces.get(index));
//						}
					} else {
						yuedan_list.clear();
						yuedan_contentViews.clear();
						gallery1.setAdapter(null);
						contentLayout.removeAllViews();
						itemFram.setVisibility(View.INVISIBLE);
						definitionIcon.setImageDrawable(null);
						getMovieYueDanServiceData();
						getTVYueDanServiceData();
					}
					break;
				case 3:
					image_bar1.clearAnimation();
					image_bar2.clearAnimation();
					image_bar3.clearAnimation();
					image_bar1.setVisibility(View.VISIBLE);
					if (image_bar3.getVisibility() == View.VISIBLE) {
						image_bar3.startAnimation(alpha_disappear);
						image_bar3.setVisibility(View.INVISIBLE);
					} else {
						image_bar2.startAnimation(alpha_appear);
						image_bar2.setVisibility(View.VISIBLE);
					}
					aq = new AQuery(Main1.this);
					highlightImageView_1.setVisibility(View.GONE);
					highlightImageView_2.setVisibility(View.GONE);
					highlightImageView_3.setVisibility(View.VISIBLE);
					highlightImageView_4.setVisibility(View.GONE);
					
					itemFram.setVisibility(View.VISIBLE);
					contentLayout.removeAllViews();
					kuView.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					contentLayout.startAnimation(alpha_appear);
					contentLayout.addView(kuView);
					gallery1.setAdapter(new MainLibAdapter(Main1.this,
							resouces_lib_nomal));
					if (indexCaces.get(index) == null) {
						gallery1.setSelection(0);
					} else {
						gallery1.setSelection(indexCaces.get(index));
					}
					playIcon.setVisibility(View.INVISIBLE);
					highlightImageView_3
							.setImageResource(resouces_lib_active[gallery1
									.getSelectedItemPosition()]);
					noticeView.setText(gallery1.getSelectedItemPosition() + 1
							+ "/" + resouces_lib_active.length);
					definitionIcon.setImageDrawable(null);
					definitionIcon.setImageDrawable(null);
					break;
				case 4:
					image_bar1.clearAnimation();
					image_bar2.clearAnimation();
					image_bar3.clearAnimation();
					image_bar1.setVisibility(View.VISIBLE);
					image_bar2.setVisibility(View.VISIBLE);
					image_bar3.setVisibility(View.VISIBLE);
					image_bar3.startAnimation(alpha_appear);
					aq = new AQuery(Main1.this);
					highlightImageView_1.setVisibility(View.GONE);
					highlightImageView_2.setVisibility(View.GONE);
					highlightImageView_3.setVisibility(View.GONE);
					highlightImageView_4.setVisibility(View.VISIBLE);

					itemFram.setVisibility(View.VISIBLE);
					contentLayout.removeAllViews();
					myView.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					contentLayout.startAnimation(alpha_appear);
					contentLayout.addView(myView);
					updateLastTimeView();
					gallery1.setAdapter(new MainLibAdapter(Main1.this,
							resouces_my_nomal));
					if (indexCaces.get(index) == null) {
						gallery1.setSelection(0);
					} else {
						gallery1.setSelection(indexCaces.get(index));
					}
					playIcon.setVisibility(View.INVISIBLE);
					highlightImageView_4
							.setImageResource(resouces_my_active[gallery1
									.getSelectedItemPosition()]);
					noticeView.setText(gallery1.getSelectedItemPosition() + 1
							+ "/" + resouces_my_active.length);
					definitionIcon.setImageDrawable(null);
					break;
				}
				if (gallery1.getAnimation() != null
						&& !gallery1.getAnimation().hasEnded()) {

				} else {
					Log.d(TAG, "startAnimation---------------------->");
					gallery1.clearAnimation();
					gallery1.startAnimation(alpha_appear);
				}

				// gallery1.startAnimation(alpha_appear);
			}
		});

		itemFram = (FrameLayout) findViewById(R.id.itemFram);
		// clock = (ClockTextView) findViewById(R.id.clock);
		highlightImageView_1 = (ImageView) findViewById(R.id.highlight_img_1);
		highlightImageView_2 = (ImageView) findViewById(R.id.highlight_img_2);
		highlightImageView_3 = (ImageView) findViewById(R.id.highlight_img_3);
		highlightImageView_4 = (ImageView) findViewById(R.id.highlight_img_4);
		highlightImageView_2.setVisibility(View.GONE);
		highlightImageView_3.setVisibility(View.GONE);
		highlightImageView_4.setVisibility(View.GONE);
		playIcon = (ImageView) findViewById(R.id.play_icon);
		definitionIcon = (ImageView) findViewById(R.id.icon_defination);

		// MarginLayoutParams mlp = (MarginLayoutParams)
		// gallery1.getLayoutParams();
		DisplayMetrics metrics = new DisplayMetrics();
		density = metrics.density;
		Display display = getWindowManager().getDefaultDisplay();
		displayWith = display.getWidth();
		// Toast.makeText(this, "widthPixels = " + display.get, 100).show();
		// Toast.makeText(this, "topMargin = " + mlp.topMargin, 100).show();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// mlp.setMargins(-displayWith+displayWith/2,
		// mlp.topMargin,
		// mlp.rightMargin,
		// mlp.bottomMargin
		// );
		gallery1.setAdapter(new MainHotItemAdapter(Main1.this, hot_list));
		// gallery1.setCallbackDuringFling(false);
		gallery1.setOnItemSelectedListener(this);
		gallery1.setOnItemClickListener(this);
		gallery1.setSelection(1);

		MarginLayoutParams mlp2 = (MarginLayoutParams) titleGroup
				.getLayoutParams();
		mlp2.setMargins((displayWith - 40) / 6 + 21, mlp2.topMargin,
				mlp2.rightMargin, mlp2.bottomMargin);
		MarginLayoutParams mlp3 = (MarginLayoutParams) noticeView
				.getLayoutParams();
		mlp3.setMargins((displayWith - 40) / 6 + 21, mlp3.topMargin,
				mlp3.rightMargin, mlp3.bottomMargin);
		// MarginLayoutParams mlp4 = (MarginLayoutParams)
		// contentLayout.getLayoutParams();
		// mlp4.setMargins((displayWith-40)/6+15,
		// mlp4.topMargin,
		// mlp4.rightMargin,
		// mlp4.bottomMargin);
		LayoutParams param = itemFram.getLayoutParams();
		param.height = 2 * displayWith / 9 + 3;
		param.width = displayWith / 6 + 3;
		itemFram.setVisibility(View.INVISIBLE);
		alpha_appear = AnimationUtils.loadAnimation(this, R.anim.alpha_appear);
		alpha_disappear = AnimationUtils.loadAnimation(this,
				R.anim.alpha_disappear);

		headers = new HashMap<String, String>();
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			headers.put("version", pInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (isNetWorkFine) {// 如果网络正常就执行

			initNetWorkData();
		}

		// 友盟自动升级
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setOnDownloadListener(null);
		UmengUpdateAgent.update(this);

	}
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch (msg.what) {
			case MESSAGE_UPDATEUSER:// userInfo相关验证完成
				if (initStep == 0) {
					initStep += 1;
					getHotServiceData();
						
					myView.setVisibility(View.INVISIBLE);
				}
				Log.d(TAG, "MESSAGE_UPDATEUSER --- >Initstep = " + initStep);
				aq.id(R.id.iv_head_user_icon).image(
						app.getUserInfo().getUserAvatarUrl(), false, true, 0,
						R.drawable.avatar_defult);
				aq.id(R.id.tv_head_user_name).text(
						app.getUserInfo().getUserName());
				
				Log.i(TAG, "getHistoryServiceData-->MESSAGE_UPDATEUSER");
				getHistoryServiceData();
				break;
			case MESSAGE_STEP1_SUCESS:// 热播列表加载完成
				Log.d(TAG, "MESSAGE_STEP1_SUCESS --- >Initstep = " + initStep);
				if (initStep == 1) {
					initStep += 1;
					getMovieYueDanServiceData();
					getTVYueDanServiceData();
				}
				break;// 悦单加载完成
			case MESSAGE_STEP2_SUCESS:// 悦单加载完成
				Log.d(TAG, "MESSAGE_STEP2_SUCESS --- >Initstep = " + initStep);
				if (initStep == 2) {
					initStep += 1;
					if (startingImageView.getVisibility() == View.VISIBLE) {
						startingImageView.setVisibility(View.GONE);
						
//						if(!isShowAd) {
//							
//							handler.postDelayed(new Runnable() {
//								
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									
//									if(!startingImageView.isShown()) {
//										UtilTools.recycleBitmap(((BitmapDrawable)startingImageView.getDrawable()).getBitmap());
//									}
//								}
//							}, 1000);
//						}
						startingImageView.startAnimation(alpha_disappear);
						rootLayout.setVisibility(View.VISIBLE);
						gallery1.requestFocus();
						handler.removeMessages(MESSAGE_START_TIMEOUT);
//						new Thread(new CheckPlayUrl()).start();
						Log.i(TAG,"removeDialog(DIALOG_WAITING);---else ---->1");
						
//						if(isShowAd) {
							
							removeDialog(DIALOG_WAITING);
//						}
					} else {
						Log.i(TAG,"removeDialog(DIALOG_WAITING);---else ---->2");
						removeDialog(DIALOG_WAITING);
						contentLayout.setVisibility(View.VISIBLE);
						gallery1.requestFocus();
//						new Thread(new CheckPlayUrl()).start();
					}

					handler.removeMessages(MESSAGE_30S_TIMEOUT);
				}

				// 当悦单加载完成时，开始下载用户收藏数据，并插入到数据库
				getShouCangData(URLUtils.getShoucangURL(app
						.getUserInfo().getUserId()));
				
				/*
				 * adkey show,the Viewo of ad init()
				 */
//				if(UtilTools.getIsShowAd(getApplicationContext())) {
					
					if (mAdView != null) {
						removeBanner();
					}
					
					mAdView = new AdView(Main1.this, Constant.MAIN_ADV_PUBLISHERID,false);
					mAdView.setAdListener(Main1.this);
					layout.addView(mAdView);
					
					//弹出免责声明
					if(!UtilTools.getDisclaimerVisible(getApplicationContext())){
						
						final Dialog dialog = new AlertDialog.Builder(Main1.this).create();
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();
						LayoutInflater inflater = LayoutInflater.from(Main1.this);
						View view = inflater.inflate(R.layout.mianze_dialog, null);
						Button buttonYes = (Button) view.findViewById(R.id.btnyes);
						buttonYes.setOnClickListener(new Button.OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								// 将内容保存在sharedPreference
								UtilTools.setDisclaimerVisible(getApplicationContext(), true);
							}
						});
						dialog.setContentView(view);
					}
				
				break;
			case MESSAGE_START_TIMEOUT:// 超时还未加载好
				if (initStep < 3) {
					startingImageView.setVisibility(View.GONE);
					
//					if(!isShowAd) {
//						
//						handler.postDelayed(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								
//								if(!startingImageView.isShown()) {
//									UtilTools.recycleBitmap(((BitmapDrawable)startingImageView.getDrawable()).getBitmap());
//								}
//							}
//						}, 1000);
//					}
					
					contentLayout.setVisibility(View.INVISIBLE);
					
					if(!UtilTools.getIsShowAd(getApplicationContext())) {
						
						showDialog(DIALOG_WAITING);
					}
				}

				break;
			case MESSAGE_UPDATEUSER_HISTORY_SUCEESS:// 超时还未加载好
				// if(initStep<3){
				// startingImageView.setVisibility(View.GONE);
				// contentLayout.setVisibility(View.INVISIBLE);
				// showDialog(DIALOG_WAITING);
				// }
				break;
			case MESSAGE_30S_TIMEOUT:// 超过30S时间，弹出网络速度慢dialog
				removeDialog(DIALOG_WAITING);
				showDialog(DIALOG_NETWORK_SLOW);
				handler.removeCallbacksAndMessages(null);
				initStep = -1;
				break;
			default:
				break;
			}

		}

	};
	private void initOverTime(View view,String duration) {
		
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_over_time_main);

		TextView tv = (TextView) view.findViewById(R.id.tv_over_time);

		String overTime = UtilTools.movieOverTime(duration);
		// String overTime = UtilTools.movieOverTime("300分钟");

		if (overTime != null && !overTime.equals("")) {

			int index = overTime.indexOf(":");

			if (index != -1) {

				tv.setText(overTime);
				ll.setVisibility(View.VISIBLE);
				
				return;
			}
		}
		
		ll.setVisibility(View.GONE);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(UtilTools.ACTION_PLAY_END_MAIN.equals(action)){
				Log.d(TAG, "receiver---->"+action);
				ReturnProgramView date = app.get_ReturnProgramView();
				if(date==null){
					return;
				}
				hot_list.clear();
				HotItemInfo item = new HotItemInfo();
				item.type = 0;
//				item.id = result.histories[0].id;
				item.prod_id = intent.getStringExtra("prod_id");
				item.prod_type = intent.getIntExtra("prod_type",-1) + "";
				if("-1".equals(item.prod_type)){
					return ;
				}
				switch (Integer.valueOf(item.prod_type)) {
				case 1:
					String bigPicUrl = date.movie.ipad_poster;
					if (bigPicUrl == null || bigPicUrl.equals("")
							|| bigPicUrl.equals(UtilTools.EMPTY)) {

						bigPicUrl = date.movie.poster;
					}
					item.prod_name = date.movie.name;
					item.prod_pic_url = bigPicUrl;
					item.stars = date.movie.stars;
					item.directors = date.movie.directors;
					item.favority_num = date.movie.favority_num;
					item.support_num = date.movie.support_num;
					item.publish_date = date.movie.publish_date;
					item.score = date.movie.score;
					item.area = date.movie.area;
//					item.cur_episode = date.movie.
					item.definition = date.movie.definition;
					item.prod_summary = date.movie.summary;
//					item.prod_subname = result.histories[0].prod_subname;
					item.duration = date.movie.duration;
					item.playback_time = intent.getLongExtra("time", 0)+"";
//					item.video_url = result.histories[0].video_url;
					break;
				case 2:
				case 131:
					String bigPicUrl1 = date.tv.ipad_poster;
					if (bigPicUrl1 == null || bigPicUrl1.equals("")
							|| bigPicUrl1.equals(UtilTools.EMPTY)) {

						bigPicUrl1 = date.tv.poster;
					}
					item.prod_name = date.tv.name;
					item.prod_pic_url = bigPicUrl1;
					item.stars = date.tv.stars;
					item.directors = date.tv.directors;
					item.favority_num = date.tv.favority_num;
					item.support_num = date.tv.support_num;
					item.publish_date = date.tv.publish_date;
					item.score = date.tv.score;
					item.area = date.tv.area;
					item.cur_episode = date.tv.cur_episode;
					item.definition = date.tv.definition;
					item.prod_summary = date.tv.summary;
					item.prod_subname = intent.getStringExtra("prod_sub_name");
//					item.duration = date.tv.d;
					item.playback_time = intent.getLongExtra("time", 0)+"";
//					item.video_url = result.histories[0].video_url;
					break;
				case 3:
					String bigPicUrl3 = date.show.ipad_poster;
					if (bigPicUrl3 == null || bigPicUrl3.equals("")
							|| bigPicUrl3.equals(UtilTools.EMPTY)) {

						bigPicUrl3 = date.show.poster;
					}
					item.prod_pic_url = bigPicUrl3;
					item.prod_name = date.show.name;
					item.stars = date.show.stars;
					item.directors = date.show.directors;
					item.favority_num = date.show.favority_num;
					item.support_num = date.show.support_num;
					item.publish_date = date.show.publish_date;
					item.score = date.show.score;
					item.area = date.show.area;
					item.cur_episode = date.show.cur_episode;
					item.definition = date.show.definition;
					item.prod_summary = date.show.summary;
					item.prod_subname = intent.getStringExtra("prod_sub_name");
//					item.duration = date.tv.d;
					item.playback_time = intent.getLongExtra("time", 0)+"";
//					item.video_url = result.histories[0].video_url;
				}
//				item.prod_name = date.
				// item.prod_pic_url = result.histories[0].big_prod_pic_url;
				
				hot_list.add(item);
				for(int i=0; i<netWorkHotList.size(); i++){
					hot_list.add(netWorkHotList.get(i));
				}
				//去重
				updateHotDate();
				updateHotContentLayoue();
				if(titleGroup.getSelectedTitleIndex()==1){
					gallery1.setAdapter(new MainHotItemAdapter(Main1.this, hot_list));
					gallery1.setSelection(0);
				}else{
					indexCaces.put(1, 0);
				}
			}
		}

	};
	
	private boolean updateHotDate(){
		boolean flag = false;
		HotItemInfo firstInfo = hot_list.get(0);
		if(firstInfo!=null&&firstInfo.type==0){
			for(int i=1; i<hot_list.size(); i++){
				if(firstInfo.prod_id.equals(hot_list.get(i).prod_id)){
					hot_list.remove(i);
					flag = true;
				}
			}
		}
		return flag;
	}
	
	private void updateHotContentLayoue(){
		hot_contentViews.clear();
		for(int i=0; i<hot_list.size(); i++){
			HotItemInfo item = hot_list.get(i);
			View hotView = LayoutInflater.from(Main1.this).inflate(
					R.layout.layout_hot, null);
			TextView hot_name_tv = (TextView) hotView
					.findViewById(R.id.hot_content_name);
			TextView hot_score_tv = (TextView) hotView
					.findViewById(R.id.hot_content_score);
			TextView hot_directors_tv = (TextView) hotView
					.findViewById(R.id.hot_content_directors);
			TextView hot_starts_tv = (TextView) hotView
					.findViewById(R.id.hot_content_stars);
			TextView hot_introduce_tv = (TextView) hotView
					.findViewById(R.id.hot_content_introduce);
			ImageView icon_douban = (ImageView) hotView
					.findViewById(R.id.icon_douban);
			if ("3".equals(item.prod_type.trim())) {
				TextView hot_title_director = (TextView) hotView
						.findViewById(R.id.title_directors);
				TextView hot_title_stars = (TextView) hotView
						.findViewById(R.id.title_stars);
				hot_title_director
						.setText(R.string.xiangqing_zongyi_zhuchi);
				hot_title_stars.setText(R.string.xiangqing_zongyi_shoubo);
				hot_starts_tv.setText(item.directors);
				hot_directors_tv.setText(item.stars);
				icon_douban.setVisibility(View.INVISIBLE);
			} else {
				hot_directors_tv.setText(item.directors);
				hot_starts_tv.setText(item.stars);
			}
			Log.d(TAG, item.prod_name);
			hot_name_tv.setText(item.prod_name);
			hot_score_tv.setText(UtilTools.formateScore(item.score));
			hot_introduce_tv.setText(item.prod_summary);
			hot_contentViews.put(i, hotView);
		}
	}

	// 数据初始化

	private void initNetWorkData() {
		
			
		initAppkeyAndBaseurl(null);
		
		
	}
	
	private void initAppkeyAndBaseurl(ReturnLogInfo returnLogInfo) {
		
		TextView personal_recordTv = (TextView) findViewById(R.id.tv_personal_record);
		TextView playStoreTv = (TextView) findViewById(R.id.tv_play_store);
		ImageView logoIv = (ImageView) findViewById(R.id.iv_head_logo);
		
		logoIv.setImageResource(R.drawable.logo_custom);
		headers.put("app_key", Constant.APPKEY);
		headers.put("client", "tv");
		app.setHeaders(headers);

		
		checkLogin();
		// getHotServiceData();
		handler.sendEmptyMessageDelayed(MESSAGE_START_TIMEOUT, LOADING_PIC_TIME);
		handler.sendEmptyMessageDelayed(MESSAGE_30S_TIMEOUT, LOADING_TIME_OUT);// 图片撤掉20S后
	}

	@Override
	public void onResume() {
		super.onResume();

		MobclickAgent.onResume(this);

		if (app.getUserInfo() != null) {
			aq.id(R.id.iv_head_user_icon).image(
					app.getUserInfo().getUserAvatarUrl(), false, true, 0,
					R.drawable.avatar_defult);
			aq.id(R.id.tv_head_user_name).text(app.getUserInfo().getUserName());
		}

		if (!isNetWorkFine && isWifiReset) {// 如果之前网络不正常并且重新设置过wifi

			if (app.isNetworkAvailable()) {

				initNetWorkData();

				isNetWorkFine = true;
				isWifiReset = false;
			} else {

				showDialog(DIALOG_NETWORK_ERROR);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);

	}

	@Override
	protected void onDestroy() {
		if (aq != null)
			aq.dismiss();
		
		if(!UtilTools.getIsShowAd(getApplicationContext()) && startingImageView.getDrawable() != null) {
			
			UtilTools.recycleBitmap(((BitmapDrawable)startingImageView.getDrawable()).getBitmap());

		}
		
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	public void ReadLocalAppKey() {
		// online 获取APPKEY
		MobclickAgent.updateOnlineConfig(this);
		String OnLine_Appkey = MobclickAgent.getConfigParams(this, "APPKEY");
		if (OnLine_Appkey != null && OnLine_Appkey.length() > 0) {
			Constant.APPKEY = OnLine_Appkey;
			headers.remove("app_key");
			headers.put("app_key", OnLine_Appkey);
			app.setHeaders(headers);
		}
	}

	public boolean checkLogin() {
		Log.i(TAG, "checkLogin--->");
		String usr_id = null;
		usr_id = app.getUserData("userId");
		if (usr_id == null) {
			String macAddress = null;
			WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr
					.getConnectionInfo());
			if (info != null) {
				macAddress = info.getMacAddress();
			}
			
			Log.i(TAG, "checkLogin--->" + macAddress);
			// 2. 通过调用 service account/generateUIID把UUID传递到服务器
			String url = Constant.BASE_URL + "account/generateUIID";

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uiid", macAddress);
			params.put("device_type", "Android");

			AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
			cb.header("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
			cb.header("app_key", Constant.APPKEY);

			cb.params(params).url(url).type(JSONObject.class)
					.weakHandler(this, "CallServiceResult");
			aq.ajax(cb);
		} else {
			UserInfo currentUserInfo = new UserInfo();
			currentUserInfo.setUserId(app.getUserData("userId"));
			currentUserInfo.setUserName(app.getUserData("userName"));
			currentUserInfo.setUserAvatarUrl(app.getUserData("userAvatarUrl"));
			headers.put("user_id", currentUserInfo.getUserId());
			app.setUser(currentUserInfo);
			 handler.sendEmptyMessage(MESSAGE_UPDATEUSER);

		}
		return false;
	}

	public void CallServiceResult(String url, JSONObject json, AjaxStatus status) {

		if (json != null) {

			if (json == null || json.equals(""))
				return;

			Log.d(TAG, "CallServiceResult" + json.toString());
			try {
				UserInfo currentUserInfo = new UserInfo();
				if (json.has("user_id")) {
					currentUserInfo.setUserId(json.getString("user_id").trim());
				} else if (json.has("id")) {
					currentUserInfo.setUserId(json.getString("id").trim());
				}

				if (json.has("user_id") || json.has("id")) {

					currentUserInfo.setUserName(json.getString("nickname"));
					currentUserInfo.setUserAvatarUrl(json.getString("pic_url"));
					app.SaveUserData("userId", currentUserInfo.getUserId());
					app.SaveUserData("userName", json.getString("nickname"));
					app.SaveUserData("userAvatarUrl", json.getString("pic_url"));
					app.setUser(currentUserInfo);
					headers.put("user_id", currentUserInfo.getUserId());
					 handler.sendEmptyMessage(MESSAGE_UPDATEUSER);
				}

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {
		final int positon1 = gallery1.getSelectedItemPosition();
		if (leftTranslateAnimationStep2 == null) {
			leftTranslateAnimationStep2 = new TranslateAnimation(
					itemFram.getLeft() + itemFram.getWidth() / 5
							- itemFram.getWidth(), itemFram.getLeft()
							- itemFram.getWidth(), 0, 0);
			leftTranslateAnimationStep2.setDuration(250);
			leftTranslateAnimationStep2
					.setInterpolator(new AccelerateInterpolator(0.1f));
		}
		if (rightTranslateAnimationStep2 == null) {
			rightTranslateAnimationStep2 = new TranslateAnimation(
					itemFram.getLeft() - itemFram.getWidth() / 4
							- itemFram.getWidth(), itemFram.getLeft()
							- itemFram.getWidth(), 0, 0);
			rightTranslateAnimationStep2.setDuration(250);
			rightTranslateAnimationStep2
					.setInterpolator(new AccelerateInterpolator(0.1f));
		}
		switch (titleGroup.getSelectedTitleIndex()) {
		case 1:
			if (arg2 < hot_list.size() && arg2 >= 0) {
				if (arg2 > 1) {
					aq.id(R.id.icon_arrow_left).visible();
				} else {
					aq.id(R.id.icon_arrow_left).invisible();
				}
				if (hot_list.size() - arg2 > 5) {
					aq.id(R.id.icon_arrow_right).visible();
				} else {
					aq.id(R.id.icon_arrow_right).invisible();
				}
				switch (Integer.valueOf(hot_list.get(arg2).definition)) {
				case 8:
					definitionIcon.setImageResource(R.drawable.icon_bd);
					break;
				case 7:
					definitionIcon.setImageResource(R.drawable.icon_hd);
					break;
				case 6:
					definitionIcon.setImageResource(R.drawable.icon_ts);
					break;
				default:
					definitionIcon.setImageDrawable(null);
					break;
				}

				ImageView img = (ImageView) gallery1.findViewWithTag(hot_list
						.get(arg2).prod_pic_url);
				if (img != null) {
					if (img.getDrawable() != null) {
						highlightImageView_1
								.setImageDrawable(img.getDrawable());
					} else {
						highlightImageView_1.setImageDrawable(null);
						aq.id(highlightImageView_1).image(
								hot_list.get(arg2).prod_pic_url, true, true);
					}
				} else {
					aq.id(highlightImageView_1).image(
							hot_list.get(arg2).prod_pic_url, true, true);
				}
				noticeView.setText(arg2 + 1 + "/" + hot_list.size());
				handler.removeCallbacks(null, null);
				handler.postDelayed((new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d(TAG, "index = " + arg2 + "lengh = "
								+ hot_contentViews.size());
						if (titleGroup.getSelectedTitleIndex() != 1) {
							return;
						}
						contentLayout.removeAllViews();
						View hotView = hot_contentViews.get(arg2);
						hotView.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT));
						if (contentLayout.getAnimation() != null
								&& !contentLayout.getAnimation().hasEnded()) {

						} else {
							contentLayout.startAnimation(alpha_appear);
						}
						
						if(hot_list != null && hot_list.get(arg2)!= null) {
							
							HotItemInfo tempInfo = hot_list.get(arg2);
							
							if(tempInfo.type == 1 && tempInfo.prod_type.equals("1")) {
								
								initOverTime(hotView, tempInfo.duration);
							}
						}
						
						contentLayout.addView(hotView);
						hotView.setVisibility(View.VISIBLE);
					}
				}), 300);
			}
			break;
		case 2:
			if (arg2 < yuedan_list.size() && arg2 >= 0) {
				if (arg2 > 1) {
					aq.id(R.id.icon_arrow_left).visible();
				} else {
					aq.id(R.id.icon_arrow_left).invisible();
				}
				if (yuedan_list.size() - arg2 > 5) {
					aq.id(R.id.icon_arrow_right).visible();
				} else {
					aq.id(R.id.icon_arrow_right).invisible();
				}
				if ("0".equals(yuedan_list.get(arg2).prod_type)) {
					definitionIcon.setImageDrawable(null);
					if ("-1".equals(yuedan_list.get(arg2).id)) {
						highlightImageView_2
								.setImageResource(R.drawable.more_movie_active);
					} else if ("-2".equals(yuedan_list.get(arg2).id)) {
						highlightImageView_2
								.setImageResource(R.drawable.more_episode_active);
					}
					noticeView.setText(arg2 + 1 + "/" + yuedan_list.size());
					handler.removeCallbacks(null, null);
					handler.postDelayed((new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (titleGroup.getSelectedTitleIndex() != 2) {
								return;
							}
							contentLayout.removeAllViews();
							if (arg2 < yuedan_contentViews.size()) {
								View yeuDanView = yuedan_contentViews.get(arg2);
								yeuDanView.setLayoutParams(new LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.MATCH_PARENT));
								if (contentLayout.getAnimation() != null
										&& !contentLayout.getAnimation()
												.hasEnded()) {

								} else {
									contentLayout.startAnimation(alpha_appear);
								}
								contentLayout.addView(yeuDanView);
							}
						}
					}), 300);
				} else {
					if (gallery1.getSelectedItemPosition() == yuedan_list
							.size() - 2) {
						highlightImageView_2
								.setImageResource(R.drawable.more_movie_active);
					} else if (gallery1.getSelectedItemPosition() == yuedan_list
							.size() - 1) {
						highlightImageView_2
								.setImageResource(R.drawable.more_episode_active);
					} else {
						if ("1".equals(yuedan_list.get(arg2).prod_type)) {
							definitionIcon
									.setImageResource(R.drawable.icon_movie);
						} else if ("2".equals(yuedan_list.get(arg2).prod_type)) {
							definitionIcon
									.setImageResource(R.drawable.icon_episode);
						}
						ImageView img2 = (ImageView) gallery1
								.findViewWithTag(yuedan_list.get(gallery1
										.getSelectedItemPosition()).pic_url);
						if (img2 != null) {
							if (img2.getDrawable() != null) {
								highlightImageView_2.setImageDrawable(img2
										.getDrawable());
							} else {
								highlightImageView_2.setImageDrawable(null);
								aq.id(highlightImageView_2)
										.image(yuedan_list.get(gallery1
												.getSelectedItemPosition()).pic_url,
												true, true);
							}
						} else {
							aq.id(highlightImageView_2)
									.image(yuedan_list.get(gallery1
											.getSelectedItemPosition()).pic_url,
											true, true);
						}
					}
					noticeView.setText(arg2 + 1 + "/" + yuedan_list.size());
					handler.removeCallbacks(null, null);
					handler.postDelayed((new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (titleGroup.getSelectedTitleIndex() != 2) {
								return;
							}
							contentLayout.removeAllViews();
							if (arg2 < yuedan_contentViews.size()) {
								View yeuDanView = yuedan_contentViews.get(arg2);
								yeuDanView.setLayoutParams(new LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.MATCH_PARENT));
								if (contentLayout.getAnimation() != null
										&& !contentLayout.getAnimation()
												.hasEnded()) {

								} else {
									contentLayout.startAnimation(alpha_appear);
								}
								contentLayout.addView(yeuDanView);
							}
						}
					}), 300);
				}
			}

			break;
		case 3:
			if (positon1 < resouces_lib_active.length) {
				if (arg2 > 1) {
					aq.id(R.id.icon_arrow_left).visible();
				} else {
					aq.id(R.id.icon_arrow_left).invisible();
				}
				if (resouces_lib_active.length - arg2 > 5) {
					aq.id(R.id.icon_arrow_right).visible();
				} else {
					aq.id(R.id.icon_arrow_right).invisible();
				}
				highlightImageView_3
						.setImageResource(resouces_lib_active[positon1]);
				noticeView.setText(positon1 + 1 + "/"
						+ resouces_lib_active.length);
			}
			break;
		case 4:
			if (positon1 < resouces_my_active.length) {
				if (arg2 > 1) {
					aq.id(R.id.icon_arrow_left).visible();
				} else {
					aq.id(R.id.icon_arrow_left).invisible();
				}
				if (resouces_my_active.length - arg2 > 5) {
					aq.id(R.id.icon_arrow_right).visible();
				} else {
					aq.id(R.id.icon_arrow_right).invisible();
				}
				highlightImageView_4
						.setImageResource(resouces_my_active[positon1]);
				noticeView.setText(positon1 + 1 + "/"
						+ resouces_my_active.length);
			}
			break;

		}
		if (indexCaces.get(titleGroup.getSelectedTitleIndex()) != null
				&& indexCaces.get(titleGroup.getSelectedTitleIndex()) < arg2) {
			itemFram.startAnimation(leftTranslateAnimationStep2);
		}
		if (indexCaces.get(titleGroup.getSelectedTitleIndex()) != null
				&& indexCaces.get(titleGroup.getSelectedTitleIndex()) > arg2) {
			itemFram.startAnimation(rightTranslateAnimationStep2);
		}
		indexCaces.put(titleGroup.getSelectedTitleIndex(), arg2);
		// TODO Auto-generated method stub
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "key code = " + keyCode, 100).show();
		Log.d(TAG, "code = " + keyCode);
		if (initStep < 3) {
			return true;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			titleGroup.selectPreTitle();
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			titleGroup.selectNextTitle();
			return true;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			// final int positon1 = gallery1.getSelectedItemPosition();
			// if(leftTranslateAnimationStep2 == null){
			// leftTranslateAnimationStep2 = new
			// TranslateAnimation(itemFram.getLeft()+itemFram.getWidth()/5-itemFram.getWidth(),
			// itemFram.getLeft()-itemFram.getWidth(),
			// 0,
			// 0);
			// leftTranslateAnimationStep2.setDuration(250);
			// leftTranslateAnimationStep2.setInterpolator(new
			// AccelerateInterpolator(0.1f));
			// }
			// switch (titleGroup.getSelectedTitleIndex()) {
			// case 1:
			// if(positon1<hot_list.size()-1){
			// //
			// aq.id(highlightImageView).image(hot_list.get(positon1+1).prod_pic_url,true,true);
			// // itemFram.startAnimation(leftTranslateAnimationStep2);
			// // noticeView.setText(positon1+2 + "/" + hot_list.size());
			// changeContent(1);
			// }
			// break;
			// case 2:
			// if(positon1<resouces_lib_active.length-1){
			// //
			// highlightImageView.setImageResource(resouces_lib_active[positon1+1]);
			// // itemFram.startAnimation(leftTranslateAnimationStep2);
			// // noticeView.setText(positon1+2 + "/" +
			// resouces_lib_active.length);
			// changeContent(1);
			// }
			// break;
			// case 3:
			// if(positon1<resouces_lib_active.length-1){
			// highlightImageView.setImageResource(resouces_lib_active[positon1+1]);
			// itemFram.startAnimation(leftTranslateAnimationStep2);
			// noticeView.setText(positon1+2 + "/" +
			// resouces_lib_active.length);
			// }
			// break;
			// case 4:
			// if(positon1<resouces_my_active.length-1){
			// highlightImageView.setImageResource(resouces_my_active[positon1+1]);
			// itemFram.startAnimation(leftTranslateAnimationStep2);
			// noticeView.setText(positon1+2 + "/" + resouces_my_active.length);
			// }
			// break;
			//
			// }
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			//
			//
			// final int positon2 = gallery1.getSelectedItemPosition();
			// if(rightTranslateAnimationStep2 == null){
			// rightTranslateAnimationStep2 = new
			// TranslateAnimation(itemFram.getLeft()-itemFram.getWidth()/4-itemFram.getWidth(),
			// itemFram.getLeft()-itemFram.getWidth(),
			// 0,
			// 0);
			// rightTranslateAnimationStep2.setDuration(250);
			// rightTranslateAnimationStep2.setInterpolator(new
			// AccelerateInterpolator(0.1f));
			// }
			//
			//
			// switch (titleGroup.getSelectedTitleIndex()) {
			// case 1:
			// // if(positon2>0){
			// // highlightImageView.setImageResource(resouces[positon2]);
			// // itemFram.startAnimation(rightTranslateAnimationStep2);
			// // noticeView.setText(positon2 + "/" + resouces.length);
			// // }
			// if(positon2>0){
			// //
			// aq.id(highlightImageView).image(hot_list.get(positon2-1).prod_pic_url,true,true);
			// // itemFram.startAnimation(rightTranslateAnimationStep2);
			// // noticeView.setText(positon2 + "/" + hot_list.size());
			// changeContent(-1);
			// }
			// break;
			// case 2:
			// if(positon2>0){
			// //
			// highlightImageView.setImageResource(resouces_lib_active[positon2-1]);
			// // itemFram.startAnimation(leftTranslateAnimationStep2);
			// // noticeView.setText(positon2 + "/" +
			// resouces_lib_active.length);
			// changeContent(-1);
			// }
			// break;
			// case 3:
			// if(positon2>0){
			// highlightImageView.setImageResource(resouces_lib_active[positon2-1]);
			// itemFram.startAnimation(leftTranslateAnimationStep2);
			// noticeView.setText(positon2 + "/" + resouces_lib_active.length);
			// }
			// break;
			// case 4:
			// if(positon2>0){
			// highlightImageView.setImageResource(resouces_my_active[positon2-1]);
			// itemFram.startAnimation(leftTranslateAnimationStep2);
			// noticeView.setText(positon2 + "/" + resouces_my_active.length);
			// }
			// break;
			//
			// }
			//
			return true;
		case KeyEvent.KEYCODE_BACK:
			if ((System.currentTimeMillis() - exitTime) > 2000) {
//				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

				Toast toast = new Toast(this);
				View v = getLayoutInflater().inflate(R.layout.toast_textview, null);
				toast.setView(v);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "item click index = " +
		// titleGroup.getSelectedTitleIndex()+"[" + index + "]", 100).show();
		switch (titleGroup.getSelectedTitleIndex()) {
		case 1:
			HotItemInfo info = hot_list.get(index);
			Intent intent;
			if (info.type == 0) {
				// 历史
				CurrentPlayDetailData playDate = new CurrentPlayDetailData();
				intent = new Intent(this, VideoPlayerJPActivity.class);
				playDate.prod_id = info.prod_id;
				playDate.prod_type = Integer.valueOf(info.prod_type);
				playDate.prod_name = info.prod_name;
				playDate.prod_url = info.video_url;

				// 清晰度
				playDate.prod_qua = UtilTools.string2Int(info.definition);

				Log.d(TAG, "url" + playDate.prod_url);
				playDate.prod_src = info.source;
				if (!"".equals(info.playback_time)) {
					playDate.prod_time = Long.valueOf(info.playback_time) * 1000;
				}
				playDate.prod_sub_name = info.prod_subname;
//				if(playDate.prod_type!=1){
//					
//					if(playDate.prod_type == 3) {
//						
//						playDate.CurrentIndex = - 1;
//					} else {
//						
////						String  currentIndex = ((HistortyAdapter)listView.getAdapter()).data.get(arg2).prod_subname;
//						if(currentIndex!=null&&!"".equals(currentIndex)){
//							int current = Integer.valueOf(currentIndex);
//							if(current>0){
//								current = current-1;
//							}
//							playDate.CurrentIndex = current;
//						}
//					}
//					
//				}
				// playDate.prod_qua = Integer.valueOf(info.definition);
				app.setmCurrentPlayDetailData(playDate);
				app.set_ReturnProgramView(null);
				startActivity(intent);
			} else if (info.type == 1) {
				int prod_type = Integer.valueOf(info.prod_type);
				switch (prod_type) {
				case 1:
					Log.d(TAG, "name---->" + info.prod_name);
					CurrentPlayDetailData playDate = new CurrentPlayDetailData();
					intent = new Intent(this, VideoPlayerJPActivity.class);
					playDate.prod_id = info.prod_id;
					playDate.prod_type = Integer.valueOf(info.prod_type);
					playDate.prod_name = info.prod_name;
//					playDate.prod_url = info.video_url;
					playDate.prod_src = info.source;
					// 清晰度
					playDate.prod_qua = UtilTools
							.string2Int(info.definition);

					if (!"".equals(info.playback_time)) {
						playDate.prod_time = Long.valueOf(info.playback_time);
					}
					// playDate.prod_qua = Integer.valueOf(info.definition);
					app.setmCurrentPlayDetailData(playDate);
					app.set_ReturnProgramView(null);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(this, ShowXiangqingTv.class);
					intent.putExtra("ID", info.prod_id);
					intent.putExtra("prod_name", info.prod_name);
					intent.putExtra("prod_url", info.prod_pic_url);
					intent.putExtra("directors", info.directors);
					intent.putExtra("stars", info.stars);
					intent.putExtra("summary", info.prod_summary);
					intent.putExtra("support_num", info.support_num);
					intent.putExtra("favority_num", info.favority_num);
					intent.putExtra("definition", info.definition);
					intent.putExtra("score", info.score);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(this, ShowXiangqingZongYi.class);
					intent.putExtra("ID", info.prod_id);
					intent.putExtra("prod_name", info.prod_name);
					intent.putExtra("prod_url", info.prod_pic_url);
					intent.putExtra("directors", info.directors);
					intent.putExtra("stars", info.stars);
					intent.putExtra("summary", info.prod_summary);
					intent.putExtra("support_num", info.support_num);
					intent.putExtra("favority_num", info.favority_num);
					intent.putExtra("definition", info.definition);
					intent.putExtra("score", info.score);
					startActivity(intent);
					break;
				case 131:
					intent = new Intent(this, ShowXiangqingDongman.class);
					intent.putExtra("ID", info.prod_id);
					intent.putExtra("prod_name", info.prod_name);
					intent.putExtra("prod_url", info.prod_pic_url);
					intent.putExtra("directors", info.directors);
					intent.putExtra("stars", info.stars);
					intent.putExtra("summary", info.prod_summary);
					intent.putExtra("support_num", info.support_num);
					intent.putExtra("favority_num", info.favority_num);
					intent.putExtra("definition", info.definition);
					intent.putExtra("score", info.score);
					startActivity(intent);
					break;
				}
			}
			break;
		case 2:
			Intent yuedanIntent = new Intent();
			YueDanInfo yueDan = yuedan_list.get(index);
			if (Integer.valueOf(yueDan.prod_type) == 0) {
				yuedanIntent.setClass(Main1.this, ShowYueDanActivity.class);
				if ("-1".equals(yueDan.id)) {
					yuedanIntent.putExtra("yuedan_type", "1");
				} else if ("-2".equals(yueDan.id)) {
					yuedanIntent.putExtra("yuedan_type", "2");
				}
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("ID", yuedan_list.get(index).id);
				bundle.putString("NAME", yuedan_list.get(index).name);
				yuedanIntent.putExtras(bundle);
				// yuedanIntent.putParcelableArrayListExtra("yuedan_list_type",
				// yuedan_list.get(index).shiPinList);
				yuedanIntent.setClass(Main1.this, ShowYueDanListActivity.class);
			}
			startActivity(yuedanIntent);
			break;
		case 3:
			switch (index) {
			case 0:
				startActivity(new Intent(this, ShowMovieActivity.class));
				break;
			case 1:
				startActivity(new Intent(this, ShowTVActivity.class));
				break;
			case 2:
				startActivity(new Intent(this, ShowDongManActivity.class));
				break;
			case 3:
				startActivity(new Intent(this, ShowZongYiActivity.class));
				break;
			case 4:
				startActivity(new Intent(this, ShowSearchActivity.class));
				break;
			}
			break;
		case 4:
			switch (index) {
			case 0:
				Intent scIntent = new Intent(this,
						ShowShoucangHistoryActivity.class);
				startActivityForResult(scIntent, 100);
				// startActivity(new
				// Intent(this,ShowShoucangHistoryActivity.class));
				break;
			case 1:
				Intent lsIntent = new Intent(this, HistoryActivity.class);
				startActivityForResult(lsIntent, 100);
				// startActivity(new Intent(this,HistoryActivity.class));
				break;
			case 2:
				// 设置
				startActivity(new Intent(this, SettingActivity.class));
				break;
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "result code =" + resultCode);
		if (resultCode == RESULT_OK) {
			titleGroup.setSelectedTitleIndex(1);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void getHotServiceData() {
		String url = Constant.BASE_URL + "tv_net_top"
				+ "?page_num=1&page_size=1000";
		// String url =
		// "http://api.joyplus.tv/joyplus-service/index.php/tv_net_top?page_num=1&page_size=1000&app_key=ijoyplus_android_0001";

		// String url = Constant.BASE_URL;
		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, "initHotData");

		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}

	public void getHistoryServiceData() {
		String url = Constant.BASE_URL + "user/playHistories"
				+ "?page_num=1&page_size=1";

		// String url = Constant.BASE_URL;
		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, "initHistoryData");

		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}

	public void getMovieYueDanServiceData() {
		String url = Constant.BASE_URL + "tops"
				+ "?page_num=1&page_size=3&topic_type=1";

		// String url = Constant.BASE_URL;
		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, "initYueDanData");

		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}

	public void getTVYueDanServiceData() {
		String url = Constant.BASE_URL + "tops"
				+ "?page_num=1&page_size=3&topic_type=2";

		// String url = Constant.BASE_URL;
		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, "initYueDanData");

		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}

	public synchronized void initYueDanData(String url, JSONObject json,
			AjaxStatus status) {
		if (status.getCode() == AjaxStatus.NETWORK_ERROR) {
			// aq.id(R.id.ProgressText).invisible();
			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}

		if (json == null || json.equals(""))
			return;

		Log.d(TAG, "initYueDanData data = " + json.toString());
		try {
			ReturnTops result = mapper.readValue(json.toString(),
					ReturnTops.class);
			for (int i = 0; i < result.tops.length; i++) {
				YueDanInfo yuedanInfo = new YueDanInfo();
				ArrayList<ShiPinInfoParcelable> shiPinInfos = new ArrayList<ShiPinInfoParcelable>();
				yuedanInfo.name = result.tops[i].name;
				yuedanInfo.id = result.tops[i].id;
				yuedanInfo.prod_type = result.tops[i].prod_type;
				yuedanInfo.pic_url = result.tops[i].big_pic_url;
				yuedanInfo.num = result.tops[i].num;
				yuedanInfo.content = result.tops[i].content;
				for (int j = 0; j < result.tops[i].items.length; j++) {
					ShiPinInfoParcelable shipinInfo = new ShiPinInfoParcelable();
					shipinInfo.setArea(result.tops[i].items[j].area);
					shipinInfo
							.setBig_prod_pic_url(result.tops[i].items[j].big_prod_pic_url);
					shipinInfo
							.setCur_episode(result.tops[i].items[j].cur_episode);
					// shipinInfo.setCur_item_name(result.tops[i].items[j].cur_i);
					shipinInfo
							.setDefinition(result.tops[i].items[j].definition);
					shipinInfo.setDirectors(result.tops[i].items[j].directors);
					shipinInfo.setDuration(result.tops[i].items[j].duration);
					shipinInfo
							.setFavority_num(result.tops[i].items[j].favority_num);
					shipinInfo.setId(result.tops[i].items[j].id);
					shipinInfo
							.setMax_episode(result.tops[i].items[j].max_episode);
					shipinInfo.setProd_id(result.tops[i].items[j].prod_id);
					shipinInfo.setProd_name(result.tops[i].items[j].prod_name);
					shipinInfo
							.setProd_pic_url(result.tops[i].items[j].prod_pic_url);
					shipinInfo.setProd_type(result.tops[i].items[j].prod_type);
					shipinInfo
							.setPublish_date(result.tops[i].items[j].publish_date);
					shipinInfo.setScore(result.tops[i].items[j].score);
					shipinInfo.setStars(result.tops[i].items[j].stars);
					shipinInfo
							.setSupport_num(result.tops[i].items[j].support_num);
					shiPinInfos.add(shipinInfo);
				}
				yuedanInfo.shiPinList = shiPinInfos;
				View yueDanView = LayoutInflater.from(Main1.this).inflate(
						R.layout.layout_list, null);

				LinearLayout layout1 = (LinearLayout) yueDanView
						.findViewById(R.id.shipin_layout1);
				LinearLayout layout2 = (LinearLayout) yueDanView
						.findViewById(R.id.shipin_layout2);
				LinearLayout layout3 = (LinearLayout) yueDanView
						.findViewById(R.id.shipin_layout3);
				LinearLayout layout4 = (LinearLayout) yueDanView
						.findViewById(R.id.shipin_layout4);
				TextView yueDanNameText = (TextView) yueDanView
						.findViewById(R.id.yuedan_name);
				TextView shipNameText1 = (TextView) yueDanView
						.findViewById(R.id.shipin1);
				TextView shipNameText2 = (TextView) yueDanView
						.findViewById(R.id.shipin2);
				TextView shipNameText3 = (TextView) yueDanView
						.findViewById(R.id.shipin3);
				TextView shipNameText4 = (TextView) yueDanView
						.findViewById(R.id.shipin4);
				TextView shiPinNumNotice = (TextView) yueDanView
						.findViewById(R.id.notice_num);
				yueDanNameText.setText(yuedanInfo.name);
				if (yuedanInfo.shiPinList != null) {
					switch (yuedanInfo.shiPinList.size()) {
					case 0:
						layout1.setVisibility(View.GONE);
						layout2.setVisibility(View.GONE);
						layout3.setVisibility(View.GONE);
						layout4.setVisibility(View.GONE);
						shiPinNumNotice.setVisibility(View.GONE);
						break;
					case 1:
						shipNameText1.setText(yuedanInfo.shiPinList.get(0)
								.getProd_name());
						layout2.setVisibility(View.GONE);
						layout3.setVisibility(View.GONE);
						layout4.setVisibility(View.GONE);
						shiPinNumNotice.setVisibility(View.GONE);
						break;
					case 2:
						shipNameText1.setText(yuedanInfo.shiPinList.get(0)
								.getProd_name());
						shipNameText2.setText(yuedanInfo.shiPinList.get(1)
								.getProd_name());
						layout3.setVisibility(View.GONE);
						layout4.setVisibility(View.GONE);
						shiPinNumNotice.setVisibility(View.GONE);
						break;
					case 3:
						shipNameText1.setText(yuedanInfo.shiPinList.get(0)
								.getProd_name());
						shipNameText2.setText(yuedanInfo.shiPinList.get(1)
								.getProd_name());
						shipNameText3.setText(yuedanInfo.shiPinList.get(2)
								.getProd_name());
						layout4.setVisibility(View.GONE);
						shiPinNumNotice.setVisibility(View.GONE);
						break;
					case 4:
						shipNameText1.setText(yuedanInfo.shiPinList.get(0)
								.getProd_name());
						shipNameText2.setText(yuedanInfo.shiPinList.get(1)
								.getProd_name());
						shipNameText3.setText(yuedanInfo.shiPinList.get(2)
								.getProd_name());
						shipNameText4.setText(yuedanInfo.shiPinList.get(3)
								.getProd_name());
						shiPinNumNotice.setVisibility(View.GONE);
						break;

					default:
						shipNameText1.setText(yuedanInfo.shiPinList.get(0)
								.getProd_name());
						shipNameText2.setText(yuedanInfo.shiPinList.get(1)
								.getProd_name());
						shipNameText3.setText(yuedanInfo.shiPinList.get(2)
								.getProd_name());
						shipNameText4.setText(yuedanInfo.shiPinList.get(3)
								.getProd_name());
						shiPinNumNotice.setText(getResources().getString(
								R.string.yuedan_notice_num, yuedanInfo.num));
						break;
					}
				}
				if ("1".equals(yuedanInfo.prod_type)) {
					yuedan_list.add(i, yuedanInfo);
					yuedan_contentViews.add(i, yueDanView);

				} else {
					yuedan_list.add(yuedanInfo);
					yuedan_contentViews.add(yueDanView);
				}
			}
			if (isYueDanLoadedFlag == 1) {
				isYueDanLoadedFlag = 2;
			} else {
				isYueDanLoadedFlag = 1;
			}
			if (isYueDanLoadedFlag == 2) {
				YueDanInfo yuedanInfo1 = new YueDanInfo();
				yuedanInfo1.prod_type = "0";
				yuedanInfo1.id = "-1";
				yuedan_list.add(yuedanInfo1);
				YueDanInfo yuedanInfo2 = new YueDanInfo();
				yuedanInfo2.prod_type = "0";
				yuedanInfo2.id = "-2";
				yuedan_list.add(yuedanInfo2);
				// itemFram.setVisibility(View.VISIBLE);
				// itemFram.setVisibility(View.VISIBLE);
				// gallery1.setAdapter(new MainYueDanItemAdapter(Main.this,
				// yuedan_list));
				// gallery1.setSelection(0);
				handler.sendEmptyMessage(MESSAGE_STEP2_SUCESS);
				return;
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

	public void initHistoryData(String url, JSONObject json, AjaxStatus status) {
		if (status.getCode() == AjaxStatus.NETWORK_ERROR) {
			// aq.id(R.id.ProgressText).invisible();
			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}

		if (json == null || json.equals(""))
			return;

		Log.d(TAG, "initHistoryData data = " + json.toString());
		Log.i(TAG, "hot_list.size()-->" + hot_list.size());
		try {
			ReturnUserPlayHistories result = mapper.readValue(json.toString(),
					ReturnUserPlayHistories.class);
			
			if(result == null || result.histories == null) {
				
				return;
			}
			HotItemInfo item = new HotItemInfo();
//			if (hot_list.size() > 0) {//第一个存储的是历史记录，因此type是0
//				if (hot_list.get(0).type == 0) {//重新loading历史数据时，删除原先的view和数据
//					hot_list.remove(0);
//					hot_contentViews.remove(0);
//				}
//			}
			if (result.histories.length == 0) {//历史记录为空，loading最原始数据即可 总共11个
				if (isHotLoadedFlag == 1) {
					if (titleGroup.getSelectedTitleIndex() == 1) {
						itemFram.setVisibility(View.VISIBLE);
						updateHotDate();
						updateHotContentLayoue();
						gallery1.setAdapter(new MainHotItemAdapter(Main1.this,
								hot_list));
						gallery1.setSelection(0);
						handler.sendEmptyMessage(MESSAGE_STEP1_SUCESS);
					}
					isHotLoadedFlag = 2;
				} else if (isHotLoadedFlag == 0) {
					isHotLoadedFlag = 1;
				} else if (isHotLoadedFlag == 2) {
					hot_list.clear();
					for(int i=0; i<netWorkHotList.size(); i++){
						hot_list.add(netWorkHotList.get(i));
					}
					if (titleGroup.getSelectedTitleIndex() == 1) {
						itemFram.setVisibility(View.VISIBLE);
						gallery1.setAdapter(new MainHotItemAdapter(Main1.this,
								hot_list));
						gallery1.setSelection(0);
						handler.sendEmptyMessage(MESSAGE_UPDATEUSER_HISTORY_SUCEESS);
					}
				}
				return;
			}
			item.type = 0;
			item.id = result.histories[0].id;
			item.prod_id = result.histories[0].prod_id;
			item.prod_name = result.histories[0].prod_name;
			item.prod_type = result.histories[0].prod_type;
			// item.prod_pic_url = result.histories[0].big_prod_pic_url;
			String bigPicUrl = result.histories[0].big_prod_pic_url;
			if (bigPicUrl == null || bigPicUrl.equals("")
					|| bigPicUrl.equals(UtilTools.EMPTY)) {

				bigPicUrl = result.histories[0].prod_pic_url;
			}
			item.prod_pic_url = bigPicUrl;
			item.stars = result.histories[0].stars;
			item.directors = result.histories[0].directors;
			item.favority_num = result.histories[0].favority_num;
			item.support_num = result.histories[0].support_num;
			item.publish_date = result.histories[0].publish_date;
			item.score = result.histories[0].score;
			item.area = result.histories[0].area;
			item.cur_episode = result.histories[0].cur_episode;
			item.definition = result.histories[0].definition;
			item.prod_summary = result.histories[0].prod_summary;
			item.prod_subname = result.histories[0].prod_subname;
			item.duration = result.histories[0].duration;
			item.playback_time = result.histories[0].playback_time;
			item.video_url = result.histories[0].video_url;
//
//			View hotView = LayoutInflater.from(Main.this).inflate(
//					R.layout.layout_hot, null);
//			TextView hot_name_tv = (TextView) hotView
//					.findViewById(R.id.hot_content_name);
//			TextView hot_score_tv = (TextView) hotView
//					.findViewById(R.id.hot_content_score);
//			TextView hot_directors_tv = (TextView) hotView
//					.findViewById(R.id.hot_content_directors);
//			TextView hot_starts_tv = (TextView) hotView
//					.findViewById(R.id.hot_content_stars);
//			TextView hot_introduce_tv = (TextView) hotView
//					.findViewById(R.id.hot_content_introduce);
//			ImageView icon_douban = (ImageView) hotView
//					.findViewById(R.id.icon_douban);
//			if ("3".equals(item.prod_type.trim())) {
//				TextView hot_title_director = (TextView) hotView
//						.findViewById(R.id.title_directors);
//				TextView hot_title_stars = (TextView) hotView
//						.findViewById(R.id.title_stars);
//				hot_title_director.setText(R.string.xiangqing_zongyi_zhuchi);
//				hot_title_stars.setText(R.string.xiangqing_zongyi_shoubo);
//				hot_starts_tv.setText(item.directors);
//				hot_directors_tv.setText(item.stars);
//				icon_douban.setVisibility(View.INVISIBLE);
//			} else {
//				hot_directors_tv.setText(item.directors);
//				hot_starts_tv.setText(item.stars);
//			}
//
//			hot_name_tv.setText(item.prod_name);
//			hot_score_tv.setText(UtilTools.formateScore(item.score));
//			hot_introduce_tv.setText(item.prod_summary);
//			hot_list.add(0, item);
			
//			Log.i(TAG, "item--->" + item.prod_id);
//			hot_list.add(item);
//			
//			for(int i=0;i<netWorkHotList.size();i++) {
//				HotItemInfo tempHotItemInfo = netWorkHotList.get(i);
//				
//				Log.i(TAG, "tempHotItemInfo--->" + tempHotItemInfo.prod_id);
//				
//				if(!tempHotItemInfo.prod_id.equals(item.prod_id)) {
//					
//					hot_list.add(tempHotItemInfo);
//				}else {
//					
//					hot_contentViews.remove(i);
//				}
//			}
			
//			hot_contentViews.add(0, hotView);
//			
//			Log.d(TAG, "lengh = " + hot_contentViews.size());
			if (isHotLoadedFlag == 1) {
				if (titleGroup.getSelectedTitleIndex() == 1) {
					itemFram.setVisibility(View.VISIBLE);
					hot_list.add(0, item);
					updateHotDate();
					updateHotContentLayoue();
					gallery1.setAdapter(new MainHotItemAdapter(Main1.this,
							hot_list));
					if (hot_list.size() > 0) {
						if (hot_list.get(0).type == 0) {
							gallery1.setSelection(1);
						} else {
							gallery1.setSelection(0);
						}
					}
					handler.sendEmptyMessage(MESSAGE_STEP1_SUCESS);
				}
				isHotLoadedFlag = 2;
				return;
			} else if (isHotLoadedFlag == 0) {
				hot_list.add(item);
				isHotLoadedFlag = 1;
			} else if (isHotLoadedFlag == 2) {
				hot_list.clear();
				hot_list.add(item);
				for(int i=0; i<netWorkHotList.size(); i++){
					hot_list.add(netWorkHotList.get(i));
				}
				updateHotDate();
				updateHotContentLayoue();
				if (titleGroup.getSelectedTitleIndex() == 1) {
					itemFram.setVisibility(View.VISIBLE);
					
					gallery1.setAdapter(new MainHotItemAdapter(Main1.this,
							hot_list));
					if (hot_list.size() > 0) {
						if (hot_list.get(0).type == 0) {
							gallery1.setSelection(1);
						} else {
							gallery1.setSelection(0);
						}
					}
					handler.sendEmptyMessage(MESSAGE_UPDATEUSER_HISTORY_SUCEESS);
				}
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

	public void initHotData(String url, JSONObject json, AjaxStatus status) {
		if (status.getCode() == AjaxStatus.NETWORK_ERROR) {
			// aq.id(R.id.ProgressText).invisible();
			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}
		try {

			if (json == null || json.equals(""))
				return;

			Log.d(TAG, "initHotData" + json.toString());
			ReturnMainHot result = mapper.readValue(json.toString(),
					ReturnMainHot.class);
			// hot_list.clear();
			
			//当历史加载完成，hot数据后加载
			
//			String tempProdId = null;
//			
//			if(hot_list != null && hot_list.size() > 0 && netWorkHotList.size() <= 0 ) {
//				
//				HotItemInfo tempInfo = hot_list.get(0);
//				if(tempInfo != null) {
//					
//					tempProdId = tempInfo.prod_id;
//				}
//			}
			for (int i = 0; i < result.items.length; i++) {
				HotItemInfo item = new HotItemInfo();
				item.type = 1;
				item.id = result.items[i].id;
				item.prod_id = result.items[i].prod_id;
				item.prod_name = result.items[i].prod_name;
				item.prod_type = result.items[i].prod_type;
				item.prod_pic_url = result.items[i].prod_pic_url;
				item.stars = result.items[i].stars;
				item.directors = result.items[i].directors;
				item.favority_num = result.items[i].favority_num;
				item.support_num = result.items[i].support_num;
				item.publish_date = result.items[i].publish_date;
				item.score = result.items[i].score;
				item.area = result.items[i].area;
				item.cur_episode = result.items[i].cur_episode;
				item.max_episode = result.items[i].max_episode;
				item.definition = result.items[i].definition;
				item.prod_summary = result.items[i].prod_summary;
				item.duration = result.items[i].duration;
				item.play_urls = result.items[i].play_urls;
				item.playback_time = "";
				hot_list.add(item);
				netWorkHotList.add(item);//网络数据
//				View hotView = LayoutInflater.from(Main.this).inflate(
//						R.layout.layout_hot, null);
//				TextView hot_name_tv = (TextView) hotView
//						.findViewById(R.id.hot_content_name);
//				TextView hot_score_tv = (TextView) hotView
//						.findViewById(R.id.hot_content_score);
//				TextView hot_directors_tv = (TextView) hotView
//						.findViewById(R.id.hot_content_directors);
//				TextView hot_starts_tv = (TextView) hotView
//						.findViewById(R.id.hot_content_stars);
//				TextView hot_introduce_tv = (TextView) hotView
//						.findViewById(R.id.hot_content_introduce);
//				ImageView icon_douban = (ImageView) hotView
//						.findViewById(R.id.icon_douban);
//				if ("3".equals(item.prod_type.trim())) {
//					TextView hot_title_director = (TextView) hotView
//							.findViewById(R.id.title_directors);
//					TextView hot_title_stars = (TextView) hotView
//							.findViewById(R.id.title_stars);
//					hot_title_director
//							.setText(R.string.xiangqing_zongyi_zhuchi);
//					hot_title_stars.setText(R.string.xiangqing_zongyi_shoubo);
//					hot_starts_tv.setText(item.directors);
//					hot_directors_tv.setText(item.stars);
//					icon_douban.setVisibility(View.INVISIBLE);
//				} else {
//					hot_directors_tv.setText(item.directors);
//					hot_starts_tv.setText(item.stars);
//				}
//
//				hot_name_tv.setText(item.prod_name);
//				hot_score_tv.setText(UtilTools.formateScore(item.score));
//				hot_introduce_tv.setText(item.prod_summary);
				
//				if(tempProdId != null && tempProdId.endsWith(item.prod_id)) {
//					
//					//说明历史加载先完成
//				} else {
//					
//					hot_list.add(item);
//					netWorkHotList.add(item);//网络数据
//					hot_contentViews.add(hotView);
//				}
			}
			// Log.d

			if (isHotLoadedFlag == 1) {
				if (titleGroup.getSelectedTitleIndex() == 1) {
					// removeSameInHotList();
					updateHotDate();
					updateHotContentLayoue();
					gallery1.setAdapter(new MainHotItemAdapter(Main1.this,
							hot_list));
					if (hot_list.size() > 0) {
						if (hot_list.get(0).type == 0) {
							gallery1.setSelection(1);
						} else {
							gallery1.setSelection(0);
						}
					}
					itemFram.setVisibility(View.VISIBLE);
					handler.sendEmptyMessage(MESSAGE_STEP1_SUCESS);
				}
				isHotLoadedFlag = 2;
				return;
			}
			isHotLoadedFlag = 1;
			// aq.id(highlightImageView).image(hot_list.get(gallery1.getSelectedItemPosition()).prod_pic_url,true,true);
			// noticeView.setText(gallery1.getSelectedItemPosition()+1 + "/" +
			// hot_list.size());
			// changeContent(0);
			// hot_list.add(arg0);
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

	// private void changeContent1(int dx){
	// final int positon = gallery1.getSelectedItemPosition();
	// switch (titleGroup.getSelectedTitleIndex()) {
	// case 1:
	//
	// // hot_name_tv = (TextView) hotView.findViewById(R.id.hot_content_name);
	// // hot_score_tv = (TextView)
	// hotView.findViewById(R.id.hot_content_score);
	// // hot_directors_tv = (TextView)
	// hotView.findViewById(R.id.hot_content_directors);
	// // hot_starts_tv = (TextView)
	// hotView.findViewById(R.id.hot_content_stars);
	// // hot_introduce_tv = (TextView)
	// hotView.findViewById(R.id.hot_content_introduce);
	// // Log.d(TAG, "------------------------");
	//
	// //
	// aq.id(highlightImageView).image(hot_list.get(positon+dx).prod_pic_url,true,true);
	//
	// ImageView img = (ImageView)
	// gallery1.findViewWithTag(hot_list.get(positon+dx).prod_pic_url);
	// if(img != null){
	// highlightImageView.setImageDrawable(img.getDrawable());
	// }else{
	// aq.id(highlightImageView).image(hot_list.get(positon+dx).prod_pic_url,true,true);
	// }
	// // if(dx==1){
	// // itemFram.startAnimation(leftTranslateAnimationStep2);
	// // }
	// // if(dx==-1){
	// // itemFram.startAnimation(rightTranslateAnimationStep2);
	// // }
	//
	// if(indexCaces.get(1)!=null&&indexCaces.get(1)<positon){
	// itemFram.startAnimation(leftTranslateAnimationStep2);
	// }
	// Log.d(TAG, "positon = " + positon + "laset = " + indexCaces.get(1));
	// if(indexCaces.get(1)!=null&&indexCaces.get(1)>positon){
	// itemFram.startAnimation(rightTranslateAnimationStep2);
	// }
	//
	// // itemFram.setVisibility(View.GONE);
	// handler.removeCallbacksAndMessages(null);
	// handler.postDelayed((new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// contentLayout.removeAllViews();
	// View hotView = hot_contentViews.get(positon);
	// hotView.setLayoutParams(new
	// LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
	// contentLayout.startAnimation(alpha_appear);
	// contentLayout.addView(hotView);
	// }
	// }),280);
	//
	//
	// // contentLayout.startAnimation(alpha_appear);
	// // hotView.invalidate();
	//
	// // aq.id(R.id.hot_content_name).text(hot_list.get(positon+dx).prod_name);
	// // aq.id(R.id.hot_content_score).text(hot_list.get(positon+dx).score);
	// //
	// aq.id(R.id.hot_content_directors).text(hot_list.get(positon+dx).directors);
	// // aq.id(R.id.hot_content_stars).text(hot_list.get(positon+dx).stars);
	// //
	// aq.id(R.id.hot_content_introduce).text(hot_list.get(positon+dx).prod_summary);
	// break;
	// case 2:
	// highlightImageView.setImageResource(resouces_lib_active[positon]);
	// noticeView.setText(positon+1 + "/" + resouces_lib_active.length);
	// contentLayout.removeAllViews();
	// yeuDanView.setLayoutParams(new
	// LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
	// contentLayout.startAnimation(alpha_appear);
	// contentLayout.addView(yeuDanView);
	// break;
	// case 3:
	// highlightImageView.setImageResource(resouces_lib_active[positon]);
	// noticeView.setText(positon+1 + "/" + resouces_lib_active.length);
	// break;
	// case 4:
	// highlightImageView.setImageResource(resouces_my_active[positon]);
	// noticeView.setText(positon+1 + "/" + resouces_my_active.length);
	// break;
	// default:
	// break;
	// }
	// }

	private void updateUser(String userId) {
		// TODO Auto-generated method stub
		if (userId.equals(app.getUserData("userId"))) {
			UserInfo currentUserInfo = new UserInfo();
			currentUserInfo.setUserId(app.getUserData("userId"));
			currentUserInfo.setUserName(app.getUserData("userName"));
			currentUserInfo.setUserAvatarUrl(app.getUserData("userAvatarUrl"));
			headers.put("user_id", currentUserInfo.getUserId());
			app.setUser(currentUserInfo);
			sendBroadcast(new Intent(ACTION_USERUPDATE));
			handler.sendEmptyMessage(MESSAGE_UPDATEUSER);//当切换用户id时，重新加载页面
		} else {
			String url = Constant.BASE_URL + "user/view?userid=" + userId;
			AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
			cb.url(url).type(JSONObject.class)
					.weakHandler(this, "getBandUserInfoResult");
			cb.SetHeader(app.getHeaders());
			aq.ajax(cb);
		}
	}

	public void getBandUserInfoResult(String url, JSONObject json,
			AjaxStatus status) {

		if (json != null) {

			if (json == null || json.equals(""))
				return;

			Log.d(TAG, "getBandUserInfoResult" + json.toString());
			try {
				UserInfo currentUserInfo = new UserInfo();
				if (json.has("user_id")) {
					currentUserInfo.setUserId(json.getString("user_id").trim());
				} else {
					currentUserInfo.setUserId(json.getString("id").trim());
				}
				currentUserInfo.setUserName(json.getString("nickname"));
				currentUserInfo.setUserAvatarUrl(json.getString("pic_url"));
				headers.put("user_id", currentUserInfo.getUserId());
				app.setUser(currentUserInfo);
				sendBroadcast(new Intent(ACTION_USERUPDATE));
				handler.sendEmptyMessage(MESSAGE_UPDATEUSER);
				// headers.put("user_id", currentUserInfo.get);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
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
		case DIALOG_NETWORK_ERROR:
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setTitle(getString(R.string.toast_no_network))
					.setPositiveButton(getString(R.string.toast_setting_wifi),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// wifi设置
									isWifiReset = true;
									try {
										startActivity(new Intent(
												Settings.ACTION_SETTINGS));
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										
										app.MyToast(getApplicationContext(), "自行进入系统网络设置界面");
									}

								}
							})
					.setNegativeButton(getString(R.string.toast_no_exit),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// 退出应用
									finish();
								}
							});
			AlertDialog dialog = builder.show();
			dialog.setCancelable(false);
			Button btn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
			btn.setFocusable(true);
			btn.setSelected(true);
			btn.requestFocus();
			return null;
		case DIALOG_NETWORK_SLOW:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this)
					.setTitle(getString(R.string.toast_network_slow))
					.setPositiveButton(getString(R.string.toast_no_retry),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									initStep = 0;
									isHotLoadedFlag = 0;
									isYueDanLoadedFlag = 0;
									showDialog(DIALOG_WAITING);
									handler.sendEmptyMessageDelayed(
											MESSAGE_30S_TIMEOUT,
											LOADING_TIME_OUT);// 图片撤掉20S后
									checkLogin();

								}
							})
					.setNegativeButton(getString(R.string.toast_no_exit),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// 退出应用
									finish();
								}
							});
			AlertDialog dialog2 = builder2.show();
			dialog2.setCancelable(false);
			Button btn2 = dialog2.getButton(DialogInterface.BUTTON_NEGATIVE);
			btn2.setFocusable(true);
			btn2.setSelected(true);
			btn2.requestFocus();
			return null;
		default:
			return super.onCreateDialog(id);
		}
	}

	private void updateLastTimeView() {
		if (app.getUserData("lastTime") == null
				|| "".equals(app.getUserData("lastTime"))) {
			lastBandTimeView.setText("您还没有同步过哟~");
		} else {
			lastBandTimeView.setText("上次同步于:\t"
					+ UtilTools.getLastBandNotice(app
							.getUserData("lastTime")));
		}
	}

	// class PLAY_URLS_INDEX{
	// int index;
	// // public String source;
	// public String url;
	// public URLS[] urls;
	// }

	// class URLS_INDEX{
	// int souces;
	// int defination;
	// String url;
	// }

	// private void removeSameInHotList(){
	// HotItemInfo info = hot_list.get(0);
	// if(info.type == 0){
	// Log.d(TAG, "---------------------> remove same");
	// if(hot_list.size()>1){
	// for(int i=1; i<hot_list.size();i++){
	// HotItemInfo info2 = hot_list.get(i);
	// if(info.prod_id.equals(info2.prod_id)){
	// hot_list.remove(info2);
	// }
	// }
	// }
	// }
	// }

	protected void getServiceData(String url, String interfaceName) {
		// TODO Auto-generated method stub

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, interfaceName);

		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}

	protected void getShouCangData(String url) {
		// TODO Auto-generated method stub

		getServiceData(url, "initShouCangServiceData");
	}
	
	protected void getHistoryData(String url) {
		// TODO Auto-generated method stub

		getServiceData(url, "initHistoryServiceData");
	}

	public void initShouCangServiceData(String url, JSONObject json,
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

			Log.d(TAG, "initShouCangServiceData" + json.toString());
			compareUsrFav4DB(
					UtilTools.returnUserFavoritiesJson(json.toString()),
					app.getUserInfo().getUserId());
			
			//获取历史播放记录数据
			getHistoryData(URLUtils.
					getHistoryURL(UtilTools.getCurrentUserId(getApplicationContext())));
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
	
	public void initHistoryServiceData(String url, JSONObject json,
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

			Log.d(TAG, "initHistoryServiceData" + json.toString());
			compareUsrHis4DB(
					UtilTools.returnUserHistoryJson(json.toString()),
					app.getUserInfo().getUserId());
			
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
	
	// 网上用户id数据和本地用户id数据进行比较
	// 以网上数据为标准，如果本地用户id数据多余的就删除，不存在的就进行添加
	private void compareUsrHis4DB(List<HotItemInfo> list, String userId) {
		
		if (list == null) {

			return;
		}
		
		String selection = UserShouCang.USER_ID + "=?";// 通过用户id，找到相应信息
		String[] selectionArgs = { userId };

		TvDatabaseHelper helper = TvDatabaseHelper
				.newTvDatabaseHelper(getApplicationContext());
		SQLiteDatabase database = helper.getWritableDatabase();// 获取写db
		
		String[] columns = { UserHistory.PRO_ID};// 返回影片id
		
		Cursor cursor_proId = database.query(
				TvDatabaseHelper.HISTORY_TABLE_NAME, columns, selection,
				selectionArgs, null, null, null);
		
		if (list.size() > 0) {// 当用户有历史记录时
			
			if (cursor_proId != null && cursor_proId.getCount() > 0) {// 数据库有数据
				
				database.delete(TvDatabaseHelper.HISTORY_TABLE_NAME, selection,
						selectionArgs);
				
			}
			
			for(HotItemInfo info:list) {
				
				DBUtils.insertHotItemInfo2DB_History(getApplicationContext(),
						info, userId, database);
			}
			
		}else {// 如果网上此用户没有任何收藏,但是数据库中有相应用户数据，清空掉此用户数据

			if (cursor_proId != null && cursor_proId.getCount() > 0) {// 数据库有数据

				database.delete(TvDatabaseHelper.HISTORY_TABLE_NAME, selection,
						selectionArgs);
			}
		}
		
		cursor_proId.close();
		helper.close();
		
	}

	// 网上用户id数据和本地用户id数据进行比较
	// 以网上数据为标准，如果本地用户id数据多余的就删除，不存在的就进行添加
	private void compareUsrFav4DB(List<HotItemInfo> list, String userId) {

		if (list == null) {

			return;
		}

		boolean isUpdateThisTime = false;// 本次是否有收藏更新

		String selection = UserShouCang.USER_ID + "=?";// 通过用户id，找到相应信息
		String[] selectionArgs = { userId };

		TvDatabaseHelper helper = TvDatabaseHelper
				.newTvDatabaseHelper(getApplicationContext());
		SQLiteDatabase database = helper.getWritableDatabase();// 获取写db

		String[] columns = { UserShouCang.PRO_ID, UserShouCang.PRO_TYPE,
				UserShouCang.CUR_EPISODE };// 返回影片id 类型和当前更新集数
		Cursor cursor_proId = database.query(
				TvDatabaseHelper.ZHUIJU_TABLE_NAME, columns, selection,
				selectionArgs, null, null, null);

		if (list.size() > 0) {// 当用户有收藏时

			if (cursor_proId != null && cursor_proId.getCount() > 0) {// 数据库有数据

				List<HotItemInfo> dbList = new ArrayList<HotItemInfo>();// 数据库list，只取其中id、type、cur_episode

				while (cursor_proId.moveToNext()) {

					int indexId = cursor_proId
							.getColumnIndex(UserShouCang.PRO_ID);
					int indexType = cursor_proId
							.getColumnIndex(UserShouCang.PRO_TYPE);
					int indexCurEpisode = cursor_proId
							.getColumnIndex(UserShouCang.CUR_EPISODE);

					HotItemInfo info = new HotItemInfo();

					if (indexId != -1) {

						Log.i(TAG,
								"compareUsrFav4DB--->:pro_id"
										+ cursor_proId.getString(indexId));
						info.prod_id = cursor_proId.getString(indexId);// 把用户id信息影片id存储到字符串中
						info.prod_type = cursor_proId.getString(indexType);
						info.cur_episode = cursor_proId
								.getString(indexCurEpisode);

					}

					dbList.add(info);
				}

				for (int i = 0; i < dbList.size(); i++)
					Log.i(TAG, "db ids---->" + dbList.get(i).prod_id);

				for (int i = 0; i < list.size(); i++)
					Log.i(TAG, "netWork ids---->" + list.get(i).prod_id);

				// 以网络的数据为标准 A 数据库为B
				// 相同list集合 network
				List<HotItemInfo> sameList4NetWork = UtilTools
						.sameList4NetWork(list, dbList);
				// 相同list集合 network
				List<HotItemInfo> sameList4DB = UtilTools.sameList4DB(
						list, dbList);
				// 不同数据集合 network
				List<HotItemInfo> differentList = UtilTools
						.differentList4NetWork(list, dbList);
				Log.i(TAG, "differentList---->" + differentList.size());

				// 首先数据库数据全部改为旧数据
				ContentValues contentValues = new ContentValues();
				contentValues.put(UserShouCang.IS_NEW, DataBaseItems.OLD);
				database.update(TvDatabaseHelper.ZHUIJU_TABLE_NAME,
						contentValues, selection, selectionArgs);

				// A与B相同的信息，数据更新为新的
				for (int i = 0; i < sameList4NetWork.size(); i++) {

					ContentValues tempValues = new ContentValues();
					tempValues.put(UserShouCang.IS_NEW, DataBaseItems.NEW);
					String tempSelection = UserShouCang.PRO_ID + "=? and "
							+ UserShouCang.USER_ID + "=?";
					String[] tempselectionArgs = {sameList4NetWork.get(i).prod_id, userId };

					// A与B有相同数据，比较其cur_episode，如果A中不等于B中数据，把IS_UPDATE改为new
					String type = sameList4NetWork.get(i).prod_type;
					if (type != null) {
						// 如果相同数据有电视剧、动漫和综艺类型
						if (type.equals(BangDanConstant.TV_TYPE)
								|| type.equals(BangDanConstant.DONGMAN_TYPE)
								|| type.equals(BangDanConstant.ZONGYI_TYPE)) {

							if (!sameList4NetWork.get(i).cur_episode
									.equals(sameList4DB.get(i).cur_episode)) {

								DBUtils.updateHotItemInfo2DB(
										getApplicationContext(),
										sameList4NetWork.get(i), userId,
										database);
								isUpdateThisTime = true;
							}

						}
					}
					database.update(TvDatabaseHelper.ZHUIJU_TABLE_NAME,
							tempValues, tempSelection, tempselectionArgs);
				}

				// 插入A不同的数据
				for (int i = 0; i < differentList.size(); i++) {

					HotItemInfo info = differentList.get(i);

					DBUtils.insertHotItemInfo2DB(
							getApplicationContext(), info, userId, database);
				}

				// 删除掉旧的数据 通过新旧数据标记来删除
				String deleteSelection = UserShouCang.IS_NEW + "=? and "
						+ UserShouCang.USER_ID + "=?";
				String[] deleteselectionArgs = { DataBaseItems.OLD + "", userId };
				database.delete(TvDatabaseHelper.ZHUIJU_TABLE_NAME,
						deleteSelection, deleteselectionArgs);

			} else {// 数据库没有数据
				// 把下载的数据插入到数据库
				for (HotItemInfo info : list) {

					DBUtils.insertHotItemInfo2DB(
							getApplicationContext(), info, userId, database);
				}
			}

		} else {// 如果网上此用户没有任何收藏,但是数据库中有相应用户数据，清空掉此用户数据

			if (cursor_proId != null && cursor_proId.getCount() > 0) {// 数据库有数据

				database.delete(TvDatabaseHelper.ZHUIJU_TABLE_NAME, selection,
						selectionArgs);
			}
		}

		cursor_proId.close();// 关闭
		helper.closeDatabase();// 关闭数据库

		Log.i(TAG, "isUpdateThisTime--->" + isUpdateThisTime);

		// 本次是否有收藏更新成功
		if (isUpdateThisTime) {// 如果成功更新

			boolean is48TimeClock = UtilTools
					.is48TimeClock(getApplicationContext());// 是否开启闹钟

			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			long time =System.currentTimeMillis()+ 48 * 60 * 60 * 1000;//教训
			// long time = 1000;

			PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(
					this, AlarmBroadcastReceiver.class),
					Intent.FLAG_ACTIVITY_NEW_TASK);
			am.set(AlarmManager.RTC_WAKEUP, time, pi);
			if (!is48TimeClock) {// 如果闹钟没有开启，存储开启状态

				UtilTools.set48TimeClock(getApplicationContext(), true);
			}
		}

		UtilTools.setCurrentUserId(getApplicationContext(), userId);

	}

//	class CheckPlayUrl implements Runnable {
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//
//			for (int i = 0; i < hot_list.size(); i++) {
//				HotItemInfo info = hot_list.get(i);
//				if (info.type > 0) {
//					List<URLS_INDEX> playUrls = new ArrayList<URLS_INDEX>();
//					for (int j = 0; j < info.play_urls.length; j++) {
//						for (int k = 0; k < info.play_urls[j].urls.length; k++) {
//							URLS_INDEX url_index = new URLS_INDEX();
//							url_index.url = info.play_urls[j].urls[k].url;
//							url_index.source_from = info.play_urls[j].source;
//							if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[0])) {
//								url_index.souces = 0;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[1])) {
//								url_index.souces = 1;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[2])) {
//								url_index.souces = 2;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[3])) {
//								url_index.souces = 3;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[4])) {
//								url_index.souces = 4;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[5])) {
//								url_index.souces = 5;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[6])) {
//								url_index.souces = 6;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[7])) {
//								url_index.souces = 7;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[8])) {
//								url_index.souces = 8;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[9])) {
//								url_index.souces = 9;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[10])) {
//								url_index.souces = 10;
//							} else if (info.play_urls[j].source.trim()
//									.equalsIgnoreCase(Constant.video_index[11])) {
//								url_index.souces = 11;
//							} else {
//								url_index.souces = 12;
//							}
//							if (info.play_urls[j].urls[k].type.trim()
//									.equalsIgnoreCase(
//											Constant.player_quality_index[1])) {
//								url_index.defination = 1;
//							} else if (info.play_urls[j].urls[k].type.trim()
//									.equalsIgnoreCase(
//											Constant.player_quality_index[0])) {
//								url_index.defination = 2;
//							} else if (info.play_urls[j].urls[k].type.trim()
//									.equalsIgnoreCase(
//											Constant.player_quality_index[2])) {
//								url_index.defination = 3;
//							} else if (info.play_urls[j].urls[k].type.trim()
//									.equalsIgnoreCase(
//											Constant.player_quality_index[3])) {
//								url_index.defination = 4;
//							} else {
//								url_index.defination = 5;
//							}
//							playUrls.add(url_index);
//						}
//					}
//
//					if (playUrls.size() > 1) {
//						Collections.sort(playUrls,
//								new DefinationComparatorIndex());
//						Collections.sort(playUrls, new SouceComparatorIndex1());
//					}
//					Log.d(TAG, "test------------------" + i
//							+ "playUrls size = " + playUrls.size() + "name = "
//							+ info.prod_name);
//					for (int n = 0; info.video_url == null
//							&& n < playUrls.size(); n++) {
//						String url = playUrls.get(n).url;
//						if (app.CheckUrl(url)) {
//							Log.d(TAG, "url-------ok----->" + url);
//							hot_list.get(i).video_url = url;
//							hot_list.get(i).source = playUrls.get(n).source_from;
//						}
//					}
//				}
//			}
//		}
//
//	}
	/*
	 * @remove banner
	 */
	private void removeBanner(){
		if(mAdView!=null){
			layout.removeView(mAdView);
			mAdView = null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.joyplus.adkey.AdListener#adClicked()
	 * @author yyc
	 */
	@Override
	public void adClicked()
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->adClicked");
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.joyplus.adkey.AdListener#adClosed(com.joyplus.adkey.Ad, boolean)
	 * @author yyc
	 */
	@Override
	public void adClosed(Ad ad, boolean completed)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->adClosed");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.joyplus.adkey.AdListener#adLoadSucceeded(com.joyplus.adkey.Ad)
	 * @author yyc
	 */
	@Override
	public void adLoadSucceeded(Ad ad)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->adLoadSucceeded");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.joyplus.adkey.AdListener#adShown(com.joyplus.adkey.Ad, boolean)
	 * @author yyc
	 */
	@Override
	public void adShown(Ad ad, boolean succeeded)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->adShown");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.joyplus.adkey.AdListener#noAdFound()
	 * @author yyc
	 */
	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->noAdFound");
	}
}
