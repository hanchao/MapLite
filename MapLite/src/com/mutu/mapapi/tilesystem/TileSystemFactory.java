package com.mutu.mapapi.tilesystem;

import java.util.ArrayList;

import com.mutu.mapapi.tileprovider.tilesource.ITileSource;


public class TileSystemFactory {

	public static TileSystem getTileSystem(final String aName) throws IllegalArgumentException {
		for (final TileSystem tileSystem : mTileSystems) {
			if (tileSystem.name().equals(aName)) {
				return tileSystem;
			}
		}
		throw new IllegalArgumentException("No such tile System: " + aName);
	}
	
	private static ArrayList<TileSystem> mTileSystems;
	static {
		mTileSystems = new ArrayList<TileSystem>();

		mTileSystems.add(new WGSTileSystem());
		mTileSystems.add(new MercatorTileSystem());
	}
}
