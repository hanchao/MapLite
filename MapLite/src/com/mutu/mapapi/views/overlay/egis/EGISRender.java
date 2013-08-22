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

public class EGISRender {
	
	
	public static void drawMapView(com.android.gis.MapView mapview,ISafeCanvas c, MapView osmv){
		
		//backColor
		//canvas.drawColor(Color.WHITE);
		
		int layerCount = mapview.GetLayerCount();
		for(int layerIndex = layerCount-1;layerIndex>=0; layerIndex--){
			Layer layer = mapview.GetLayerAt(layerIndex);
			if(layer == null){
				continue;
			}
			drawLayer(layer,c,osmv);
		}
	}
	
	
	public static void drawLayer(Layer layer,ISafeCanvas c, MapView osmv){
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
		
		drawDataset(dataset,c,osmv,paint);
	}
	
	public static void drawDataset(Dataset dataset,ISafeCanvas c, MapView osmv,SafePaint paint){
		if(dataset.IsRaster()){
			drawDatasetRaster((DatasetRaster)dataset,c,osmv);
		}else{
			drawDatasetVector((DatasetVector)dataset,c,osmv,paint);
		}
	}
	
	public static void drawDatasetRaster(DatasetRaster datasetRaster,ISafeCanvas c, MapView osmv){
		
	}
	
	public static void drawDatasetVector(DatasetVector datasetVector,ISafeCanvas c, MapView osmv,SafePaint paint){
		BoundingBoxE6 boundingBox = osmv.getBoundingBox();
		Rect2D viewBounds = new Rect2D();
		viewBounds.left = boundingBox.getLonWestE6()/1E6;
		viewBounds.top = boundingBox.getLatNorthE6()/1E6;
		viewBounds.right = boundingBox.getLonEastE6()/1E6;
		viewBounds.bottom = boundingBox.getLatSouthE6()/1E6;
		Recordset recordset = datasetVector.QueryByBounds(viewBounds);
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
			drawGeometry(geometry,c,osmv,paint);
			geometry.Delete();
			recordset.MoveNext();
		}
	}
	
	public static void drawGeometry(Geometry geometry,ISafeCanvas c, MapView osmv,SafePaint paint){
		int GeometryType = geometry.GetType();
		switch (GeometryType)
		{
			case Geometry.GEOPOINT:
				{
					drawGeoPoint((GeoPoint)geometry,c,osmv,paint);
				}
				break;
			case Geometry.GEOLINE:
				{
					drawGeoLine((GeoLine)geometry,c,osmv,paint);
				}
				break;
			case Geometry.GEOREGION:
				{
					drawGeoRegion((GeoRegion)geometry,c,osmv,paint);
				}
				break;
			default:
				break;
		}
	}
	
	public static void drawGeoPoint(GeoPoint geoPoint,ISafeCanvas c, MapView osmv,SafePaint paint){
		Point2D pntMap = geoPoint.GetPoint();
		
		Point pntDevice = MapToDevicePoint(pntMap,osmv);
		
		c.drawCircle(pntDevice.x,pntDevice.y, 5, paint);
	}
	
	public static void drawGeoLine(GeoLine geoLine,ISafeCanvas c, MapView osmv,SafePaint paint){
		Point2D[] pntMaps = geoLine.GetPoints(0);
		
		SafeTranslatedPath path = new SafeTranslatedPath();
		path.onDrawCycleStart(c);
		int pointCount = pntMaps.length;
		for(int pointIndex = 0;pointIndex<pointCount; pointIndex++){
			Point pntDevice = MapToDevicePoint(pntMaps[pointIndex],osmv);
			if(pointIndex == 0){
				path.moveTo(pntDevice.x, pntDevice.y);
			}else{
				path.lineTo(pntDevice.x, pntDevice.y);
			}
		}
		
		paint.setStyle(Paint.Style.STROKE);  
		c.drawPath(path,paint);
	}
	
	public static void drawGeoRegion(GeoRegion geoRegion,ISafeCanvas c, MapView osmv,SafePaint paint){
	
		Point2D[] pntMaps = geoRegion.GetPoints(0);
		
		SafeTranslatedPath path = new SafeTranslatedPath();
		path.onDrawCycleStart(c);
		int pointCount = pntMaps.length;
		for(int pointIndex = 0;pointIndex<pointCount; pointIndex++){
			Point pntDevice = MapToDevicePoint(pntMaps[pointIndex],osmv);
			if(pointIndex == 0){
				path.moveTo(pntDevice.x, pntDevice.y);
			}else{
				path.lineTo(pntDevice.x, pntDevice.y);
			}
		}
		
		paint.setStyle(Paint.Style.FILL_AND_STROKE);  
		c.drawPath(path,paint);
		
	}
	
	public static Point MapToDevicePoint(Point2D pntSource,MapView osmv){
		TileSystem tileSystem = osmv.getTileProvider().getTileSource().getTileSystem();
		Point mapCoords = tileSystem.LatLongToPixelXY(pntSource.y,pntSource.x,
				MapViewConstants.MAXIMUM_ZOOMLEVEL,null);
		final int worldWidthSize_2 = tileSystem.MapWidthPixelSize(MapViewConstants.MAXIMUM_ZOOMLEVEL) / 2;
		final int worldHeigthSize_2 = tileSystem.MapHeigthPixelSize(MapViewConstants.MAXIMUM_ZOOMLEVEL) / 2;
		mapCoords.offset(-worldWidthSize_2, -worldHeigthSize_2);
		
		final Projection pj = osmv.getProjection();
		final int zoomDiff = MapViewConstants.MAXIMUM_ZOOMLEVEL - pj.getZoomLevel();
		
		return new Point(mapCoords.x >> zoomDiff, mapCoords.y >> zoomDiff);
	}
}
