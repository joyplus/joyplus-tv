package com.joyplus.tv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.widget.Log;
import com.joyplus.tv.utils.UtilTools;
import com.umeng.analytics.MobclickAgent;

public class Main extends Activity implements AdListener{
	
	private static final String TAG = "AD_LOGO";
	
	private static final int DIALOG_NETWORK_ERROR = 1;

	private AdManager mManager;

	private App app;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
		setContentView(R.layout.logo);// 显示welcom.xml
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏显示
		
		app = (App) getApplicationContext();
		
		if (!app.isNetworkAvailable()) {// 如果没有网络，弹出提示dialog
			
			showDialog(DIALOG_NETWORK_ERROR);
			
			return;
		}
		
		MobclickAgent.onError(this);
		
		MobclickAgent.updateOnlineConfig(this);
		
		String umengChannel = null;
		
		try {
			ApplicationInfo info=this.getPackageManager().getApplicationInfo(getPackageName(),
			        PackageManager.GET_META_DATA);
			umengChannel =info.metaData.getString("UMENG_CHANNEL");
//			Log.i(TAG, "UMENG_CHANNEL--->" + umengChannel);
			
			if (umengChannel != null && !umengChannel.equals("")) {

				if (UtilTools.getUmengChannel(getApplicationContext()) != null
						&& !UtilTools.getUmengChannel(getApplicationContext())
								.equals("")
						&& !UtilTools.getUmengChannel(getApplicationContext())
								.equals(umengChannel)) {

					UtilTools.setLoadingAdvID(getApplicationContext(), "");
					UtilTools.setMainAdvID(getApplicationContext(), "");
					UtilTools.setPlayerAdvID(getApplicationContext(), "");
				}
		
				UtilTools.setUmengChannel(getApplicationContext(), umengChannel);
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(UtilTools.getLoadingAdvID(getApplicationContext()) != null
				&& !UtilTools.getLoadingAdvID(getApplicationContext()).equals("")) {
			
			Constant.LOADING_ADV_PUBLISHERID = UtilTools.getLoadingAdvID(getApplicationContext());
		} else {
			
			if(umengChannel != null && !umengChannel.equals("")) {
				
				String loadingAdvID = MobclickAgent.getConfigParams(this, umengChannel + "_LOADING_ADV_PUBLISHERID");
				String str = umengChannel + "_LOADING_ADV_PUBLISHERID";
//				Log.i(TAG, "name--->" +str + " loadingAdvID--->" + loadingAdvID);
				if(loadingAdvID != null && !loadingAdvID.equals("")){
					
					Constant.LOADING_ADV_PUBLISHERID = loadingAdvID;

					UtilTools.setLoadingAdvID(getApplicationContext(), Constant.LOADING_ADV_PUBLISHERID);
				}
			}
		}
		
		if(UtilTools.getMainAdvID(getApplicationContext()) != null
				&& !UtilTools.getMainAdvID(getApplicationContext()).equals("")) {
			
			Constant.MAIN_ADV_PUBLISHERID = UtilTools.getMainAdvID(getApplicationContext());
		} else {
			
			if(umengChannel != null && !umengChannel.equals("")) {
				
				String mainAdvID = MobclickAgent.getConfigParams(this, umengChannel + "_MAIN_ADV_PUBLISHERID");
//				Log.i(TAG, "mainAdvID--->" + mainAdvID);
				if(mainAdvID != null && !mainAdvID.equals("")){
					
					Constant.MAIN_ADV_PUBLISHERID = mainAdvID;
					UtilTools.setMainAdvID(getApplicationContext(), Constant.MAIN_ADV_PUBLISHERID);
				}
			}
		}
		
		if(UtilTools.getPlayerAdvID(getApplicationContext()) != null
				&& !UtilTools.getPlayerAdvID(getApplicationContext()).equals("")) {
			
			Constant.PLAYER_ADV_PUBLISHERID = UtilTools.getPlayerAdvID(getApplicationContext());
		} else {
			
			if(umengChannel != null && !umengChannel.equals("")) {
				
				String playerAdv = MobclickAgent.getConfigParams(this, umengChannel + "_PLAYER_ADV_PUBLISHERID");
//				Log.i(TAG, "playerAdv--->" + playerAdv);
				if(playerAdv != null && !playerAdv.equals("")){
					
					Constant.MAIN_ADV_PUBLISHERID = playerAdv;
					UtilTools.setPlayerAdvID(getApplicationContext(), playerAdv);
				}
			}
		}
			
			
			mManager = new AdManager(this,Constant.LOADING_ADV_PUBLISHERID,Constant.cacheMode);
			mManager.setListener(this);
			mManager.requestAd();

			UtilTools.setIsShowAd(getApplicationContext(), false);

		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
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
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		MobclickAgent.onResume(this);
		
	}

	@Override
	public void onPause() {
		super.onPause();
		
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mManager!=null)
			mManager.release();
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
		Log.i(TAG,"adClosed");
		final Intent intent = new Intent(Main.this, Main1.class);// AndroidMainScreen为主界面
		startActivity(intent);
		Main.this.finish();
	}

	@Override
	public void adLoadSucceeded(Ad ad)
	{
		// TODO Auto-generated method stub
		Log.i(TAG,"adLoadSucceeded");
		if (mManager != null && mManager.isCacheLoaded())
		{
			Log.i(TAG, "isCacheLoaded"+mManager.isCacheLoaded()+":"+mManager.isAdLoaded());
			mManager.showAd();
			UtilTools.setIsShowAd(getApplicationContext(), true);
		}else{
			UtilTools.setIsShowAd(getApplicationContext(), false);
			final Intent intent = new Intent(Main.this, Main1.class);// AndroidMainScreen为主界面
			startActivity(intent);
			Main.this.finish();
		}
	}

	@Override
	public void adShown(Ad ad, boolean succeeded)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stu
		Intent intent = new Intent(Main.this, Main1.class);// AndroidMainScreen为主界面
		startActivity(intent);
		finish();
	}
	
	// 返回键
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getRepeatCount() == 0) {
					if(mManager!=null)
						mManager.release();
				}
			}
			return super.dispatchKeyEvent(event);
		}
}