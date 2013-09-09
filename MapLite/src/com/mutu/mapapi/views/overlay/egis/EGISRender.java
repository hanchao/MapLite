package com.mutu.mapapi.views.overlay.egis;

import com.android.gis.Dataset;
import com.android.gis.DatasetRaster;
import com.android.gis.DatasetVector;
import com.android.gis.GeoPoint;
import com.android.gis.GeoLine;
import com.android.gis.GeoRegion;
import com.android.gis.Geometry;
import com.android.gis.Layer;
import com.android.gis.Point2D;
import com.android.gis.Recordset;
import com.android.gis.Rect2D;
import com.android.gis.Style;


import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tileprovider.util.SimpleInvalidationHandler;
import com.mutu.mapapi.tilesystem.TileSystem;
import com.mutu.mapapi.util.BoundingBoxE6;
import com.mutu.mapapi.views.MapView;
import com.mutu.mapapi.views.MapView.Projection;
import com.mutu.mapapi.views.safecanvas.ISafeCanvas;
import com.mutu.mapapi.views.safecanvas.SafePaint;
import com.mutu.mapapi.views.safecanvas.SafeTranslatedPath;
import com.mutu.mapapi.views.util.constants.MapViewConstants;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;

public class EGISRender {
	
	TileSystem mTileSystem;
	int mZoomlevel = 0;
	Rect mScreenRect = new Rect();
	Rect2D mViewBounds = new Rect2D();
	
	Handler mInvalidationHandler = null;
	
	boolean mIsStop = false;
	
	
	public void init(MapView osmv){
		mZoomlevel = osmv.getProjection().getZoomLevel();
		mTileSystem = osmv.getTileProvider().getTileSource().getTileSystem();
		osmv.getScreenRect(mScreenRect);
		final int worldWidthSize_2 = mTileSystem.MapWidthPixelSize(mZoomlevel) / 2;
		final int worldHeigthSize_2 = mTileSystem.MapHeigthPixelSize(mZoomlevel) / 2;
		
		mScreenRect.offset(worldWidthSize_2, worldHeigthSize_2);
				
		BoundingBoxE6 boundingBox = osmv.getBoundingBox();
		mViewBounds.left = boundingBox.getLonWestE6()/1E6;
		mViewBounds.top = boundingBox.getLatNorthE6()/1E6;
		mViewBounds.right = boundingBox.getLonEastE6()/1E6;
		mViewBounds.bottom = boundingBox.getLatSouthE6()/1E6;
		
		mInvalidationHandler = new SimpleInvalidationHandler(osmv);
	}
	
	public boolean isExpired(MapView osmv){
		if(mZoomlevel != osmv.getProjection().getZoomLevel()){
			return true;
		}
		
		Rect screenRect = new Rect();
		osmv.getScreenRect(screenRect);
		final int worldWidthSize_2 = mTileSystem.MapWidthPixelSize(mZoomlevel) / 2;
		final int worldHeigthSize_2 = mTileSystem.MapHeigthPixelSize(mZoomlevel) / 2;
		
		screenRect.offset(worldWidthSize_2, worldHeigthSize_2);
		
		if(!mScreenRect.equals(screenRect)){
			return true;
		}
		return false;
	}
	
	public void drawMapView(com.android.gis.MapView mapview,Canvas c){
		
		//backColor
		c.drawColor(Color.TRANSPARENT);
		
		int layerCount = mapview.GetLayerCount();
		for(int layerIndex = layerCount-1;layerIndex>=0; layerIndex--){
			Layer layer = mapview.GetLayerAt(layerIndex);
			if(layer == null){
				continue;
			}
			drawLayer(layer,c);
			if(isStop()){
				break;
			}
		}
		setStop(false);
	}
	
	
	public void drawLayer(Layer layer,Canvas c){
		if(!layer.IsVisible()){
			return;
		}
		
		Dataset dataset = layer.GetDataset();
		if(dataset == null){
			return;
		}
		
		
		
		Style style = layer.GetStyle();
		SafePaint paint = new SafePaint();
		paint.setColor(style.lineColor);
		
		drawDataset(dataset,c,paint);
	}
	
	public void drawDataset(Dataset dataset,Canvas c,Paint paint){
		if(dataset.IsRaster()){
			drawDatasetRaster((DatasetRaster)dataset,c);
		}else{
			drawDatasetVector((DatasetVector)dataset,c,paint);
		}
	}
	
	public void drawDatasetRaster(DatasetRaster datasetRaster,Canvas c){
		
	}
	
	public void drawDatasetVector(DatasetVector datasetVector,Canvas c,Paint paint){
//		BoundingBoxE6 boundingBox = osmv.getBoundingBox();
//		Rect2D viewBounds = new Rect2D();
//		viewBounds.left = boundingBox.getLonWestE6()/1E6;
//		viewBounds.top = boundingBox.getLatNorthE6()/1E6;
//		viewBounds.right = boundingBox.getLonEastE6()/1E6;
//		viewBounds.bottom = boundingBox.getLatSouthE6()/1E6;
		Recordset recordset = datasetVector.QueryByBounds(mViewBounds);
		if(recordset == null){
			return;
		}
		recordset.MoveFirst();
		while(!recordset.IsEOF()){
			Geometry geometry = recordset.GetGeometry();
			if(geometry == null){
				recordset.MoveNext();
				continue;
			}
			drawGeometry(geometry,c,paint);
			geometry.Delete();
			recordset.MoveNext();
			if(isStop()){
				break;
			}
		}
		invalidate();
	}
	
	public void drawGeometry(Geometry geometry,Canvas c,Paint paint){
		int GeometryType = geometry.GetType();
		switch (GeometryType)
		{
			case Geometry.GEOPOINT:
				{
					drawGeoPoint((GeoPoint)geometry,c,paint);
				}
				break;
			case Geometry.GEOLINE:
				{
					drawGeoLine((GeoLine)geometry,c,paint);
				}
				break;
			case Geometry.GEOREGION:
				{
					drawGeoRegion((GeoRegion)geometry,c,paint);
				}
				break;
			default:
				break;
		}
	}
	
	public void drawGeoPoint(GeoPoint geoPoint,Canvas c,Paint paint){
		Point2D pntMap = geoPoint.GetPoint();
		
		Point pntDevice = MapToDevicePoint(pntMap);
		
		c.drawCircle(pntDevice.x,pntDevice.y, 5, paint);
	}
	
	public void drawGeoLine(GeoLine geoLine,Canvas c,Paint paint){
		Point2D[] pntMaps = geoLine.GetPoints(0);
		
		SafeTranslatedPath path = new SafeTranslatedPath();
		//path.onDrawCycleStart(c);
		int pointCount = pntMaps.length;
		for(int pointIndex = 0;pointIndex<pointCount; pointIndex++){
			Point pntDevice = MapToDevicePoint(pntMaps[pointIndex]);
			if(pointIndex == 0){
				path.moveTo(pntDevice.x, pntDevice.y);
			}else{
				path.lineTo(pntDevice.x, pntDevice.y);
			}
		}
		
		paint.setStyle(Paint.Style.STROKE);  
		c.drawPath(path,paint);
	}
	
	public void drawGeoRegion(GeoRegion geoRegion,Canvas c,Paint paint){
	
		Point2D[] pntMaps = geoRegion.GetPoints(0);
		
		SafeTranslatedPath path = new SafeTranslatedPath();
		//path.onDrawCycleStart(c);
		int pointCount = pntMaps.length;
		for(int pointIndex = 0;pointIndex<pointCount; pointIndex++){
			Point pntDevice = MapToDevicePoint(pntMaps[pointIndex]);
			if(pointIndex == 0){
				path.moveTo(pntDevice.x, pntDevice.y);
			}else{
				path.lineTo(pntDevice.x, pntDevice.y);
			}
		}
		
		paint.setStyle(Paint.Style.FILL_AND_STROKE);  
		c.drawPath(path,paint);
		
	}
	
	public Point MapToDevicePoint(Point2D pntSource){

		Point mapCoords = mTileSystem.LatLongToPixelXY(pntSource.y,pntSource.x,
				mZoomlevel,null);
		
//		final int worldWidth_2 = mTileSystem.MapWidthPixelSize(mZoomlevel) / 2;
//		final int worldHeigth_2 = mTileSystem.MapHeigthPixelSize(mZoomlevel) / 2;
//		screenRect.offset(worldWidth_2, worldHeigth_2);
		
//		final int worldWidthSize_2 = tileSystem.MapWidthPixelSize(MapViewConstants.MAXIMUM_ZOOMLEVEL) / 2;
//		final int worldHeigthSize_2 = tileSystem.MapHeigthPixelSize(MapViewConstants.MAXIMUM_ZOOMLEVEL) / 2;
//		mapCoords.offset(-worldWidthSize_2, -worldHeigthSize_2);
//		
//		
//		final int zoomDiff = MapViewConstants.MAXIMUM_ZOOMLEVEL - pj.getZoomLevel();
		
		return new Point(mapCoords.x - mScreenRect.left, mapCoords.y - mScreenRect.top);
	}
	
//	public static Point MapToDevicePoint(Point2D pntSource,MapView osmv){
//		TileSystem tileSystem = osmv.getTileProvider().getTileSource().getTileSystem();
//		Point mapCoords = tileSystem.LatLongToPixelXY(pntSource.y,pntSource.x,
//				MapViewConstants.MAXIMUM_ZOOMLEVEL,null);
//		final int worldWidthSize_2 = tileSystem.MapWidthPixelSize(MapViewConstants.MAXIMUM_ZOOMLEVEL) / 2;
//		final int worldHeigthSize_2 = tileSystem.MapHeigthPixelSize(MapViewConstants.MAXIMUM_ZOOMLEVEL) / 2;
//		mapCoords.offset(-worldWidthSize_2, -worldHeigthSize_2);
//		
//		final Projection pj = osmv.getProjection();
//		final int zoomDiff = MapViewConstants.MAXIMUM_ZOOMLEVEL - pj.getZoomLevel();
//		
//		return new Point(mapCoords.x >> zoomDiff, mapCoords.y >> zoomDiff);
//	}
	
	void invalidate(){
		if (mInvalidationHandler != null) {
			mInvalidationHandler.sendEmptyMessage(MapTile.MAPTILE_SUCCESS_ID);
		}
	}
	
	public boolean isStop(){
		return mIsStop;
	}
	
	public void setStop(boolean aIsStop){
		mIsStop = aIsStop;
	}
}
