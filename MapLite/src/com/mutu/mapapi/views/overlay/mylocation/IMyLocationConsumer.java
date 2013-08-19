package com.mutu.mapapi.views.overlay.mylocation;

import android.location.Location;

public interface IMyLocationConsumer {
	void onLocationChanged(Location location, IMyLocationProvider source);
}
