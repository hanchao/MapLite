package com.mutu.mapapi.views.overlay.egis;

import android.content.Context;
import android.graphics.Point;

import com.android.gis.Layer;
import com.android.gis.Workspace;
import com.mutu.mapapi.tilesystem.TileSystem;
import com.mutu.mapapi.views.MapView;
import com.mutu.mapapi.views.MapView.Projection;
import com.mutu.mapapi.views.overlay.SafeDrawOverlay;
import com.mutu.mapapi.views.safecanvas.ISafeCanvas;
import com.mutu.mapapi.views.safecanvas.SafePaint;
import com.mutu.mapapi.views.util.constants.MapViewConstants;

public class EGISOverlay extends SafeDrawOverlay{

	Workspace mWorkspace = new Workspace();
	com.android.gis.MapView mMapView = null;
	Layer mLayer = null;
	public EGISOverlay(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
		boolean opened = mWorkspace.open("/sdcard/jingjin/jingjin.smwu"); 
		if(opened){
			String mapname = mWorkspace.getMapNameAt(0);
			mMapView = new com.android.gis.MapView(ctx);
			mMapView.AttachWorkspace(mWorkspace);
			if(mMapView.Open(mapname)){
				
			}
		}
	}

	@Override
	protected void drawSafe(ISafeCanvas c, MapView osmv, boolean shadow) {
		// TODO Auto-generated method stub
		if (shadow)
			return;
		
		TileSystem tileSystem = osmv.getTileProvider().getTileSource().getTileSystem();
		Point mMapCoords = tileSystem.LatLongToPixelXY(0,116,
				MapViewConstants.MAXIMUM_ZOOMLEVEL,null);
		final int worldWidthSize_2 = tileSystem.MapWidthPixelSize(MapViewConstants.MAXIMUM_ZOOMLEVEL) / 2;
		final int worldHeigthSize_2 = tileSystem.MapHeigthPixelSize(MapViewConstants.MAXIMUM_ZOOMLEVEL) / 2;
		mMapCoords.offset(-worldWidthSize_2, -worldHeigthSize_2);
		
		final Projection pj = osmv.getProjection();
		final int zoomDiff = MapViewConstants.MAXIMUM_ZOOMLEVEL - pj.getZoomLevel();
		
		c.drawCircle(mMapCoords.x >> zoomDiff, mMapCoords.y >> zoomDiff, 5, new SafePaint());
		
		
		if(mMapView == null)
			return;
		
		EGISRender.drawMapView(mMapView, c, osmv);
	}

}
