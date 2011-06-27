package carnero.cgeo.original.googlemaps;

import android.content.Context;
import carnero.cgeo.original.R;
import carnero.cgeo.original.models.Coord;
import carnero.cgeo.original.models.User;
import carnero.cgeo.original.mapinterfaces.CacheOverlayItemImpl;
import carnero.cgeo.original.mapinterfaces.GeoPointImpl;
import carnero.cgeo.original.mapinterfaces.MapFactory;
import carnero.cgeo.original.mapinterfaces.OverlayImpl;
import carnero.cgeo.original.mapinterfaces.OverlayBase;
import carnero.cgeo.original.mapinterfaces.UserOverlayItemImpl;

public class googleMapFactory implements MapFactory{

	@Override
	public Class getMapClass() {
		return googleMapActivity.class;
	}

	@Override
	public int getMapViewId() {
		return R.id.map;
	}

	@Override
	public int getMapLayoutId() {
		return R.layout.googlemap;
	}

	@Override
	public GeoPointImpl getGeoPointBase(int latE6, int lonE6) {
		return new googleGeoPoint(latE6, lonE6);
	}

	@Override
	public OverlayImpl getOverlayBaseWrapper(OverlayBase ovlIn) {
		googleOverlay baseOvl = new googleOverlay(ovlIn);
		return baseOvl;
	}
	
	@Override
	public CacheOverlayItemImpl getCacheOverlayItem(Coord coordinate, String type) {
		googleCacheOverlayItem baseItem = new googleCacheOverlayItem(coordinate, type);
		return baseItem;
	}

	@Override
	public UserOverlayItemImpl getUserOverlayItemBase(Context context, User userOne) {
		googleUsersOverlayItem baseItem = new googleUsersOverlayItem(context, userOne);
		return baseItem;
	}

}
