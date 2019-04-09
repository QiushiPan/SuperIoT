package com.example.superiot;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.service.LocationService;
import com.baidu.location.service.Utils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/***

 * @author baidu
 * 
 */
public class LocationFilter extends Activity {
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private Button reset;
	private TextView loc;
	private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>();
	private double lanumber,lonnumber;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationfilter);
		mMapView = (MapView) findViewById(R.id.bmapView);
		reset = (Button) findViewById(R.id.clear);
		loc = (TextView) findViewById(R.id.loc);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));

		Intent intent = getIntent();
		String content = intent.getStringExtra("content");
		if(content != null)
			{
			lanumber=Double.valueOf(content.substring(0, 2))+Double.valueOf(content.substring(2, 8))/600000;
			BigDecimal   b1   =   new   BigDecimal(lanumber);
			lanumber   =   b1.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();  
			lonnumber=Double.valueOf(content.substring(8, 11))+Double.valueOf(content.substring(11, 17))/600000;
			BigDecimal   b2   =   new   BigDecimal(lonnumber);
			lonnumber   =   b2.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
			loc.setText(String.valueOf(lanumber)+content.substring(17, 18)+" / "+String.valueOf(lonnumber)+content.substring(18, 19));
			if(content.charAt(17)=='S')
				lanumber = -lanumber;
		    if(content.charAt(18)=='W')
		    	lonnumber = -lonnumber;
			}
		//loc.setText(content.substring(0,2)+"."+ content.substring(2, 8)+content.substring(17, 18)+" / "+content.substring(8, 11)+"."+content.substring(11, 17)+content.substring(18, 19));
		

		try {
		LatLng point = new LatLng(lanumber, lonnumber);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark); 
		OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
		mBaiduMap.addOverlay(option);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
		}catch (Exception e) {
			// TODO: handle exception
		}
	}



	/***
	 * 接收定位结果消息，并显示在地图上
	 */


	@Override
	protected void onDestroy() {
		super.onDestroy();
//		WriteLog.getInstance().close();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBaiduMap != null)
					mBaiduMap.clear();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();

	}

	/**
	 * 
	 * @author baidu
	 *
	 */
	class LocationEntity {
		BDLocation location;
		long time;
	}
}
