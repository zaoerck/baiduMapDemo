package edu.hhu.zaoerck.baiduMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class InfoActivity extends Activity {

	private SharedPreferences sp;
	private TextView account;
	private TextView gender;
	private TextView phoneNum;
	private TextView email;
	private TextView birthday;
	private TextView address;
	private EditText introduction;
	private TextView interest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info);
		init();
		
	}
	
	private void init(){
		sp = getSharedPreferences("baiduMap", Context.MODE_PRIVATE);
		account = (TextView) findViewById(R.id.account);
		gender = (TextView) findViewById(R.id.gender);
		phoneNum = (TextView) findViewById(R.id.phoneNum);
		email = (TextView) findViewById(R.id.email);
		birthday = (TextView) findViewById(R.id.birthdate);
		address = (TextView) findViewById(R.id.birthAddress);
		introduction = (EditText) findViewById(R.id.introduction);
		interest = (TextView) findViewById(R.id.interest);
		
		String accountString = sp.getString("account", "");
		String genderString = sp.getString("gender", "");
		String phoneNumString = sp.getString("phoneNum", "0");
		String emailString = sp.getString("email", "0");
		String birthdayString = sp.getString("birthday", "0");
		String addressString = sp.getString("address", "0");
		String introductionString = sp.getString("introduction", "0");
		String interestString = sp.getString("interest", "0");
		
		account.setText(accountString);
		gender.setText(genderString);
		phoneNum.setText(phoneNumString);
		email.setText(emailString);
		birthday.setText(birthdayString);
		introduction.setText(introductionString);
		interest.setText(interestString);
		address.setText(addressString);
	}

}
