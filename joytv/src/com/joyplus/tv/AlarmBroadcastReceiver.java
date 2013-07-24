package com.joyplus.tv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.joyplus.tv.utils.DBUtils;
import com.joyplus.tv.utils.UtilTools;

//闹钟开启后48小时，取消全部影片的置顶状态
public class AlarmBroadcastReceiver extends BroadcastReceiver {
	static final String TAG = "AlarmBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Log.i(TAG, "AlarmBroadcastReceiver--->> onReceive");
		
		String userId = UtilTools.getCurrentUserId(context);
		DBUtils.cancelTopState(context, userId);//取消置顶状态
		
		//把存储闹钟状态设为false
		UtilTools.set48TimeClock(context, false);
	}

}
