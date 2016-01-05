package edu.hhu.zaoerck.baiduMap;

import edu.hhu.zaoerck.baiduMap.R;

import edu.hhu.zaoerck.baiduMap.view.HandyTextView;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AboutActivity extends Activity {
	private HandyTextView mHtvVersionName;
	private Button mBtnCheckNewVersion;
	private Button mBtnIntroduction;
	private Button mBtnGoOfficialWebsite;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
	}
	
	
	protected void initViews() {
		mHtvVersionName = (HandyTextView) findViewById(R.id.about_htv_versionname);
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			mHtvVersionName.setText("版本: Android " + packageInfo.versionName);
		} catch (NameNotFoundException e) {
			mHtvVersionName.setVisibility(View.GONE);
		}
		mBtnCheckNewVersion = (Button) findViewById(R.id.about_btn_checknewversion);
		mBtnIntroduction = (Button) findViewById(R.id.about_btn_introduction);
		mBtnGoOfficialWebsite = (Button) findViewById(R.id.about_btn_go_official_website);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

}
