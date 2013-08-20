package com.mutu.mapapi.mappoint;

import com.mutu.mapapi.util.GeoPoint;

import android.graphics.Point;

public abstract class TileSystem {

	protected int mTileSize = 256;
	protected double EarthRadius = 6378137;
	protected double MinLatitude = -90.0;
	protected double MaxLatitude = 90.0;
	protected double MinLongitude = -180;
	protected double MaxLongitude = 180;
	
	public void setTileSize(final int tileSize) {
		mTileSize = tileSize;
	}

	public int getTileSize() {
		return mTileSize;
	}
	
	/**
	 * Clips a number to the specified minimum and maximum values.
	 * 
	 * @param n
	 *            The number to clip
	 * @param minValue
	 *            Minimum allowable value
	 * @param maxValue
	 *            Maximum allowable value
	 * @return The clipped value.
	 */
	protected static double Clip(final double n, final double minValue, final double maxValue) {
		return Math.min(Math.max(n, minValue), maxValue);
	}

	/**
	 * Determines the map width and height (in pixels) at a specified level of detail.
	 * 
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail)
	 * @return The map width and height in pixels
	 */

	public abstract int MapWidthPixelSize(final int levelOfDetail);

	public abstract int MapHeigthPixelSize(final int levelOfDetail);
	
	public abstract int MapWidthTileSize(final int levelOfDetail);

	public abstract int MapHeigthTileSize(final int levelOfDetail);
	
	/**
	 * Determines the ground resolution (in meters per pixel) at a specified latitude and level of
	 * detail.
	 * 
	 * @param latitude
	 *            Latitude (in degrees) at which to measure the ground resolution
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail)
	 * @return The ground resolution, in meters per pixel
	 */
	public double GroundResolution(double latitude, final int levelOfDetail) {
		latitude = Clip(latitude, MinLatitude, MaxLatitude);
		return Math.cos(latitude * Math.PI / 180) * 2 * Math.PI * EarthRadius
				/ MapWidthPixelSize(levelOfDetail);
	}
	
	/**
	 * Determines the map scale at a specified latitude, level of detail, and screen resolution.
	 * 
	 * @param latitude
	 *            Latitude (in degrees) at which to measure the map scale
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail)
	 * @param screenDpi
	 *            Resolution of the screen, in dots per inch
	 * @return The map scale, expressed as the denominator N of the ratio 1 : N
	 */
	public double MapScale(final double latitude, final int levelOfDetail,
			final int screenDpi) {
		return GroundResolution(latitude, levelOfDetail) * screenDpi / 0.0254;
	}
	
	public abstract String name();
	
	public abstract Point LatLongToPixelXY(double latitude, double longitude,
			final int levelOfDetail, final Point reuse);
	
	public abstract GeoPoint PixelXYToLatLong(final int pixelX, final int pixelY,
			final int levelOfDetail, final GeoPoint reuse);
	
	/**
	 * Converts pixel XY coordinates into tile XY coordinates of the tile containing the specified
	 * pixel.
	 * 
	 * @param pixelX
	 *            Pixel X coordinate
	 * @param pixelY
	 *            Pixel Y coordinate
	 * @param reuse
	 *            An optional Point to be recycled, or null to create a new one automatically
	 * @return Output parameter receiving the tile X and Y coordinates
	 */
	public Point PixelXYToTileXY(final int pixelX, final int pixelY, final Point reuse) {
		final Point out = (reuse == null ? new Point() : reuse);

		out.x = pixelX / mTileSize;
		out.y = pixelY / mTileSize;
		return out;
	}

	/**
	 * Converts tile XY coordinates into pixel XY coordinates of the upper-left pixel of the
	 * specified tile.
	 * 
	 * @param tileX
	 *            Tile X coordinate
	 * @param tileY
	 *            Tile X coordinate
	 * @param reuse
	 *            An optional Point to be recycled, or null to create a new one automatically
	 * @return Output parameter receiving the pixel X and Y coordinates
	 */
	public Point TileXYToPixelXY(final int tileX, final int tileY, final Point reuse) {
		final Point out = (reuse == null ? new Point() : reuse);

		out.x = tileX * mTileSize;
		out.y = tileY * mTileSize;
		return out;
	}

	/**
	 * Converts tile XY coordinates into a QuadKey at a specified level of detail.
	 * 
	 * @param tileX
	 *            Tile X coordinate
	 * @param tileY
	 *            Tile Y coordinate
	 * @param levelOfDetail
	 *            Level of detail, from 1 (lowest detail) to 23 (highest detail)
	 * @return A string containing the QuadKey
	 */
	public String TileXYToQuadKey(final int tileX, final int tileY, final int levelOfDetail) {
		final StringBuilder quadKey = new StringBuilder();
		for (int i = levelOfDetail; i > 0; i--) {
			char digit = '0';
			final int mask = 1 << (i - 1);
			if ((tileX & mask) != 0) {
				digit++;
			}
			if ((tileY & mask) != 0) {
				digit++;
				digit++;
			}
			quadKey.append(digit);
		}
		return quadKey.toString();
	}

	/**
	 * Converts a QuadKey into tile XY coordinates.
	 * 
	 * @param quadKey
	 *            QuadKey of the tile
	 * @param reuse
	 *            An optional Point to be recycled, or null to create a new one automatically
	 * @return Output parameter receiving the tile X and y coordinates
	 */
	public Point QuadKeyToTileXY(final String quadKey, final Point reuse) {
		final Point out = (reuse == null ? new Point() : reuse);
		int tileX = 0;
		int tileY = 0;

		final int levelOfDetail = quadKey.length();
		for (int i = levelOfDetail; i > 0; i--) {
			final int mask = 1 << (i - 1);
			switch (quadKey.charAt(levelOfDetail - i)) {
			case '0':
				break;

			case '1':
				tileX |= mask;
				break;

			case '2':
				tileY |= mask;
				break;

			case '3':
				tileX |= mask;
				tileY |= mask;
				break;

			default:
				throw new IllegalArgumentException("Invalid QuadKey digit sequence.");
			}
		}
		out.set(tileX, tileY);
		return out;
	}
}
