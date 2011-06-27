package carnero.cgeo.original.googlemaps;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import carnero.cgeo.original.models.Coord;
import carnero.cgeo.original.mapinterfaces.CacheOverlayItemImpl;

public class googleCacheOverlayItem extends OverlayItem implements CacheOverlayItemImpl {
	private String cacheType = null;
	private Coord coord;

	public googleCacheOverlayItem(Coord coordinate, String type) {
		super(new GeoPoint((int)(coordinate.latitude * 1e6), (int)(coordinate.longitude * 1e6)), coordinate.name, "");

		this.cacheType = type;
		this.coord = coordinate;
	}
	
	public Coord getCoord() {
		return coord;
	}

	public String getType() {
		return cacheType;
	}

}
