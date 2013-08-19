package com.mutu.mapapi.tileprovider.modules;

public interface INetworkAvailablityCheck {

	boolean getNetworkAvailable();

	boolean getWiFiNetworkAvailable();

	boolean getCellularDataNetworkAvailable();

	boolean getRouteToPathExists(int hostAddress);
}
