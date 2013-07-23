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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.joyplus.tv.entity.CurrentPlayDetailData;
import com.joyplus.tv.entity.HotItemInfo;
import com.joyplus.tv.entity.ShiPinInfoParcelable;
import com.joyplus.tv.entity.YueDanInfo;
import com.joyplus.tv.ui.CustomGallery;
import com.joyplus.tv.ui.MyScrollLayout;
import com.joyplus.tv.ui.MyScrollLayout.OnViewChangeListener;
import com.joyplus.tv.ui.WaitingDialog;
import com.joyplus.tv.utils.Log;
import com.joyplus.tv.utils.UtilTools;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class Main1 extends Activity implements OnItemSelectedListener,
		OnItemClickListener , AdListener{
	private String TAG = "Main";
	
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

	private int[] resouces_my_nomal = { R.drawable.follow_normal,
			R.drawable.recent_normal, R.drawable.system_normal };

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

	private View kuView;
	private View myView;
	private TextView lastBandTimeView;

	private Map<Integer, Integer> indexCaces = new HashMap<Integer, Integer>();

	private Animation alpha_appear;
	private Animation alpha_disappear;

	private TranslateAnimation leftTranslateAnimationStep1;
	private TranslateAnimation leftTranslateAnimationStep2;
	private TranslateAnimation rightTranslateAnimationStep1;
	private TranslateAnimation rightTranslateAnimationStep2;

	private static final int DIALOG_NETWORK_ERROR = DIALOG_WAITING + 1;
	private static final int DIALOG_NETWORK_SLOW = DIALOG_NETWORK_ERROR + 1;

	private boolean isNetWorkFine = true;// 记录网络是否正常
	private boolean isWifiReset = false;// wifi网络是否重新设置
	
	private List<HotItemInfo> hot_list = new ArrayList<HotItemInfo>();//用户数据，有可能去过重
	private List<HotItemInfo> netWorkHotList = new ArrayList<HotItemInfo>();//网络获取数据，不改变
	private List<YueDanInfo> yuedan_list = new ArrayList<YueDanInfo>();
	private int isHotLoadedFlag = 1;
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

		startingImageView = (ImageView) findViewById(R.id.image_starting);
		
		if(!UtilTools.getIsShowAd(getApplicationContext())) {
			
			BitmapFactory.Options opt = new BitmapFactory.Options();

			opt.inTempStorage = new byte[16 * 1024];

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

					} else {
						definitionIcon.setImageDrawable(null);
						itemFram.setVisibility(View.INVISIBLE);
						hot_list.clear();
						hot_contentViews.clear();
						gallery1.setAdapter(null);
						contentLayout.removeAllViews();
						Log.i(TAG, "SetOnViewChangeListener--->isHotLoadedFlag != 2");
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

			}
		});

		itemFram = (FrameLayout) findViewById(R.id.itemFram);
		highlightImageView_1 = (ImageView) findViewById(R.id.highlight_img_1);
		highlightImageView_2 = (ImageView) findViewById(R.id.highlight_img_2);
		highlightImageView_3 = (ImageView) findViewById(R.id.highlight_img_3);
		highlightImageView_4 = (ImageView) findViewById(R.id.highlight_img_4);
		highlightImageView_2.setVisibility(View.GONE);
		highlightImageView_3.setVisibility(View.GONE);
		highlightImageView_4.setVisibility(View.GONE);
		playIcon = (ImageView) findViewById(R.id.play_icon);
		definitionIcon = (ImageView) findViewById(R.id.icon_defination);

		DisplayMetrics metrics = new DisplayMetrics();
		density = metrics.density;
		Display display = getWindowManager().getDefaultDisplay();
		displayWith = display.getWidth();

		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		gallery1.setAdapter(new MainHotItemAdapter(Main1.this, hot_list));

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
				
				Log.i(TAG, "getHistoryServiceData-->MESSAGE_UPDATEUSER");
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
						
						startingImageView.startAnimation(alpha_disappear);
						rootLayout.setVisibility(View.VISIBLE);
						gallery1.requestFocus();
						handler.removeMessages(MESSAGE_START_TIMEOUT);

						Log.i(TAG,"removeDialog(DIALOG_WAITING);---else ---->1");
						
							
							removeDialog(DIALOG_WAITING);
					} else {
						Log.i(TAG,"removeDialog(DIALOG_WAITING);---else ---->2");
						removeDialog(DIALOG_WAITING);
						contentLayout.setVisibility(View.VISIBLE);
						gallery1.requestFocus();
					}

					handler.removeMessages(MESSAGE_30S_TIMEOUT);
				}
				
				/*
				 * adkey show,the Viewo of ad init()
				 */
					
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
					
					contentLayout.setVisibility(View.INVISIBLE);
					
					if(!UtilTools.getIsShowAd(getApplicationContext())) {
						
						showDialog(DIALOG_WAITING);
					}
				}

				break;
			case MESSAGE_UPDATEUSER_HISTORY_SUCEESS:// 超时还未加载好

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
		handler.sendEmptyMessage(MESSAGE_UPDATEUSER);
		handler.sendEmptyMessageDelayed(MESSAGE_START_TIMEOUT, LOADING_PIC_TIME);
		handler.sendEmptyMessageDelayed(MESSAGE_30S_TIMEOUT, LOADING_TIME_OUT);// 图片撤掉20S后
	}

	@Override
	public void onResume() {
		super.onResume();

		MobclickAgent.onResume(this);

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

			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
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
				break;
			case 1:
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

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, "initHotData");

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

			}

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

	protected void getServiceData(String url, String interfaceName) {
		// TODO Auto-generated method stub

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, interfaceName);

		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}
	
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
