package com.mutu.mapapi.tileprovider.modules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tileprovider.tilesource.ITileSource;
import com.mutu.mapapi.util.GEMFFile;

public class GEMFFileArchive implements IArchiveFile {

	private final GEMFFile mFile;

	private GEMFFileArchive(final File pFile) throws FileNotFoundException, IOException {
		mFile = new GEMFFile(pFile);
	}

	public static GEMFFileArchive getGEMFFileArchive(final File pFile) throws FileNotFoundException, IOException {
		return new GEMFFileArchive(pFile);
	}

	@Override
	public InputStream getInputStream(final ITileSource pTileSource, final MapTile pTile) {
		return mFile.getInputStream(pTile.getX(), pTile.getY(), pTile.getZoomLevel());
	}

	@Override
	public String toString() {
		return "GEMFFileArchive [mGEMFFile=" + mFile.getName() + "]";
	}

}
