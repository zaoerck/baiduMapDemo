package edu.hhu.zaoerck.baiduMap;


import edu.hhu.zaoerck.baiduMap.R;

import edu.hhu.zaoerck.baiduMap.view.HandyTextView;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class AboutActivity extends Activity  {
	private HandyTextView mHtvVersionName;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
		context = AboutActivity.this;
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
	}
	
	public void assess(View v){
		Toast.makeText(AboutActivity.this, "抱歉！该功能暂不支持", Toast.LENGTH_SHORT).show();
	}
	
	public void checkUpdate(View v){
		Toast.makeText(AboutActivity.this, "抱歉！该功能暂不支持", Toast.LENGTH_SHORT).show();
	}
	
	public void about(View v) {
		LayoutInflater factory = LayoutInflater.from(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				AboutActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		final View aboutView = factory.inflate(R.layout.introduction, null);
		builder.setTitle("版本介绍");
		builder.setView(aboutView);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//TODO
					}
				});
		builder.create();
		builder.show();
	}
}
