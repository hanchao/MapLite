package com.mutu.mapapi.tileprovider.tilesource;


import com.mutu.mapapi.ResourceProxy.string;
import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tilesystem.TileSystem;
import com.mutu.mapapi.tilesystem.WGSTileSystem;


public class TiandituTileSource extends OnlineTileSourceBase {

	private String mLayerName = "";
	
	public TiandituTileSource(final String aName, final string aResourceId, final int aZoomMinLevel,
			final int aZoomMaxLevel, final int aTileSizePixels, 
			final TileSystem aTileSystem,
			final String aImageFilenameEnding,
			final String aLayerName,
			final String... aBaseUrl) {
		super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,aTileSystem,
				aImageFilenameEnding, aBaseUrl);
		mLayerName = aLayerName;
	}
	
	@Override
	public String getTileURLString(MapTile aTile) {
		int zoom = aTile.getZoomLevel();
		if(getTileSystem() instanceof WGSTileSystem){
			zoom += 1;
		}
		return getBaseUrl() + "?T=" + mLayerName + "&x=" + aTile.getX() + "&y=" + aTile.getY() + "&l=" + zoom;
	}
}