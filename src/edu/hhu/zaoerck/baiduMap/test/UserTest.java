package edu.hhu.zaoerck.baiduMap.test;

import edu.hhu.zaoerck.baiduMap.domain.User;
import edu.hhu.zaoerck.baiduMap.service.DatabaseHelper;
import edu.hhu.zaoerck.baiduMap.service.UserService;
import android.database.sqlite.SQLiteDatabase;
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
		user.setAccount("account");
		user.setPassword("1234567890");
		user.setGender("男");
		user.setBirthday("1993-10-07");
		user.setPhoneNum("15951706092");
		user.setEmail("hello@qq.com");
		user.setAddress("hubei");
		user.setInterest("hehe");
		user.setIntroduction("介绍一下自己吧，请你介绍一下自己吧，介绍一下自己吧，介绍一下自己吧，介绍一下自己吧");
		user.setRemAccountStatus("0");
		user.setRemPasswordStatus("0");
		userService.register(user);
	}
	
	//注册失败
	public void loginTest() throws Throwable{
		UserService userService = new UserService(this.getContext());
		String account = "account";
		String password = "1234567890";
		boolean flag = userService.login(account, password);
		if(flag){
			Log.i("TAG", "登录成功");
		}
		else {
			Log.i("TAG", "登录失败");
		}
	}
	
	public void logoutTest() throws Throwable{
		UserService userService = new UserService(this.getContext());
		User user = new User();
		user.setAccount("account");
		userService.logout(user);
	}
}
