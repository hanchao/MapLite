package com.mutu.mapapi.tileprovider.tilesource;

import com.mutu.mapapi.ResourceProxy.string;
import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tilesystem.TileSystem;

public class SuperMapCloudTileSource extends OnlineTileSourceBase {

	public SuperMapCloudTileSource(final String aName, final string aResourceId, final int aZoomMinLevel,
			final int aZoomMaxLevel, final int aTileSizePixels, 
			final TileSystem aTileSystem,
			final String aImageFilenameEnding,
			final String... aBaseUrl) {
		super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,aTileSystem,
				aImageFilenameEnding, aBaseUrl);
	}

	@Override
	public String getTileURLString(MapTile aTile) {
		//http://t0.supermapcloud.com/FileService/image?map=quanguo&type=web&x=3372&y=1550&z=12
		return getBaseUrl() + "?map=quanguo&type=web&x=" + aTile.getX() + "&y=" + aTile.getY() + "&z=" + aTile.getZoomLevel();
	}
}
