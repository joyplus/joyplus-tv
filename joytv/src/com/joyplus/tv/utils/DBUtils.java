package com.joyplus.tv.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.joyplus.tv.database.TvDatabaseHelper;
import com.joyplus.tv.entity.HotItemInfo;
import com.joyplus.tv.entity.MovieItemData;
import com.joyplus.tv.utils.DataBaseItems.UserHistory;
import com.joyplus.tv.utils.DataBaseItems.UserShouCang;

public class DBUtils {
	
	private static final String TAG = "DBUtils";
	
	public static List<MovieItemData> getList4DB(Context context,String userId,String type) {
		
		
		List<MovieItemData> list = new ArrayList<MovieItemData>();
		
		String selection = UserShouCang.USER_ID + " = ? and " + UserShouCang.PRO_TYPE + " = ? and "
				+ UserShouCang.IS_UPDATE + " = ?";//通过用户id，找到相应信息
		String[] selectionArgs = {userId,type,DataBaseItems.NEW + ""};
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		Cursor cursor = database.query(TvDatabaseHelper.ZHUIJU_TABLE_NAME, null, selection, 
				selectionArgs, null, null, DataBaseItems.ORDER_BY_TIME_DESC);
		
		Log.i(TAG, "getList4DB----> userId--->>" + userId + " type--->>" + type 
				+ " count--->" + cursor.getCount());
		if(cursor != null && cursor.getCount() > 0) {//数据库有数据
			
			while(cursor.moveToNext()) {
				
				//总共10组数据
				int indexId = cursor.getColumnIndex(UserShouCang.PRO_ID);
				int indexName = cursor.getColumnIndex(UserShouCang.NAME);
				int indexScore = cursor.getColumnIndex(UserShouCang.SCORE);
				int indexType = cursor.getColumnIndex(UserShouCang.PRO_TYPE);
				int indexPicUrl = cursor.getColumnIndex(UserShouCang.PIC_URL);
				int indexDuration = cursor.getColumnIndex(UserShouCang.DURATION);
				int indexCurEpisode = cursor.getColumnIndex(UserShouCang.CUR_EPISODE);
				int indexMaxEpisode = cursor.getColumnIndex(UserShouCang.MAX_EPISODE);
				int indexStars = cursor.getColumnIndex(UserShouCang.STARS);
				int indexDirectors = cursor.getColumnIndex(UserShouCang.DIRECTORS);
				
				if(indexId != -1) {
					
					String pro_id = cursor.getString(indexId);
					String name = cursor.getString(indexName);
					String score = cursor.getString(indexScore);
					String type2 = cursor.getString(indexType);
					String pic_url = cursor.getString(indexPicUrl);
					String duration = cursor.getString(indexDuration);
					String curEpisode = cursor.getString(indexCurEpisode);
					String maxEpisode = cursor.getString(indexMaxEpisode);
					String stars = cursor.getString(indexStars);
					String directors = cursor.getString(indexDirectors);
					
					MovieItemData item = new MovieItemData();
					
					item.setMovieID(pro_id);
					item.setMovieName(name);
					item.setMovieScore(score);
					item.setMovieProType(type2);
					item.setMoviePicUrl(pic_url);
					item.setMovieDuration(duration);
					item.setMovieCurEpisode(curEpisode);
					item.setMovieMaxEpisode(maxEpisode);
					item.setStars(stars);
					item.setDirectors(directors);
					
					list.add(item);
				}
				
			}
		}
		
		cursor.close();
		helper.closeDatabase();
		
		return list;
	}
	
	//收藏48小时后，取消全部置顶状态
	public static void cancelTopState(Context context,String userId) {
		
		String selection = UserShouCang.USER_ID + "=?";//通过用户id，找到相应信息
		String[] selectionArgs = {userId};
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		ContentValues tempValues = new ContentValues();
		tempValues.put(UserShouCang.IS_UPDATE, DataBaseItems.OLD);
		
		database.update(TvDatabaseHelper.ZHUIJU_TABLE_NAME, tempValues, selection, selectionArgs);
		
		helper.closeDatabase();
	}
	
	//取消收藏，删除prodId影片数据
	public static void deleteData4ProId(Context context,String userId,String proId) {
		
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		String deleteSelection = UserShouCang.PRO_ID  + " = ? and " + UserShouCang.USER_ID + " = ?";
		String[] deleteselectionArgs = {proId,userId};
		int dele = database.delete(TvDatabaseHelper.ZHUIJU_TABLE_NAME, deleteSelection, deleteselectionArgs);
		
		Log.i(TAG, "deleteData4ProId--->" + dele + " PROD_ID--->" + proId);
		
		helper.closeDatabase();
		
	}
	
	//HotItemInfo 插入数据,置顶状态不开启 Shoucang
	public static void insertHotItemInfo2DB(Context context,HotItemInfo info,String userId,SQLiteDatabase database) {
		
		ContentValues tempContentValues = new ContentValues();
		tempContentValues.put(UserShouCang.USER_ID, userId);
		tempContentValues.put(UserShouCang.PRO_ID, info.prod_id);
		tempContentValues.put(UserShouCang.NAME, info.prod_name);
		tempContentValues.put(UserShouCang.SCORE, info.score);
		tempContentValues.put(UserShouCang.PRO_TYPE, info.prod_type);
		tempContentValues.put(UserShouCang.PIC_URL, info.prod_pic_url);
		tempContentValues.put(UserShouCang.DURATION, info.duration);
		tempContentValues.put(UserShouCang.CUR_EPISODE, info.cur_episode);
		tempContentValues.put(UserShouCang.MAX_EPISODE, info.max_episode);
		tempContentValues.put(UserShouCang.STARS, info.stars);
		tempContentValues.put(UserShouCang.DIRECTORS, info.directors);
		tempContentValues.put(UserShouCang.IS_NEW, DataBaseItems.NEW);
		tempContentValues.put(UserShouCang.IS_UPDATE, DataBaseItems.OLD);
//		tempContentValues.put(UserShouCang.IS_UPDATE, DataBaseItems.NEW);//测试
		
		database.insert(TvDatabaseHelper.ZHUIJU_TABLE_NAME, null, tempContentValues);
	}
	
	//HotItemInfo 插入数据,置顶状态开启 Shoucang
	public static void updateHotItemInfo2DB(Context context,HotItemInfo info,String userId,SQLiteDatabase database) {
		
		ContentValues tempContentValues = new ContentValues();
		tempContentValues.put(UserShouCang.USER_ID, userId);
		tempContentValues.put(UserShouCang.PRO_ID, info.prod_id);
		tempContentValues.put(UserShouCang.NAME, info.prod_name);
		tempContentValues.put(UserShouCang.SCORE, info.score);
		tempContentValues.put(UserShouCang.PRO_TYPE, info.prod_type);
		tempContentValues.put(UserShouCang.PIC_URL, info.prod_pic_url);
		tempContentValues.put(UserShouCang.DURATION, info.duration);
		tempContentValues.put(UserShouCang.CUR_EPISODE, info.cur_episode);
		tempContentValues.put(UserShouCang.MAX_EPISODE, info.max_episode);
		tempContentValues.put(UserShouCang.STARS, info.stars);
		tempContentValues.put(UserShouCang.DIRECTORS, info.directors);
		tempContentValues.put(UserShouCang.IS_NEW, DataBaseItems.NEW);
//		tempContentValues.put(UserShouCang.IS_UPDATE, DataBaseItems.OLD);
		tempContentValues.put(UserShouCang.IS_UPDATE, DataBaseItems.NEW);//测试
		tempContentValues.put(UserShouCang.INTEGER_1, System.currentTimeMillis());//插入当前时间
		
		String updateSelection = UserShouCang.PRO_ID  + "=? and " + UserShouCang.USER_ID + "=?";
		String[] updateselectionArgs = {info.prod_id,userId};
		
//		database.insert(TvDatabaseHelper.ZHUIJU_TABLE_NAME, null, tempContentValues);
		int updateInt = database.update(TvDatabaseHelper.ZHUIJU_TABLE_NAME, tempContentValues, updateSelection, updateselectionArgs);
	    Log.i(TAG, "info.prod_id--->" + info.prod_id + " updateInt--->" + updateInt);
	}
	
	//HotItemInfo 只插入一条数据
	public static void insertOneHotItemInfo2DB(Context context,String userId,String proId,HotItemInfo info) {
		
		deleteData4ProId(context, userId, proId);
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		insertHotItemInfo2DB(context, info, userId, database);
		
		helper.closeDatabase();
	}
	
	//HotItemInfo 插入数据 History
	public static void insertHotItemInfo2DB_History(Context context,HotItemInfo info,String userId,SQLiteDatabase database) {
		
		ContentValues tempContentValues = new ContentValues();
		tempContentValues.put(UserHistory.USER_ID, userId);
		tempContentValues.put(UserHistory.PROD_TYPE, info.prod_type);
		tempContentValues.put(UserHistory.PROD_NAME, info.prod_name);
		tempContentValues.put(UserHistory.PROD_SUBNAME, info.prod_subname);
		tempContentValues.put(UserHistory.PRO_ID, info.prod_id);
//		tempContentValues.put(UserHistory.CREATE_DATE, info.);
		tempContentValues.put(UserHistory.PLAY_TYPE, info.play_type);
		tempContentValues.put(UserHistory.PLAYBACK_TIME, info.playback_time);
		tempContentValues.put(UserHistory.VIDEO_URL, info.video_url);
		tempContentValues.put(UserHistory.DURATION, info.duration);
		tempContentValues.put(UserHistory.BOFANG_ID, info.id);
		tempContentValues.put(UserHistory.PROD_PIC_URL, info.prod_pic_url);
//		tempContentValues.put(UserHistory.BIG_PROD_PIC_URL, info);
		tempContentValues.put(UserHistory.DEFINITION, info.definition);
		tempContentValues.put(UserHistory.STARS, info.stars);
		tempContentValues.put(UserHistory.DIRECTORS, info.directors);
		tempContentValues.put(UserHistory.FAVORITY_NUM, info.favority_num);
		tempContentValues.put(UserHistory.SUPPORT_NUM, info.support_num);
		tempContentValues.put(UserHistory.PUBLISH_DATE, info.publish_date);
		tempContentValues.put(UserHistory.SCORE, info.score);
		tempContentValues.put(UserHistory.AREA, info.area);
		tempContentValues.put(UserHistory.MAX_EPISODE, info.max_episode);
		tempContentValues.put(UserHistory.CUR_EPISODE, info.cur_episode);
		tempContentValues.put(UserHistory.IS_NEW, DataBaseItems.NEW);
		
//		tempContentValues.put(UserShouCang.IS_UPDATE, DataBaseItems.NEW);//测试
		
		database.insert(TvDatabaseHelper.HISTORY_TABLE_NAME, null, tempContentValues);
	}
	
	public static String getDuartion4HistoryDB(Context context,String userId,String prod_id,String prodSubName) {
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		Cursor cursor = null;
		
		String[] columns = { UserHistory.PLAYBACK_TIME };// 返回当前更新集数
		
		if(prodSubName != null && !prodSubName.equalsIgnoreCase("")) {
			
			String selection = UserHistory.PRO_ID  + " = ? and " + UserHistory.USER_ID  + " = ? and " + UserHistory.PROD_SUBNAME+" = ?";
			String[] selectionArgs = {prod_id,userId,prodSubName};
			
			cursor = database.query(TvDatabaseHelper.HISTORY_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		} else {
			
			String selection = UserHistory.PRO_ID  + " = ? and " + UserHistory.USER_ID  + " = ?";
			String[] selectionArgs = {prod_id,userId};
			
			cursor = database.query(TvDatabaseHelper.HISTORY_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		}

		
		if(cursor != null && cursor.getCount() > 0 ) {
			
			while(cursor.moveToNext()) {
				
				int indexPlaybackTime = cursor
						.getColumnIndex(UserHistory.PLAYBACK_TIME);
				
				if(indexPlaybackTime != -1) {
					
					String curEpisode = cursor.getString(indexPlaybackTime);
					
					cursor.close();
					helper.closeDatabase();
					return curEpisode;
				}
			}
		}
		
		cursor.close();
		helper.closeDatabase();
		
		return "";
	}
	
	//删除单条数据
	public static void deleteOneHotItemInfo2DB_History(Context context,String userId,String prod_id) {
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		String deleteSelection = UserHistory.PRO_ID  + " = ? and " + UserHistory.USER_ID + " = ?";
		String[] deleteselectionArgs = {prod_id,userId};
		int dele = database.delete(TvDatabaseHelper.HISTORY_TABLE_NAME, deleteSelection, deleteselectionArgs);
		
		Log.i(TAG, "deleteData4ProId--->" + dele + " PROD_ID--->" + prod_id);
		
		helper.closeDatabase();
	}
	
	//当前影片是否是置顶影片，并且返回当前更新集数
	public static String getTopPlayerCurEpisode(Context context, String userId,String proId) {
		
		String selection = UserShouCang.USER_ID + "=? and " + UserShouCang.PRO_ID + 
				"=? and " + UserShouCang.IS_UPDATE + "=?";//通过用户id，找到相应信息
		String[] selectionArgs = {userId,proId,DataBaseItems.NEW + ""};
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		String[] columns = { UserShouCang.CUR_EPISODE };// 返回当前更新集数
		
		Cursor cursor = database.query(TvDatabaseHelper.ZHUIJU_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		
		if(cursor != null && cursor.getCount() > 0 ) {
			
			while(cursor.moveToNext()) {
				
				int indexCurEpisode = cursor
						.getColumnIndex(UserShouCang.CUR_EPISODE);
				
				if(indexCurEpisode != -1) {
					
					String curEpisode = cursor.getString(indexCurEpisode);
					return curEpisode;
				}
			}

		}
		
		cursor.close();
		helper.close();
		
		return "";
		
	}
	
	//取消当前影片的置顶状态
	public static void cancelAPlayTopState(Context context,String userId,String pro_id) {
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		String updateSelection = UserShouCang.PRO_ID  + "=? and " + UserShouCang.USER_ID + "=?";
		String[] updateselectionArgs = {pro_id,userId};
		
		ContentValues tempValues = new ContentValues();
		tempValues.put(UserShouCang.IS_UPDATE, DataBaseItems.OLD);
		database.update(TvDatabaseHelper.ZHUIJU_TABLE_NAME,tempValues, updateSelection, updateselectionArgs);
		
		helper.closeDatabase();
	}
	
	//通过proid获取单条历史记录
	public static HotItemInfo getHotItemInfo4DB_History(Context context,String userId,String prod_id) {
		
		TvDatabaseHelper helper = TvDatabaseHelper.newTvDatabaseHelper(context);
		SQLiteDatabase database = helper.getWritableDatabase();//获取写db
		
		String selection = UserHistory.PRO_ID  + "=? and " + UserShouCang.USER_ID + "=?";
		String[] selectionArgs = {prod_id,userId};
		
		String[] columns = { UserHistory.PROD_TYPE,
				UserHistory.PROD_SUBNAME,UserHistory.PLAYBACK_TIME};// 返回当前类型、看到的集数【电影为Empty】,所看到的的时间
		
		Cursor cursor = database.query(TvDatabaseHelper.HISTORY_TABLE_NAME, columns, selection, selectionArgs, null, null, 
				DataBaseItems.ORDER_BY_ID_DESC);
		
		HotItemInfo info = new HotItemInfo();
		
		if(cursor != null && cursor.getCount() > 0) {
			
			Log.i(TAG, "cursor.getCount()--->" + cursor.getCount());
			
			if(cursor.getCount() >= 1) {
				
				while(cursor.moveToNext()) {
					
					int indexType = cursor.getColumnIndex(UserHistory.PROD_TYPE);
					int indexSubName = cursor.getColumnIndex(UserHistory.PROD_SUBNAME);
					int indexPlayBackTime = cursor.getColumnIndex(UserHistory.PLAYBACK_TIME);
					
					if(indexSubName != -1) {
						
						String type = cursor.getString(indexType);
						String subName = cursor.getString(indexSubName);
						String playBackTime = cursor.getString(indexPlayBackTime);
						
						info.prod_type = type;
						info.prod_subname = subName;
						info.playback_time = playBackTime;
						Log.i(TAG, "sub_name---->" + subName);
					}
				}
			}
		}
		
		cursor.close();
		helper.close();
		
		return info;
	}
	
	public static int getHistoryPlayIndex4DB(Context context,String prod_id,String prod_type) {
		
		HotItemInfo info = getHotItemInfo4DB_History(context,
				UtilTools.getCurrentUserId(context), prod_id);
		
		if(info != null){
			
			String type = info.prod_type;
			Log.i(TAG, "type--->" + type);
			if(type != null && type.equals(prod_type)){
				
				String prod_subName = info.prod_subname;
				Log.i(TAG, "prod_subName--->" + prod_subName);
				if(prod_subName != null && !prod_subName.equals("")
						&& !prod_subName.equals("EMPTY")) {
					
					int currentIndex = -1;
					try {
						currentIndex = Integer.valueOf(prod_subName);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return currentIndex;
				}
			}
				
		}
		
		return -1;
		
	}

}
