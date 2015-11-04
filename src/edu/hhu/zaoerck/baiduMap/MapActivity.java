package edu.hhu.zaoerck.baiduMap;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.ScaleDrawable;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.ZoomControls;

public class MapActivity extends Activity {
	
	MapView mMapView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.map);
      //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView);
        //移除百度logo
        mMapView.removeViewAt(1);
        //隐藏缩放控件
        hideZoomControls();
        //隐藏比例尺
        mMapView.removeViewAt(2);
	}
	
	//隐藏缩放控件
	protected void hideZoomControls(){
		int childCount = mMapView.getChildCount();
		View zoom = null;
		for (int i = 0; i < childCount; i++) {
			View child = mMapView.getChildAt(i);
			if (child instanceof ZoomControls) {
				zoom = child;
				break;
			}
		}
		zoom.setVisibility(View.GONE);
	}
	
	
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
        }  
}
