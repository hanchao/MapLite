package com.mutu.maplite;

import com.mutu.mapapi.events.MapListener;
import com.mutu.mapapi.events.ScrollEvent;
import com.mutu.mapapi.events.ZoomEvent;
import com.mutu.mapapi.util.GeoPoint;
import com.mutu.mapapi.views.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class CompareActivity extends Activity  implements MapListener{

	MapView mapview1 = null;
	MapView mapview2 = null;
	private static final int MENU_LAST_ID = Menu.FIRST + 1; // Always set to last unused id
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compare);
		
		mapview1 = (MapView)findViewById(R.id.mapview1);
		mapview1.setMultiTouchControls(true);
		mapview1.setBuiltInZoomControls(true);
		
		mapview2 = (MapView)findViewById(R.id.mapview2);
		mapview2.setMultiTouchControls(true);
		mapview2.setBuiltInZoomControls(true);
		
        mapview1.getController().setZoom(6);
        mapview1.getController().setCenter(new GeoPoint(25.041667, 102.705));
        mapview2.getController().setZoom(6);
        mapview2.getController().setCenter(new GeoPoint(25.041667, 102.705));
        mapview1.setMapListener(this);
        mapview2.setMapListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compare, menu);
		return true;
	}

	@Override
	public boolean onScroll(ScrollEvent event) {
		// TODO Auto-generated method stub
		if(event.getSource() == mapview1){
			mapview2.setMapListener(null);
			mapview2.scrollTo(event.getX(), event.getY());
			mapview2.setMapListener(this);
		}else{
			mapview1.setMapListener(null);
			mapview1.scrollTo(event.getX(), event.getY());
			mapview1.setMapListener(this);
		}
		return true;
	}

	@Override
	public boolean onZoom(ZoomEvent event) {
		// TODO Auto-generated method stub
		if(event.getSource() == mapview1){
			mapview2.setMapListener(null);
			mapview2.getController().setCenter(event.getSource().getMapCenter());
			mapview2.getController().setZoom(event.getZoomLevel());
			mapview2.setMapListener(this);
		}else{
			mapview1.setMapListener(null);
			mapview1.getController().setCenter(event.getSource().getMapCenter());
			mapview1.getController().setZoom(event.getZoomLevel());
			mapview1.setMapListener(this);
		}
		return true;
	}

}
