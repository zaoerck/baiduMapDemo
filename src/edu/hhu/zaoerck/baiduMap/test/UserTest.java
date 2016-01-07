package edu.hhu.zaoerck.baiduMap.test;

import edu.hhu.zaoerck.baiduMap.domain.User;
import edu.hhu.zaoerck.baiduMap.service.DatabaseHelper;
import edu.hhu.zaoerck.baiduMap.service.UserService;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

public class UserTest extends AndroidTestCase{
	public void datatest() throws Throwable{
		DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());
		dbHelper.getWritableDatabase();
	}
	//注册测试
	public void registerTest() throws Throwable{
		UserService userService = new UserService(this.getContext());
		User user = new User();
		user.setAccount("ck");
		user.setPassword("123456789");
		user.setGender("男");
		user.setBirthday("1993-11-07");
		user.setPhoneNum("55555555555");
		user.setEmail("hi@qq.com");
		user.setAddress("南京");
		user.setInterest("乒乓");
		user.setIntroduction("介绍");
		user.setRemAccountStatus("1");
		user.setRemPasswordStatus("3");
		user.setPreLoginStatus("5");
		userService.register(user);
	}
	
	//登录测试
	public void loginTest() throws Throwable{
		UserService userService = new UserService(this.getContext());
		String account = "ck";
		String password = "1234567890";
		boolean flag = userService.login(account, password);
		if(flag){
			Log.i("TAG", "登录成功");
		}
		else {
			Log.i("TAG", "登录失败");
		}
	}
	//注销测试
	public void logoutTest() throws Throwable{
		UserService userService = new UserService(this.getContext());
		User user = new User();
		user.setAccount("account");
		userService.logout(user);
	}
	
	//查找测试
	public void findTest() throws Throwable{
		UserService userService = new UserService(this.getContext());
		String account1 = "ck";
		String account2 = "hello";
		boolean flag1 = userService.find("account", account1);
		boolean flag2 = userService.find("account", account2);
		//测试查找成功
		if(flag1 == true) {
			Log.i("TAG", "查找" + account1 + "成功" + "good");
		}
		else {
			Log.i("TAG", "查找" + account1 + "失败" + "bad");
		}
		
		//测试查找失败
		if(flag2 == true) {
			Log.i("TAG", "查找" + account2 + "成功" + "bad");
		}
		else {
			Log.i("TAG", "查找" + account2 + "失败" + "good");
		}
	}
	
	//修改测试
	public void changeTest() throws Throwable{
		UserService userService = new UserService(this.getContext());
		String account = "ck";
		String dataType = "interest";
		String value = "修改后";
		userService.change(account, dataType, value);
	}
	
	//获取测试
	public void getTextTest() throws Throwable{
		UserService userService = new UserService(this.getContext());
		String account = "ck";
		int row = 4;
		String birthday = userService.getText(account, row);
		Log.i("TAG", "查询到了" + "birthday" + "的值为 " + birthday);
	}
	
	public void getCursorTest() throws Throwable{
		String account = null; 
		UserService userService = new UserService(this.getContext());
		String dataType = "preLoginStatus";
		String value = "5";
		if (userService.find(dataType, value)) {
			Log.i("TAG", "查询到了preLoginStatus为4的用户");
			Cursor cursor = userService.getCursor(dataType, value);
			 if (cursor.moveToFirst() == true){
				account = cursor.getString(1);
				Log.i("TAG", "preLoginStatus为4的用户为：" + account);
				cursor.close();
			}
		}
		else {
			Log.i("TAG", "没有查询到了preLoginStatus为4的用户");
		}
	}
}
