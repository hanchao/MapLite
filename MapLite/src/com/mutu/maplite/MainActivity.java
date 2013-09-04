package com.mutu.maplite;




import java.util.ArrayList;
import java.util.List;

import com.android.gis.Workspace;
import com.mutu.mapapi.api.IGeoPoint;
import com.mutu.mapapi.events.MapListener;
import com.mutu.mapapi.events.ScrollEvent;
import com.mutu.mapapi.events.ZoomEvent;
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
import com.mutu.mapapi.views.overlay.ScaleBarOverlay;
import com.mutu.mapapi.views.overlay.SimpleLocationOverlay;
import com.mutu.mapapi.views.overlay.TilesOverlay;
import com.mutu.mapapi.views.overlay.compass.CompassOverlay;
import com.mutu.mapapi.views.overlay.compass.InternalCompassOrientationProvider;
import com.mutu.mapapi.views.overlay.egis.EGISOverlay;
import com.mutu.mapapi.views.overlay.mylocation.GpsMyLocationProvider;
import com.mutu.mapapi.views.overlay.mylocation.MyLocationNewOverlay;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements MapListener{

	ListView mListView = null;
    String mStrDemos[] = {
			"MapActivity",
			"CompareActivity",
//			"PoiSearchDemo",
//			"RoutePlanDemo",
//			"Overlaytest",
//			"MyLocationDemo",
//			"GeoCoderDemo",
//			"OfflineDemo",
//			"BusLineSearchDemo",
//			"ReleaseEngine"
	};
    Class<?> mActivities[] = {
    		MapActivity.class,
    		CompareActivity.class,
//    		PoiSearch.class,
//    		RoutePlan.class,
//    		LocationOverlay.class,
//    		MyLocation.class,
//    		GeoCoder.class,
//    		OfflineDemo.class,
//    		BusLineSearch.class
    };
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView)findViewById(R.id.listView); 

		List<String> data = new ArrayList<String>();
		for (int i = 0; i < mStrDemos.length; i++) {
			data.add(mStrDemos[i]);
		}
        mListView.setAdapter((ListAdapter) new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data));
        mListView.setOnItemClickListener(new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {  
            	onListItemClick(index);
            }  
        });
        
//		mapview1 = (MapView)findViewById(R.id.mapview1);
//		mapview1.setMultiTouchControls(true);
//		mapview1.setBuiltInZoomControls(true);
//		
//		mapview2 = (MapView)findViewById(R.id.mapview2);
//		mapview2.setMultiTouchControls(true);
//		mapview2.setBuiltInZoomControls(true);
		
        //EGIS
//		{
//			Workspace mWorkspace = new Workspace();
//			boolean opened = mWorkspace.open("/sdcard/jingjin/jingjin.smwu"); 
//			if(opened && mWorkspace.getMapCount()!=0){
//				String mapname = mWorkspace.getMapNameAt(0);
//				com.android.gis.MapView mMapView = new com.android.gis.MapView(this);
//				mMapView.AttachWorkspace(mWorkspace);
//				if(mMapView.Open(mapname)){
//					EGISOverlay egisOverlay = new EGISOverlay(this,mMapView);
//					this.mapview.getOverlays().add(egisOverlay);
//				}
//			}
//		}
		
//		if(mapview.getTileProvider().getTileSource().equals(TileSourceFactory.getTileSource("tianditu_vec"))){
//			final ITileSource tileSource = TileSourceFactory.TIANDITU_CVA;
//			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//			mapview.getOverlayManager().add(mapOverlay);
//			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
//					mapview));
//		}
//		if(mapview.getTileProvider().getTileSource().equals(TileSourceFactory.getTileSource("tianditu_img"))){
//			final ITileSource tileSource = TileSourceFactory.TIANDITU_CIA;
//			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//			mapview.getOverlayManager().add(mapOverlay);
//			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
//					mapview));
//		}

//		{
//			final ITileSource tileSource = TileSourceFactory.YUNNAN_BASICMAP;
//			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//			mapview.getOverlayManager().add(mapOverlay);
//			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
//					mapview));
//		}
//		{
//			final ITileSource tileSource = TileSourceFactory.YUNNAN_BASICLABEL;
//			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//			mapview.getOverlayManager().add(mapOverlay);
//			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
//					mapview));
//		}
		
//		{
//			final ITileSource tileSource = TileSourceFactory.YUNNAN_YNYXMAP;
//			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//			mapview.getOverlayManager().add(mapOverlay);
//			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
//					mapview));
//		}
//		
//		{
//			final ITileSource tileSource = TileSourceFactory.YUNNAN_IMAGEVECTOR;
//			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//			mapview.getOverlayManager().add(mapOverlay);
//			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
//					mapview));
//		}
//		
//		{
//			final ITileSource tileSource = TileSourceFactory.YUNNAN_IMAGELABEL;
//			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//			TilesOverlay mapOverlay = new TilesOverlay(tileProvider, this);
//			mapview.getOverlayManager().add(mapOverlay);
//			tileProvider.setTileRequestCompleteHandler(new SimpleInvalidationHandler(
//					mapview));
//		}
		
//		SimpleLocationOverlay simpleLocationOverlay = new SimpleLocationOverlay(this);
//		simpleLocationOverlay.setLocation(new GeoPoint(25.041667, 102.705));
//		mapview.getOverlayManager().add(simpleLocationOverlay);
//		
//		MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(this, new GpsMyLocationProvider(this),
//                mapview);
//		mapview.getOverlayManager().add(locationOverlay);
//		
//		CompassOverlay compassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this),
//				mapview);
//		
//		mapview.getOverlayManager().add(compassOverlay);
//		
//        locationOverlay.enableMyLocation(new GpsMyLocationProvider(this));
//        compassOverlay.enableCompass(new InternalCompassOrientationProvider(this));
//        
//
//        {
//    		final DisplayMetrics dm = this.getResources().getDisplayMetrics();
//        	ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(this);
//        	scaleBarOverlay.setCentred(true);
//        	scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
//        	this.mapview.getOverlays().add(scaleBarOverlay);
//        }
//		
//		/* MiniMap */
//		{
//			final ITileSource tileSource = mapview.getTileProvider().getTileSource();
//			MapTileProviderBasic tileProvider = new MapTileProviderBasic(this, tileSource);
//			MinimapOverlay miniMapOverlay = new MinimapOverlay(this,
//					mapview.getTileRequestCompleteHandler(),tileProvider);
//			this.mapview.getOverlays().add(miniMapOverlay);
//		}
		

//        mapview1.getController().setZoom(6);
//        mapview1.getController().setCenter(new GeoPoint(25.041667, 102.705));
//        mapview2.getController().setZoom(6);
//        mapview2.getController().setCenter(new GeoPoint(25.041667, 102.705));
//        mapview1.setMapListener(this);
//        mapview2.setMapListener(this);
        
        
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
		//mapview.getOverlayManager().onCreateOptionsMenu(menu, MENU_LAST_ID, mapview);
        
		return true;
	}

    @Override
    public boolean onPrepareOptionsMenu(final Menu pMenu)
    {
    	//mapview.getOverlayManager().onPrepareOptionsMenu(pMenu, MENU_LAST_ID, mapview);
        return super.onPrepareOptionsMenu(pMenu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
//		if (mapview.getOverlayManager().onOptionsItemSelected(item, MENU_LAST_ID, mapview))
//			return true;
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onScroll(ScrollEvent event) {
		// TODO Auto-generated method stub
//		if(event.getSource() == mapview1){
//			mapview2.setMapListener(null);
//			mapview2.scrollTo(event.getX(), event.getY());
//			mapview2.setMapListener(this);
//		}else{
//			mapview1.setMapListener(null);
//			mapview1.scrollTo(event.getX(), event.getY());
//			mapview1.setMapListener(this);
//		}
		return true;
	}

	@Override
	public boolean onZoom(ZoomEvent event) {
		// TODO Auto-generated method stub
//		if(event.getSource() == mapview1){
//			mapview2.setMapListener(null);
//			mapview2.getController().setCenter(event.getSource().getMapCenter());
//			mapview2.getController().setZoom(event.getZoomLevel());
//			mapview2.setMapListener(this);
//		}else{
//			mapview1.setMapListener(null);
//			mapview1.getController().setCenter(event.getSource().getMapCenter());
//			mapview1.getController().setZoom(event.getZoomLevel());
//			mapview1.setMapListener(this);
//		}
		return true;
	}
	
	void onListItemClick(int index) {
    	if (index < 0 || index >= mActivities.length+1)
    		return;


		Intent intent = null;
		intent = new Intent(this, mActivities[index]);
		this.startActivity(intent);
    }
}
