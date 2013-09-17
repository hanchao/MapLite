package com.mutu.mapapi.search.overlays;

import java.util.List;

import com.mutu.mapapi.search.utils.BonusPackHelper;
import com.mutu.mapapi.views.MapView;
import com.mutu.mapapi.views.MapView.Projection;
import com.mutu.mapapi.views.overlay.ItemizedIconOverlay;
import com.mutu.mapapi.views.overlay.OverlayItem;
import com.mutu.mapapi.views.safecanvas.ISafeCanvas;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;

/**
 * An itemized overlay with an InfoWindow or "bubble" which opens 
 * when the user taps on an overlay item, and displays item attributes. <br>
 * Items must be ExtendedOverlayItem. <br>
 * 
 * 
 * @see ExtendedOverlayItem
 * @see InfoWindow
 * 
 * @author M.Kergall
 */
public class ItemizedOverlayWithBubble<Item extends OverlayItem> extends ItemizedIconOverlay<Item> {
	//protected List<Item> mItemsList;
	protected InfoWindow mBubble; //only one for all items of this overlay => one at a time
	protected OverlayItem mItemWithBubble; //the item currently showing the bubble. Null if none. 

	static int layoutResId = 0;
	
	public ItemizedOverlayWithBubble(final Context context, final List<Item> aList, 
			final MapView mapView, final InfoWindow bubble) {
		super(context, aList, new OnItemGestureListener<Item>() {
            @Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                    return false;
            }
            @Override public boolean onItemLongPress(final int index, final OverlayItem item) {
                    return false;
            }
    	} );
		//mItemsList = aList;
		if (bubble != null){
			mBubble = bubble;
		} else {
			//build default bubble:
			String packageName = context.getPackageName();
			if (layoutResId == 0){
				layoutResId = context.getResources().getIdentifier("layout/bonuspack_bubble", null, packageName);
				if (layoutResId == 0)
					Log.e(BonusPackHelper.LOG_TAG, "ItemizedOverlayWithBubble: layout/bonuspack_bubble not found in "+packageName);
			}
			mBubble = new DefaultInfoWindow(layoutResId, mapView);
		}
		mItemWithBubble = null;
	}

	public ItemizedOverlayWithBubble(final Context context, final List<Item> aList, 
			final MapView mapView) {
		this(context, aList, mapView, null);
	}

	public void showBubbleOnItem(final ExtendedOverlayItem eItem, final MapView mapView, boolean panIntoView) {
		mItemWithBubble = eItem;
		if (eItem != null){
			eItem.showBubble(mBubble, mapView, panIntoView);
			//setFocus((Item)eItem);
		}
	}
	
	/**
	 * Opens the bubble on the item. 
	 * For each ItemizedOverlay, only one bubble is opened at a time. 
	 * If you want more bubbles opened simultaneously, use many ItemizedOverlays. 
	 * 
	 * @param index of the overlay item to show
	 * @param mapView
	 * @param panIntoView true if you want the map view to be centered on it. 
	 */
	public void showBubbleOnItem(final int index, final MapView mapView, boolean panIntoView) {
		ExtendedOverlayItem eItem = (ExtendedOverlayItem)(getItem(index));
		showBubbleOnItem(eItem, mapView, panIntoView);
	}
	
	/**
	 * Close the bubble (if it's opened). 
	 */
	public void hideBubble(){
		mBubble.close();
		mItemWithBubble = null;
	}
	
	@Override protected boolean onSingleTapUpHelper(final int index, final Item item, final MapView mapView) {
		showBubbleOnItem(index, mapView, true);
		return true;
	}
	
	/** @return the item currently showing the bubble, or null if none.  */
	public OverlayItem getBubbledItem(){
		if (mBubble.isOpen())
			return mItemWithBubble;
		else
			return null;
	}
	
	/** @return the index of the item currently showing the bubble, or -1 if none.  */
	public int getBubbledItemId(){
		OverlayItem item = getBubbledItem();
		if (item == null)
			return -1;
		else
			return mItemList.indexOf(item);
	}
	
	@Override public synchronized Item removeItem(final int position){
		Item result = super.removeItem(position);
		if (mItemWithBubble == result){
			hideBubble();
		}
		return result;
	}
	
	@Override public synchronized boolean removeItem(final Item item){
		boolean result = super.removeItem(item);
		if (mItemWithBubble == item){
			hideBubble();
		}
		return result;
	}
	
	@Override public synchronized void removeAllItems(){
		super.removeAllItems();
		hideBubble();
	}

	@Override public synchronized void drawSafe(final ISafeCanvas canvas, final MapView mapView, final boolean shadow) {
		//1. Fixing drawing focused item on top in ItemizedOverlay (osmdroid issue 354):
		//2. Fixing lack of synchronization on mItemList
		if (shadow) {
		        return;
		}
		final Projection pj = mapView.getProjection();
		final int size = mItemList.size() - 1;
		final Point mCurScreenCoords = new Point();
		
		/* Draw in backward cycle, so the items with the least index are on the front. */
		for (int i = size; i >= 0; i--) {
	        final Item item = getItem(i);
			if (item != mItemWithBubble){
		        pj.toMapPixels(item.getPoint(), mCurScreenCoords);
		        onDrawItem(canvas, item, mCurScreenCoords);
			}
		}
		//draw focused item last:
		if (mItemWithBubble != null){
	        pj.toMapPixels(mItemWithBubble.getPoint(), mCurScreenCoords);
	        onDrawItem(canvas, (Item)mItemWithBubble, mCurScreenCoords);
		}
    }
	
}
