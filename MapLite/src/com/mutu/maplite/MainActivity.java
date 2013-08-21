package com.mutu.maplite;




import java.util.ArrayList;

import com.mutu.mapapi.api.IGeoPoint;
import com.mutu.mapapi.search.PoiInfo;
import com.mutu.mapapi.search.PoiSearch;
import com.mutu.mapapi.tileprovider.MapTileProviderBasic;
import com.mutu.mapapi.tileprovider.tilesource.ITileSource;
import com.mutu.mapapi.tileprovider.tilesource.TileSourceFactory;
import com.mutu.mapapi.tileprovider.util.SimpleInvalidationHandler;
import com.mutu.mapapi.tilesystem.MercatorTileSystem;
import com.mutu.mapapi.tilesystem.TileSystem;
import com.mutu.mapapi.tilesystem.TileSystemFactory;
import com.mutu.mapapi.util.GeoPoint;
import com.mutu.mapapi.views.MapView;
import com.mutu.mapapi.views.overlay.DirectedLocationOverlay;
import com.mutu.mapapi.views.overlay.ItemizedIconOverlay;
import com.mutu.mapapi.views.overlay.MinimapOverlay;
import com.mutu.mapapi.views.overlay.OverlayItem;
import com.mutu.mapapi.views.overlay.PoiOverlay;
import com.mutu.mapapi.views.overlay.ScaleBarOverlay;
import com.mutu.mapapi.views.overlay.SimpleLocationOverlay;
import com.mutu.mapapi.views.overlay.TilesOverlay;
import com.mutu.mapapi.views.overlay.compass.CompassOverlay;
import com.mutu.mapapi.views.overlay.compass.InternalCompassOrientationProvider;
import com.mutu.mapapi.views.overlay.mylocation.GpsMyLocationProvider;
import com.mutu.mapapi.views.overlay.mylocation.MyLocationNewOverlay;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	MapView mapview = null;
    private static final int MENU_LAST_ID = Menu.FIRST + 1; // Always set to last unused id

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mapview = (MapView)findViewById(R.id.mapview);
		mapview.setMultiTouchControls(true);
		mapview.setBuiltInZoomControls(true);
		
		if(mapview.getTileProvider().getTileSource().equals(TileSourceFactory.getTileSource("tianditu_vec"))){
			final ITileSource tileSource = TileSourceFactory.TIANDITU_CVA;
			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
			mapview.getOverlayManager().add(mapOverlay);
			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
					mapview));
		}
		{
			final ITileSource tileSource = TileSourceFactory.YUNNAN_BASICMAP;
			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
			mapview.getOverlayManager().add(mapOverlay);
			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
					mapview));
		}
		{
			final ITileSource tileSource = TileSourceFactory.YUNNAN_BASICLABEL;
			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
			mapview.getOverlayManager().add(mapOverlay);
			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
					mapview));
		}
		
//		SimpleLocationOverlay simpleLocationOverlay = new SimpleLocationOverlay(this);
//		simpleLocationOverlay.setLocation(new GeoPoint(0,0));
//		mapview.getOverlayManager().add(simpleLocationOverlay);
		
		MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(this, new GpsMyLocationProvider(this),
                mapview);
		mapview.getOverlayManager().add(locationOverlay);
		
		CompassOverlay compassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this),
				mapview);
		
		mapview.getOverlayManager().add(compassOverlay);
		
        locationOverlay.enableMyLocation(new GpsMyLocationProvider(this));
        compassOverlay.enableCompass(new InternalCompassOrientationProvider(this));
        
		/* MiniMap */
		{
			final ITileSource tileSource = mapview.getTileProvider().getTileSource();
			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
			MinimapOverlay miniMapOverlay = new MinimapOverlay(this,
					mapview.getTileRequestCompleteHandler(),tileProvider);
			this.mapview.getOverlays().add(miniMapOverlay);
		}
		
        mapview.getController().setZoom(6);
        mapview.getController().setCenter(new GeoPoint(25.041667, 102.705));
        
        
//        PoiSearch poiSearch = new PoiSearch();
//        ArrayList<PoiInfo> poi = poiSearch.poiSearchInCity("");
//        
//        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>(); 
        
 //       PoiOverlay poiOverlay = new PoiOverlay(){};
//        Drawable drawable1 = this.getResources().getDrawable(R.drawable.ic_launcher);  
//        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>(); 
//        OverlayItem oitem1 = new OverlayItem("item1", "item1 info", new GeoPoint(25.041667, 102.705));  
//       // oitem1.setMarker(drawable1);//设置overlayitem图片  
//        items.add(oitem1);  
//        
//      //自定义图层的使用  
//        ItemizedIconOverlay mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(this,items,  
//                //重载点击overlayitem的反应   
//                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>(){   
//                        @Override  
//                        public boolean onItemSingleTapUp(final int index, final OverlayItem item)   
//                        {  
//                            return true;  
//                        }  
//          
//                        @Override  
//                        public boolean onItemLongPress(final int index,final OverlayItem item) {  
//                            return true;  
//                        }  
//                });  
//       //添加相关的图层  
//        mapview.getOverlays().add(mMyLocationOverlay);  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		
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
