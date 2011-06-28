package carnero.cgeo.original.mapsforge;

import android.content.Context;
import carnero.cgeo.original.R;
import carnero.cgeo.original.models.Coord;
import carnero.cgeo.original.models.User;
import carnero.cgeo.original.mapinterfaces.CacheOverlayItemImpl;
import carnero.cgeo.original.mapinterfaces.GeoPointImpl;
import carnero.cgeo.original.mapinterfaces.MapFactory;
import carnero.cgeo.original.mapinterfaces.OverlayBase;
import carnero.cgeo.original.mapinterfaces.OverlayImpl;
import carnero.cgeo.original.mapinterfaces.UserOverlayItemImpl;

public class mfMapFactory implements MapFactory{

	@Override
	public Class getMapClass() {
		return mfMapActivity.class;
	}

	@Override
	public int getMapViewId() {
		return R.id.mfmap;
	}

	@Override
	public int getMapLayoutId() {
		return R.layout.map_mapsforge;
	}

	@Override
	public GeoPointImpl getGeoPointBase(int latE6, int lonE6) {
		return new mfGeoPoint(latE6, lonE6);
	}

	@Override
	public OverlayImpl getOverlayBaseWrapper(OverlayBase ovlIn) {
		mfOverlay baseOvl = new mfOverlay(ovlIn);
		return baseOvl;
	}
	
	@Override
	public CacheOverlayItemImpl getCacheOverlayItem(Coord coordinate, String type) {
		mfCacheOverlayItem baseItem = new mfCacheOverlayItem(coordinate, type);
		return baseItem;
	}

	@Override
	public UserOverlayItemImpl getUserOverlayItemBase(Context context, User userOne) {
		mfUsersOverlayItem baseItem = new mfUsersOverlayItem(context, userOne);
		return baseItem;
	}

}
