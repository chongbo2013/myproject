package com.yeyanxiang.project.pub.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月13日
 * 
 * @简介
 */
public class ContinueDBUtil extends SQLiteOpenHelper {
	public final static String DATABASE_NAME = "myproject.db";
	public final static int DATABASE_VERSION = 2;

	// 体征数据表格
	public final static String sign_TABLE_NAME = "sign_table";
	public final static String sign_id = "sign_id";
	public final static String sign_time = "sign_time";// 时间
	public final static String sign_nSPR = "sign_nSPR";// 心率
	public final static String sign_fBodyTemp = "sign_fBodyTemp";// 体温
	public final static String sign_nSPO = "sign_nSPO";// 血氧
	public final static String sign_nBPSys = "sign_nBPSys";// 高压
	public final static String sign_nBPDia = "sign_nBPDia";// 低压
	public final static String sign_nBPflag = "sign_nBPflag";// 血压更改标记
	public final static String sign_fECG = "sign_fECG";// 心电
	public final static String sign_start_time = "sign_start_time";
	public final static String sign_stop_time = "sign_stop_time";
	public final static String sign_endflag = "sign_endflag";

	public ContinueDBUtil(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String signsql = "CREATE TABLE " + sign_TABLE_NAME + " ("
				+ sign_id + " INTEGER primary key autoincrement," + sign_time
				+ " INTEGER," + sign_fBodyTemp + " float," + sign_nSPR
				+ " INTEGER," + sign_nSPO + " INTEGER," + sign_nBPSys
				+ " INTEGER," + sign_nBPDia + " INTEGER," + sign_nBPflag
				+ " INTEGER," + sign_fECG + " text," + sign_start_time
				+ " INTEGER," + sign_stop_time + " INTEGER," + sign_endflag
				+ " INTEGER)";
		// System.out.println(signsql);
		db.execSQL(signsql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + sign_TABLE_NAME);
		onCreate(db);
	}

	public Cursor select(String slection, String TABLE_NAME) {
		return this.getReadableDatabase().query(TABLE_NAME, null, slection,
				null, null, null, null);
	}

	public long insert(ContentValues values, String TABLE_NAME) {
		long row = this.getWritableDatabase().insert(TABLE_NAME, null, values);
		close();
		return row;
	}

	public int update(ContentValues cv, String whereClause, String TABLE_NAME) {
		int result = this.getWritableDatabase().update(TABLE_NAME, cv,
				whereClause, null);
		close();
		return result;
	}

	public void close() {
		SQLiteDatabase db = this.getWritableDatabase();
		if (db.isOpen())
			db.close();
	}
}
