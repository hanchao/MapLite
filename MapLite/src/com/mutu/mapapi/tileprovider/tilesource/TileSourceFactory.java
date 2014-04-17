package com.mutu.mapapi.tileprovider.tilesource;

import java.util.ArrayList;

import com.mutu.mapapi.ResourceProxy;
import com.mutu.mapapi.tilesystem.TileSystemFactory;

public class TileSourceFactory {

	// private static final Logger logger = LoggerFactory.getLogger(TileSourceFactory.class);

	/**
	 * Get the tile source with the specified name.
	 *
	 * @param aName
	 *            the tile source name
	 * @return the tile source
	 * @throws IllegalArgumentException
	 *             if tile source not found
	 */
	public static ITileSource getTileSource(final String aName) throws IllegalArgumentException {
		for (final ITileSource tileSource : mTileSources) {
			if (tileSource.name().equals(aName)) {
				return tileSource;
			}
		}
		throw new IllegalArgumentException("No such tile source: " + aName);
	}

	public static boolean containsTileSource(final String aName) {
		for (final ITileSource tileSource : mTileSources) {
			if (tileSource.name().equals(aName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the tile source at the specified position.
	 *
	 * @param aOrdinal
	 * @return the tile source
	 * @throws IllegalArgumentException
	 *             if tile source not found
	 */
	public static ITileSource getTileSource(final int aOrdinal) throws IllegalArgumentException {
		for (final ITileSource tileSource : mTileSources) {
			if (tileSource.ordinal() == aOrdinal) {
				return tileSource;
			}
		}
		throw new IllegalArgumentException("No tile source at position: " + aOrdinal);
	}

	public static ArrayList<ITileSource> getTileSources() {
		return mTileSources;
	}

	public static void addTileSource(final ITileSource mTileSource) {
		mTileSources.add(mTileSource);
	}

	public static final OnlineTileSourceBase MAPNIK = new XYTileSource("Mapnik",
			ResourceProxy.string.mapnik, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), ".png", "http://tile.openstreetmap.org/");

	public static final OnlineTileSourceBase CYCLEMAP = new XYTileSource("CycleMap",
			ResourceProxy.string.cyclemap, 0, 17, 256, TileSystemFactory.getTileSystem("Mercator"), ".png",
			"http://a.tile.opencyclemap.org/cycle/",
			"http://b.tile.opencyclemap.org/cycle/",
			"http://c.tile.opencyclemap.org/cycle/");

	public static final OnlineTileSourceBase PUBLIC_TRANSPORT = new XYTileSource(
			"OSMPublicTransport", ResourceProxy.string.public_transport, 0, 17, 256, TileSystemFactory.getTileSystem("Mercator"), ".png",
			"http://tile.xn--pnvkarte-m4a.de/tilegen/");

	public static final OnlineTileSourceBase BASE = new XYTileSource("Base",
			ResourceProxy.string.base, 4, 17, 256, TileSystemFactory.getTileSystem("Mercator"), ".png", "http://topo.openstreetmap.de/base/");

	public static final OnlineTileSourceBase TOPO = new XYTileSource("Topo",
			ResourceProxy.string.topo, 4, 17, 256, TileSystemFactory.getTileSystem("Mercator"), ".png", "http://topo.openstreetmap.de/topo/");

	public static final OnlineTileSourceBase HILLS = new XYTileSource("Hills",
			ResourceProxy.string.hills, 8, 17, 256, TileSystemFactory.getTileSystem("Mercator"), ".png", "http://topo.geofabrik.de/hills/");

	public static final OnlineTileSourceBase CLOUDMADESTANDARDTILES = new CloudmadeTileSource(
			"CloudMadeStandardTiles", ResourceProxy.string.cloudmade_standard, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), ".png",
			"http://a.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s",
			"http://b.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s",
			"http://c.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s");

	// FYI - This tile source has a tileSize of "6"
	public static final OnlineTileSourceBase CLOUDMADESMALLTILES = new CloudmadeTileSource(
			"CloudMadeSmallTiles", ResourceProxy.string.cloudmade_small, 0, 21, 64, TileSystemFactory.getTileSystem("Mercator"), ".png",
			"http://a.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s",
			"http://b.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s",
			"http://c.tile.cloudmade.com/%s/%d/%d/%d/%d/%d%s?token=%s");

	public static final OnlineTileSourceBase MAPQUESTOSM =
		new XYTileSource("MapquestOSM", ResourceProxy.string.mapquest_osm, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), ".png",
				"http://otile1.mqcdn.com/tiles/1.0.0/map/",
				"http://otile2.mqcdn.com/tiles/1.0.0/map/",
				"http://otile3.mqcdn.com/tiles/1.0.0/map/",
				"http://otile4.mqcdn.com/tiles/1.0.0/map/");

	public static final OnlineTileSourceBase MAPQUESTAERIAL =
		new XYTileSource("MapquestAerial", ResourceProxy.string.mapquest_aerial, 0, 11, 256, TileSystemFactory.getTileSystem("Mercator"), ".png",
				"http://otile1.mqcdn.com/tiles/1.0.0/sat/",
				"http://otile2.mqcdn.com/tiles/1.0.0/sat/",
				"http://otile3.mqcdn.com/tiles/1.0.0/sat/",
				"http://otile4.mqcdn.com/tiles/1.0.0/sat/");

	public static final OnlineTileSourceBase DEFAULT_TILE_SOURCE = MAPNIK;

	// The following tile sources are overlays, not standalone map views.
	// They are therefore not in mTileSources.

	public static final OnlineTileSourceBase FIETS_OVERLAY_NL = new XYTileSource("Fiets",
			ResourceProxy.string.fiets_nl, 3, 18, 256, TileSystemFactory.getTileSystem("Mercator"), ".png",
			"http://overlay.openstreetmap.nl/openfietskaart-overlay/");

	public static final OnlineTileSourceBase BASE_OVERLAY_NL = new XYTileSource("BaseNL",
			ResourceProxy.string.base_nl, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), ".png",
			"http://overlay.openstreetmap.nl/basemap/");

	public static final OnlineTileSourceBase ROADS_OVERLAY_NL = new XYTileSource("RoadsNL",
			ResourceProxy.string.roads_nl, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), ".png",
			"http://overlay.openstreetmap.nl/roads/");

	public static final OnlineTileSourceBase MAPBOX = new XYTileSource("mapbox",
			ResourceProxy.string.mapbox, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), ".png", 
			"http://a.tiles.mapbox.com/v3/tmcw.map-7s15q36b/",
			"http://b.tiles.mapbox.com/v3/tmcw.map-7s15q36b/",
			"http://c.tiles.mapbox.com/v3/tmcw.map-7s15q36b/",
			"http://d.tiles.mapbox.com/v3/tmcw.map-7s15q36b/");

	public static final OnlineTileSourceBase ARCGIS_WORLD_STREET_MAP = new ArcGISOnlineTileSource("arcgis_world_street_map",
			ResourceProxy.string.arcgis_world_street_map, 0, 20, 256, TileSystemFactory.getTileSystem("Mercator"), "", "http://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/");
	
	public static final OnlineTileSourceBase ARCGIS_WORLD_IMAGERY = new ArcGISOnlineTileSource("arcgis_world_magery",
			ResourceProxy.string.arcgis_world_magery, 0, 20, 256, TileSystemFactory.getTileSystem("Mercator"), "", "http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/");
	
	public static final OnlineTileSourceBase ARCGIS_CHINA_MAP = new ArcGISOnlineTileSource("arcgis_china_map",
			ResourceProxy.string.arcgis_china_map, 0, 20, 256, TileSystemFactory.getTileSystem("Mercator"), "", "http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/");
	
	public static final OnlineTileSourceBase GOOGLE = new GoogleTileSource("Google",
			ResourceProxy.string.google, 0, 22, 256, TileSystemFactory.getTileSystem("Mercator"), "", 
			"http://mt0.google.cn/vt/lyrs=m@167000000&hl=zh-CN&gl=cn&",
			"http://mt1.google.cn/vt/lyrs=m@167000000&hl=zh-CN&gl=cn&",
			"http://mt2.google.cn/vt/lyrs=m@167000000&hl=zh-CN&gl=cn&",
			"http://mt3.google.cn/vt/lyrs=m@167000000&hl=zh-CN&gl=cn&");
	
	public static final OnlineTileSourceBase AMAP = new AMapTileSource("AMap",
			ResourceProxy.string.amap, 1, 18, 256, TileSystemFactory.getTileSystem("Mercator"), "", 
			"http://webrd01.is.autonavi.com/appmaptile",
			"http://webrd02.is.autonavi.com/appmaptile",
			"http://webrd03.is.autonavi.com/appmaptile",
			"http://webrd04.is.autonavi.com/appmaptile");
		
	public static final OnlineTileSourceBase SOSO = new SosoTileSource("soso",
			ResourceProxy.string.soso, 1, 18, 256, TileSystemFactory.getTileSystem("Mercator"), ".png", 
			"http://p0.map.soso.com/maptilesv2/",
			"http://p1.map.soso.com/maptilesv2/",
			"http://p2.map.soso.com/maptilesv2/",
			"http://p3.map.soso.com/maptilesv2/");
	
	public static final OnlineTileSourceBase BAIDU = new BaiduTileSource("Baidu",
			ResourceProxy.string.baidu, 0, 20, 256, TileSystemFactory.getTileSystem("Mercator"), "", 
			"http://shangetu0.map.bdimg.com/it/",
			"http://shangetu1.map.bdimg.com/it/",
			"http://shangetu2.map.bdimg.com/it/",
			"http://shangetu3.map.bdimg.com/it/",
			"http://shangetu4.map.bdimg.com/it/",
			"http://shangetu5.map.bdimg.com/it/",
			"http://shangetu6.map.bdimg.com/it/",
			"http://shangetu7.map.bdimg.com/it/",
			"http://shangetu8.map.bdimg.com/it/",
			"http://shangetu9.map.bdimg.com/it/");
	
	public static final OnlineTileSourceBase SUPERMAPCLOUD = new SuperMapCloudTileSource("supermapcloud",
			ResourceProxy.string.supermapcloud, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), "", 
			"http://t0.supermapcloud.com/FileService/image");
	
	public static final OnlineTileSourceBase TIANDITU_VEC = new TiandituTileSource("tianditu_vec",
			ResourceProxy.string.tianditu_vec, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", "vec_c", 
			"http://t0.tianditu.com/DataServer",
			"http://t1.tianditu.com/DataServer",
			"http://t2.tianditu.com/DataServer",
			"http://t3.tianditu.com/DataServer",
			"http://t4.tianditu.com/DataServer",
			"http://t5.tianditu.com/DataServer",
			"http://t6.tianditu.com/DataServer",
			"http://t7.tianditu.com/DataServer");
	
	public static final OnlineTileSourceBase TIANDITU_CVA = new TiandituTileSource("tianditu_cva",
			ResourceProxy.string.tianditu_cva, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", "cva_c",
			"http://t0.tianditu.com/DataServer",
			"http://t1.tianditu.com/DataServer",
			"http://t2.tianditu.com/DataServer",
			"http://t3.tianditu.com/DataServer",
			"http://t4.tianditu.com/DataServer",
			"http://t5.tianditu.com/DataServer",
			"http://t6.tianditu.com/DataServer",
			"http://t7.tianditu.com/DataServer");
	
	public static final OnlineTileSourceBase TIANDITU_IMG = new TiandituTileSource("tianditu_img",
			ResourceProxy.string.tianditu_img, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", "img_c", 
			"http://t0.tianditu.com/DataServer",
			"http://t1.tianditu.com/DataServer",
			"http://t2.tianditu.com/DataServer",
			"http://t3.tianditu.com/DataServer",
			"http://t4.tianditu.com/DataServer",
			"http://t5.tianditu.com/DataServer",
			"http://t6.tianditu.com/DataServer",
			"http://t7.tianditu.com/DataServer");
	
	public static final OnlineTileSourceBase TIANDITU_CIA = new TiandituTileSource("tianditu_cia",
			ResourceProxy.string.tianditu_cia, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", "cia_c", 
			"http://t0.tianditu.com/DataServer",
			"http://t1.tianditu.com/DataServer",
			"http://t2.tianditu.com/DataServer",
			"http://t3.tianditu.com/DataServer",
			"http://t4.tianditu.com/DataServer",
			"http://t5.tianditu.com/DataServer",
			"http://t6.tianditu.com/DataServer",
			"http://t7.tianditu.com/DataServer");
	
	public static final OnlineTileSourceBase TIANDITU_VEC_W = new TiandituTileSource("tianditu_vec_w",
			ResourceProxy.string.tianditu_vec, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), "", "vec_w", 
			"http://t0.tianditu.com/DataServer",
			"http://t1.tianditu.com/DataServer",
			"http://t2.tianditu.com/DataServer",
			"http://t3.tianditu.com/DataServer",
			"http://t4.tianditu.com/DataServer",
			"http://t5.tianditu.com/DataServer",
			"http://t6.tianditu.com/DataServer",
			"http://t7.tianditu.com/DataServer");
	
	public static final OnlineTileSourceBase TIANDITU_CVA_W = new TiandituTileSource("tianditu_cva_w",
			ResourceProxy.string.tianditu_cva, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), "", "cva_w",
			"http://t0.tianditu.com/DataServer",
			"http://t1.tianditu.com/DataServer",
			"http://t2.tianditu.com/DataServer",
			"http://t3.tianditu.com/DataServer",
			"http://t4.tianditu.com/DataServer",
			"http://t5.tianditu.com/DataServer",
			"http://t6.tianditu.com/DataServer",
			"http://t7.tianditu.com/DataServer");
	
	public static final OnlineTileSourceBase TIANDITU_IMG_W = new TiandituTileSource("tianditu_img_w",
			ResourceProxy.string.tianditu_img, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), "", "img_w", 
			"http://t0.tianditu.com/DataServer",
			"http://t1.tianditu.com/DataServer",
			"http://t2.tianditu.com/DataServer",
			"http://t3.tianditu.com/DataServer",
			"http://t4.tianditu.com/DataServer",
			"http://t5.tianditu.com/DataServer",
			"http://t6.tianditu.com/DataServer",
			"http://t7.tianditu.com/DataServer");
	
	public static final OnlineTileSourceBase TIANDITU_CIA_W = new TiandituTileSource("tianditu_cia_w",
			ResourceProxy.string.tianditu_cia, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), "", "cia_w", 
			"http://t0.tianditu.com/DataServer",
			"http://t1.tianditu.com/DataServer",
			"http://t2.tianditu.com/DataServer",
			"http://t3.tianditu.com/DataServer",
			"http://t4.tianditu.com/DataServer",
			"http://t5.tianditu.com/DataServer",
			"http://t6.tianditu.com/DataServer",
			"http://t7.tianditu.com/DataServer");
	
	public static final OnlineTileSourceBase YUNNAN_BASICMAP = new ArcGISOnlineTileSource("yunnan_basicmap",
			ResourceProxy.string.yunnan_basicmap, 6, 16, 256, TileSystemFactory.getTileSystem("WGS"), "", "http://ditu.ynmap.org.cn/RemoteRest/services/basicmap/MapServer/tile/");
	
	public static final OnlineTileSourceBase YUNNAN_BASICLABEL = new ArcGISOnlineTileSource("yunnan_basiclabel",
			ResourceProxy.string.yunnan_basiclabel, 6, 16, 256, TileSystemFactory.getTileSystem("WGS"), "", "http://ditu.ynmap.org.cn/RemoteRest/services/basiclabel/MapServer/tile/");
	
	public static final OnlineTileSourceBase YUNNAN_YNYXMAP = new ArcGISOnlineTileSource("yunnan_ynyxmap",
			ResourceProxy.string.yunnan_ynyxmap, 6, 16, 256, TileSystemFactory.getTileSystem("WGS"), "", "http://ditu.ynmap.org.cn/RemoteRest/services/YNYXmap/MapServer/tile/");
	
	public static final OnlineTileSourceBase YUNNAN_IMAGEVECTOR = new ArcGISOnlineTileSource("yunnan_imagevector",
			ResourceProxy.string.yunnan_imagevector, 6, 16, 256, TileSystemFactory.getTileSystem("WGS"), "", "http://ditu.ynmap.org.cn/RemoteRest/services/imagevector/MapServer/tile/");
	
	public static final OnlineTileSourceBase YUNNAN_IMAGELABEL = new ArcGISOnlineTileSource("yunnan_imagelabel",
			ResourceProxy.string.yunnan_imagelabel, 6, 16, 256, TileSystemFactory.getTileSystem("WGS"), "", "http://ditu.ynmap.org.cn/RemoteRest/services/imagelabel/MapServer/tile/");
	
	public static final CompositeTileSource TIANDITU_VECTOR = new CompositeTileSource("tianditu_vector",
			ResourceProxy.string.tianditu_vector, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", 
			TIANDITU_VEC,TIANDITU_CVA);
	
	public static final CompositeTileSource TIANDITU_IMAGE = new CompositeTileSource("tianditu_image",
			ResourceProxy.string.tianditu_image, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", 
			TIANDITU_IMG,TIANDITU_CIA );
	
	public static final CompositeTileSource TIANDITU_VECTOR_W = new CompositeTileSource("tianditu_vector_w",
			ResourceProxy.string.tianditu_vector, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), "", 
			TIANDITU_VEC_W,TIANDITU_CVA_W);
	
	public static final CompositeTileSource TIANDITU_IMAGE_W = new CompositeTileSource("tianditu_image_w",
			ResourceProxy.string.tianditu_image, 0, 18, 256, TileSystemFactory.getTileSystem("Mercator"), "", 
			TIANDITU_IMG_W,TIANDITU_CIA_W );
	
	public static final CompositeTileSource YUNNAN_BASIC = new CompositeTileSource("yunnan_basic",
			ResourceProxy.string.yunnan_basic, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", 
			YUNNAN_BASICMAP,YUNNAN_BASICLABEL);
	
	public static final CompositeTileSource YUNNAN_IMAGE = new CompositeTileSource("yunnan_image",
			ResourceProxy.string.yunnan_image, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", 
			YUNNAN_YNYXMAP,YUNNAN_IMAGEVECTOR,YUNNAN_IMAGELABEL );
	
	public static final CompositeTileSource TIANDITU_YUNNAN_VECTOR = new CompositeTileSource("tianditu_yunnan_vector",
			ResourceProxy.string.tianditu_yunnan_vector, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", 
			TIANDITU_VEC,TIANDITU_CVA,YUNNAN_BASICMAP,YUNNAN_BASICLABEL);
	
	public static final CompositeTileSource TIANDITU_YUNNAN_IMAGE = new CompositeTileSource("tianditu_yunnan_image",
			ResourceProxy.string.tianditu_yunnan_image, 0, 17, 256, TileSystemFactory.getTileSystem("WGS"), "", 
			TIANDITU_IMG,TIANDITU_CIA,YUNNAN_YNYXMAP,YUNNAN_IMAGEVECTOR,YUNNAN_IMAGELABEL);
	
	private static ArrayList<ITileSource> mTileSources;
	static {
		mTileSources = new ArrayList<ITileSource>();
		mTileSources.add(MAPNIK);
		mTileSources.add(CYCLEMAP);
//		mTileSources.add(PUBLIC_TRANSPORT);
//		mTileSources.add(BASE);
//		mTileSources.add(TOPO);
//		mTileSources.add(HILLS);
//		mTileSources.add(CLOUDMADESTANDARDTILES);
//		mTileSources.add(CLOUDMADESMALLTILES);
		mTileSources.add(MAPQUESTOSM);
		mTileSources.add(MAPQUESTAERIAL);
		mTileSources.add(MAPBOX);
		mTileSources.add(ARCGIS_WORLD_STREET_MAP);
		mTileSources.add(ARCGIS_WORLD_IMAGERY);
		mTileSources.add(ARCGIS_CHINA_MAP);
		mTileSources.add(GOOGLE);
		mTileSources.add(AMAP);
		mTileSources.add(SOSO);
		mTileSources.add(BAIDU);
		mTileSources.add(SUPERMAPCLOUD);
		mTileSources.add(TIANDITU_VEC);
		mTileSources.add(TIANDITU_CVA);
		mTileSources.add(TIANDITU_IMG);
		mTileSources.add(TIANDITU_CIA);
		mTileSources.add(TIANDITU_VEC_W);
		mTileSources.add(TIANDITU_CVA_W);
		mTileSources.add(TIANDITU_IMG_W);
		mTileSources.add(TIANDITU_CIA_W);
		mTileSources.add(YUNNAN_BASICMAP);
		mTileSources.add(YUNNAN_BASICLABEL);
		mTileSources.add(YUNNAN_YNYXMAP);
		mTileSources.add(YUNNAN_IMAGEVECTOR);
		mTileSources.add(YUNNAN_IMAGELABEL);
		mTileSources.add(TIANDITU_VECTOR);
		mTileSources.add(TIANDITU_IMAGE);
		mTileSources.add(TIANDITU_VECTOR_W);
		mTileSources.add(TIANDITU_IMAGE_W);
		mTileSources.add(YUNNAN_BASIC);
		mTileSources.add(YUNNAN_IMAGE);		
		mTileSources.add(TIANDITU_YUNNAN_VECTOR);
		mTileSources.add(TIANDITU_YUNNAN_IMAGE);
	}
}
