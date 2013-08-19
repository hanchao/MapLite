package com.mutu.maplite;




import com.mutu.mapapi.api.IGeoPoint;
import com.mutu.mapapi.mappoint.TileSystem;
import com.mutu.mapapi.tileprovider.MapTileProviderBasic;
import com.mutu.mapapi.tileprovider.tilesource.ITileSource;
import com.mutu.mapapi.tileprovider.tilesource.TileSourceFactory;
import com.mutu.mapapi.util.GeoPoint;
import com.mutu.mapapi.views.MapView;
import com.mutu.mapapi.views.overlay.DirectedLocationOverlay;
import com.mutu.mapapi.views.overlay.ScaleBarOverlay;
import com.mutu.mapapi.views.overlay.SimpleLocationOverlay;
import com.mutu.mapapi.views.overlay.TilesOverlay;
import com.mutu.mapapi.views.overlay.compass.CompassOverlay;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.view.Menu;

public class MainActivity extends Activity {

	MapView mapview = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mapview = (MapView)findViewById(R.id.mapview);
		mapview.setMultiTouchControls(true);
		mapview.setBuiltInZoomControls(true);
		
		
//		IGeoPoint neGeoPoint = TileSystem.PixelXYToLatLong(255, 255,
//				0, null);
//		
//		Point pnt = TileSystem.LatLongToPixelXY(neGeoPoint.getLatitudeE6() / 1E6, neGeoPoint.getLongitudeE6() / 1E6, 0, null);
		
		
//		final ITileSource tileSource = TileSourceFactory.TIANDITU_CVA;
//		MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//		TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//		mapview.getOverlayManager().add(mapOverlay);
		
		DirectedLocationOverlay directedLocationOverlay = new DirectedLocationOverlay(this);
		mapview.getOverlayManager().add(directedLocationOverlay);
		ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(this);
		mapview.getOverlayManager().add(scaleBarOverlay);
		
		SimpleLocationOverlay compassOverlay = new SimpleLocationOverlay(this);
		compassOverlay.setLocation(new GeoPoint(0,0));
		mapview.getOverlayManager().add(compassOverlay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
