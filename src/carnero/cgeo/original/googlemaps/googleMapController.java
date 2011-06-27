package carnero.cgeo.original.googlemaps;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;

import carnero.cgeo.original.mapinterfaces.GeoPointImpl;
import carnero.cgeo.original.mapinterfaces.MapControllerImpl;

public class googleMapController implements MapControllerImpl {

	private MapController mapController;
	
	public googleMapController(MapController mapControllerIn) {
		mapController = mapControllerIn;
	}

	@Override
	public void animateTo(GeoPointImpl geoPoint) {
		mapController.animateTo((GeoPoint)geoPoint);
	}

	@Override
	public void setCenter(GeoPointImpl geoPoint) {
		mapController.setCenter((GeoPoint)geoPoint);
	}

	@Override
	public void setZoom(int mapzoom) {
		mapController.setZoom(mapzoom);
	}

	@Override
	public void zoomToSpan(int latSpanE6, int lonSpanE6) {
		mapController.zoomToSpan(latSpanE6, lonSpanE6);
	}
	
}
