package carnero.cgeo.original.mapcommon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.location.Location;
import android.text.Html;
import android.util.Log;
import carnero.cgeo.original.cgBase;
import carnero.cgeo.original.cgCoord;
import carnero.cgeo.original.cgSettings;
import carnero.cgeo.original.cgeodetail;
import carnero.cgeo.original.cgeonavigate;
import carnero.cgeo.original.cgeopopup;
import carnero.cgeo.original.cgeowaypoint;
import carnero.cgeo.original.mapinterfaces.GeoPointImpl;
import carnero.cgeo.original.mapinterfaces.ItemizedOverlayImpl;
import carnero.cgeo.original.mapinterfaces.MapFactory;
import carnero.cgeo.original.mapinterfaces.MapProjectionImpl;
import carnero.cgeo.original.mapinterfaces.OverlayBase;
import carnero.cgeo.original.mapinterfaces.MapViewImpl;
import carnero.cgeo.original.mapinterfaces.CacheOverlayItemImpl;

import java.util.ArrayList;

public class cgMapOverlay extends ItemizedOverlayBase implements OverlayBase {

	private ArrayList<CacheOverlayItemImpl> items = new ArrayList<CacheOverlayItemImpl>();
	private Context context = null;
	private Boolean fromDetail = false;
	private ProgressDialog waitDialog = null;
	private Point center = new Point();
	private Point left = new Point();
	private Paint blockedCircle = null;
	private PaintFlagsDrawFilter setfil = null;
	private PaintFlagsDrawFilter remfil = null;
	private cgSettings settings;

	public cgMapOverlay(cgSettings settingsIn, ItemizedOverlayImpl ovlImpl, Context contextIn, Boolean fromDetailIn) {
		super(ovlImpl);

		populate();
		settings = settingsIn;

		context = contextIn;
		fromDetail = fromDetailIn;
	}

	public void updateItems(CacheOverlayItemImpl item) {
		ArrayList<CacheOverlayItemImpl> itemsPre = new ArrayList<CacheOverlayItemImpl>();
		itemsPre.add(item);

		updateItems(itemsPre);
	}

	public void updateItems(ArrayList<CacheOverlayItemImpl> itemsPre) {
		if (itemsPre == null) {
			return;
		}

		for (CacheOverlayItemImpl item : itemsPre) {
			item.setMarker(boundCenterBottom(item.getMarker(0)));
		}

		items = (ArrayList<CacheOverlayItemImpl>) itemsPre.clone();

		setLastFocusedItemIndex(-1); // to reset tap during data change
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapViewImpl mapView, boolean shadow) {
		super.draw(canvas, mapView, false);
	}

	@Override
	public boolean onTap(int index) {
		try {
			if (items.size() <= index) {
				return false;
			}

			if (waitDialog == null) {
				waitDialog = new ProgressDialog(context);
				waitDialog.setMessage("loading details...");
				waitDialog.setCancelable(false);
			}
			waitDialog.show();

			CacheOverlayItemImpl item = items.get(index);
			cgCoord coordinate = item.getCoord();

			if (coordinate.type != null && coordinate.type.equalsIgnoreCase("cache") == true && coordinate.geocode != null && coordinate.geocode.length() > 0) {
				Intent popupIntent = new Intent(context, cgeopopup.class);

				popupIntent.putExtra("fromdetail", fromDetail);
				popupIntent.putExtra("geocode", coordinate.geocode);

				context.startActivity(popupIntent);
			} else if (coordinate.type != null && coordinate.type.equalsIgnoreCase("waypoint") == true && coordinate.id != null && coordinate.id > 0) {
				Intent popupIntent = new Intent(context, cgeowaypoint.class);

				popupIntent.putExtra("waypoint", coordinate.id);
				popupIntent.putExtra("geocode", coordinate.geocode);

				context.startActivity(popupIntent);
			} else {
				waitDialog.dismiss();
				return false;
			}

			waitDialog.dismiss();
		} catch (Exception e) {
			Log.e(cgSettings.tag, "cgMapOverlay.onTap: " + e.toString());
		}

		return false;
	}

	@Override
	public CacheOverlayItemImpl createItem(int index) {
		try {
			return items.get(index);
		} catch (Exception e) {
			Log.e(cgSettings.tag, "cgMapOverlay.createItem: " + e.toString());
		}

		return null;
	}

	@Override
	public int size() {
		try {
			return items.size();
		} catch (Exception e) {
			Log.e(cgSettings.tag, "cgMapOverlay.size: " + e.toString());
		}

		return 0;
	}

	public void infoDialog(int index) {
		final CacheOverlayItemImpl item = items.get(index);
		final cgCoord coordinate = item.getCoord();

		if (coordinate == null) {
			Log.e(cgSettings.tag, "cgMapOverlay:infoDialog: No coordinates given");
			return;
		}

		try {
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setCancelable(true);

			if (coordinate.type.equalsIgnoreCase("cache")) {
				dialog.setTitle("cache");

				String cacheType;
				if (cgBase.cacheTypesInv.containsKey(coordinate.typeSpec) == true) {
					cacheType = cgBase.cacheTypesInv.get(coordinate.typeSpec);
				} else {
					cacheType = cgBase.cacheTypesInv.get("mystery");
				}

				dialog.setMessage(Html.fromHtml(item.getTitle()) + "\n\ngeocode: " + coordinate.geocode.toUpperCase() + "\ntype: " + cacheType);
				if (fromDetail == false) {
					dialog.setPositiveButton("detail", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int id) {
							Intent cachesIntent = new Intent(context, cgeodetail.class);
							cachesIntent.putExtra("geocode", coordinate.geocode.toUpperCase());
							context.startActivity(cachesIntent);

							dialog.cancel();
						}
					});
				} else {
					dialog.setPositiveButton("navigate", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int id) {
							cgeonavigate navigateActivity = new cgeonavigate();

							cgeonavigate.coordinates = new ArrayList<cgCoord>();
							cgeonavigate.coordinates.add(coordinate);

							Intent navigateIntent = new Intent(context, navigateActivity.getClass());
							navigateIntent.putExtra("latitude", coordinate.latitude);
							navigateIntent.putExtra("longitude", coordinate.longitude);
							navigateIntent.putExtra("geocode", coordinate.geocode.toUpperCase());
							context.startActivity(navigateIntent);
							dialog.cancel();
						}
					});
				}
			} else {
				dialog.setTitle("waypoint");

				String waypointType;
				if (cgBase.cacheTypesInv.containsKey(coordinate.typeSpec) == true) {
					waypointType = cgBase.waypointTypes.get(coordinate.typeSpec);
				} else {
					waypointType = cgBase.waypointTypes.get("waypoint");
				}

				dialog.setMessage(Html.fromHtml(item.getTitle()) + "\n\ntype: " + waypointType);
				dialog.setPositiveButton("navigate", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						cgeonavigate navigateActivity = new cgeonavigate();

						cgeonavigate.coordinates = new ArrayList<cgCoord>();
						cgeonavigate.coordinates.add(coordinate);

						Intent navigateIntent = new Intent(context, navigateActivity.getClass());
						navigateIntent.putExtra("latitude", coordinate.latitude);
						navigateIntent.putExtra("longitude", coordinate.longitude);
						navigateIntent.putExtra("geocode", coordinate.name);

						context.startActivity(navigateIntent);
						dialog.cancel();
					}
				});
			}

			dialog.setNegativeButton("dismiss", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});

			AlertDialog alert = dialog.create();
			alert.show();
		} catch (Exception e) {
			Log.e(cgSettings.tag, "cgMapOverlay.infoDialog: " + e.toString());
		}
	}
}
