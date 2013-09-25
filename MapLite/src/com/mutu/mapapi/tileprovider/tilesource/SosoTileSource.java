package com.mutu.mapapi.tileprovider.tilesource;

import com.mutu.mapapi.ResourceProxy.string;
import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tilesystem.TileSystem;

public class SosoTileSource extends OnlineTileSourceBase {

	public SosoTileSource(final String aName, final string aResourceId, final int aZoomMinLevel,
			final int aZoomMaxLevel, final int aTileSizePixels, 
			final TileSystem aTileSystem,
			final String aImageFilenameEnding,
			final String... aBaseUrl) {
		super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,aTileSystem,
				aImageFilenameEnding, aBaseUrl);
	}

	@Override
	public String getTileURLString(MapTile aTile) {
		
		int x = aTile.getX();
		int y = (1<<aTile.getZoomLevel()) -1 - aTile.getY();
		int dx = (int) Math.floor(x/16);
		int dy = (int) Math.floor(y/16);
		
		return getBaseUrl() + aTile.getZoomLevel() + "/" + dx + "/" + dy + "/" + x + "_" + y + mImageFilenameEnding;
		
	}

}
