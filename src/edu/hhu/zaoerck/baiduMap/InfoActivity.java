package edu.hhu.zaoerck.baiduMap;

import edu.hhu.zaoerck.baiduMap.domain.User;
import edu.hhu.zaoerck.baiduMap.service.UserService;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class InfoActivity extends Activity {

	private TextView account;
	private TextView gender;
	private TextView phoneNum;
	private TextView email;
	private TextView birthday;
	private TextView address;
	private TextView introduction;
	private TextView interest;
	UserService userService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info);
		init();
		
	}
	
	private void init(){
		
		userService = new UserService(InfoActivity.this);
		account = (TextView) findViewById(R.id.account);
		gender = (TextView) findViewById(R.id.gender);
		phoneNum = (TextView) findViewById(R.id.phoneNum);
		email = (TextView) findViewById(R.id.email);
		birthday = (TextView) findViewById(R.id.birthdate);
		address = (TextView) findViewById(R.id.birthAddress);
		introduction = (TextView) findViewById(R.id.introduction);
		interest = (TextView) findViewById(R.id.interest);
		
		String dataType = "preLoginStatus";
		String value = "1";
		String [] user = new String [12];
		Cursor cursor = userService.getCursor(dataType, value);
		if (cursor.moveToFirst() == true){
			for(int i = 0; i < 12; i++){
				user[i] = cursor.getString(i+1);
			}
			cursor.close();
		}
		
		account.setText(user[0]);
		gender.setText(user[2]);
		birthday.setText(user[3]);
		phoneNum.setText(user[4]);
		email.setText(user[5]);
		introduction.setText(user[8]);
		interest.setText(user[7]);
		address.setText(user[6]);
	}
	
	public void logout(View v){
		UserService userService = new UserService(InfoActivity.this);
		User user = new User();
		String accountFromDB = null;
		
		String dataType = "preLoginStatus";
		String value = "1";
		if (userService.find(dataType, value)) {
			Cursor cursor = userService.getCursor(dataType, value);
			 if (cursor.moveToFirst() == true){
				 accountFromDB = cursor.getString(1);
				 user.setAccount(accountFromDB);
				cursor.close();
			}
		}
		userService.logout(user);
		InfoActivity.this.finish();
		
	}

}
