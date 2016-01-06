package edu.hhu.zaoerck.baiduMap.service;

import android.R.integer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	static String fileName = "user.db";
	static int dbVersion = 1;
	
	public DatabaseHelper(Context context){
		super(context, fileName, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "create table user(id integer primary key autoincrement," +
				"account varchar(20)," +
				"password varchar(20)," +
				"gender varchar(20)," +
				"birthday varchar(20)," +
				"phoneNum varchar(20)," +
				"email varchar(20)," +
				"address varchar(20)," +
				"interest varchar(20)," +
				"introduction text," +
				"remAccountStatus varchar(20)," +
				"remPasswordStatus varchar(20)" +
				")";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
