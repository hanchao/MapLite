package com.mutu.mapapi.mappoint;

/*
 * http://msdn.microsoft.com/en-us/library/bb259689.aspx
 *
 * Copyright (c) 2006-2009 Microsoft Corporation.  All rights reserved.
 *
 *
 */

import com.mutu.mapapi.util.GeoPoint;
import com.mutu.mapapi.util.TileLooper;

import android.graphics.Point;

/**
 * This class provides methods to handle the Mercator projection that is used for the osmdroid tile
 * system.
 */
public final class WGSTileSystem extends TileSystem{
	
	public WGSTileSystem(){
		EarthRadius = 6378137;
		MinLatitude = -90.0;
		MaxLatitude = 90.0;
		MinLongitude = -180;
		MaxLongitude = 180;
	}


	public String name(){
		return "WGS";
	}
	
	public int MapWidthPixelSize(final int levelOfDetail) {
		return (mTileSize << levelOfDetail) * 2;
	}

	public int MapHeigthPixelSize(final int levelOfDetail) {
		return mTileSize << levelOfDetail;
	}
	
	public int MapWidthTileSize(final int levelOfDetail) {
		return (1 << levelOfDetail) * 2;
	}

	public int MapHeigthTileSize(final int levelOfDetail) {
		return 1 << levelOfDetail;
	}
	
	/**
	 * Converts a point from latitude/longitude WGS-84 coordinates (in degrees) into pixel XY
	 * coordinates at a specified level of detail.
	 * 
	 * @param latitude
	 *            Latitude of the point, in degrees
	 * @param longitude
	 *            Longitude of the point, in degrees
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail)
	 * @param reuse
	 *            An optional Point to be recycled, or null to create a new one automatically
	 * @return Output parameter receiving the X and Y coordinates in pixels
	 */
	public Point LatLongToPixelXY(double latitude, double longitude,
			final int levelOfDetail, final Point reuse) {
		final Point out = (reuse == null ? new Point() : reuse);

		latitude = Clip(latitude, MinLatitude, MaxLatitude);
		longitude = Clip(longitude, MinLongitude, MaxLongitude);

		final int mapWidthPixelSize = MapWidthPixelSize(levelOfDetail);
		final int mapHeigthPixelSize = MapHeigthPixelSize(levelOfDetail);

		out.x = (int) ((longitude + 180) / 360 * mapWidthPixelSize);
		out.y = (int) ((90 - latitude) / 180 * mapHeigthPixelSize);
		return out;
	}

	/**
	 * Converts a pixel from pixel XY coordinates at a specified level of detail into
	 * latitude/longitude WGS-84 coordinates (in degrees).
	 * 
	 * @param pixelX
	 *            X coordinate of the point, in pixels
	 * @param pixelY
	 *            Y coordinate of the point, in pixels
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail)
	 * @param reuse
	 *            An optional GeoPoint to be recycled, or null to create a new one automatically
	 * @return Output parameter receiving the latitude and longitude in degrees.
	 */
	public GeoPoint PixelXYToLatLong(final int pixelX, final int pixelY,
			final int levelOfDetail, final GeoPoint reuse) {
		final GeoPoint out = (reuse == null ? new GeoPoint(0, 0) : reuse);

		final double mapWidthPixelSize = MapWidthPixelSize(levelOfDetail);
		final double mapHeigthPixelSize = MapHeigthPixelSize(levelOfDetail);

		final double latitude = 90 - pixelY / mapHeigthPixelSize * 180;
		final double longitude = pixelX / mapWidthPixelSize * 360 - 180;

		out.setLatitudeE6((int) (latitude * 1E6));
		out.setLongitudeE6((int) (longitude * 1E6));
		return out;
	}
}
