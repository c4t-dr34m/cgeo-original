package carnero.cgeo.original.googlemaps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import carnero.cgeo.original.mapcommon.UsersOverlay;
import carnero.cgeo.original.mapinterfaces.ItemizedOverlayImpl;
import carnero.cgeo.original.mapinterfaces.MapProjectionImpl;
import carnero.cgeo.original.mapinterfaces.MapViewImpl;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class googleUsersOverlay extends ItemizedOverlay<googleUsersOverlayItem> implements ItemizedOverlayImpl {

	private UsersOverlay base;

	public googleUsersOverlay(Context contextIn, Drawable markerIn) {
		super(boundCenter(markerIn));
		base = new UsersOverlay(this, contextIn);
	}
	
	@Override
	public UsersOverlay getBase() {
		return base;
	}

	@Override
	protected googleUsersOverlayItem createItem(int i) {
		if (base == null)
			return null;

		return (googleUsersOverlayItem) base.createItem(i);
	}

	@Override
	public int size() {
		if (base == null)
			return 0;

		return base.size();
	}

	@Override
	protected boolean onTap(int arg0) {
		if (base == null)
			return false;
		
		return base.onTap(arg0);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		base.draw(canvas, (MapViewImpl) mapView, shadow);
	}

	@Override
	public void superPopulate() {
		populate();
	}

	@Override
	public Drawable superBoundCenter(Drawable markerIn) {
		return super.boundCenter(markerIn);
	}

	@Override
	public Drawable superBoundCenterBottom(Drawable marker) {
		return super.boundCenterBottom(marker);
	}

	@Override
	public void superSetLastFocusedItemIndex(int i) {
		super.setLastFocusedIndex(i);
	}

	@Override
	public boolean superOnTap(int index) {
		return super.onTap(index);
	}

	@Override
	public void superDraw(Canvas canvas, MapViewImpl mapView, boolean shadow) {
		super.draw(canvas, (MapView) mapView, shadow);
	}

	@Override
	public void superDrawOverlayBitmap(Canvas canvas, Point drawPosition,
			MapProjectionImpl projection, byte drawZoomLevel) {
		// Nothing to do here
	}

}