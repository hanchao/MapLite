package com.mutu.mapapi.tileprovider;

import com.mutu.mapapi.tileprovider.modules.INetworkAvailablityCheck;
import com.mutu.mapapi.tileprovider.modules.MapTileDownloader;
import com.mutu.mapapi.tileprovider.modules.MapTileFileArchiveProvider;
import com.mutu.mapapi.tileprovider.modules.MapTileFilesystemProvider;
import com.mutu.mapapi.tileprovider.modules.NetworkAvailabliltyCheck;
import com.mutu.mapapi.tileprovider.modules.TileWriter;
import com.mutu.mapapi.tileprovider.tilesource.ITileSource;
import com.mutu.mapapi.tileprovider.tilesource.TileSourceFactory;
import com.mutu.mapapi.tileprovider.util.SimpleRegisterReceiver;

import android.content.Context;

/**
 * This top-level tile provider implements a basic tile request chain which includes a
 * {@link MapTileFilesystemProvider} (a file-system cache), a {@link MapTileFileArchiveProvider}
 * (archive provider), and a {@link MapTileDownloader} (downloads map tiles via tile source).
 * 
 * @author Marc Kurtz
 * 
 */
public class MapTileProviderBasic extends MapTileProviderArray implements IMapTileProviderCallback {

	// private static final Logger logger = LoggerFactory.getLogger(MapTileProviderBasic.class);

	/**
	 * Creates a {@link MapTileProviderBasic}.
	 */
	public MapTileProviderBasic(final Context pContext) {
		this(pContext, TileSourceFactory.DEFAULT_TILE_SOURCE);
	}

	/**
	 * Creates a {@link MapTileProviderBasic}.
	 */
	public MapTileProviderBasic(final Context pContext, final ITileSource pTileSource) {
		this(new SimpleRegisterReceiver(pContext), new NetworkAvailabliltyCheck(pContext),
				pTileSource);
	}

	/**
	 * Creates a {@link MapTileProviderBasic}.
	 */
	public MapTileProviderBasic(final IRegisterReceiver pRegisterReceiver,
			final INetworkAvailablityCheck aNetworkAvailablityCheck, final ITileSource pTileSource) {
		super(pTileSource, pRegisterReceiver);

		final TileWriter tileWriter = new TileWriter();

		final MapTileFilesystemProvider fileSystemProvider = new MapTileFilesystemProvider(
				pRegisterReceiver, pTileSource);
		mTileProviderList.add(fileSystemProvider);

		final MapTileFileArchiveProvider archiveProvider = new MapTileFileArchiveProvider(
				pRegisterReceiver, pTileSource);
		mTileProviderList.add(archiveProvider);

		final MapTileDownloader downloaderProvider = new MapTileDownloader(pTileSource, tileWriter,
				aNetworkAvailablityCheck);
		mTileProviderList.add(downloaderProvider);
	}
}
