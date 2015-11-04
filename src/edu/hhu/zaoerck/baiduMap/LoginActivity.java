package edu.hhu.zaoerck.baiduMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private Button login;
//	private Button register;
	private CheckBox remAccount;
	private CheckBox remPassword;
	private EditText account;
	private EditText password;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init();
		
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
//		register = (Button) findViewById(R.id.register);
//		register.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
//				startActivity(intent);
//			}
//		});
	}
	
	public void init(){
		account = (EditText)findViewById(R.id.account);
		password = (EditText)findViewById(R.id.password);
		remAccount = (CheckBox)findViewById(R.id.remAccount);
		remPassword = (CheckBox)findViewById(R.id.remPassword);
		login = (Button) findViewById(R.id.login);
	}
	
	public void register(View v){
		Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
		startActivity(intent);
	}
}
