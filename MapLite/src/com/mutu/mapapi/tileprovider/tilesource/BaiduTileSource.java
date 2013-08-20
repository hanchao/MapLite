package com.mutu.mapapi.tileprovider.tilesource;


import com.mutu.mapapi.ResourceProxy.string;
import com.mutu.mapapi.mappoint.TileSystem;
import com.mutu.mapapi.tileprovider.MapTile;

public class BaiduTileSource extends OnlineTileSourceBase {

	public BaiduTileSource(final String aName, final string aResourceId, final int aZoomMinLevel,
			final int aZoomMaxLevel, final int aTileSizePixels,
			final TileSystem aTileSystem,
			final String aImageFilenameEnding,
			final String... aBaseUrl) {
		super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,aTileSystem,
				aImageFilenameEnding, aBaseUrl);
	}
	
	@Override
	public String getTileURLString(MapTile aTile) {
		
		int zoom = aTile.getZoomLevel();

		 int offsetX = (int)Math.pow(2, zoom-1);

		 int offsetY = offsetX - 1;

		 int numX = aTile.getX() - offsetX;

		 int numY = -aTile.getY() + offsetY;

		 String xstr = numX < 0 ? "M" + String.valueOf(-numX) : String.valueOf(numX);

		 String ystr = numY < 0 ? "M" + String.valueOf(-numY) : String.valueOf(numY);

		 return getBaseUrl() + "u=x=" + xstr + ";y=" + ystr + ";z=" + zoom + ";v=017;type=web&fm=44&udt=20130712";
	}

}
