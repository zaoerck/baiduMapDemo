package edu.hhu.zaoerck.baiduMap;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	EditText account;
	EditText password;
	EditText rePassword;
	RadioButton male;
	RadioButton female;
	EditText phoneNum;
	EditText email;
	EditText birthday;
	EditText address;
	Spinner spinner;
	EditText introduction;
	Button register2;
	SharedPreferences sp;
	Editor editor;
	String interest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		initData();
		addAction();
		
	}
	
	private void initData(){
		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);
		rePassword = (EditText) findViewById(R.id.rePassword);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
		phoneNum = (EditText) findViewById(R.id.phoneNum);
		email = (EditText) findViewById(R.id.email);
		birthday = (EditText) findViewById(R.id.birthdate);
		address = (EditText) findViewById(R.id.birthAddress);
		introduction = (EditText) findViewById(R.id.introduction);
		register2 = (Button) findViewById(R.id.register2);
		spinner = (Spinner) findViewById(R.id.interest);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				interest = (String) spinner.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sp = getSharedPreferences("baiduMap", Context.MODE_PRIVATE);
		editor = sp.edit();
		
	}
	
	private void addAction(){
		//为注册按钮添加动作监听
		register2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String accountStr = account.getText().toString().trim();
				String passwordStr = password.getText().toString().trim();
				String rePasswordStr = rePassword.getText().toString().trim();
				String gender = male.isChecked()?"男":"女";
				String phoneNumStr = phoneNum.getText().toString().trim();
				String emailStr = email.getText().toString().trim();
				String birthdayStr = birthday.getText().toString().trim();
				String addressStr = address.getText().toString().trim();
				String interestStr = interest.toString().trim();
				String introductionStr = introduction.getText().toString().trim();
				
				//获取当前时间
				Calendar now = Calendar.getInstance();
				int currentYear = now.get(Calendar.YEAR);
				int currentMonth = now.get(Calendar.MONTH);
				int currentDay = now.get(Calendar.DAY_OF_MONTH);
				int birthYear;
				int birthMonth;
				int birthDay;
				
				
				
				//2.做一些数据格式和有效性的验证
				String accountFromSp = sp.getString("account", "");
				if((accountStr.equals(""))||(passwordStr.equals(""))||(rePasswordStr.equals(""))){
					Toast.makeText(RegisterActivity.this, "账号或密码不能为空！", Toast.LENGTH_LONG).show();
				}
				//2.1账号是否存在的验证
				else if((!accountFromSp.equals(""))&&(accountFromSp.equals(accountStr))){
					Toast.makeText(RegisterActivity.this, "账号已存在！", Toast.LENGTH_LONG).show();
				}
				//2.2密码是否一致的验证
				else if(!passwordStr.equals(rePasswordStr)){
					Toast.makeText(RegisterActivity.this, "密码不一致！", Toast.LENGTH_LONG).show();
				}
				//判断手机号是否为空
				else if(phoneNumStr.equals("")||phoneNumStr.length()!=11){
					Toast.makeText(RegisterActivity.this, "手机号格式错误或为空！", Toast.LENGTH_LONG).show();
				}
				//判断邮箱格式
				else if(!checkEmail(emailStr)){
					Toast.makeText(RegisterActivity.this, "邮箱格式错误或为空！", Toast.LENGTH_LONG).show();
				}
				//2.3生日是否有效的验证（最起码不能超过当天）
				else {
					//获取生日的年月日
					String[] a = birthdayStr.split("-");
					try{
						birthYear = Integer.parseInt(a[0]);
						birthMonth = Integer.parseInt(a[1]);
						birthDay = Integer.parseInt(a[2]); 

					}catch(NumberFormatException e){
						Toast.makeText(RegisterActivity.this, "日期不能为空！", Toast.LENGTH_LONG).show();
					    return;
					}
					if((birthYear >= currentYear)&&(birthMonth >= currentMonth)&&(birthDay >= currentDay)){
						Toast.makeText(RegisterActivity.this, "生日日期无效！", Toast.LENGTH_LONG).show();
					}
					else if(addressStr.equals("")){
						Toast.makeText(RegisterActivity.this, "籍贯不能为空！", Toast.LENGTH_LONG).show();
					}
					else if(introductionStr.equals("")){
						Toast.makeText(RegisterActivity.this, "自我介绍不能为空", Toast.LENGTH_LONG).show();
					}
					else{
						editor.putString("account", accountStr);
						editor.putString("password", passwordStr);
						editor.putString("rePassword", rePasswordStr);
						editor.putString("gender", gender);
						editor.putString("phoneNum", phoneNumStr);
						editor.putString("email", emailStr);
						editor.putString("birthday", birthdayStr);
						editor.putString("address", addressStr);
						editor.putString("interest", interestStr);
						editor.putString("introduction", introductionStr);
					
						//提交存储信息并提示
						editor.commit();
						Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
						finish();
					}
				
				}
			}
		});
	}
	
	/**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
	
	public void dateChoose(View v){
		Calendar c = Calendar.getInstance();
		//直接创建一个DatePickerDialog对话实例，并将它显示出来
		new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				EditText show = (EditText) findViewById(R.id.birthdate);
				show.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
			}
		}
		//设置初始日期
		,c.get(Calendar.YEAR)
		,c.get(Calendar.MONTH)
		,c.get(Calendar.DAY_OF_MONTH)).show();
	}
	
	public void cancel(View v){
		finish();
	}

}
