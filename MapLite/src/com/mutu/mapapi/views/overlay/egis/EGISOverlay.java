package com.mutu.mapapi.views.overlay.egis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Point;

import com.android.gis.Layer;
import com.android.gis.Workspace;
import com.mutu.mapapi.tileprovider.modules.MapTileDownloader;
import com.mutu.mapapi.tilesystem.TileSystem;
import com.mutu.mapapi.views.MapView;
import com.mutu.mapapi.views.MapView.Projection;
import com.mutu.mapapi.views.overlay.SafeDrawOverlay;
import com.mutu.mapapi.views.safecanvas.ISafeCanvas;
import com.mutu.mapapi.views.safecanvas.SafePaint;
import com.mutu.mapapi.views.util.constants.MapViewConstants;

public class EGISOverlay extends SafeDrawOverlay{

	private static final Logger logger = LoggerFactory.getLogger(MapTileDownloader.class);

	
	com.android.gis.MapView mMapView = null;
	Layer mLayer = null;
	public EGISOverlay(Context ctx, com.android.gis.MapView mapView) {
		super(ctx);
		// TODO Auto-generated constructor stub
		mMapView = mapView;
	}

	@Override
	protected void drawSafe(ISafeCanvas c, MapView osmv, boolean shadow) {
		// TODO Auto-generated method stub
		if (shadow)
			return;
		
		if(mMapView == null)
			return;
		
		
		long iSTime = System.nanoTime();
		
		EGISRender.drawMapView(mMapView, c, osmv);
		long iETime = System.nanoTime();
		logger.debug("EGISOverlay draw time :" + (iETime - iSTime)/1000000000);
	}

}
