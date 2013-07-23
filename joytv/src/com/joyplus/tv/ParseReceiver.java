package com.joyplus.tv;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.joyplus.tv.utils.Log;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


public class ParseReceiver extends BroadcastReceiver {
private static final String TAG = "MyCustomReceiver";
 
  @Override
  public void onReceive(Context context, Intent intent) {
    try {
    	
      String action = intent.getAction();
      String channel = intent.getExtras().getString("com.parse.Channel");
      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
 
      Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
      Iterator itr = json.keys();
      while (itr.hasNext()) {
        String key = (String) itr.next();
        
//        if(key.contains(MainActivity.URI)) {
//        	
//        	Log.d(TAG, "URI " + json.getString(key));
//        	Intent localIntent = new Intent();
//        	localIntent.setClass(context, VideoActivity.class);
//        	Bundle bundle = new Bundle();
//        	bundle.putString(MainActivity.URI, json.getString(key));
//        	localIntent.putExtras(bundle);
//            localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(localIntent);
//            
//        }
        Log.d(TAG, "..." + key + " => " + json.getString(key));
      }
    } catch (JSONException e) {
      Log.d(TAG, "JSONException: " + e.getMessage());
    }
  }
}
