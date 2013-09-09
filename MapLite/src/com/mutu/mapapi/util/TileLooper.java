package com.mutu.mapapi.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tilesystem.TileSystem;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * A class that will loop around all the map tiles in the given viewport.
 */
public abstract class TileLooper {

	protected final Point mUpperLeft = new Point();
	protected final Point mLowerRight = new Point();

	public final void loop(final Canvas pCanvas, final int pZoomLevel, final int pTileSizePx, final Rect pViewPort, final TileSystem tileSystem) {
		// Calculate the amount of tiles needed for each side around the center one.
		tileSystem.PixelXYToTileXY(pViewPort.left, pViewPort.top, mUpperLeft);
		mUpperLeft.offset(-1, -1);
		tileSystem.PixelXYToTileXY(pViewPort.right, pViewPort.bottom, mLowerRight);

		final int mapTileWidthUpperBound = tileSystem.MapWidthTileSize(pZoomLevel);
		final int mapTileHeigthUpperBound = tileSystem.MapHeigthTileSize(pZoomLevel);

		initialiseLoop(pZoomLevel, pTileSizePx);
		
		/* Draw all the MapTiles (from the center to the edge). */
		ArrayList<Point> tilePoss = new ArrayList<Point>();
		
		for (int y = mUpperLeft.y; y <= mLowerRight.y; y++) {
			for (int x = mUpperLeft.x; x <= mLowerRight.x; x++) {
				tilePoss.add(new Point(x,y));
			}
		}

		class TileComparator implements Comparator<Point> {

			@Override
			public int compare(Point arg0, Point arg1) {
				// TODO Auto-generated method stub
				Point aFirst = ((Point) arg0);
				Point aSecond = ((Point) arg1);
				
				Point aFirstPixel = tileSystem.TileXYToPixelXY(aFirst.x, aFirst.y, null);
				Point aSecondPixel = tileSystem.TileXYToPixelXY(aSecond.x, aSecond.y, null);

				double aFirstDis = Math.pow(aFirstPixel.x-pViewPort.centerX(),2) + Math.pow(aFirstPixel.y-pViewPort.centerY(),2);
				double aSecondDis = Math.pow(aSecondPixel.x-pViewPort.centerX(),2) + Math.pow(aSecondPixel.y-pViewPort.centerY(),2);
				
				return (int) (aSecondDis - aFirstDis);
			}
		}
		Collections.sort(tilePoss, new TileComparator());
		
		for (Point tilepos : tilePoss){
			final int tileY = MyMath.mod(tilepos.y, mapTileHeigthUpperBound);
			final int tileX = MyMath.mod(tilepos.x, mapTileWidthUpperBound);
			final MapTile tile = new MapTile(pZoomLevel, tileX, tileY);
			handleTile(pCanvas, pTileSizePx, tile, tilepos.x, tilepos.y);
		}

//		/* Draw all the MapTiles (from the upper left to the lower right). */
//		for (int y = mUpperLeft.y; y <= mLowerRight.y; y++) {
//			for (int x = mUpperLeft.x; x <= mLowerRight.x; x++) {
//				// Construct a MapTile to request from the tile provider.
//				final int tileY = MyMath.mod(y, mapTileHeigthUpperBound);
//				final int tileX = MyMath.mod(x, mapTileWidthUpperBound);
//				final MapTile tile = new MapTile(pZoomLevel, tileX, tileY);
//				handleTile(pCanvas, pTileSizePx, tile, x, y);
//			}
//		}

		finaliseLoop();
	}

	public abstract void initialiseLoop(int pZoomLevel, int pTileSizePx);

	public abstract void handleTile(Canvas pCanvas, int pTileSizePx, MapTile pTile, int pX, int pY);

	public abstract void finaliseLoop();
}
