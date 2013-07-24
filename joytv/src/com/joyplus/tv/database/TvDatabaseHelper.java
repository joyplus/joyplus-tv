package com.joyplus.tv.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.joyplus.tv.utils.DataBaseItems;
import com.joyplus.tv.utils.DataBaseItems.SQLite3_DataType;
import com.joyplus.tv.utils.DataBaseItems.UserHistory;
import com.joyplus.tv.utils.DataBaseItems.UserShouCang;

public class TvDatabaseHelper extends SQLiteOpenHelper implements UserShouCang,SQLite3_DataType{

	public static final String DATABASE_NAME = "joyplus.db";
	public static final int DATABASE_VERSION = 1;
	public static final String ZHUIJU_TABLE_NAME = "zhuiju";//追剧收藏
	public static final String HISTORY_TABLE_NAME = "history";//历史记录

	private SQLiteDatabase sqLiteDatabase;

	public static TvDatabaseHelper newTvDatabaseHelper(Context context) {

		return new TvDatabaseHelper(context);
	}

	public void openDatabaseWithHelper() {

		try {
			if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {

				if (!sqLiteDatabase.isReadOnly()) {

					return;
				}

				sqLiteDatabase.close();
			}
			sqLiteDatabase = getWritableDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void openDatabaseReadOnly() {
		try {
			if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
				if (sqLiteDatabase.isReadOnly())
					return;

				sqLiteDatabase.close();
			}
			sqLiteDatabase = getReadableDatabase();
		} catch (SQLException e) {
			 e.printStackTrace();
		}
	}
	
    //Database Close
    public void closeDatabase() {
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            	
            	sqLiteDatabase.close();
            }
            
            close();
        } catch (SQLException e) {
        	e.printStackTrace();
//            return;
        }
    }

	private TvDatabaseHelper(Context context) {
		this(context, DATABASE_NAME, DATABASE_VERSION);
	}

	public TvDatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public TvDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
//	private String sql_table_zhuiju = "create table if not exists "
//			+ ZHUIJU_TABLE_NAME + " ( " + ID
//			+ " integer primary key autoincrement, " 
//			+ USER_ID + TEXT_DOT 
//			+ PRO_ID + TEXT_DOT
//			+ NAME + TEXT_DOT 
//			+ SCORE + TEXT_DOT 
//			+ PRO_TYPE + TEXT_DOT 
//			+ PIC_URL + TEXT_DOT
//			+ DURATION + TEXT_DOT 
//			+ CUR_EPISODE + TEXT_DOT
//			+ MAX_EPISODE + TEXT_DOT 
//			+ STARS + TEXT_DOT 
//			+ DIRECTORS + TEXT_DOT 
//			+ IS_NEW + INTEGER 
//			+ " )";
	
	private String sql_table_zhuiju = "create table if not exists "
			+ ZHUIJU_TABLE_NAME + " ( " + ID
			+ " integer primary key autoincrement, "
			+ PRO_ID + TEXT_DOT
			+ USER_ID + TEXT_DOT 
			+ NAME + TEXT_DOT 
			+ SCORE + TEXT_DOT 
			+ PRO_TYPE + TEXT_DOT 
			+ PIC_URL + TEXT_DOT
			+ DURATION + TEXT_DOT 
			+ CUR_EPISODE + TEXT_DOT
			+ MAX_EPISODE + TEXT_DOT 
			+ STARS + TEXT_DOT 
			+ DIRECTORS + TEXT_DOT 
			+ IS_NEW + INTEGER_DOT
			
			+ TEXT_1 + TEXT_DOT
			+ TEXT_2 + TEXT_DOT
			+ TEXT_3 + TEXT_DOT
			+ TEXT_4 + TEXT_DOT
			
			+ INTEGER_1 + INTEGER_DOT
			+ INTEGER_2 + INTEGER_DOT 
			+ INTEGER_3 + INTEGER_DOT 
			+ INTEGER_4 + INTEGER_DOT 
			
			+ IS_UPDATE + INTEGER 
			+ " )";
	
	private String sql_table_history = "create table if not exists "
			+ HISTORY_TABLE_NAME + " ( " + UserHistory.ID
			+ " integer primary key autoincrement, "
			+ UserHistory.USER_ID + TEXT_DOT
			+ UserHistory.PROD_TYPE + TEXT_DOT 
			+ UserHistory.PROD_NAME + TEXT_DOT 
			+ UserHistory.PROD_SUBNAME + TEXT_DOT 
			+ UserHistory.PRO_ID + TEXT_DOT 
//			+ UserHistory.CREATE_DATE + TEXT_DOT
			+ UserHistory.PLAY_TYPE + TEXT_DOT 
			+ UserHistory.PLAYBACK_TIME + TEXT_DOT
			+ UserHistory.VIDEO_URL + TEXT_DOT 
			+ UserHistory.DURATION + TEXT_DOT 
			+ UserHistory.BOFANG_ID + TEXT_DOT 
			+ UserHistory.PROD_PIC_URL + TEXT_DOT 
//			+ UserHistory.BIG_PROD_PIC_URL + TEXT_DOT 
			+ UserHistory.DEFINITION + TEXT_DOT 
			+ UserHistory.STARS + TEXT_DOT 
			+ UserHistory.DIRECTORS + TEXT_DOT 
			+ UserHistory.FAVORITY_NUM + TEXT_DOT 
			+ UserHistory.SUPPORT_NUM + TEXT_DOT 
			+ UserHistory.PUBLISH_DATE + TEXT_DOT 
			+ UserHistory.SCORE + TEXT_DOT 
			+ UserHistory.AREA + TEXT_DOT 
			+ UserHistory.MAX_EPISODE + TEXT_DOT 
			+ UserHistory.CUR_EPISODE + TEXT_DOT 
			
			+ TEXT_1 + TEXT_DOT
			+ TEXT_2 + TEXT_DOT
			+ TEXT_3 + TEXT_DOT
			+ TEXT_4 + TEXT_DOT
			
			+ INTEGER_1 + INTEGER_DOT 
			+ INTEGER_2 + INTEGER_DOT 
			+ INTEGER_3 + INTEGER_DOT 
			+ INTEGER_4 + INTEGER_DOT 
			
			+ IS_NEW + INTEGER
			+ " )";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(sql_table_zhuiju);
		db.execSQL(sql_table_history);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
