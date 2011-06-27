package carnero.cgeo.original.mapinterfaces;

import carnero.cgeo.original.libs.Settings;
import carnero.cgeo.original.mapcommon.MapOverlay;
import carnero.cgeo.original.mapcommon.UsersOverlay;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Defines common functions of the provider-specific
 * MapView implementations
 * @author rsudev
 *
 */
public interface MapViewImpl {

	void invalidate();

	void setSatellite(boolean b);

	void setBuiltInZoomControls(boolean b);

	void displayZoomControls(boolean b);

	void preLoad();

	void clearOverlays();
	
	void addOverlay(OverlayImpl ovl);

	MapControllerImpl getMapController();

	void destroyDrawingCache();

	boolean isSatellite();

	GeoPointImpl getMapViewCenter();

	int getLatitudeSpan();

	int getLongitudeSpan();

	int getMapZoomLevel();

	int getWidth();

	int getHeight();

	MapProjectionImpl getMapProjection();

	Context getContext();

	MapOverlay createAddMapOverlay(Settings settings, Context context,
			Drawable drawable, boolean fromDetailIntent);

	UsersOverlay createAddUsersOverlay(Context context, Drawable markerIn);

	boolean needsScaleOverlay();

	void setBuiltinScale(boolean b);

	void setMapSource(Settings settings);

}
