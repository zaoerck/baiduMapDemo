package edu.hhu.zaoerck.baiduMap;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
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
		//设置初试日期
		,c.get(Calendar.YEAR)
		,c.get(Calendar.MONTH)
		,c.get(Calendar.DAY_OF_MONTH)).show();
	}

}
