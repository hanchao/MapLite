package com.mutu.mapapi.util;


import com.mutu.mapapi.mappoint.TileSystem;
import com.mutu.mapapi.mappoint.TileSystemFactory;
import com.mutu.mapapi.tileprovider.MapTile;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * A class that will loop around all the map tiles in the given viewport.
 */
public abstract class TileLooper {

	protected final Point mUpperLeft = new Point();
	protected final Point mLowerRight = new Point();

	public final void loop(final Canvas pCanvas, final int pZoomLevel, final int pTileSizePx, final Rect pViewPort, TileSystem tileSystem) {
		// Calculate the amount of tiles needed for each side around the center one.
		tileSystem.PixelXYToTileXY(pViewPort.left, pViewPort.top, mUpperLeft);
		mUpperLeft.offset(-1, -1);
		tileSystem.PixelXYToTileXY(pViewPort.right, pViewPort.bottom, mLowerRight);

		final int mapTileWidthUpperBound = tileSystem.MapWidthTileSize(pZoomLevel);
		final int mapTileHeigthUpperBound = tileSystem.MapHeigthTileSize(pZoomLevel);

		initialiseLoop(pZoomLevel, pTileSizePx);

		/* Draw all the MapTiles (from the upper left to the lower right). */
		for (int y = mUpperLeft.y; y <= mLowerRight.y; y++) {
			for (int x = mUpperLeft.x; x <= mLowerRight.x; x++) {
				// Construct a MapTile to request from the tile provider.
				final int tileY = MyMath.mod(y, mapTileHeigthUpperBound);
				final int tileX = MyMath.mod(x, mapTileWidthUpperBound);
				final MapTile tile = new MapTile(pZoomLevel, tileX, tileY);
				handleTile(pCanvas, pTileSizePx, tile, x, y);
			}
		}

		finaliseLoop();
	}

	public abstract void initialiseLoop(int pZoomLevel, int pTileSizePx);

	public abstract void handleTile(Canvas pCanvas, int pTileSizePx, MapTile pTile, int pX, int pY);

	public abstract void finaliseLoop();
}
