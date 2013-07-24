package com.joyplus.tv;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.banner.AdView;
import com.joyplus.tv.Service.Return.ReturnProgramView;
import com.joyplus.tv.database.TvDatabaseHelper;
import com.joyplus.tv.entity.CurrentPlayDetailData;
import com.joyplus.tv.entity.HotItemInfo;
import com.joyplus.tv.entity.URLS_INDEX;
import com.joyplus.tv.ui.ArcView;
import com.joyplus.tv.utils.BangDanConstant;
import com.joyplus.tv.utils.DBUtils;
import com.joyplus.tv.utils.DataBaseItems.UserHistory;
import com.joyplus.tv.utils.DataBaseItems.UserShouCang;
import com.joyplus.tv.utils.DefinationComparatorIndex;
import com.joyplus.tv.utils.JieMianConstant;
import com.joyplus.tv.utils.Log;
import com.joyplus.tv.utils.SouceComparatorIndex1;
import com.joyplus.tv.utils.UtilTools;
import com.umeng.analytics.MobclickAgent;

public class VideoPlayerJPActivity extends Activity implements
		MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
		MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
		MediaPlayer.OnInfoListener, MediaPlayer.OnSeekCompleteListener,
		MediaPlayer.OnVideoSizeChangedListener, OnSeekBarChangeListener,
		OnClickListener, AdListener {

	private static final String TAG = "VideoPlayerActivity";

	private static final int MESSAGE_RETURN_DATE_OK = 0;
	private static final int MESSAGE_URLS_READY = MESSAGE_RETURN_DATE_OK + 1;
	private static final int MESSAGE_PALY_URL_OK = MESSAGE_URLS_READY + 1;
	private static final int MESSAGE_URL_NEXT = MESSAGE_PALY_URL_OK + 1;
	private static final int MESSAGE_UPDATE_PROGRESS = MESSAGE_URL_NEXT + 1;
	private static final int MESSAGE_HIDE_PROGRESSBAR = MESSAGE_UPDATE_PROGRESS + 1;
	private static final int MESSAGE_HIDE_VOICE = MESSAGE_HIDE_PROGRESSBAR + 1;

	/**
	 * 数据加载
	 */
	private static final int STATUE_LOADING = 0;
	/**
	 * 播放
	 */
	private static final int STATUE_PLAYING = STATUE_LOADING + 1;
	/**
	 * 暂停
	 */
	private static final int STATUE_PAUSE = STATUE_PLAYING + 1;
	/**
	 * 快退、快进
	 */
	private static final int STATUE_FAST_DRAG = STATUE_PAUSE + 1;

	private static final int OFFSET = 33;
	private int seekBarWidthOffset = 40;

	private TextView mVideoNameText; // 名字
	private ImageView mDefinationIcon;// 清晰度icon
	private SeekBar mSeekBar; // 进度条
	private RelativeLayout mTimeLayout; // 时间提示块
	private TextView mCurrentTimeTextView; // 当前播放时间
	private TextView mTotalTimeTextView; // 总时长
	private RelativeLayout mFastIcon; // 快进（退）标识图标
	private TextView mFastTextView; // 快进（退）标识提示

	private TextView mLastTimeTextView;// 上次播放时间
	private TextView mResourceTextView;// 视频来源
	private TextView mSpeedTextView;// 网速
	private TextView mPercentTextView;// 完成百分比

	private ImageButton mPreButton;// 上一集
	private ImageButton mNextButton;// 下一集
	private ImageButton mTopButton;// 上面的（继续）按钮
	private ImageButton mBottomButton;// 上面的（收藏）按钮
	private ImageButton mCenterButton;// 中间的按钮

	private ImageButton mContinueButton;// 继续

	private ArcView mVoiceProgress; // 声音大小显示

	/**
	 * 预加载层
	 */
	private RelativeLayout mPreLoadLayout;
	/**
	 * 播放提示相关层
	 */
	private RelativeLayout mNoticeLayout;
	/**
	 * 上下集控制层
	 */
	private LinearLayout mControlLayout;
	/**
	 * 声音相关层
	 */
	private LinearLayout mVocieLayout;

	/**
	 * 暂停继续层
	 */
	private LinearLayout mContinueLayout;

	/**
	 * 基本播放参数
	 */
	private String mProd_id;
	private String mProd_name;
	private int mProd_type;
	private String mProd_src;// 来源
	
	private String url_temp;//首次url备份
	private int mDefination = 0; // 清晰度 6为尝鲜，7为普清，8为高清
	private String mProd_sub_name = null;
	private int mEpisodeIndex = -1; // 当前集数对应的index
	private long lastTime = 0;

	/**
	 * 收藏
	 */
	private boolean isShoucang = false;// 默认为没有收藏

	/**
	 * 网络数据
	 */
	private int currentPlayIndex;
	private String currentPlayUrl;
	private ReturnProgramView m_ReturnProgramView = null;
	private List<URLS_INDEX> playUrls = new ArrayList<URLS_INDEX>();

	private AQuery aq;
	private App app;

	private long mStartRX = 0;
	private long rxByteslast = 0;
	private long mLoadingPreparedPercent = 0;

	private int mStatue = 0;

	private int mTimeJumpSpeed = 0;
	private int mFastJumpTime = 0;
	int[] mTimes = { 1000, 333, 40 };

	/**
	 * android本身VideoView
	 */
	private VideoView mVideoView;

	private AudioManager mAudioManager;

	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	
	private Animation mAlphaDispear;
	private boolean isSeekBarIntoch = false;
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * blew is adkey varies
	 * @author yyc
	 */
	private RelativeLayout layout;
	private AdView mAdView;
//	private String publisherId = "5b702aa315663879a582097a36ba0cdc";//要显示广告的publisherId
//	private boolean animation = true;//该广告加载时是否用动画效果
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(Constant.VIDEOPLAYERCMD)) {
				int mCMD = intent.getIntExtra("cmd", 0);
				Log.d(TAG, "onReceive------>" + mCMD);
				String mContent = intent.getStringExtra("content");
				String mProd_url = intent.getStringExtra("prod_url");
				if (!mProd_url.equalsIgnoreCase(url_temp)){
					Log.d(TAG, "mProd_url != url_temp");
					return ;
				}
				
				
				/*
				 * “403”：视频推送后，手机发送播放指令。 “405”：视频推送后，手机发送暂停指令。
				 * “407”：视频推送后，手机发送快进指令。 “409”：视频推送后，手机发送后退指令。
				 */
				switch (mCMD) {
				case 403:
					if (!mVideoView.isPlaying()) {
						mStatue = STATUE_PLAYING;
						mSeekBar.setEnabled(true);
						mVideoView.start();
						mContinueLayout.setVisibility(View.GONE);
						mControlLayout.setVisibility(View.GONE);
						mHandler.sendEmptyMessageDelayed(
								MESSAGE_HIDE_PROGRESSBAR, 2500);
					}
					break;
				case 405:
					if (mVideoView.isPlaying()) {
						mVideoView.pause();
						mStatue = STATUE_PAUSE;
						mSeekBar.setEnabled(false);
						mNoticeLayout.setVisibility(View.VISIBLE);
						mContinueLayout.setVisibility(View.VISIBLE);
						mContinueButton.requestFocus();
					}
					break;
				case 407:
					if (Integer.parseInt(mContent) <= mVideoView.getDuration()) {
						int destination = Integer.parseInt(mContent);
						if (destination < mVideoView.getDuration()) {
							mVideoView.seekTo(destination);
						}
						mNoticeLayout.setVisibility(View.VISIBLE);
						mHandler.sendEmptyMessageDelayed(
								MESSAGE_HIDE_PROGRESSBAR, 2500);
						// mVideoView.seekTo(c)
						// if (mPlayer.getDuration() -
						// Integer.parseInt(mContent) < 10000
						// && mCurrentPlayData.prod_type != 1)// 下一集
						// mPlayer.OnContinueVideoPlay();
						// else
						// mPlayer.onSeekMove(Integer.parseInt(mContent));
					}
					break;
				case 409:
					finish();
					break;
				}

			} else {
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.i(TAG, "onCreate--->");
		setContentView(R.layout.video_player_main);
		aq = new AQuery(this);
		app = (App) getApplication();
		mAlphaDispear = AnimationUtils.loadAnimation(this, R.anim.alpha_disappear);
		//广告位初始化
		layout = (RelativeLayout)findViewById(R.id.adsdkContent);
		
//		if(UtilTools.getIsShowAd(getApplicationContext())) {
			
			if (mAdView != null) {
				removeBanner();
				if(layout!=null)
					layout.setVisibility(View.GONE);
			}
//		}
		
		initViews();
		mSeekBar.setEnabled(false);
		m_ReturnProgramView = app.get_ReturnProgramView();
		initVedioDate();

		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		winParams.buttonBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
		// winParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		win.setAttributes(winParams);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.VIDEOPLAYERCMD);
		registerReceiver(mReceiver, intentFilter);

		// 获取是否收藏
		getIsShoucangData();
	}
	
	private void dismissView(View v){
		v.setVisibility(View.GONE);
		v.startAnimation(mAlphaDispear);
	}

	private void initVedioDate() {
		mStatue = STATUE_LOADING;
		mSeekBar.setEnabled(false);
		mSeekBar.setProgress(0);
		mTotalTimeTextView.setText("--:--");
		mPreLoadLayout.setVisibility(View.VISIBLE);
		mNoticeLayout.setVisibility(View.VISIBLE);
		mContinueLayout.setVisibility(View.GONE);
		mControlLayout.setVisibility(View.GONE);
		mStartRX = TrafficStats.getTotalRxBytes();// 获取网络速度
		if (mStartRX == TrafficStats.UNSUPPORTED) {
			mSpeedTextView
					.setText("Your device does not support traffic stat monitoring.");
		} else {

			mHandler.postDelayed(mLoadingRunnable, 500);
		}
		// 点击某部影片播放时，会全局设置CurrentPlayData
		CurrentPlayDetailData playDate = app.getmCurrentPlayDetailData();
		if (playDate == null) {// 如果不设置就不播放
			Log.e(TAG, "playDate----->null");
			finish();
			return;
		}
		// 初始化基本播放数据
		mProd_id = playDate.prod_id;
		mProd_type = playDate.prod_type;
		mProd_name = playDate.prod_name;
		mProd_sub_name = playDate.prod_sub_name;
		currentPlayUrl = playDate.prod_url;
		url_temp = playDate.prod_url;
		mDefination = playDate.prod_qua;
		lastTime = (int) playDate.prod_time;
		mProd_src = playDate.prod_src;

		Log.d(TAG, "name ----->" + mProd_name);
		Log.d(TAG, "currentPlayUrl ----->" + currentPlayUrl);
		
		if(mDefination == 0){
			mDefination = 8;
		}
		
		// 更新播放来源和上次播放时间
		updateSourceAndTime();
		updateName();
		if (currentPlayUrl != null && URLUtil.isNetworkUrl(currentPlayUrl)) {
			if (mProd_type<0) {
//				mHandler.sendEmptyMessage(MESSAGE_PALY_URL_OK);
				new Thread(new UrlRedirectTask()).start();
			} else {
				if (m_ReturnProgramView != null) {// 如果不为空，获取服务器返回的详细数据

					mHandler.sendEmptyMessage(MESSAGE_RETURN_DATE_OK);
				} else {// 如果为空，就重新获取

					getProgramViewDetailServiceData();
				}
			}
		} else {
			if (m_ReturnProgramView != null) {// 如果不为空，获取服务器返回的详细数据

				m_ReturnProgramView = app.get_ReturnProgramView();
				mHandler.sendEmptyMessage(MESSAGE_RETURN_DATE_OK);
			} else {// 如果为空，就重新获取

				getProgramViewDetailServiceData();
			}
		}
		
		Log.d(TAG, "defination----->" + mDefination);
		if(lastTime<=0){
			String lastTimeStr = DBUtils.getDuartion4HistoryDB(
					getApplicationContext(),
					UtilTools.getCurrentUserId(getApplicationContext()), mProd_id,mProd_sub_name);
			Log.i(TAG, "DBUtils.getDuartion4HistoryDB-->lastTimeStr:" + lastTimeStr);

			if (lastTimeStr != null && !lastTimeStr.equals("")) {

				try {
					long tempTime = Integer.valueOf(lastTimeStr);
					Log.i(TAG, "DBUtils.getDuartion4HistoryDB-->time:" + tempTime);
					if (tempTime != 0) {

						lastTime = tempTime * 1000;
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MESSAGE_RETURN_DATE_OK:
				new Thread(new PrepareTask()).start();
				break;
			case MESSAGE_URLS_READY:// url 准备好了
				if(playUrls.size()<=0){
					if(!VideoPlayerJPActivity.this.isFinishing()){
						showDialog(0);
					}
					return;
				}
				currentPlayIndex = 0;
				currentPlayUrl = playUrls.get(currentPlayIndex).url;
				mProd_src = playUrls.get(currentPlayIndex).source_from;
				if (currentPlayUrl != null
						&& URLUtil.isNetworkUrl(currentPlayUrl)) {
					// 地址跳转相关。。。
					new Thread(new UrlRedirectTask()).start();
//					mHandler.sendEmptyMessage(MESSAGE_PALY_URL_OK);
					// 要根据不同的节目做相应的处理。这里仅仅是为了验证上下集
				}
				break;
			case MESSAGE_URL_NEXT:
				if (playUrls.size() <= 0) {
					if (app.get_ReturnProgramView() != null) {
						m_ReturnProgramView = app.get_ReturnProgramView();
						mHandler.sendEmptyMessage(MESSAGE_RETURN_DATE_OK);
					} else {
						if (mProd_type > 0 && !"-1".equals(mProd_id)
								&& mProd_id != null) {
							getProgramViewDetailServiceData();
						}
					}
				} else {
					if (currentPlayIndex < playUrls.size() - 1) {
						currentPlayIndex += 1;
						currentPlayUrl = playUrls.get(currentPlayIndex).url;
						mProd_src = playUrls.get(currentPlayIndex).source_from;
						if (currentPlayUrl != null
								&& URLUtil.isNetworkUrl(currentPlayUrl)) {
							// 地址跳转相关。。。
							Log.d(TAG, currentPlayUrl);
							Log.d(TAG, mProd_src);
							new Thread(new UrlRedirectTask()).start();
//							mHandler.sendEmptyMessage(MESSAGE_PALY_URL_OK);
						}
					} else {
						// 所有的片源都不能播放
						Log.e(TAG, "no url can play!");
						if(!VideoPlayerJPActivity.this.isFinishing()){
							showDialog(0);
							
							//所有url不能播放，向服务器传递-1
							saveToServer(-1, 0);
						}
					}
				}
				break;
			case MESSAGE_PALY_URL_OK:
				updateName();
				updateSourceAndTime();
				mVideoView.setVideoURI(Uri.parse(currentPlayUrl));
				if (lastTime > 0) {
					mVideoView.seekTo((int) lastTime);
				}
				mVideoView.start();
				break;
			case MESSAGE_UPDATE_PROGRESS:
				updateSeekBar();
				break;
			case MESSAGE_HIDE_PROGRESSBAR:
				dismissView(mNoticeLayout);
//				mNoticeLayout.setVisibility(View.GONE);
				break;
			case MESSAGE_HIDE_VOICE:
				dismissView(mVocieLayout);
//				mVocieLayout.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};

	private void updateName() {
		switch (mProd_type) {
		case -1:
		case 1:
			mVideoNameText.setText(mProd_name);
			break;
		case 2:
		case 131:
			mVideoNameText.setText(mProd_name + " 第" + mProd_sub_name + "集");
			break;
		case 3:
			mVideoNameText.setText(mProd_name + " " + mProd_sub_name);
			break;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		Log.i(TAG, "onStart--->");
		
		super.onStart();
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}

	private void onVolumeSlide(int index) {
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
		int mAngle = index * 360 / mMaxVolume;
		// 变更进度条
		if (index == 0)
			mVoiceProgress.setBackgroundResource(R.drawable.player_volume_mute);
		else {
			mVoiceProgress.setBackgroundResource(R.drawable.player_volume);

		}
		mVoiceProgress.SetAngle(mAngle);

	}

	private void initViews() {
		mVideoNameText = (TextView) findViewById(R.id.tv_play_name);
		mDefinationIcon = (ImageView) findViewById(R.id.iv_1080_720);
		mSeekBar = (SeekBar) findViewById(R.id.sb_seekbar);
		mTimeLayout = (RelativeLayout) findViewById(R.id.rl_popup_time);
		mCurrentTimeTextView = (TextView) findViewById(R.id.tv_popup_time_current_time);
		mTotalTimeTextView = (TextView) findViewById(R.id.tv_total_time);
		mFastIcon = (RelativeLayout) findViewById(R.id.rl_popup_time_fast);
		mFastTextView = (TextView) findViewById(R.id.tv_popup_time_fast);

		mLastTimeTextView = (TextView) findViewById(R.id.tv_preload_bofang_record);
		mResourceTextView = (TextView) findViewById(R.id.tv_preload_source_laizi);// 视频来源
		mSpeedTextView = (TextView) findViewById(R.id.tv_preload_network_kb);
		mPercentTextView = (TextView) findViewById(R.id.tv_preload_network_accomplish);

		mPreButton = (ImageButton) findViewById(R.id.ib_control_left);
		mNextButton = (ImageButton) findViewById(R.id.ib_control_right);
		mTopButton = (ImageButton) findViewById(R.id.ib_control_top);
		mBottomButton = (ImageButton) findViewById(R.id.ib_control_bottom);
		mCenterButton = (ImageButton) findViewById(R.id.ib_control_center);
		mContinueButton = (ImageButton) findViewById(R.id.btn_continue);

		mPreButton.setOnClickListener(this);
		mNextButton.setOnClickListener(this);
		mTopButton.setOnClickListener(this);
		mBottomButton.setOnClickListener(this);
		mCenterButton.setOnClickListener(this);
		mContinueButton.setOnClickListener(this);

		mVoiceProgress = (ArcView) findViewById(R.id.av_volume);

		mPreLoadLayout = (RelativeLayout) findViewById(R.id.rl_preload);
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// opt.inPreferredConfig = Bitmap.Config.RGB_565; // Each pixel is
		// stored 2 bytes
		// opt.inPreferredConfig = Bitmap.Config.ARGB_8888; //Each pixel is
		// stored 4 bytes

		opt.inTempStorage = new byte[16 * 1024];
		opt.inPurgeable = true;
		opt.inInputShareable = true;

		try {
			mPreLoadLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(
					getResources(), R.drawable.player_bg, opt)));
		} catch (OutOfMemoryError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mNoticeLayout = (RelativeLayout) findViewById(R.id.rl_titile_seekbar);
		mControlLayout = (LinearLayout) findViewById(R.id.ll_control_buttons);
		mVocieLayout = (LinearLayout) findViewById(R.id.ll_volume);
		mContinueLayout = (LinearLayout) findViewById(R.id.ll_continue);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setOnErrorListener(this);
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(mStatue == STATUE_PLAYING){
					mHandler.removeMessages(MESSAGE_HIDE_PROGRESSBAR);
					mNoticeLayout.setVisibility(View.VISIBLE);
					mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_PROGRESSBAR, 2500);	
				}
				return false;
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "keycode ---------->" + keyCode);
		Log.d(TAG, "mStatue ---------->" + mStatue);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_ESCAPE:
			switch (mStatue) {
			case STATUE_LOADING:
				finish();
				return true;
			case STATUE_PLAYING:
				if (mProd_type == 2 || mProd_type == 131 || mProd_type == 3) {
					showControlLayout();
					return true;
				} else {
					// mVideoView.stopPlayback();
					setResult2Xiangqing();
					finish();
				}
				break;
			case STATUE_PAUSE:
				return true;
			case STATUE_FAST_DRAG:
				mTimeJumpSpeed = 0;
				upDateFastTimeBar();
				mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS);
				mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS, 1000);
				mStatue = STATUE_PLAYING;
				mSeekBar.setProgress(mVideoView.getCurrentPosition());
				mSeekBar.setEnabled(true);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			switch (mStatue) {
			case STATUE_PLAYING:
				
//				if(UtilTools.getIsShowAd(getApplicationContext())) {
					
					/*
					 * 显示banner
					 */
					showBanner();
					layout.setVisibility(View.VISIBLE);
//				}
				
				mVocieLayout.setVisibility(View.GONE);
				mHandler.removeMessages(MESSAGE_HIDE_VOICE);
				mStatue = STATUE_PAUSE;
				mSeekBar.setEnabled(false);
				mVideoView.pause();
				mHandler.removeMessages(MESSAGE_HIDE_PROGRESSBAR);
				mContinueLayout.setVisibility(View.VISIBLE);
				mNoticeLayout.setVisibility(View.VISIBLE);
				mContinueButton.requestFocus();
				break;
			case STATUE_FAST_DRAG:
				if (mFastJumpTime < mVideoView.getDuration()) {
					mVideoView.seekTo(mFastJumpTime);
					mSeekBar.setProgress(mFastJumpTime);
				}else{
					mSeekBar.setProgress(mVideoView.getCurrentPosition());
				}
				mTimeJumpSpeed = 0;
				upDateFastTimeBar();
				mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS);
				mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS, 1000);
				mStatue = STATUE_PLAYING;
				mSeekBar.setEnabled(true);
				break;
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (mStatue == STATUE_PLAYING) {
				mHandler.removeMessages(MESSAGE_HIDE_VOICE);
				mVolume = mAudioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				if (mVolume < 0) {
					mVolume = 0;
				}
				mVocieLayout.setVisibility(View.VISIBLE);
				mVolume++;
				onVolumeSlide(mVolume);
				mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_VOICE, 2500);
			}
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			if (mStatue == STATUE_PLAYING) {
				mHandler.removeMessages(MESSAGE_HIDE_VOICE);
				mVolume = mAudioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				if (mVolume < 0) {
					mVolume = 0;
				}
				mVocieLayout.setVisibility(View.VISIBLE);
				mVolume++;
				onVolumeSlide(mVolume);
				mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_VOICE, 2500);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (mStatue == STATUE_PLAYING) {
				mHandler.removeMessages(MESSAGE_HIDE_VOICE);
				mVolume = mAudioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				if (mVolume < 0) {
					mVolume = 0;
				}
				mVocieLayout.setVisibility(View.VISIBLE);
				mVolume--;
				onVolumeSlide(mVolume);
				mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_VOICE, 2500);
			}
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (mStatue == STATUE_PLAYING) {
				mHandler.removeMessages(MESSAGE_HIDE_VOICE);
				mVolume = mAudioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				if (mVolume < 0) {
					mVolume = 0;
				}
				mVocieLayout.setVisibility(View.VISIBLE);
				mVolume--;
				onVolumeSlide(mVolume);
				mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_VOICE, 2500);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (mStatue == STATUE_PLAYING) {
				mStatue = STATUE_FAST_DRAG;
				mSeekBar.setEnabled(false);
				mTimeJumpSpeed = -1;
				mFastJumpTime = (int) mVideoView.getCurrentPosition();
				upDateFastTimeBar();
				return true;
			} else if (mStatue == STATUE_FAST_DRAG) {
				switch (mTimeJumpSpeed) {
				case -1:
				case -2:
					mTimeJumpSpeed -= 1;
					break;
				case 1:
					mTimeJumpSpeed = -1;
					break;
				case 2:
				case 3:
					mTimeJumpSpeed = 1;
				}
				upDateFastTimeBar();
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (mStatue == STATUE_PLAYING) {
				mStatue = STATUE_FAST_DRAG;
				mSeekBar.setEnabled(false);
				mTimeJumpSpeed = 1;
				mFastJumpTime = (int) mVideoView.getCurrentPosition();
				upDateFastTimeBar();
				return true;
			} else if (mStatue == STATUE_FAST_DRAG) {
				switch (mTimeJumpSpeed) {
				case 1:
				case 2:
					mTimeJumpSpeed += 1;
					break;
				case -1:
					mTimeJumpSpeed = 1;
					break;
				case -2:
				case -3:
					mTimeJumpSpeed = -1;
				}
				upDateFastTimeBar();
				return true;
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showControlLayout() {
		// 判断上下集能不能用
		Log.d(TAG, "mEpisodeIndex----->" + mEpisodeIndex);
		if (mProd_type == 3) {
			if (mEpisodeIndex > 0&&m_ReturnProgramView.show.episodes[mEpisodeIndex-1].down_urls!=null) {
				mNextButton.setEnabled(true);
				mNextButton.setFocusable(true);
			} else {
				mNextButton.setEnabled(false);
				mNextButton.setFocusable(false);
			}

			if (mEpisodeIndex < (m_ReturnProgramView.show.episodes.length - 1)&&m_ReturnProgramView.show.episodes[mEpisodeIndex+1].down_urls!=null) {
				mPreButton.setEnabled(true);
				mPreButton.setFocusable(true);
			} else {
				mPreButton.setEnabled(false);
				mPreButton.setFocusable(false);
			}

		} else {
			if (mEpisodeIndex > 0&&m_ReturnProgramView.tv.episodes[mEpisodeIndex-1].down_urls!=null) {
				mPreButton.setEnabled(true);
				mPreButton.setFocusable(true);
			} else {
				mPreButton.setEnabled(false);
				mPreButton.setFocusable(false);
			}

			if (mEpisodeIndex < (m_ReturnProgramView.tv.episodes.length - 1)&&m_ReturnProgramView.tv.episodes[mEpisodeIndex+1].down_urls!=null) {
				mNextButton.setEnabled(true);
				mNextButton.setFocusable(true);
			} else {
				mNextButton.setEnabled(false);
				mNextButton.setFocusable(false);
			}
		}

		if (isShoucang) {

			mBottomButton.setBackgroundResource(R.drawable.player_btn_unfav);
		} else {

			mBottomButton.setBackgroundResource(R.drawable.player_btn_fav);
		}

//		if(UtilTools.getIsShowAd(getApplicationContext())) {
			
			/*
			 * 显示banner
			 */
			showBanner();
			layout.setVisibility(View.VISIBLE);
//		}
		
		mVocieLayout.setVisibility(View.GONE);
		mHandler.removeMessages(MESSAGE_HIDE_VOICE);
		mStatue = STATUE_PAUSE;
		mSeekBar.setEnabled(false);
		mVideoView.pause();
		mHandler.removeMessages(MESSAGE_HIDE_PROGRESSBAR);
		mControlLayout.setVisibility(View.VISIBLE);
		mNoticeLayout.setVisibility(View.VISIBLE);
		mCenterButton.requestFocus();
		// if( getCurrentFocus().getId() != mSeekBar.getId()) {
		//
		// mSeekBar.requestFocus();
		// }
		//
		// Log.d(TAG,"FOUCED ID -->" + getCurrentFocus().getId());
		// mHandler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// mTopButton.requestFocus();
		// }
		// }, 200);
	}

	private void upDateFastTimeBar() {
		if (mTimeJumpSpeed > 0) {
			mFastIcon.setVisibility(View.VISIBLE);
			mFastIcon.setBackgroundResource(R.drawable.play_time_right);
			mFastTextView.setText("x" + Math.abs(mTimeJumpSpeed));
			mHandler.removeMessages(MESSAGE_HIDE_PROGRESSBAR);
			mNoticeLayout.setVisibility(View.VISIBLE);
		} else if (mTimeJumpSpeed < 0) {
			mFastIcon.setVisibility(View.VISIBLE);
			mFastIcon.setBackgroundResource(R.drawable.play_time_left);
			mFastTextView.setText("x" + Math.abs(mTimeJumpSpeed));
			mHandler.removeMessages(MESSAGE_HIDE_PROGRESSBAR);
			mNoticeLayout.setVisibility(View.VISIBLE);
		} else if (mTimeJumpSpeed == 0) {
			mFastIcon.setVisibility(View.GONE);
			mFastTextView.setText("");
			mHandler.removeMessages(MESSAGE_HIDE_PROGRESSBAR);
			mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_PROGRESSBAR, 2500);
		}
		// mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS);
		// mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS, 500);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		// 播放有问题 选下一个地址
		mHandler.sendEmptyMessage(MESSAGE_URL_NEXT);
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// 播放完成
		autoPlayNext();
	}
	
	private void autoPlayNext(){
		switch (mProd_type) {
		case 1:
			finish();
			break;
		case 2:
		case 131:
			if(mEpisodeIndex<m_ReturnProgramView.tv.episodes.length-1){
				playNext();
			}else{
				setResult2Xiangqing();//返回集数和是否收藏
				finish();
			}
			break;
		case 3:
			if(mEpisodeIndex>0){
				playNext();
			}else{
				setResult2Xiangqing();//返回集数和是否收藏
				finish();
			}
			break;
		default:
			finish();
			break;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// 准备好了
		mTotalTimeTextView.setText(UtilTools.formatDuration(mVideoView
				.getDuration()));
		mSeekBar.setMax((int) mVideoView.getDuration());
		mSeekBar.setOnSeekBarChangeListener(VideoPlayerJPActivity.this);
		mSeekBar.setProgress((int) lastTime);
		mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS, 1000);
//		mHandler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//			}
//		}, 500);
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// 快进好了（拖动） 系统不支持？
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		// 缓冲进度
	}

	private void updateSeekBar() {
		switch (mStatue) {
		case STATUE_LOADING:
			long current = mVideoView.getCurrentPosition();// 当前进度
			long lastProgress = mSeekBar.getProgress();
//			Log.d(TAG, "loading --->" + current);
			// updateTimeNoticeView(mSeekBar.getProgress());
			if(current>lastProgress){
				hidePreLoad(); 
			}else{
				mSeekBar.setProgress((int) current);
				mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS, 1000);
			}
			break;
		case STATUE_PLAYING:
			if(!isSeekBarIntoch){
				long current1 = mVideoView.getCurrentPosition();// 当前进度
				mSeekBar.setProgress((int) current1);
				// updateTimeNoticeView(mSeekBar.getProgress());
			}
			mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS, 1000);
			break;
		case STATUE_FAST_DRAG:
			if (mTimeJumpSpeed > 0) {
				mFastJumpTime = (int) (mFastJumpTime + (mVideoView
						.getDuration() / 500));
			} else if (mTimeJumpSpeed < 0) {
				mFastJumpTime = (int) (mFastJumpTime - (mVideoView
						.getDuration() / 500));
			}

			if (mFastJumpTime > mVideoView.getDuration()) {
				mFastJumpTime = (int) mVideoView.getDuration();
			}
			if (mFastJumpTime < 0) {
				mFastJumpTime = 0;
			}
			mSeekBar.setProgress(mFastJumpTime);
			// updateTimeNoticeView(mSeekBar.getProgress());
			mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS,
					mTimes[Math.abs(mTimeJumpSpeed) - 1]);
			break;
		default:
			mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS, 1000);
			break;
		}
	}

	private void updateTimeNoticeView(int progress) {
		RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		parms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

		double mLeft = (double) progress / mVideoView.getDuration()
				* (mSeekBar.getMeasuredWidth() - seekBarWidthOffset) + OFFSET;

		if (progress > 0)
			parms.leftMargin = (int) mLeft;
		else
			parms.leftMargin = OFFSET;
		parms.bottomMargin = 20 + 10;
		mTimeLayout.setLayoutParams(parms);

		mCurrentTimeTextView.setText(UtilTools.formatDuration(progress));
		mCurrentTimeTextView.setVisibility(View.VISIBLE);
	}

	private final Runnable mLoadingRunnable = new Runnable() {
		long beginTimeMillis, timeTakenMillis, m_bitrate;

		public void run() {

			// long txBytes = TrafficStats.getTotalTxBytes()- mStartTX;
			// TX.setText(Long.toString(txBytes));
			long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;

			timeTakenMillis = System.currentTimeMillis() - beginTimeMillis;
			beginTimeMillis = System.currentTimeMillis();
			if(timeTakenMillis!=0){
				m_bitrate = ((rxBytes - rxByteslast) * 8 * 1000 / timeTakenMillis) / 8000;
				rxByteslast = rxBytes;

				mSpeedTextView.setText("（" + Long.toString(m_bitrate) + "kb/s）");
				mLoadingPreparedPercent = mLoadingPreparedPercent + m_bitrate;
				if (mLoadingPreparedPercent >= 100
						&& mLoadingPreparedPercent / 100 < 100)
					mPercentTextView.setText(", 已完成"
							+ Long.toString(mLoadingPreparedPercent / 100) + "%");

				// Fun_downloadrate();
			}
			mHandler.postDelayed(mLoadingRunnable, 500);
		}
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		updateTimeNoticeView(mSeekBar.getProgress());
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		isSeekBarIntoch = true;
		mHandler.removeMessages(MESSAGE_HIDE_PROGRESSBAR);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		isSeekBarIntoch = false;
		mVideoView.seekTo(mSeekBar.getProgress());
		mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_PROGRESSBAR, 2500);
//		mHandler.re
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_control_top:
//			mControlLayout.setVisibility(View.GONE);
			dismissView(mControlLayout);
			mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_PROGRESSBAR, 2500);
			mStatue = STATUE_PLAYING;
			mSeekBar.setEnabled(true);
			mVideoView.requestFocus();
			mVideoView.start();
			
//			if(UtilTools.getIsShowAd(getApplicationContext())) {
				
				removeBanner();
				if(layout!=null)	
					layout.setVisibility(View.GONE);
//			}

			break;
		case R.id.btn_continue:
//			mContinueLayout.setVisibility(View.GONE);
			dismissView(mContinueLayout);
			mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_PROGRESSBAR, 2500);
			mStatue = STATUE_PLAYING;
			mSeekBar.setEnabled(true);
			mVideoView.requestFocus();
			mVideoView.start();
			removeBanner();
			if(layout!=null)
				layout.setVisibility(View.GONE);
			break;
		case R.id.ib_control_center:
			// mVideoView.stopPlayback();
			setResult2Xiangqing();
			finish();
			break;
		case R.id.ib_control_left:
			playPrevious();
			break;
		case R.id.ib_control_right:
			playNext();
			break;
		case R.id.ib_control_bottom:
			if (!isShoucang) {
				String url = Constant.BASE_URL + "program/favority";

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("prod_id", mProd_id);

				AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
				cb.SetHeader(app.getHeaders());

				cb.params(params).url(url).type(JSONObject.class)
						.weakHandler(this, "favorityResult");
				aq.ajax(cb);
			} else {// 取消收藏
				String url = Constant.BASE_URL + "program/unfavority";

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("prod_id", mProd_id);

				AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
				cb.SetHeader(app.getHeaders());

				cb.params(params).url(url).type(JSONObject.class)
						.weakHandler(this, "unfavorityResult");

				aq.ajax(cb);
			}
			break;
		default:
			break;
		}
	}

	private void showLoading() {
		removeBanner();
		
//		if(UtilTools.getIsShowAd(getApplicationContext())) {
			
			if(layout!=null)
				layout.setVisibility(View.GONE);
//		}
		
		mLoadingPreparedPercent = 0;
		rxByteslast = 0;
		mStartRX = TrafficStats.getTotalRxBytes();
		if (mStartRX == TrafficStats.UNSUPPORTED) {
			mSpeedTextView
					.setText("Your device does not support traffic stat monitoring.");
		} else {
			mHandler.postDelayed(mLoadingRunnable, 500);
		}
		mPercentTextView.setText("已完成0%");
		mPreLoadLayout.setVisibility(View.VISIBLE);
		mNoticeLayout.setVisibility(View.VISIBLE);
//		mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_PROGRESSBAR, 2500);
	}

	private void playNext() {
		// TODO Auto-generated method stub
		url_temp = null;
		mStatue = STATUE_LOADING;
		mSeekBar.setProgress(0);
		mSeekBar.setEnabled(false);
//		mTotalTimeTextView.setText(UtilTools.formatDuration(0));
		mTotalTimeTextView.setText("--:--");
		mHandler.removeCallbacksAndMessages(null);
		mControlLayout.setVisibility(View.GONE);
		lastTime = 0;
		mVideoView.stopPlayback();
		showLoading();
		if (mProd_type == 3) {
			mEpisodeIndex -= 1;
		} else {
			mEpisodeIndex += 1;
		}
		mHandler.sendEmptyMessage(MESSAGE_RETURN_DATE_OK);
	}

	private void playPrevious() {
		// TODO Auto-generated method stub
		url_temp = null;
		mStatue = STATUE_LOADING;
		mSeekBar.setProgress(0);
		mSeekBar.setEnabled(false);
//		mTotalTimeTextView.setText(UtilTools.formatDuration(0));
		mTotalTimeTextView.setText("--:--");
		mHandler.removeCallbacksAndMessages(null);
		mControlLayout.setVisibility(View.GONE);
		lastTime = 0;
		mVideoView.stopPlayback();
		showLoading();
		if (mProd_type == 3) {
			mEpisodeIndex += 1;
		} else {
			mEpisodeIndex -= 1;
		}
		mHandler.sendEmptyMessage(MESSAGE_RETURN_DATE_OK);
	}

	protected void getServiceData(String url, String interfaceName) {
		// TODO Auto-generated method stub

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.url(url).type(JSONObject.class).weakHandler(this, interfaceName);

		cb.SetHeader(app.getHeaders());

		Log.d(TAG, url);
		Log.d(TAG, "header appkey" + app.getHeaders().get("app_key"));

		aq.ajax(cb);
	}

	private void getProgramViewDetailServiceData() {
		// TODO Auto-generated method stub

		String url = Constant.BASE_URL + "program/view" + "?prod_id="
				+ mProd_id;
		getServiceData(url, "initMovieDate");
	}

	public void initMovieDate(String url, JSONObject json, AjaxStatus status) {

		if (status.getCode() == AjaxStatus.NETWORK_ERROR || json == null) {
			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));

			return;
		}

		if (json == null || json.equals(""))
			return;

		Log.d(TAG, "data = " + json.toString());
		ObjectMapper mapper = new ObjectMapper();
		try {
			m_ReturnProgramView = null;
			m_ReturnProgramView = mapper.readValue(json.toString(),
					ReturnProgramView.class);
			// 检测URL
			mHandler.sendEmptyMessage(MESSAGE_RETURN_DATE_OK);
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

	private void updateSourceAndTime() {
		Log.d(TAG, " ---- sre = " + mProd_src);
		if (mProd_src == null || mProd_src.length() == 1
				|| "null".equals(mProd_src)) {
			mResourceTextView.setText("");
		} else {
			String strSrc = "";
			if (mProd_src.equalsIgnoreCase("wangpan")) {
				strSrc = "PPTV";
			} else if (mProd_src.equalsIgnoreCase("le_tv_fee")) {
				strSrc = "乐  视";
			} else if (mProd_src.equalsIgnoreCase("letv")) {
				strSrc = "乐  视";
			} else if (mProd_src.equalsIgnoreCase("fengxing")) {
				strSrc = "风  行";
			} else if (mProd_src.equalsIgnoreCase("qiyi")) {
				strSrc = "爱  奇  艺";
			} else if (mProd_src.equalsIgnoreCase("youku")) {
				strSrc = "优  酷";
			} else if (mProd_src.equalsIgnoreCase("sinahd")) {
				strSrc = "新  浪  视  频";
			} else if (mProd_src.equalsIgnoreCase("sohu")) {
				strSrc = "搜  狐  视  频";
			} else if (mProd_src.equalsIgnoreCase("qq")) {
				strSrc = "腾  讯  视  频";
			} else if (mProd_src.equalsIgnoreCase("pptv")) {
				strSrc = "PPTV";
			} else if (mProd_src.equalsIgnoreCase("m1905")) {
				strSrc = "电  影  网";
			} else {
				strSrc = "PPTV";
			}
			mResourceTextView.setText(strSrc);
		}
		if(lastTime>0){
			mLastTimeTextView.setVisibility(View.VISIBLE);
			mLastTimeTextView.setText("上次播放: " + UtilTools.formatDuration(lastTime));
		}else{
			mLastTimeTextView.setVisibility(View.GONE);
		}
		if(playUrls.size()>0&&currentPlayIndex<=playUrls.size()-1){
			Log.d(TAG, "type---->" + playUrls.get(currentPlayIndex).defination_from_server);
			mDefinationIcon.setVisibility(View.VISIBLE);
			if(Constant.player_quality_index[0].equalsIgnoreCase(playUrls.get(currentPlayIndex).defination_from_server)){
				mDefinationIcon.setImageResource(R.drawable.player_1080p);
			}else if(Constant.player_quality_index[1].equalsIgnoreCase(playUrls.get(currentPlayIndex).defination_from_server)){
				mDefinationIcon.setImageResource(R.drawable.player_720p);
			}else{
				mDefinationIcon.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 把m_ReturnProgramView中数据转化成基本数据
	 * 
	 * @author Administrator
	 * 
	 */
	class PrepareTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			playUrls.clear();
			switch (mProd_type) {
			case 1:
				
				if(m_ReturnProgramView.movie != null) {
					
					mProd_name = m_ReturnProgramView.movie.name;
					
					if(m_ReturnProgramView.movie.episodes != null 
							&& m_ReturnProgramView.movie.episodes.length > 0 
							&& m_ReturnProgramView.movie.episodes[0].down_urls != null) {
						
						for (int i = 0; i < m_ReturnProgramView.movie.episodes[0].down_urls.length; i++) {
							
							if(m_ReturnProgramView.movie.episodes[0].down_urls[i] != null) {
								
								String souces = m_ReturnProgramView.movie.episodes[0].down_urls[i].source;
								
								if(m_ReturnProgramView.movie.episodes[0].down_urls[i].urls != null) {
									
									for (int j = 0; j < m_ReturnProgramView.movie.episodes[0].down_urls[i].urls.length; j++) {
										
										if(m_ReturnProgramView.movie.episodes[0].down_urls[i].urls[j] != null) {
											
											URLS_INDEX url = new URLS_INDEX();
											url.source_from = souces;
											url.defination_from_server = m_ReturnProgramView.movie.episodes[0].down_urls[i].urls[j].type;
											url.url = m_ReturnProgramView.movie.episodes[0].down_urls[i].urls[j].url;
											playUrls.add(url);
										}

									}
								}
							}
						}
					}

				}
				
				break;
			case 2:
			case 131:
				
				if(m_ReturnProgramView.tv != null) {
					
					mProd_name = m_ReturnProgramView.tv.name;
					
					if(m_ReturnProgramView.tv.episodes != null) {
						
						if (mEpisodeIndex == -1) {
							for (int i = 0; i < m_ReturnProgramView.tv.episodes.length; i++) {
								if (m_ReturnProgramView.tv.episodes[i] != null 
										&& mProd_sub_name
										.equals(m_ReturnProgramView.tv.episodes[i].name)) {
									mEpisodeIndex = i;
									if(m_ReturnProgramView.tv.episodes[i].down_urls == null){
										mHandler.sendEmptyMessage(MESSAGE_URLS_READY);
										return; 
									}
									for (int j = 0; j < m_ReturnProgramView.tv.episodes[i].down_urls.length; j++) {
										
										if(m_ReturnProgramView.tv.episodes[i].down_urls[j] != null) {
											
											String souces = m_ReturnProgramView.tv.episodes[i].down_urls[j].source;
											
											if(m_ReturnProgramView.tv.episodes[i].down_urls[j].urls != null) {
												
												for (int k = 0; k < m_ReturnProgramView.tv.episodes[i].down_urls[j].urls.length; k++) {
													
													if(m_ReturnProgramView.tv.episodes[i].down_urls[j].urls[k] != null) {
														
														URLS_INDEX url = new URLS_INDEX();
														url.source_from = souces;
														url.defination_from_server = m_ReturnProgramView.tv.episodes[i].down_urls[j].urls[k].type;
														url.url = m_ReturnProgramView.tv.episodes[i].down_urls[j].urls[k].url;
														playUrls.add(url);
													}
												}
											}
										}

									}
								}
							}
						} else {
							
							if(m_ReturnProgramView.tv.episodes.length > mEpisodeIndex
									&& m_ReturnProgramView.tv.episodes[mEpisodeIndex] != null) {
								
								if(m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls != null) {
									
									for (int j = 0; j < m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls.length; j++) {
										
										if(m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls[j] != null) {
											
											String souces = m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls[j].source;
											
											if( m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls[j].urls != null) {
												
												for (int k = 0; k < m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls[j].urls.length; k++) {
													
													if(m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls[j].urls[k] != null) {
														
														URLS_INDEX url = new URLS_INDEX();
														url.source_from = souces;
														url.defination_from_server = m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls[j].urls[k].type;
														url.url = m_ReturnProgramView.tv.episodes[mEpisodeIndex].down_urls[j].urls[k].url;
														playUrls.add(url);
													}
												}
											}
										}
									}
								}
								mProd_sub_name = m_ReturnProgramView.tv.episodes[mEpisodeIndex].name;
							}
						}
					}
				}
				
				break;
			case 3:
				
				if(m_ReturnProgramView.show != null) {
					
					mProd_name = m_ReturnProgramView.show.name;
					
					if(m_ReturnProgramView.show.episodes != null) {
						
						if (mEpisodeIndex == -1) {
							for (int i = 0; i < m_ReturnProgramView.show.episodes.length; i++) {
								
								if(m_ReturnProgramView.show.episodes[i] != null) {
									
									if (UtilTools.isSame4Str(mProd_sub_name, m_ReturnProgramView.show.episodes[i].name)) {
										mEpisodeIndex = i;
										mProd_sub_name = m_ReturnProgramView.show.episodes[i].name;
										if(m_ReturnProgramView.show.episodes[i].down_urls==null){
											mHandler.sendEmptyMessage(MESSAGE_URLS_READY);
											return ;
										}
										for (int j = 0; j < m_ReturnProgramView.show.episodes[i].down_urls.length; j++) {
											
											
											if(m_ReturnProgramView.show.episodes[i].down_urls[j] != null) {
												
												String souces = m_ReturnProgramView.show.episodes[i].down_urls[j].source;
												
												if(m_ReturnProgramView.show.episodes[i].down_urls[j].urls != null) {
													
													for (int k = 0; k < m_ReturnProgramView.show.episodes[i].down_urls[j].urls.length; k++) {
														
														if(m_ReturnProgramView.show.episodes[i].down_urls[j].urls[k] != null) {
															
															URLS_INDEX url = new URLS_INDEX();
															url.source_from = souces;
															url.defination_from_server = m_ReturnProgramView.show.episodes[i].down_urls[j].urls[k].type;
															url.url = m_ReturnProgramView.show.episodes[i].down_urls[j].urls[k].url;
															playUrls.add(url);
														}
													}
												}
											}
										}
									}
								}
							}
						} else {
							
							if(m_ReturnProgramView.show.episodes.length > mEpisodeIndex ) {
								
								if(m_ReturnProgramView.show.episodes[mEpisodeIndex] != null 
										&& m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls != null) {
									
									for (int j = 0; j < m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls.length; j++) {
										
										if(m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls[j] != null) {
											
											String souces = m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls[j].source;
											
											if(m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls[j].urls != null) {
												
												for (int k = 0; k < m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls[j].urls.length; k++) {
													
													if(m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls[j].urls[k] != null) {
														
														URLS_INDEX url = new URLS_INDEX();
														url.source_from = souces;
														url.defination_from_server = m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls[j].urls[k].type;
														url.url = m_ReturnProgramView.show.episodes[mEpisodeIndex].down_urls[j].urls[k].url;
														playUrls.add(url);
													}
												}
											}
										}

									}
								}
								mProd_sub_name = m_ReturnProgramView.show.episodes[mEpisodeIndex].name;
							}
						}
					}
				}
				break;
			}
			Log.d(TAG, "playUrls size ------->" + playUrls.size());
			for (int i = 0; i < playUrls.size(); i++) {
				URLS_INDEX url_index = playUrls.get(i);
				if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[0])) {
					url_index.souces = 0;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[1])) {
					url_index.souces = 1;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[2])) {
					url_index.souces = 2;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[3])) {
					url_index.souces = 3;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[4])) {
					url_index.souces = 4;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[5])) {
					url_index.souces = 5;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[6])) {
					url_index.souces = 6;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[7])) {
					url_index.souces = 7;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[8])) {
					url_index.souces = 8;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[9])) {
					url_index.souces = 9;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[10])) {
					url_index.souces = 10;
				} else if (url_index.source_from.trim().equalsIgnoreCase(
						Constant.video_index[11])) {
					url_index.souces = 11;
				} else {
					url_index.souces = 12;
				}
				switch (mDefination) {
				case BangDanConstant.GAOQING:// 高清
					if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[1])) {
						url_index.defination = 1;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[0])) {
						url_index.defination = 2;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[2])) {
						url_index.defination = 3;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[3])) {
						url_index.defination = 4;
					} else {
						url_index.defination = 5;
					}
					break;
				case BangDanConstant.CHAOQING:// 超清
					if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[0])) {
						url_index.defination = 1;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[1])) {
						url_index.defination = 2;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[2])) {
						url_index.defination = 3;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[3])) {
						url_index.defination = 4;
					} else {
						url_index.defination = 5;
					}
					break;
				case BangDanConstant.CHANGXIAN:// 标清
					if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[2])) {
						url_index.defination = 1;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[3])) {
						url_index.defination = 2;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[1])) {
						url_index.defination = 3;
					} else if (url_index.defination_from_server.trim()
							.equalsIgnoreCase(Constant.player_quality_index[0])) {
						url_index.defination = 4;
					} else {
						url_index.defination = 5;
					}
					break;
				}
				if (url_index.source_from
						.equalsIgnoreCase(Constant.BAIDU_WANGPAN)) {
					Document doc = null;
					try {
						doc = Jsoup.connect(url_index.url).timeout(10000).get();
						// doc = Jsoup.connect(htmlStr).timeout(10000).get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (doc != null) {
						Element e = doc.getElementById("fileDownload");
						if (e != null) {
							Log.d(TAG, "url = " + e.attr("href"));
							if (e.attr("href") != null
									&& e.attr("href").length() > 0) {
								url_index.url = e.attr("href");
							}
						}
					}
				}
			}
			if (playUrls.size() > 1) {
				Collections.sort(playUrls, new SouceComparatorIndex1());
				Collections.sort(playUrls, new DefinationComparatorIndex());
			}
			// url list 准备完成
			mHandler.sendEmptyMessage(MESSAGE_URLS_READY);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		
		Log.i(TAG, "onPause--->");
		
		// TODO Auto-generated method stub
		MobclickAgent.onPause(this);
		if (mProd_type > 0&&mStatue!=STATUE_LOADING) {
			// SaveToServer(mVideoView.getDuration(),
			// mVideoView.getCurrentPosition());
			long duration = mVideoView.getDuration();
			long curretnPosition = mVideoView.getCurrentPosition();
			Log.d(TAG, "duration ->" + duration);
			Log.d(TAG, "curretnPosition ->" + curretnPosition);
			if(duration-curretnPosition<10*1000){
				saveToServer(duration / 1000, (duration / 1000) -10);
			}else{
				saveToServer(duration / 1000, curretnPosition / 1000);
			}
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		
		Log.i(TAG, "onStop--->");
		
		// TODO Auto-generated method stub
		// if(mStatue != STATUE_LOADING&&mProd_type>0){
		// // SaveToServer(mVideoView.getDuration(),
		// mVideoView.getCurrentPosition());
		// long duration = mVideoView.getDuration();
		// long curretnPosition = mVideoView.getCurrentPosition();
		// Log.d(TAG, "duration ->" + duration);
		// Log.d(TAG, "curretnPosition ->" + curretnPosition);
		// SaveToServer(duration/1000, curretnPosition/1000);
		// }
		if(!isFinishing()){
			finish();
		}
		super.onStop();
	}

	public void saveToServer(long duration, long playBackTime) {
		String url = Constant.BASE_URL + "program/play";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("app_key", Constant.APPKEY);// required string
		params.put("prod_id", mProd_id);
		params.put("prod_name", mProd_name);// required
		params.put("prod_subname", mProd_sub_name);
		params.put("prod_type", mProd_type);// required int 视频类别
		params.put("play_type", "1");
		params.put("playback_time", playBackTime);// _time required int
		params.put("duration", duration);// required int 视频时长， 单位：秒
		params.put("video_url", currentPlayUrl);// required
		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		cb.SetHeader(app.getHeaders());
		cb.params(params).url(url).type(JSONObject.class)
				.weakHandler(this, "CallProgramPlayResult");
		aq.ajax(cb);

		// DB操作，把存储到服务器的数据保存到数据库
		TvDatabaseHelper helper = TvDatabaseHelper
				.newTvDatabaseHelper(getApplicationContext());
		SQLiteDatabase database = helper.getWritableDatabase();// 获取写db

		String selection = UserShouCang.USER_ID + "=? and "
				+ UserHistory.PRO_ID + "=?";// 通过用户id，找到相应信息
		String[] selectionArgs = {
				UtilTools.getCurrentUserId(getApplicationContext()), mProd_id };

		database.delete(TvDatabaseHelper.HISTORY_TABLE_NAME, selection,
				selectionArgs);

		HotItemInfo info = new HotItemInfo();
		info.prod_type = mProd_type + "";
		info.prod_name = mProd_name;

		info.prod_subname = mProd_sub_name;
		info.prod_id = mProd_id;
		info.play_type = "1";
		info.playback_time = playBackTime + "";
		info.video_url = currentPlayUrl;
		info.duration = duration + "";

		DBUtils.insertHotItemInfo2DB_History(getApplicationContext(), info,
				UtilTools.getCurrentUserId(getApplicationContext()), database);

		helper.closeDatabase();
		
		//发送更新最新记录广播
		app.set_ReturnProgramView(m_ReturnProgramView);
		Intent historyIntent  = new Intent(UtilTools.ACTION_PLAY_END_HISTORY);
		historyIntent.putExtra("prod_id", mProd_id);
		historyIntent.putExtra("prod_sub_name", mProd_sub_name);
		historyIntent.putExtra("prod_type", mProd_type);
		historyIntent.putExtra("time", playBackTime);
		sendBroadcast(historyIntent);
		
		Intent mainIntent  = new Intent(UtilTools.ACTION_PLAY_END_MAIN);
		mainIntent.putExtra("prod_id", mProd_id);
		mainIntent.putExtra("prod_sub_name", mProd_sub_name);
		mainIntent.putExtra("prod_type", mProd_type);
		mainIntent.putExtra("time", playBackTime);
		sendBroadcast(mainIntent);
		
	}
	
	private void setResult2Xiangqing() {
		
		Intent dataIntent = getIntent();
		dataIntent.putExtra("prod_subname", mProd_sub_name);
		
		if(isShoucang) {
			
			setResult(JieMianConstant.SHOUCANG_ADD,dataIntent);
		} else {
			
			setResult(JieMianConstant.SHOUCANG_CANCEL,dataIntent);
		}
	}

	public void CallProgramPlayResult(String url, JSONObject json,
			AjaxStatus status) {
		if (json != null) {
			Log.d(TAG, json.toString());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "--------on new Intent--------------");
		super.onNewIntent(intent);
		mHandler.removeCallbacksAndMessages(null);
		m_ReturnProgramView = null;
		if (mVideoView!=null) { 
			mVideoView.stopPlayback();
			mVideoView.resume();
		}
		lastTime = 0;
		rxByteslast = 0;
		mLoadingPreparedPercent = 0;
		mEpisodeIndex = -1;
		mPercentTextView.setText(", 已完成"
				+ Long.toString(mLoadingPreparedPercent / 100) + "%");
		initVedioDate();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		Log.i(TAG, "onDestroy--->");
		
		unregisterReceiver(mReceiver);
		if (mVideoView != null) {
			mVideoView.stopPlayback();
		}
		
		if(mPreLoadLayout.getBackground() != null) {
			
			UtilTools.recycleBitmap(((BitmapDrawable)mPreLoadLayout.getBackground()).getBitmap());
		}
		
		super.onDestroy();
	}

	private void getIsShoucangData() {
		String url = Constant.BASE_URL + "program/is_favority";
		// +"?prod_id=" + prod_id;
		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("prod_id", mProd_id);
		cb.params(params).url(url).type(JSONObject.class)
				.weakHandler(this, "initIsShoucangData");
		cb.SetHeader(app.getHeaders());
		aq.ajax(cb);
	}

	public void initIsShoucangData(String url, JSONObject json,
			AjaxStatus status) {

		if (status.getCode() == AjaxStatus.NETWORK_ERROR || json == null) {
			app.MyToast(aq.getContext(),
					getResources().getString(R.string.networknotwork));
			return;
		}

		if (json == null || json.equals(""))
			return;

		Log.d(TAG, "data = " + json.toString());

		String flag = json.toString();

		if (!flag.equals("")) {

			if (flag.contains("true")) {

				isShoucang = true;
			} else {

				isShoucang = false;
			}
		} else {

			isShoucang = true;
		}
	}

	public void favorityResult(String url, JSONObject json,
			AjaxStatus status) {

		if (json != null) {
			try {
				// woof is "00000",now "20024",by yyc
				if (json.getString("res_code").trim().equalsIgnoreCase("00000")) {
					app.MyToast(this, "收藏成功!");
					
					isShoucang = true;
					mBottomButton.setBackgroundResource(R.drawable.player_btn_unfav);
					
					
				} else {
					
					isShoucang = true;
					mBottomButton.setBackgroundResource(R.drawable.player_btn_unfav);
					app.MyToast(this, "已收藏!");
				}
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// ajax error, show error code
			if (status.getCode() == AjaxStatus.NETWORK_ERROR)
				app.MyToast(aq.getContext(),
						getResources().getString(R.string.networknotwork));
		}

	}

	public void unfavorityResult(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				if (json.getString("res_code").trim().equalsIgnoreCase("00000")) {
					app.MyToast(this, "取消收藏成功!");

					mBottomButton
					.setBackgroundResource(R.drawable.player_btn_fav);
					isShoucang = false;
//					setResult(JieMianConstant.SHOUCANG_CANCEL);
				} else {
					
					app.MyToast(this, "取消收藏失败!");
					isShoucang = true;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			// ajax error, show error code
			if (status.getCode() == AjaxStatus.NETWORK_ERROR)
				app.MyToast(this,
						getResources().getString(R.string.networknotwork));
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		 Dialog alertDialog = new AlertDialog.Builder(this). 
	                setTitle("提示"). 
	                setMessage("该视频无法播放"). 
	                setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}

					}).
	                create();
		 	alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	        alertDialog.show(); 
		return super.onCreateDialog(id);
	}
	
	/**
	 * 地址跳转
	 */
	
	class  UrlRedirectTask implements Runnable{

		@Override
		public void run() {
			
			// TODO Auto-generated method stub
			
			Log.i(TAG, "UrlRedirectTask-->" + currentPlayUrl);
			
			if(currentPlayUrl != null && !currentPlayUrl.equals("")) {
				
				if(currentPlayUrl.indexOf(("{now_date}")) != -1) {
					
					currentPlayUrl = currentPlayUrl.replace("{now_date}", System.currentTimeMillis()/1000 + "");
				}
			}
			
			String str = getRedirectUrl();
			
			if(str!=null){
				currentPlayUrl = str;
				mHandler.sendEmptyMessage(MESSAGE_PALY_URL_OK);
			}else{
				mHandler.sendEmptyMessage(MESSAGE_URL_NEXT);
			}
		}
		
	}
	
	private String getRedirectUrl(){
		String urlStr = null;
//		while(urlStr == null) {
			
		List<String> list = new ArrayList<String>();
		
		try {
			urlRedirect(currentPlayUrl,list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//超时异常
		}
		if(list.size() > 0) {
			 urlStr = list.get(list.size() -1);
		}
//		}
		return urlStr;
	}
	
	private void urlRedirect(String urlStr,List<String> list) {
		
		// 模拟火狐ios发用请求 使用userAgent
		AndroidHttpClient mAndroidHttpClient = AndroidHttpClient
				.newInstance(Constant.USER_AGENT_IOS);

		HttpParams httpParams = mAndroidHttpClient.getParams();
		// 连接时间最长5秒，可以更改
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000 * 1);
		
		URL url;
		try {
			url = new URL(urlStr);
//			URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(),null);
//			HttpGet mHttpGet = new HttpGet(uri);
			HttpGet mHttpGet = new HttpGet(url.toURI());
			HttpResponse response = mAndroidHttpClient.execute(mHttpGet);
			StatusLine statusLine = response.getStatusLine();
			
			int status = statusLine.getStatusCode();
			Log.i(TAG, "HTTP STATUS : " + status);
			
			if (status == HttpStatus.SC_OK) {
				Log.i(TAG, "HttpStatus.SC_OK--->" + urlStr);
				// 正确的话直接返回，不进行下面的步骤
				mAndroidHttpClient.close();
				list.add(urlStr);
				
				return;//后面不执行
			} else {
				
				Log.i(TAG, "NOT HttpStatus.SC_OK--->" + urlStr);
				
				if (status == HttpStatus.SC_MOVED_PERMANENTLY || // 网址被永久移除
						status == HttpStatus.SC_MOVED_TEMPORARILY || // 网址暂时性移除
						status == HttpStatus.SC_SEE_OTHER || // 重新定位资源
						status == HttpStatus.SC_TEMPORARY_REDIRECT) {// 暂时定向
					
					Header header = response.getFirstHeader("Location");// 拿到重新定位后的header
					
					if(header != null) {
						
						String location = header.getValue();// 从header重新取出信息
						Log.i(TAG, "Location: " + location);
						if(location != null && !location.equals("")) {
							
							urlRedirect(location, list);
							
							mAndroidHttpClient.close();// 关闭此次连接
							return;//后面不执行
						}
					}
					
					list.add(null);
					mAndroidHttpClient.close();
					
					return;

				} else {//地址真的不存在
					
					mAndroidHttpClient.close();
					list.add(null);
					
					return;//后面不执行
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	private void hidePreLoad(){
		Log.d(TAG, "hidePreLoad----------->");
		mPreLoadLayout.setVisibility(View.GONE);
		mHandler.removeCallbacks(mLoadingRunnable);
		mStatue = STATUE_PLAYING;
		mSeekBar.setEnabled(true);
		mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PROGRESS, 1000);
		mHandler.sendEmptyMessageDelayed(MESSAGE_HIDE_PROGRESSBAR, 5000);
	}
	
	/*
	 * adkey player picture in picture
	 * author yyc
	 */
	private void showBanner(){
		if (mAdView != null) {
			removeBanner();
		}
		mAdView = new AdView(this, Constant.PLAYER_ADV_PUBLISHERID,Constant.ANIMATION);
		mAdView.setAdListener(this);
		layout.addView(mAdView);
	}
	
	private void removeBanner(){
		if(mAdView!=null){
			layout.removeView(mAdView);
			mAdView = null;
		}
	}
	
	@Override
	public void adClicked()
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void adClosed(Ad ad, boolean completed)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void adLoadSucceeded(Ad ad)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void adShown(Ad ad, boolean succeeded)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stub
		
	}
}
