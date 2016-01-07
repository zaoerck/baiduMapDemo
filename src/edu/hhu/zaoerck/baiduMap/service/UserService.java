package edu.hhu.zaoerck.baiduMap.service;

import java.util.Set;

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
				"remAccountStatus, remPasswordStatus, preLoginStatus) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		Object obj[]={user.getAccount(), user.getPassword(), user.getGender(), user.getBirthday(), user.getPhoneNum(), user.getEmail(), 
				user.getAddress(), user.getInterest(), user.getIntroduction(), user.getRemAccountStatus(), user.getRemPasswordStatus(),user.getPreLoginStatus()};
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
	
	//查找
	public boolean find(String dataType, String value){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where " + dataType +"=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{value});		
		if(cursor.moveToFirst()==true){
			cursor.close();
			return true;
		}
		return false;
	}
	
	//修改账号为account的user的dataType的值为value
	public boolean change(String account,String dataType, String value){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="update user set " + dataType + "=? where account =?";
		Object obj[]={value, account};
		sdb.execSQL(sql, obj);
		return true;
	}
	
	//根据account获取指定列row的值
	/*
	 *0 id
	 *1 account
	 *2 password
	 *3 gender
	 *4 birthday
	 *5 phoneNum
	 *6 email
	 *7 address
	 *8 interest
	 *9 introduction
	 *10 remAccountStatus
	 *11 remPasswordStatus
	 *12 preLoginStatus
	 */
	public String getText(String account, int row){
		String result = null;
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where account=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{account});
		if(cursor.moveToFirst() == true){
			result = cursor.getString(row);
		}
		cursor.close();
		return result;
	}
	
	//返回匹配的用户的Cursor
	public Cursor getCursor(String dataType, String value){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where " + dataType +"=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{value});		
		if(cursor.moveToFirst()==true){
			return cursor;
		}
		return null;
	}
	
}

