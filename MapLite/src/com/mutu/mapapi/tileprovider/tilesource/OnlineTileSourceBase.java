package com.mutu.mapapi.tileprovider.tilesource;


import com.mutu.mapapi.ResourceProxy.string;
import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tilesystem.TileSystem;

public abstract class OnlineTileSourceBase extends BitmapTileSourceBase {

	private final String mBaseUrls[];

	public OnlineTileSourceBase(final String aName, final string aResourceId,
			final int aZoomMinLevel, final int aZoomMaxLevel, final int aTileSizePixels,
			final TileSystem aTileSystem,
			final String aImageFilenameEnding, final String... aBaseUrl) {
		super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,aTileSystem,
				aImageFilenameEnding);
		mBaseUrls = aBaseUrl;
	}

	public abstract String getTileURLString(MapTile aTile);

	/**
	 * Get the base url, which will be a random one if there are more than one.
	 */
	protected String getBaseUrl() {
		return mBaseUrls[random.nextInt(mBaseUrls.length)];
	}
}
