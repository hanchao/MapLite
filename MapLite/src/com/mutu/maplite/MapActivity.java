package com.mutu.maplite;

import com.android.gis.Workspace;
import com.mutu.mapapi.util.GeoPoint;
import com.mutu.mapapi.views.MapView;
import com.mutu.mapapi.views.overlay.ScaleBarOverlay;
import com.mutu.mapapi.views.overlay.SimpleLocationOverlay;
import com.mutu.mapapi.views.overlay.compass.CompassOverlay;
import com.mutu.mapapi.views.overlay.compass.InternalCompassOrientationProvider;
import com.mutu.mapapi.views.overlay.egis.EGISOverlay;
import com.mutu.mapapi.views.overlay.mylocation.GpsMyLocationProvider;
import com.mutu.mapapi.views.overlay.mylocation.MyLocationNewOverlay;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends Activity {

	MapView mapview = null;
    private static final int MENU_LAST_ID = Menu.FIRST + 1; // Always set to last unused id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		mapview = (MapView)findViewById(R.id.mapview);
		mapview.setMultiTouchControls(true);
		mapview.setBuiltInZoomControls(true);
		
        mapview.getController().setZoom(6);//39°54′57″N 116°23′26″E
        mapview.getController().setCenter(new GeoPoint(39.85, 116.35));
        
		SimpleLocationOverlay simpleLocationOverlay = new SimpleLocationOverlay(this);
		simpleLocationOverlay.setLocation(new GeoPoint(25.041667, 102.705));
		mapview.getOverlayManager().add(simpleLocationOverlay);
      {
  		final DisplayMetrics dm = this.getResources().getDisplayMetrics();
      	ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(this);
      	scaleBarOverlay.setCentred(true);
      	scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
      	this.mapview.getOverlays().add(scaleBarOverlay);
      }
      
		MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(this, new GpsMyLocationProvider(this),
		    mapview);
		mapview.getOverlayManager().add(locationOverlay);
		
		CompassOverlay compassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this),
			mapview);
		
		mapview.getOverlayManager().add(compassOverlay);
		
		locationOverlay.enableMyLocation(new GpsMyLocationProvider(this));
		compassOverlay.enableCompass(new InternalCompassOrientationProvider(this));

		//EGIS
		{
			Workspace mWorkspace = new Workspace();
			boolean opened = mWorkspace.open("/sdcard/jingjin/jingjin.smwu"); 
			if(opened && mWorkspace.getMapCount()!=0){
				String mapname = mWorkspace.getMapNameAt(0);
				com.android.gis.MapView mMapView = new com.android.gis.MapView(this);
				mMapView.AttachWorkspace(mWorkspace);
				if(mMapView.Open(mapname)){
					EGISOverlay egisOverlay = new EGISOverlay(this,mMapView);
					this.mapview.getOverlays().add(egisOverlay);
				}
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.map, menu);
		
        // Put overlay items first
		mapview.getOverlayManager().onCreateOptionsMenu(menu, MENU_LAST_ID, mapview);
        
		return true;
	}

    @Override
    public boolean onPrepareOptionsMenu(final Menu pMenu)
    {
    	mapview.getOverlayManager().onPrepareOptionsMenu(pMenu, MENU_LAST_ID, mapview);
        return super.onPrepareOptionsMenu(pMenu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mapview.getOverlayManager().onOptionsItemSelected(item, MENU_LAST_ID, mapview))
			return true;
		
		return super.onOptionsItemSelected(item);
	}
}
