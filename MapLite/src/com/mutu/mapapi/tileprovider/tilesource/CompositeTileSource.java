package com.mutu.mapapi.tileprovider.tilesource;

import com.mutu.mapapi.ResourceProxy.string;
import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tilesystem.TileSystem;

public class CompositeTileSource extends BitmapTileSourceBase {
	
	private final OnlineTileSourceBase mTileSources[];
	
	public CompositeTileSource(String aName, string aResourceId,
			int aZoomMinLevel, int aZoomMaxLevel, int aTileSizePixels,
			TileSystem aTileSystem, String aImageFilenameEnding, final OnlineTileSourceBase... aTileSource) {
		super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,
				aTileSystem, aImageFilenameEnding);
		// TODO Auto-generated constructor stub
		mTileSources  = aTileSource;
	}

	public OnlineTileSourceBase get(final int pIndex) {
		return mTileSources[pIndex];
	}

	public int size() {
		return mTileSources.length;
	}

	public String[] getTileURLString(MapTile aTile){
		String []urlList = new String[mTileSources.length];
		for (int i=0;i<mTileSources.length;i++){
			urlList[i] = mTileSources[i].getTileURLString(aTile);
		}
		return urlList;
	}
}
