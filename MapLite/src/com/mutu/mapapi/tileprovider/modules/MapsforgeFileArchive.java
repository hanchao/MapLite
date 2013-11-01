package com.mutu.mapapi.tileprovider.modules;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.renderer.DatabaseRenderer;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Bitmap.Config;
//import android.graphics.Color;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.mutu.mapapi.tileprovider.MapTile;
import com.mutu.mapapi.tileprovider.tilesource.ITileSource;
import com.mutu.mapapi.tileprovider.util.StreamUtils;

public class MapsforgeFileArchive implements IArchiveFile {

	private static final Logger logger = LoggerFactory.getLogger(MapsforgeFileArchive.class);
	
	private final File mFile;
	private final MapDatabase mMapDatabase;
	private final DatabaseRenderer mDatabaseRenderer;
	
	private MapsforgeFileArchive(final File pFile){
		mFile = pFile;
		mMapDatabase = new MapDatabase();
		FileOpenResult result = mMapDatabase.openFile(pFile);
		mDatabaseRenderer = new DatabaseRenderer(mMapDatabase);
		
	}

	public static MapsforgeFileArchive getMapsforgeFileArchive(final File pFile) throws FileNotFoundException, IOException {
		return new MapsforgeFileArchive(pFile);
	}
	
	@Override
	public InputStream getInputStream(ITileSource tileSource, MapTile tile) {
		// TODO Auto-generated method stub
		
		try {
			Tile maptile = new Tile(tile.getX(),tile.getY(),(byte) tile.getZoomLevel());
			//MapReadResult map = mMapDatabase.readMapData();
			//logger.debug("poi = " + map.pointOfInterests.size() + "way = " + map.ways.size());
			
//			DatabaseRenderer DatabaseRenderer = new DatabaseRenderer(mMapDatabase,AndroidGraphicFactory.INSTANCE);
			RendererJob rendererJob = new RendererJob(maptile, mFile, InternalRenderTheme.OSMARENDER, 1.5f);
			Bitmap bitmap = mDatabaseRenderer.executeJob(rendererJob);
			
//			Bitmap bitmap = Bitmap.createBitmap(tileSource.getTileSizePixels(), tileSource.getTileSizePixels(), Config.ARGB_8888);
//			Canvas canvas = new Canvas(bitmap);
//			canvas.drawColor(Color.BLUE);
			
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			BufferedOutputStream out = new BufferedOutputStream(dataStream, StreamUtils.IO_BUFFER_SIZE);
			
			bitmap.compress(CompressFormat.PNG, 0, out);
			out.flush();
	
	
			final byte[] data = dataStream.toByteArray();
			final ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
			return byteStream;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("Error getting tile: " + tile, e);
		}
		return null;
	}

	@Override
	public String toString() {
		return "MapsforgeFileArchive [mMapDatabase=" + mMapDatabase.toString() + "]";
	}
}
