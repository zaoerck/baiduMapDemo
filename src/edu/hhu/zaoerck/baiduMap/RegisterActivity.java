package edu.hhu.zaoerck.baiduMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
	}

}
