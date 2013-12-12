package com.mutu.mapapi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mutu.mapapi.views.util.constants.MapViewConstants;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Default implementation of {@link org.osmdroid.ResourceProxy} that returns fixed string to get
 * string resources and reads the jar package to get bitmap resources.
 */
public class DefaultResourceProxyImpl implements ResourceProxy, MapViewConstants {

	private static final Logger logger = LoggerFactory.getLogger(DefaultResourceProxyImpl.class);

	private Resources mResources;
	private DisplayMetrics mDisplayMetrics;

	/**
	 * Constructor.
	 *
	 * @param pContext
	 *            Used to get the display metrics that are used for scaling the bitmaps returned by
	 *            {@link #getBitmap} and {@link #getDrawable}.
	 *            Can be null, in which case the bitmaps are not scaled.
	 */
	public DefaultResourceProxyImpl(final Context pContext) {
		if (pContext != null) {
			mResources = pContext.getResources();
			mDisplayMetrics = mResources.getDisplayMetrics();
			if (DEBUGMODE) {
				logger.debug("mDisplayMetrics=" + mDisplayMetrics);
			}
		}
	}

	@Override
	public String getString(final string pResId) {
		switch (pResId) {
		case mapnik:
			return "Mapnik";
		case cyclemap:
			return "Cycle Map";
		case public_transport:
			return "Public transport";
		case base:
			return "OSM base layer";
		case topo:
			return "Topographic";
		case hills:
			return "Hills";
		case cloudmade_standard:
			return "CloudMade (Standard tiles)";
		case cloudmade_small:
			return "CloudMade (small tiles)";
		case mapquest_osm:
			return "Mapquest";
		case mapquest_aerial:
			return "Mapquest Aerial";
		case bing:
			return "Bing";
		case fiets_nl:
			return "OpenFietsKaart overlay";
		case base_nl:
			return "Netherlands base overlay";
		case roads_nl:
			return "Netherlands roads overlay";
		case unknown:
			return "Unknown";
		case format_distance_meters:
			return "%s m";
		case format_distance_kilometers:
			return "%s km";
		case format_distance_miles:
			return "%s mi";
		case format_distance_nautical_miles:
			return "%s nm";
		case format_distance_feet:
			return "%s ft";
		case online_mode:
			return "Online mode";
		case offline_mode:
			return "Offline mode";
		case my_location:
			return "My location";
		case compass:
			return "Compass";
		case map_mode:
			return "Map mode";
		case mapbox:
			return "mapbox";
		case arcgis_world_street_map:
			return "arcgis world street map";
		case arcgis_world_magery:
			return "arcgis world magery";
		case arcgis_china_map:
			return "arcgis china map";
		case google:
			return "google";
		case amap:
			return "AMap";
		case soso:
			return "soso";
		case baidu:
			return "baidu";
		case supermapcloud:
			return "supermapcloud";
		case tianditu_vec:
			return "tianditu_vec";
		case tianditu_cva:
			return "tianditu_cva";
		case tianditu_img:
			return "tianditu_img";
		case tianditu_cia:
			return "tianditu_cia";
		case yunnan_basicmap:
			return "yunnan_basicmap";
		case yunnan_basiclabel:
			return "yunnan_basiclabel";
		case yunnan_ynyxmap:
			return "yunnan_ynyxmap";
		case yunnan_imagevector:
			return "yunnan_imagevector";
		case yunnan_imagelabel:
			return "yunnan_imagelabel";
		case tianditu_vector:
			return "tianditu_vector";
		case tianditu_image:
			return "tianditu_image";
		case yunnan_basic:
			return "yunnan_basic";
		case yunnan_image:
			return "yunnan_image";
		case tianditu_yunnan_vector:
			return "tianditu_yunnan_vector";
		case tianditu_yunnan_image:
			return "tianditu_yunnan_image";
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String getString(final string pResId, final Object... formatArgs) {
		return String.format(getString(pResId), formatArgs);
	}

	@Override
	public Bitmap getBitmap(final bitmap pResId) {
		InputStream is = null;
		try {
			final String resName = pResId.name() + ".png";
			is = getClass().getResourceAsStream(resName);
			if (is == null) {
				throw new IllegalArgumentException("Resource not found: " + resName);
			}
			BitmapFactory.Options options = null;
			if (mDisplayMetrics != null) {
				options = getBitmapOptions();
			}
			return BitmapFactory.decodeStream(is, null, options);
		} catch (final OutOfMemoryError e) {
			logger.error("OutOfMemoryError getting bitmap resource: " + pResId);
			System.gc();
			// there's not much we can do here
			// - when we load a bitmap from resources we expect it to be found
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException ignore) {
				}
			}
		}
	}

	private BitmapFactory.Options getBitmapOptions() {
		try {
			// TODO I think this can all be done without reflection now because all these properties are SDK 4
			final Field density = DisplayMetrics.class.getDeclaredField("DENSITY_DEFAULT");
			final Field inDensity = BitmapFactory.Options.class.getDeclaredField("inDensity");
			final Field inTargetDensity = BitmapFactory.Options.class
					.getDeclaredField("inTargetDensity");
			final Field targetDensity = DisplayMetrics.class.getDeclaredField("densityDpi");
			final BitmapFactory.Options options = new BitmapFactory.Options();
			inDensity.setInt(options, density.getInt(null));
			inTargetDensity.setInt(options, targetDensity.getInt(mDisplayMetrics));
			return options;
		} catch (final IllegalAccessException ex) {
			// ignore
		} catch (final NoSuchFieldException ex) {
			// ignore
		}
		return null;
	}

	@Override
	public Drawable getDrawable(final bitmap pResId) {
		return mResources != null
				? new BitmapDrawable(mResources, getBitmap(pResId))
				: new BitmapDrawable(getBitmap(pResId));
	}

	@Override
	public float getDisplayMetricsDensity() {
		return mDisplayMetrics.density;
	}

}
