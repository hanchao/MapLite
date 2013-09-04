package com.mutu.maplite;

import com.mutu.mapapi.util.GeoPoint;
import com.mutu.mapapi.views.MapView;

import android.os.Bundle;
import android.app.Activity;
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
		
        mapview.getController().setZoom(6);
        mapview.getController().setCenter(new GeoPoint(25.041667, 102.705));
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
