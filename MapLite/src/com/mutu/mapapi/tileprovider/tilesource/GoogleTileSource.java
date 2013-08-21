package com.mutu.mapapi.tileprovider.tilesource;


import com.mutu.mapapi.ResourceProxy.string;
import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tilesystem.TileSystem;

public class GoogleTileSource extends OnlineTileSourceBase {

	public GoogleTileSource(final String aName, final string aResourceId, final int aZoomMinLevel,
			final int aZoomMaxLevel, final int aTileSizePixels, 
			final TileSystem aTileSystem,
			final String aImageFilenameEnding,
			final String... aBaseUrl) {
		super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,aTileSystem,
				aImageFilenameEnding, aBaseUrl);
	}
	
	@Override
	public String getTileURLString(MapTile aTile) {
		return getBaseUrl() + "&x=" + aTile.getX() + "&y=" + aTile.getY() + "&z=" + aTile.getZoomLevel() + "";
	}

}
