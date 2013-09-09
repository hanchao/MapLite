package com.mutu.mapapi.views.overlay.egis;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;

import com.android.gis.Layer;
import com.android.gis.Workspace;
import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tileprovider.modules.MapTileDownloader;
import com.mutu.mapapi.tileprovider.tilesource.ITileSource;
import com.mutu.mapapi.tileprovider.util.SimpleInvalidationHandler;
import com.mutu.mapapi.tilesystem.TileSystem;
import com.mutu.mapapi.views.MapView;
import com.mutu.mapapi.views.MapView.Projection;
import com.mutu.mapapi.views.overlay.SafeDrawOverlay;
import com.mutu.mapapi.views.safecanvas.ISafeCanvas;
import com.mutu.mapapi.views.safecanvas.SafePaint;
import com.mutu.mapapi.views.util.constants.MapViewConstants;

public class EGISOverlay extends SafeDrawOverlay{

	private static final Logger logger = LoggerFactory.getLogger(MapTileDownloader.class);

	private final AtomicReference<Bitmap> mCacheBitmap = new AtomicReference<Bitmap>();
	
	com.android.gis.MapView mMapView = null;
	Layer mLayer = null;
	
	
	Bitmap mDrawBitmap = null;
	int mHeight = 0;
	int mWidth = 0;
			
	MapRender mMapRender = new MapRender();
	Thread mThread = null;
	Handler mRenderCompleteHandler;
	EGISRender mEGISRender = new EGISRender();
	
	
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
		
		
		if(mDrawBitmap == null || mEGISRender.isExpired(osmv)){
			stopRenderMap();
			renderMapAsync(osmv);
		}
		long iSTime = System.nanoTime();
		
		
//		Canvas canvas = new Canvas(drawBitmap);
//		//canvas.drawColor(Color.BLACK);
//		//EGISRender.drawMapView(mMapView, canvas, osmv);

		
		
		if(mDrawBitmap != null){
			c.getWrappedCanvas().drawBitmap(mDrawBitmap,0,0,null);
		}
		long iETime = System.nanoTime();
		logger.debug("EGISOverlay draw time :" + (iETime - iSTime)/1000000000);
	}

	void stopRenderMap(){
		try {
			if(mThread != null){
				mEGISRender.setStop(true);
				mThread.join();
				mEGISRender.setStop(false);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void renderMapAsync(MapView osmv){
		mWidth = osmv.getWidth();
		mHeight = osmv.getHeight();
		if(mDrawBitmap == null || mWidth != mDrawBitmap.getWidth() || mHeight != mDrawBitmap.getHeight()){
			mDrawBitmap = Bitmap.createBitmap(mWidth, mHeight,Config.ARGB_8888 );
		}else{
			mDrawBitmap.eraseColor(Color.argb(0,0,0,0));
		}
		
		mEGISRender.init(osmv);
		mEGISRender.setStop(false);
		mThread = new Thread(mMapRender);
		mThread.start();
	}
	class MapRender implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Canvas canvas = new Canvas(mDrawBitmap);
//			canvas.drawColor(Color.BLACK);
			mEGISRender.drawMapView(mMapView, canvas);

		}
		
	}
}
