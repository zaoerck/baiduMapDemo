package edu.hhu.zaoerck.baiduMap.service;

import edu.hhu.zaoerck.baiduMap.domain.User;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserService {
	private DatabaseHelper dbHelper;
	public UserService(Context context){
		dbHelper=new DatabaseHelper(context);
	}
	
	//登录用
	public boolean login(String account,String password){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where account=? and password=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{account,password});		
		if(cursor.moveToFirst()==true){
			cursor.close();
			return true;
		}
		return false;
	}
	//注册用
	public boolean register(User user){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="insert into user(account, password, gender, birthday, phoneNum, email, address, interest, introduction, " +
				"remAccountStatus, remPasswordStatus) values(?,?,?,?,?,?,?,?,?,?,?)";
		Object obj[]={user.getAccount(), user.getPassword(), user.getGender(), user.getBirthday(), user.getPhoneNum(), user.getEmail(), 
				user.getAddress(), user.getInterest(), user.getIntroduction(), user.getRemAccountStatus(), user.getRemPasswordStatus()};
		sdb.execSQL(sql, obj);	
		return true;
	}
	
	//注销
	public boolean logout(User user){
		SQLiteDatabase sdb = dbHelper.getReadableDatabase();
		String sql="delete from user where account=?";
		Object obj[]={user.getAccount()};
		sdb.execSQL(sql, obj);
		return true;
	}
}

