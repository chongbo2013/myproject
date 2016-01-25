package com.yeyanxiang.project.mediaplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年2月27日
 * 
 * @简介
 */
public class DbUtil extends SQLiteOpenHelper {
	public final static int DATABASE_VERSION = 1;
	public final static String DATABASE_NAME = "video.db";
	public final static String TABLE_NAME = "video_tab";
	public final static String ID = "video_id";
	public final static String VIDEO_PATH = "video_path";
	public final static String VIDEO_PROCESS = "video_process";

	public DbUtil(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DbUtil(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		final String sql = "CREATE TABLE " + TABLE_NAME + " (" + ID
				+ " INTEGER primary key autoincrement," + VIDEO_PATH + " text,"
				+ VIDEO_PROCESS + " INTEGER)";
		System.out.println(sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public long getvideoprocess(String video_path) {
		long process = 0;
		Cursor cursor = select(video_path);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				process = cursor.getLong(cursor.getColumnIndex(VIDEO_PROCESS));
			}
		}
		close();
		return process;
	}

	public long updatevideoprocess(String video_path, long videoprocess) {
		Cursor cursor = select(video_path);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				ContentValues values = new ContentValues();
				values.put(VIDEO_PROCESS, videoprocess);
				int raw = getWritableDatabase().update(TABLE_NAME, values,
						VIDEO_PATH + "='" + video_path + "'", null);
				close();
				return raw;
			} else {
				return insert(video_path, videoprocess);
			}
		} else {
			return insert(video_path, videoprocess);
		}

	}

	public Cursor select(String video_path) {
		try {
			return getReadableDatabase().query(TABLE_NAME, null,
					VIDEO_PATH + "='" + video_path + "'", null, null, null,
					null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public long insert(String video_path, long playprocess) {
		ContentValues values = new ContentValues();
		values.put(VIDEO_PATH, video_path);
		values.put(VIDEO_PROCESS, playprocess);
		long raw = getWritableDatabase().insert(TABLE_NAME, null, values);
		close();
		return raw;
	}

	public void close() {
		SQLiteDatabase db = this.getWritableDatabase();
		if (db.isOpen())
			db.close();
	}
}
