package org.osmdroid.tileprovider;

import java.util.LinkedHashMap;

import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class LRUMapTileCache extends LinkedHashMap<MapTile, Drawable>
	implements OpenStreetMapTileProviderConstants {

	private static final Logger logger = LoggerFactory.getLogger(LRUMapTileCache.class);

	private static final long serialVersionUID = -541142277575493335L;

	private int mCapacity;

	public LRUMapTileCache(final int aCapacity) {
		super(aCapacity + 2, 0.1f, true);
		mCapacity = aCapacity;
	}

	public void ensureCapacity(final int aCapacity) {
		if (aCapacity > mCapacity) {
			logger.info("Tile cache increased from " + mCapacity + " to " + aCapacity);
			mCapacity = aCapacity;
		}
	}

	@Override
	public Drawable remove(final Object aKey) {
		final Drawable drawable = super.remove(aKey);
		if (drawable instanceof BitmapDrawable) {
			final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
		return drawable;
	}

	@Override
	public void clear() {
		// remove them all individually so that they get recycled
		while (!isEmpty()) {
			remove(keySet().iterator().next());
		}

		// and then clear
		super.clear();
	}

	@Override
	protected boolean removeEldestEntry(final java.util.Map.Entry<MapTile, Drawable> aEldest) {
		if (size() > mCapacity) {
			final MapTile eldest = aEldest.getKey();
			if (DEBUGMODE) {
				logger.debug("Remove old tile: " + eldest);
			}
			remove(eldest);
			// don't return true because we've already removed it
		}
		return false;
	}
}
