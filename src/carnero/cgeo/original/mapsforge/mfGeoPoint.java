package carnero.cgeo.original.mapsforge;

import org.mapsforge.android.maps.GeoPoint;

import carnero.cgeo.original.mapinterfaces.GeoPointImpl;

public class mfGeoPoint extends GeoPoint implements GeoPointImpl {

	public mfGeoPoint(int latitudeE6, int longitudeE6) {
		super(latitudeE6, longitudeE6);
	}
}
