package com.mutu.mapapi.api;

/**
 * An interface that contains the common features of osmdroid and Google Maps v2.
 */
public interface IMap {

	/**
	 * Get the current zoom level of the map
	 */
	float getZoomLevel();

	/**
	 * Set the zoom level of the map
	 */
	void setZoom(float zoomLevel);

	/**
	 * Get the center of the map
	 */
	IGeoPoint getCenter();

	/**
	 * Set the center of the map
	 */
	void setCenter(double latitude, double longitude);

	/**
	 * Get the bearing of the map.
	 * Zero means the top of the map is facing north.
	 */
	float getBearing();

	/**
	 * Set the bearing of the map.
	 * Set to zero for the top of the map to face north.
	 */
	void setBearing(float bearing);

	/**
	 * Set the position of the map
	 */
	void setPosition(IPosition position);

	/**
	 * Increase zoom level by one
	 */
	boolean zoomIn();

	/**
	 * Decrease zoom level by one
	 */
	boolean zoomOut();

	/**
	 * Whether to show the "my location" dot on the map
	 */
	void setMyLocationEnabled(boolean enabled);

	/**
	 * Whether the map is currently showing the "my location" dot
	 */
	boolean isMyLocationEnabled();

	/**
	 * Get the map projection
	 */
	IProjection getProjection();

	/**
	 * Add a marker.
	 */
	void addMarker(Marker marker);

	/**
	 * Add a polyline.
	 * @return an id that can be used for adding points with {@link #addPointToPolyline}
	 */
	long addPolyline(Polyline polyline);

	/**
	 * Add a point to a polyline
	 * @param id the id returned from {@link #addPolyline(Polyline)}
	 * @param point the point to add
	 */
	void addPointToPolyline(long id, IGeoPoint point);

	/**
	 * Removes all markers, polylines, polygons, overlays, etc from the map.
	 */
	void clear();

	/**
	 * Sets a callback that's invoked when the map view changes position.
	 */
	void setOnCameraChangeListener(OnCameraChangeListener listener);
}
