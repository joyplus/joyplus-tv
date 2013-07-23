package com.joyplus.tv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.joyplus.tv.ui.UserInfo;
import com.joyplus.tv.utils.UtilTools;
import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends Activity implements OnClickListener {
	
	private static final String TAG = "SettingActivity";
	
	private static final int SHOW_DIALOG_UNBAND = 0;
	
	private LinearLayout unbandLayout;
	private TextView aboutLayout,declarationLayout,faqLayout;
	private App app;
	private AQuery aq;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		unbandLayout = (LinearLayout) findViewById(R.id.bandLayout);
		aboutLayout = (TextView) findViewById(R.id.about_layout);
		declarationLayout = (TextView) findViewById(R.id.declaration_layout);
		faqLayout = (TextView) findViewById(R.id.faq_layout);
		app = (App) getApplication();
		aq = new AQuery(this);
		
		unbandLayout.setOnClickListener(this);
		aboutLayout.setOnClickListener(this);
		declarationLayout.setOnClickListener(this);
		faqLayout.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick(View v)--->");
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.about_layout:
			Intent intentAbout = new Intent(this,AboutActivity.class);
			startActivity(intentAbout);
			break;
		case R.id.declaration_layout:
			Intent intentDeclaration = new Intent(this,DeclarationActivity.class);
			startActivity(intentDeclaration);
			break;
		case R.id.faq_layout:
			Intent intentFaq = new Intent(this,FAQActivity.class);
			startActivity(intentFaq);
			break;
		case R.id.bandLayout:
			break;

		default:
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case SHOW_DIALOG_UNBAND:
			
			return null;

		default:
			break;
		}
		
		
		return super.onCreateDialog(id);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		MobclickAgent.onResume(this);
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
		if(aq!=null){
			aq.dismiss();
		}
		super.onDestroy();
	}
}
