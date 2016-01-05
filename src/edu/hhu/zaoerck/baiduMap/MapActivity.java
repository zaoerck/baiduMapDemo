package edu.hhu.zaoerck.baiduMap;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class MapActivity extends Activity implements OnMenuItemClickListener, OnClickListener {

	private MapView mapView;
	private BaiduMap baiduMap;
	private Context context;
	private int currentZoom = 8;
	private SharedPreferences sp;
	private Editor editor;
	
	
	
	private LocationClient locationClient;
	private BDLocationListener locationListener;
	private BDNotifyListener notifyListener;

	private double longitude;// 精度
	private double latitude;// 维度
	private float radius;// 定位精度半径，单位是米
	private String addrStr;// 反地理编码
	private String province;// 省份信息
	private String city;// 城市信息
	private String district;// 区县信息
	private float direction;// 手机方向信息

	private int locType;

	// 定位按钮
	private Button locateBtn;
	// 定位模式 （普通-跟随-罗盘）
	private MyLocationConfiguration.LocationMode currentMode;
	// 定位图标描述
	private BitmapDescriptor currentMarker = null;
	// 记录是否第一次定位
	private boolean isFirstLoc = true;
	
	//振动器设备
	private Vibrator mVibrator;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);
		// 地图初始化
		initComponents();

		locateBtn = (Button) findViewById(R.id.locate_btn);
		locateBtn.setOnClickListener(this);
		currentMode = MyLocationConfiguration.LocationMode.NORMAL;
		locateBtn.setText("普通");
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		init();
//		// 设置中心点坐标
//		LatLng point = new LatLng(0, 0);
//		BitmapDescriptor bitmap = BitmapDescriptorFactory
//				.fromResource(R.drawable.mark);
//		// 构建MarkerOption，用于在地图上添加Marker
//		OverlayOptions option = new MarkerOptions().position(point)
//				.icon(bitmap);
//		// 定义地图状态
//		MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(12)
//				.build();
//		// 在地图上添加Marker，并显示
//		baiduMap.addOverlay(option);
//		// 定义
//		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
//				.newMapStatus(mMapStatus);
//		baiduMap.setMapStatus(mapStatusUpdate);
	}
	
	private void init() {
		baiduMap.setMyLocationEnabled(true);
		// 1. 初始化LocationClient类
		locationClient = new LocationClient(getApplicationContext());
		// 2. 声明LocationListener类
		locationListener = new MyLocationListener();
		// 3. 注册监听函数
		locationClient.registerLocationListener(locationListener);
		// 4. 设置参数
		LocationClientOption locOption = new LocationClientOption();
//		locOption.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
		locOption.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		locOption.setCoorType("bd09ll");// 设置定位结果类型
//		locOption.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
		locOption.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		locOption.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向

		locationClient.setLocOption(locOption);
		// 5. 注册位置提醒监听事件
		notifyListener = new MyNotifyListener();
		notifyListener.SetNotifyLocation(longitude, latitude, 3000, "bd09ll");//精度，维度，范围，坐标类型
		locationClient.registerNotify(notifyListener);
		// 6. 开启/关闭 定位SDK
		locationClient.start();
		// locationClient.stop();
		// 发起定位，异步获取当前位置，因为是异步的，所以立即返回，不会引起阻塞
		// 定位的结果在ReceiveListener的方法onReceive方法的参数中返回。
		// 当定位SDK从定位依据判定，位置和上一次没发生变化，而且上一次定位结果可用时，则不会发生网络请求，而是返回上一次的定位结果。
		// 返回值，0：正常发起了定位 1：service没有启动 2：没有监听函数
		// 6：两次请求时间太短（前后两次请求定位时间间隔不能小于1000ms）
		/*
		 * if (locationClient != null && locationClient.isStarted()) {
		 * requestResult = locationClient.requestLocation(); } else {
		 * Log.d("LocSDK5", "locClient is null or not started"); }
		 */

	}
	
	class MyLocationListener implements BDLocationListener {
		// 异步返回的定位结果
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			locType = location.getLocType();
			Toast.makeText(MapActivity.this, "当前定位的返回值是："+locType, Toast.LENGTH_SHORT).show();
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			if (location.hasRadius()) {// 判断是否有定位精度半径
				radius = location.getRadius();
			}
			if (locType == BDLocation.TypeGpsLocation) {//
				Toast.makeText(
						MapActivity.this,
						"当前速度是：" + location.getSpeed() + "~~定位使用卫星数量："
								+ location.getSatelliteNumber(),
						Toast.LENGTH_SHORT).show();
			} else if (locType == BDLocation.TypeNetWorkLocation) {
				addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
				Toast.makeText(MapActivity.this, addrStr,
						Toast.LENGTH_SHORT).show();
			}
			direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
			province = location.getProvince();// 省份
			city = location.getCity();// 城市
			district = location.getDistrict();// 区县
			Toast.makeText(MapActivity.this,
					province + "~" + city + "~" + district, Toast.LENGTH_SHORT)
					.show();
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(radius)//
					.direction(direction)// 方向
					.latitude(latitude)//
					.longitude(longitude)//
					.build();
			// 设置定位数据
			baiduMap.setMyLocationData(locData);
			LatLng ll = new LatLng(latitude, longitude);
			float f = baiduMap.getMaxZoomLevel();
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, f-3);
			baiduMap.animateMapStatus(msu);

		}
	}
	
	/**
	 * 位置提醒监听器
	 * @author ys
	 *
	 */
	class MyNotifyListener extends BDNotifyListener {
		@Override
		public void onNotify(BDLocation bdLocation, float distance) {
			super.onNotify(bdLocation, distance);
			mVibrator.vibrate(1000);//振动提醒已到设定位置附近
	    	Toast.makeText(MapActivity.this, "震动提醒", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.locate_btn:// 定位
			switch (currentMode) {
			case NORMAL:
				locateBtn.setText("跟随");
				currentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
				break;
			case FOLLOWING:
				locateBtn.setText("罗盘");
				currentMode = MyLocationConfiguration.LocationMode.COMPASS;
				break;
			case COMPASS:
				locateBtn.setText("普通");
				currentMode = MyLocationConfiguration.LocationMode.NORMAL;
				break;
			}
			baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					currentMode, true, currentMarker));
			break;
		}
	}

	private void initComponents() {
		context = MapActivity.this;
		// 获取地图控件引用
		mapView = (MapView) findViewById(R.id.bmapView);
		// 移除百度logo
		mapView.removeViewAt(1);
		// 隐藏缩放控件
		hideZoomControls();
		baiduMap = mapView.getMap();
		sp = getSharedPreferences("cityLocation", Context.MODE_PRIVATE);
		editor = sp.edit();
		
	}


	// 隐藏缩放控件
	protected void hideZoomControls() {
		int childCount = mapView.getChildCount();
		View zoom = null;
		for (int i = 0; i < childCount; i++) {
			View child = mapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				zoom = child;
				break;
			}
		}
		zoom.setVisibility(View.GONE);
	}

	

	// 添加菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		// groupId,itemId,orderId,名称
		menu.add(1, 1, 1, "经纬度定位").setOnMenuItemClickListener(this);
		menu.add(2, 2, 2, "城市定位").setOnMenuItemClickListener(this);
		menu.add(3, 3, 3, "公里数计算").setOnMenuItemClickListener(this);
		menu.add(4, 4, 4, "当前用户信息").setOnMenuItemClickListener(this);
		menu.add(5, 5, 5, "清除屏幕").setOnMenuItemClickListener(this);
		menu.add(6, 6, 6, "关于").setOnMenuItemClickListener(this);
		menu.add(7, 7, 7, "退出").setOnMenuItemClickListener(this);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		int itemId = item.getItemId();
		LayoutInflater factory = LayoutInflater.from(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MapActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		switch (itemId) {
		case 1:
			final View jwddwView = factory.inflate(R.layout.setlocation, null);
			builder.setTitle("经纬度定位");
			builder.setView(jwddwView);
			final EditText jingduEditText = (EditText) jwddwView
					.findViewById(R.id.jingduEditTextId);
			final EditText weiduEditText = (EditText) jwddwView
					.findViewById(R.id.weiduEditTextId);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							String jingduString = jingduEditText.getText()
									.toString().trim();
							String weiduString = weiduEditText.getText()
									.toString().trim();
							double jindu;
							double weidu;
							try{
								jindu = Double.parseDouble(jingduString);
								weidu = Double.parseDouble(weiduString);

							}catch(NumberFormatException e){
								Toast.makeText(MapActivity.this, "请输入正确经纬度", Toast.LENGTH_LONG).show();
							    return;
							}
							
							LatLng point = new LatLng(weidu, jindu);
							BitmapDescriptor bitmap = BitmapDescriptorFactory
									.fromResource(R.drawable.mark);
							OverlayOptions option = new MarkerOptions()
									.position(point).icon(bitmap);
							baiduMap.addOverlay(option);
							baiduMap.setMapStatus(MapStatusUpdateFactory
									.newMapStatus(new MapStatus.Builder()
											.target(point).zoom(currentZoom)
											.build()));
						}
					});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();
			break;
		case 2:
			//TODO 城市定位
			final View setCityView = factory.inflate(R.layout.setcity, null);
			builder.setTitle("城市定位");
			builder.setView(setCityView);
			
			final EditText cityEditText = (EditText) setCityView.findViewById(R.id.city);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String cityString = cityEditText.getText().toString().trim();
					//TODO 判断城市的经纬度
					location();
					String cityStringFromSp = sp.getString(cityString, "");
					if(cityStringFromSp.equals("")){
						Toast.makeText(MapActivity.this, "暂不支持查询该城市", Toast.LENGTH_LONG).show();
						return;
					}
					else {
						String position[] = cityStringFromSp.split(",");
						double latitude = Double.parseDouble(position[1]);
						double longtitude = Double.parseDouble(position[0]);
						LatLng point = new LatLng(latitude, longtitude);
						BitmapDescriptor bitmap = BitmapDescriptorFactory
								.fromResource(R.drawable.mark);
						OverlayOptions option = new MarkerOptions()
								.position(point).icon(bitmap);
						baiduMap.addOverlay(option);
						baiduMap.setMapStatus(MapStatusUpdateFactory
								.newMapStatus(new MapStatus.Builder()
										.target(point).zoom(currentZoom)
										.build()));
					}
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();
			break;
		case 3:
			//TODO 公里数计算
			final View countDistanceView = factory.inflate(R.layout.distance, null);
			builder.setTitle("城市定位");
			builder.setView(countDistanceView);
			
			final EditText cityAEditText = (EditText) countDistanceView.findViewById(R.id.cityA);
			final EditText cityBEditText = (EditText) countDistanceView.findViewById(R.id.cityB);
			final EditText longitude_placeAEditText = (EditText) countDistanceView.findViewById(R.id.longitude_placeA);
			final EditText latitude_placeAEditText = (EditText) countDistanceView.findViewById(R.id.latitude_placeA);
			final EditText longitude_placeBEditText = (EditText) countDistanceView.findViewById(R.id.longitude_placeB);
			final EditText latitude_placeBEditText = (EditText) countDistanceView.findViewById(R.id.latitude_placeB);
			final LinearLayout nameALayout = (LinearLayout) countDistanceView.findViewById(R.id.nameA);
			final LinearLayout nameBLayout = (LinearLayout) countDistanceView.findViewById(R.id.nameB);
			final LinearLayout positionALayout = (LinearLayout) countDistanceView.findViewById(R.id.positionA);
			final LinearLayout positionBLayout = (LinearLayout) countDistanceView.findViewById(R.id.positionB);
			final CheckBox withCityName = (CheckBox)countDistanceView.findViewById(R.id.withCityName);
			final CheckBox withPosition = (CheckBox)countDistanceView.findViewById(R.id.withPosition);
			
			String latitude_placeB = latitude_placeBEditText.getText().toString().trim();
			
			withCityName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						withPosition.setChecked(false);
						nameALayout.setVisibility(View.VISIBLE);
						nameBLayout.setVisibility(View.VISIBLE);
						positionALayout.setVisibility(View.GONE);
						positionBLayout.setVisibility(View.GONE);
					}else {
						withPosition.setChecked(true);
						nameALayout.setVisibility(View.GONE);
						nameBLayout.setVisibility(View.GONE);
						positionALayout.setVisibility(View.VISIBLE);
						positionBLayout.setVisibility(View.VISIBLE);
					}
				}
			});
			
			withPosition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						withCityName.setChecked(false);
						nameALayout.setVisibility(View.GONE);
						nameBLayout.setVisibility(View.GONE);
						positionALayout.setVisibility(View.VISIBLE);
						positionBLayout.setVisibility(View.VISIBLE);
					}else {
						withCityName.setChecked(true);
						nameALayout.setVisibility(View.VISIBLE);
						nameBLayout.setVisibility(View.VISIBLE);
						positionALayout.setVisibility(View.GONE);
						positionBLayout.setVisibility(View.GONE);
					}
				}
			});
			
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//TODO 写两点距离的逻辑
					String cityAString = cityAEditText.getText().toString().trim();
					String cityBString = cityBEditText.getText().toString().trim();
					String longitude_placeA = longitude_placeAEditText.getText().toString().trim();
					String latitude_placeA = latitude_placeAEditText.getText().toString().trim();
					String longitude_placeB = longitude_placeBEditText.getText().toString().trim();
					String laitude_placeB = latitude_placeBEditText.getText().toString().trim();
					if(withPosition.isChecked()){
						double longtitudeA, longtitudeB, latitudeA,latitudeB;
						try{
							longtitudeA = Double.parseDouble(longitude_placeA);
							latitudeA = Double.parseDouble(latitude_placeA);
							longtitudeB = Double.parseDouble(longitude_placeB);
							latitudeB = Double.parseDouble(laitude_placeB);

						}catch(NumberFormatException e){
							Toast.makeText(MapActivity.this, "请输入正确经纬度", Toast.LENGTH_LONG).show();
						    return;
						}
						LatLng point1 = new LatLng(longtitudeA, latitudeA);
						LatLng point2 = new LatLng(longtitudeB, latitudeB);
						DecimalFormat df = new DecimalFormat("######0.00"); 
						double distance = DistanceUtil. getDistance(point1, point2)/1000;
						
						Toast.makeText(MapActivity.this, "距离为" + df.format(distance) + "公里", Toast.LENGTH_LONG).show();
						return;
					}
					else if (withCityName.isChecked()) {
						//TODO asdf
						String cityAStringFromSp = sp.getString(cityAString, "");
						String cityBStringFromSp = sp.getString(cityBString, "");
						if(cityAStringFromSp.equals("") || cityBStringFromSp.equals("")){
							Toast.makeText(MapActivity.this, "暂不支持查询该城市", Toast.LENGTH_LONG).show();
							return;
						}
						else{
							String positionA[] = cityAStringFromSp.split(",");
							double latitudeA = Double.parseDouble(positionA[1]);
							double longtitudeA = Double.parseDouble(positionA[0]);
							LatLng pointA = new LatLng(latitudeA, longtitudeA);
							
							String positionB[] = cityBStringFromSp.split(",");
							double latitudeB = Double.parseDouble(positionB[1]);
							double longtitudeB = Double.parseDouble(positionB[0]);
							LatLng pointB = new LatLng(latitudeB, longtitudeB);
							
							DecimalFormat df = new DecimalFormat("######0.00"); 
							double distance = DistanceUtil. getDistance(pointA, pointB)/1000;
							Toast.makeText(MapActivity.this, "距离为" + df.format(distance) + "公里", Toast.LENGTH_LONG).show();
							return;
						}
					}
					
					
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();
			
			break;
		case 4:
			//TODO 当前用户信息
			Intent intent = new Intent(MapActivity.this,InfoActivity.class);
			startActivity(intent);
			break;
		case 5:
			
			baiduMap.clear();
			break;
		case 6:
			//TODO 当前用户信息
			Intent intent_about = new Intent(MapActivity.this,AboutActivity.class);
			startActivity(intent_about);
			break;
		case 7:
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("退出提醒");
			builder.setMessage("你确认退出吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							MapActivity.this.finish();
						}
					});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();
			break;
		default:
			break;
		}
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MapActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("退出提醒");
			builder.setMessage("你确认退出吗？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							MapActivity.this.finish();
						}
					});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	
	
	private void location(){
		//TODO 将地理信息写入xml中
		editor.putString("北京","116.41667,39.91667");
		editor.putString("Beijing","116.41667,39.91667");
		editor.putString("上海","121.48023,31.23630");
		editor.putString("Shanghai","121.43333,34.50000");
		editor.putString("天津","117.20000,39.13333");
		editor.putString("Tianjin","117.20000,39.13333");
		editor.putString("香港","114.10000,22.20000");
		editor.putString("广州","113.23333,23.16667");
		editor.putString("Guangzhou","113.23333,23.16667");
		editor.putString("珠海","113.51667,22.30000");
		editor.putString("Zhuhai","113.51667,22.30000");
		editor.putString("深圳","114.06667,22.61667");
		editor.putString("Shenzhen","114.06667,22.61667");
		editor.putString("杭州","120.20000,30.26667");
		editor.putString("Hangzhou","120.20000,30.26667");
		editor.putString("重庆","106.45000,29.56667");
		editor.putString("Chongqin","106.45000,29.56667");
		editor.putString("南京","118.78333,32.05000");
		editor.putString("Nanjing","118.78333,32.05000");
		editor.putString("南昌","115.90000,28.68333");
		editor.putString("Nanchang","115.90000,28.68333");
		editor.commit();
	}
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
		
		baiduMap.setMyLocationEnabled(false);
		locationClient.unRegisterLocationListener(locationListener);
		//取消位置提醒
		locationClient.removeNotifyEvent(notifyListener);
		locationClient.stop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mapView. onResume ()，实现地图生命周期管理
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mapView. onPause ()，实现地图生命周期管理
		mapView.onPause();
	}
}
