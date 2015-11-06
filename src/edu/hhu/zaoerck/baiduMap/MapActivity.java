package edu.hhu.zaoerck.baiduMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends Activity implements OnClickListener,
		OnMenuItemClickListener {

	private MapView mapView;
	private BaiduMap baiduMap;
	private Context context;
	private ImageView trafficImageView;
	private ImageView staImageView;
	private int currentZoom = 8;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);
		initComponents();
		addActions();

		// 设置中心点坐标
		LatLng point = new LatLng(0, 0);
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.mark);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(12)
				.build();
		// 在地图上添加Marker，并显示
		baiduMap.addOverlay(option);
		// 定义
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		baiduMap.setMapStatus(mapStatusUpdate);
	}

	private void initComponents() {
		context = MapActivity.this;
		// 获取地图控件引用
		mapView = (MapView) findViewById(R.id.bmapView);
		trafficImageView = (ImageView) findViewById(R.id.trafficImageViewId);
		staImageView = (ImageView) findViewById(R.id.staImageViewId);
		// 移除百度logo
		mapView.removeViewAt(1);
		// 隐藏缩放控件
		hideZoomControls();
		// 隐藏比例尺
		mapView.removeViewAt(2);
		// View child = mMapView.getChildAt(1);
		// // 隐藏百度logo和缩放控件ZoomControl
		// if (child instanceof ImageView || child instanceof ZoomControls ) {
		// child.setVisibility(View.INVISIBLE);
		// }
		baiduMap = mapView.getMap();
		
	}

	private void addActions() {
		trafficImageView.setOnClickListener(this);
		staImageView.setOnClickListener(this);
		
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
		menu.add(6, 6, 6, "退出").setOnMenuItemClickListener(this);
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
					Toast.makeText(MapActivity.this, "还没写的城市经纬度判断", Toast.LENGTH_LONG).show();
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
					
					Toast.makeText(MapActivity.this, "还没写的公里数计算", Toast.LENGTH_LONG).show();
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create();
			builder.show();
			
			break;
		case 4:
			//TODO 当前用户信息
			break;
		case 5:
			baiduMap.clear();
			break;
		case 6:
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.trafficImageViewId:
			baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.staImageViewId:
			baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapView.onPause();
	}
}
