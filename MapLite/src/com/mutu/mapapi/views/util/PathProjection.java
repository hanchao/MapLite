package com.mutu.mapapi.views.util;

import java.util.List;



import com.mutu.mapapi.tilesystem.TileSystem;
import com.mutu.mapapi.tilesystem.TileSystemFactory;
import com.mutu.mapapi.util.BoundingBoxE6;
import com.mutu.mapapi.util.GeoPoint;
import com.mutu.mapapi.views.MapView.Projection;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

public class PathProjection {

	public static Path toPixels(Projection projection, final List<? extends GeoPoint> in,
			final Path reuse) {
		return toPixels(projection, in, reuse, true);
	}

	public static Path toPixels(Projection projection, final List<? extends GeoPoint> in,
			final Path reuse, final boolean doGudermann) throws IllegalArgumentException {
		if (in.size() < 2) {
			throw new IllegalArgumentException("List of GeoPoints needs to be at least 2.");
		}

		final Path out = (reuse != null) ? reuse : new Path();
		out.incReserve(in.size());

		TileSystem tileSystem = TileSystemFactory.getTileSystem("Mercator");
		boolean first = true;
		for (final GeoPoint gp : in) {
			final Point underGeopointTileCoords = tileSystem.LatLongToPixelXY(
					gp.getLatitudeE6() / 1E6, gp.getLongitudeE6() / 1E6, projection.getZoomLevel(),
					null);
			tileSystem.PixelXYToTileXY(underGeopointTileCoords.x, underGeopointTileCoords.y,
					underGeopointTileCoords);

			/*
			 * Calculate the Latitude/Longitude on the left-upper ScreenCoords of the MapTile.
			 */
			final Point upperRight = tileSystem.TileXYToPixelXY(underGeopointTileCoords.x,
					underGeopointTileCoords.y, null);
			final Point lowerLeft = tileSystem.TileXYToPixelXY(underGeopointTileCoords.x
					+ tileSystem.getTileSize(),
					underGeopointTileCoords.y + tileSystem.getTileSize(), null);
			final GeoPoint neGeoPoint = tileSystem.PixelXYToLatLong(upperRight.x, upperRight.y,
					projection.getZoomLevel(), null);
			final GeoPoint swGeoPoint = tileSystem.PixelXYToLatLong(lowerLeft.x, lowerLeft.y,
					projection.getZoomLevel(), null);
			final BoundingBoxE6 bb = new BoundingBoxE6(neGeoPoint.getLatitudeE6(),
					neGeoPoint.getLongitudeE6(), swGeoPoint.getLatitudeE6(),
					swGeoPoint.getLongitudeE6());

			final PointF relativePositionInCenterMapTile;
			if (doGudermann && (projection.getZoomLevel() < 7)) {
				relativePositionInCenterMapTile = bb
						.getRelativePositionOfGeoPointInBoundingBoxWithExactGudermannInterpolation(
								gp.getLatitudeE6(), gp.getLongitudeE6(), null);
			} else {
				relativePositionInCenterMapTile = bb
						.getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation(
								gp.getLatitudeE6(), gp.getLongitudeE6(), null);
			}

			final Rect screenRect = projection.getScreenRect();
			Point centerMapTileCoords = tileSystem.PixelXYToTileXY(screenRect.centerX(),
					screenRect.centerY(), null);
			final Point upperLeftCornerOfCenterMapTile = tileSystem.TileXYToPixelXY(
					centerMapTileCoords.x, centerMapTileCoords.y, null);
			final int tileDiffX = centerMapTileCoords.x - underGeopointTileCoords.x;
			final int tileDiffY = centerMapTileCoords.y - underGeopointTileCoords.y;
			final int underGeopointTileScreenLeft = upperLeftCornerOfCenterMapTile.x
					- (tileSystem.getTileSize() * tileDiffX);
			final int underGeopointTileScreenTop = upperLeftCornerOfCenterMapTile.y
					- (tileSystem.getTileSize() * tileDiffY);

			final int x = underGeopointTileScreenLeft
					+ (int) (relativePositionInCenterMapTile.x * tileSystem.getTileSize());
			final int y = underGeopointTileScreenTop
					+ (int) (relativePositionInCenterMapTile.y * tileSystem.getTileSize());

			/* Add up the offset caused by touch. */
			if (first) {
				out.moveTo(x, y);
				// out.moveTo(x + MapView.this.mTouchMapOffsetX, y +
				// MapView.this.mTouchMapOffsetY);
			} else {
				out.lineTo(x, y);
			}
			first = false;
		}

		return out;
	}
}
