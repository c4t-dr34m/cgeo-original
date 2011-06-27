package carnero.cgeo.original.mapsforge;

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;

import carnero.cgeo.original.models.Coord;
import carnero.cgeo.original.mapinterfaces.CacheOverlayItemImpl;

public class mfCacheOverlayItem extends OverlayItem implements CacheOverlayItemImpl {
	private String cacheType = null;
	private Coord coord;

	public mfCacheOverlayItem(Coord coordinate, String type) {
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

	@Override
	public Drawable getMarker(int index) {
		return getMarker();
	}

}
