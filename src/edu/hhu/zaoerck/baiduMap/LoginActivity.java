package edu.hhu.zaoerck.baiduMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button login;
//	private Button register;
	private CheckBox remAccount;
	private CheckBox remPassword;
	private EditText account;
	private EditText password;
	private SharedPreferences sp;
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		init();
		addAction();
	}
	
	private void addAction(){
		remAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					remAccount.setChecked(true);
				}else {
					remAccount.setChecked(false);
					remPassword.setChecked(false);
				}
			}
		});
		
		remPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					remAccount.setChecked(true);
					remPassword.setChecked(true);
				}else {
					remPassword.setChecked(false);
				}
			}
		});
	}
	
	
	public void init(){
		account = (EditText)findViewById(R.id.account);
		password = (EditText)findViewById(R.id.password);
		remAccount = (CheckBox)findViewById(R.id.remAccount);
		remPassword = (CheckBox)findViewById(R.id.remPassword);
		login = (Button) findViewById(R.id.login);
		sp = getSharedPreferences("baiduMap", Context.MODE_PRIVATE);
		editor = sp.edit();
		String accountFromSP = sp.getString("account", "");
		String passwordFromSP = sp.getString("password", "");
		String remAccountStatus = sp.getString("remAccountStatus", "0");
		String remPasswordStatus = sp.getString("remPasswordStatus", "0");
		String preLoginStatus = sp.getString("preLoginStatus", "0");
		if(preLoginStatus.equals("1")){
			if(remAccountStatus.equals("1")){
				account.setText(accountFromSP);
				remAccount.setChecked(true);
			}
			if(remPasswordStatus.equals("1")){
				password.setText(passwordFromSP);
				remPassword.setChecked(true);
			}
		}
	}
	/*
	 * 登录操作,检查remAccount和remPassword状态，并提交到SharedPreferences
	 */
	public void login(View v){
		String remAccountStatus = "0";
		String remPasswordStatus = "0";
		String loginStatus = "0";
		if(remAccount.isChecked()){
			remAccountStatus = "1";
		}
		if(remPassword.isChecked()){
			remPasswordStatus = "1";
		}
		editor.putString("remAccountStatus", remAccountStatus);
		editor.putString("remPasswordStatus", remPasswordStatus);
		editor.commit();
		
		String accountFromSP = sp.getString("account", "");
		String accountInput = account.getText().toString().trim();
		String passwordInput = password.getText().toString().trim();
		if((accountFromSP.equals("")) || (accountInput.equals(""))){
			Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_LONG).show();
			loginStatus = "0";
			editor.putString("preLoginStatus", loginStatus);
			editor.commit();
			account.setText("");
			password.setText("");
			return;
		}
		String passwordFromSp = sp.getString("password", "");
		
		if((accountInput.equals(accountFromSP)) && (passwordInput.equals(passwordFromSp))){
			loginStatus = "1";
			editor.putString("preLoginStatus", loginStatus);
			editor.commit();
			Toast.makeText(LoginActivity.this, "登录成功，马上进入地图页面...", Toast.LENGTH_LONG).show();
			//TODO 要写进入地图页面
			Intent intent = new Intent(LoginActivity.this,MapActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		else {
			loginStatus = "0";
			editor.putString("preLoginStatus", loginStatus);
			editor.commit();
			Toast.makeText(LoginActivity.this, "密码不正确", Toast.LENGTH_LONG).show();
			account.setText("");
			password.setText("");
			return;
		}
	}
	
	public void register(View v){
		Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
		startActivity(intent);
	}
}
