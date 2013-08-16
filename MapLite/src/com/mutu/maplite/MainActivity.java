package com.mutu.maplite;


import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

import android.os.Bundle;
import android.app.Activity;
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
		
//		final ITileSource tileSource = TileSourceFactory.TIANDITU_CVA;
//		MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//		TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//		mapview.getOverlayManager().add(mapOverlay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
