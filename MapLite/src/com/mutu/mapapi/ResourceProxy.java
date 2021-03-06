package com.mutu.mapapi;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface ResourceProxy {

	public static enum string {

		// tile sources
		mapnik, cyclemap, public_transport, base, topo, hills, cloudmade_small, cloudmade_standard, mapquest_osm, mapquest_aerial, bing,

		// overlays
		fiets_nl, base_nl, roads_nl,

		// other stuff
		unknown, format_distance_meters, format_distance_kilometers, format_distance_miles, format_distance_nautical_miles, format_distance_feet, online_mode, offline_mode, my_location, compass, map_mode,

		//mapbox
		mapbox,
		
		// arcgis
		arcgis_world_street_map,arcgis_world_magery,arcgis_china_map,
		
		//google
		google,
		
		//AMAP
		amap,
		
		//soso
		soso,
		
		//baidu
		baidu,
		
		//supermapcloud
		supermapcloud,
		
		//tianditu
		tianditu_vec,tianditu_cva,tianditu_img,tianditu_cia,tianditu_vector,tianditu_image,
		
		//yunnan
		yunnan_basicmap,yunnan_basiclabel,yunnan_ynyxmap,yunnan_imagevector,yunnan_imagelabel,yunnan_basic,yunnan_image,
		
		//tianditu_yunnan
		tianditu_yunnan_vector,tianditu_yunnan_image,
	}

	public static enum bitmap {

		/**
		 * For testing - the image doesn't exist.
		 */
		unknown,

		center, direction_arrow, marker_default, marker_default_focused_base, navto_small, next, previous, person,

		/**
		 * Menu icons
		 */
		ic_menu_offline, ic_menu_mylocation, ic_menu_compass, ic_menu_mapmode
	}

	String getString(string pResId);

	/**
	 * Use a string resource as a format definition, and format using the supplied format arguments.
	 *
	 * @param pResId
	 * @param formatArgs
	 * @return
	 */
	String getString(string pResId, Object... formatArgs);

	Bitmap getBitmap(bitmap pResId);

	/**
	 * Get a bitmap as a {@link Drawable}
	 *
	 * @param pResId
	 * @return
	 */
	Drawable getDrawable(bitmap pResId);

	/**
	 * Gets the density from the current screen's DisplayMetrics
	 *
	 * @return the screen's density
	 */
	float getDisplayMetricsDensity();
}
