package carnero.cgeo.original;

import carnero.cgeo.original.libs.App;
import carnero.cgeo.original.models.Waypoint;
import carnero.cgeo.original.models.Coord;
import carnero.cgeo.original.libs.Settings;
import carnero.cgeo.original.libs.Base;
import carnero.cgeo.original.libs.UpdateLoc;
import carnero.cgeo.original.libs.Geo;
import carnero.cgeo.original.libs.Warning;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Button;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import java.util.ArrayList;

public class waypointDetail extends Activity {

	private static final int MENU_ID_EXTERN = 23;
	private static final int MENU_ID_RMAPS = 21;
	private static final int MENU_ID_LOCUS = 20;
	private static final int MENU_ID_NAVIGATION = 0;
	private static final int MENU_ID_CACHES_AROUND = 5;
	private static final int MENU_ID_TURNBYTURN = 4;
	private static final int MENU_ID_MAP = 1;
	private static final int MENU_ID_RADAR = 3;
	private static final int MENU_ID_COMPASS = 2;
	private GoogleAnalyticsTracker tracker = null;
	private Waypoint waypoint = null;
	private String geocode = null;
	private int id = -1;
	private App app = null;
	private Resources res = null;
	private Activity activity = null;
	private Settings settings = null;
	private Base base = null;
	private Warning warning = null;
	private ProgressDialog waitDialog = null;
	private Geo geo = null;
	private UpdateLoc geoUpdate = new update();
	private Handler loadWaypointHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				if (waypoint == null) {
					if (waitDialog != null) {
						waitDialog.dismiss();
						waitDialog = null;
					}

					warning.showToast(res.getString(R.string.err_waypoint_load_failed));

					finish();
					return;
				} else {
					final TextView identification = (TextView) findViewById(R.id.identification);
					final TextView coords = (TextView) findViewById(R.id.coordinates);
					final TextView note = (TextView) findViewById(R.id.note);
					final ImageView compass = (ImageView) findViewById(R.id.compass);

					if (waypoint.name != null && waypoint.name.length() > 0) {
						base.setTitle(activity, Html.fromHtml(waypoint.name.trim()).toString());
					} else {
						base.setTitle(activity, res.getString(R.string.waypoint_title));
					}

					if (waypoint.prefix.equalsIgnoreCase("OWN") == false) {
						identification.setText(waypoint.prefix.trim() + "/" + waypoint.lookup.trim());
					} else {
						identification.setText(res.getString(R.string.waypoint_custom));
					}

					if (waypoint.latitude != null && waypoint.longitude != null) {
						coords.setText(Html.fromHtml(base.formatCoordinate(waypoint.latitude, "lat", true) + " | " + base.formatCoordinate(waypoint.longitude, "lon", true)), TextView.BufferType.SPANNABLE);
						compass.setVisibility(View.VISIBLE);
					} else {
						coords.setText(res.getString(R.string.waypoint_unknown_coordinates));
						compass.setVisibility(View.GONE);
					}

					if (waypoint.note != null && waypoint.note.length() > 0) {
						note.setText(Html.fromHtml(waypoint.note.trim()), TextView.BufferType.SPANNABLE);
					}

					Button buttonEdit = (Button) findViewById(R.id.edit);
					buttonEdit.setOnClickListener(new editWaypointListener(waypoint.id));

					Button buttonDelete = (Button) findViewById(R.id.delete);
					if (waypoint.type != null && waypoint.type.equalsIgnoreCase("own") == true) {
						buttonDelete.setOnClickListener(new deleteWaypointListener(waypoint.id));
						buttonDelete.setVisibility(View.VISIBLE);
					}

					if (waitDialog != null) {
						waitDialog.dismiss();
						waitDialog = null;
					}
				}
			} catch (Exception e) {
				if (waitDialog != null) {
					waitDialog.dismiss();
					waitDialog = null;
				}
				Log.e(Settings.tag, "cgeowaypoint.loadWaypointHandler: " + e.toString());
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// init
		activity = this;
		res = this.getResources();
		app = (App) this.getApplication();
		settings = new Settings(this, getSharedPreferences(Settings.preferences, 0));
		base = new Base(app, settings, getSharedPreferences(Settings.preferences, 0));
		warning = new Warning(this);

		// set layout
		if (settings.skin == 1) {
			setTheme(R.style.light);
		} else {
			setTheme(R.style.dark);
		}
		setContentView(R.layout.waypoint);
		base.setTitle(activity, "waypoint");

		// google analytics
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.start(Settings.analytics, this);
		tracker.dispatch();
		base.sendAnal(activity, tracker, "/waypoint/detail");

		// get parameters
		Bundle extras = getIntent().getExtras();

		// try to get data from extras
		if (extras != null) {
			id = extras.getInt("waypoint");
			geocode = extras.getString("geocode");
		}

		if (id <= 0) {
			warning.showToast(res.getString(R.string.err_waypoint_unknown));
			finish();
			return;
		}

		if (geo == null) {
			geo = app.startGeo(activity, geoUpdate, base, settings, warning, 0, 0);
		}

		waitDialog = ProgressDialog.show(this, null, res.getString(R.string.waypoint_loading), true);
		waitDialog.setCancelable(true);

		(new loadWaypoint()).start();
	}

	@Override
	public void onResume() {
		super.onResume();

		settings.load();

		if (geo == null) {
			geo = app.startGeo(activity, geoUpdate, base, settings, warning, 0, 0);
		}

		if (waitDialog == null) {
			waitDialog = ProgressDialog.show(this, null, res.getString(R.string.waypoint_loading), true);
			waitDialog.setCancelable(true);

			(new loadWaypoint()).start();
		}
	}

	@Override
	public void onDestroy() {
		if (geo != null) {
			geo = app.removeGeo();
		}
		if (tracker != null) {
			tracker.stop();
		}

		super.onDestroy();
	}

	@Override
	public void onStop() {
		if (geo != null) {
			geo = app.removeGeo();
		}

		super.onStop();
	}

	@Override
	public void onPause() {
		if (geo != null) {
			geo = app.removeGeo();
		}

		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ID_COMPASS, 0, res.getString(R.string.cache_menu_compass)).setIcon(android.R.drawable.ic_menu_compass); // compass

		SubMenu subMenu = menu.addSubMenu(1, MENU_ID_NAVIGATION, 0, res.getString(R.string.cache_menu_navigate)).setIcon(android.R.drawable.ic_menu_more);
		subMenu.add(0, MENU_ID_RADAR, 0, res.getString(R.string.cache_menu_radar)); // radar
		subMenu.add(0, MENU_ID_MAP, 0, res.getString(R.string.cache_menu_map)); // c:geo map
		if (base.isLocus(activity)) {
			subMenu.add(0, MENU_ID_LOCUS, 0, res.getString(R.string.cache_menu_locus)); // ext.: locus
		}
		if (base.isRmaps(activity)) {
			subMenu.add(0, MENU_ID_RMAPS, 0, res.getString(R.string.cache_menu_rmaps)); // ext.: rmaps
		}
		subMenu.add(0, MENU_ID_EXTERN, 0, res.getString(R.string.cache_menu_map_ext)); // ext.: other
		subMenu.add(0, MENU_ID_TURNBYTURN, 0, res.getString(R.string.cache_menu_tbt)); // turn-by-turn

		menu.add(0, MENU_ID_CACHES_AROUND, 0, res.getString(R.string.cache_menu_around)).setIcon(android.R.drawable.ic_menu_rotate); // cacheList around

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		try {
			boolean visible = waypoint != null && waypoint.latitude != null && waypoint.longitude != null;
			menu.findItem(MENU_ID_NAVIGATION).setVisible(visible);
			menu.findItem(MENU_ID_COMPASS).setVisible(visible);
			menu.findItem(MENU_ID_CACHES_AROUND).setVisible(visible);
		} catch (Exception e) {
			// nothing
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int menuItem = item.getItemId();

		if (menuItem == MENU_ID_MAP) {
			showOnMap();
			return true;
		} else if (menuItem == MENU_ID_COMPASS) {
			goCompass(null);
			return true;
		} else if (menuItem == MENU_ID_RADAR) {
			radarTo();
			return true;
		} else if (menuItem == MENU_ID_TURNBYTURN) {
			if (geo != null) {
				base.runNavigation(activity, res, settings, warning, tracker, waypoint.latitude, waypoint.longitude, geo.latitudeNow, geo.longitudeNow);
			} else {
				base.runNavigation(activity, res, settings, warning, tracker, waypoint.latitude, waypoint.longitude);
			}

			return true;
		} else if (menuItem == MENU_ID_CACHES_AROUND) {
			cachesAround();
			return true;
		} else if (menuItem == MENU_ID_LOCUS) {
			base.runExternalMap(Base.mapAppLocus, activity, res, warning, tracker, waypoint); // locus
			return true;
		} else if (menuItem == MENU_ID_RMAPS) {
			base.runExternalMap(Base.mapAppRmaps, activity, res, warning, tracker, waypoint); // rmaps
			return true;
		} else if (menuItem == MENU_ID_EXTERN) {
			base.runExternalMap(Base.mapAppAny, activity, res, warning, tracker, waypoint); // extern
			return true;
		}

		return false;
	}

	private void showOnMap() {
		if (waypoint == null || waypoint.latitude == null || waypoint.longitude == null) {
			warning.showToast(res.getString(R.string.err_location_unknown));
		}

		Intent mapIntent = new Intent(activity, settings.getMapFactory().getMapClass());
		mapIntent.putExtra("latitude", waypoint.latitude);
		mapIntent.putExtra("longitude", waypoint.longitude);
		mapIntent.putExtra("wpttype", waypoint.type);

		activity.startActivity(mapIntent);
	}

	private void radarTo() {
		if (waypoint == null || waypoint.latitude == null || waypoint.longitude == null) {
			warning.showToast(res.getString(R.string.err_location_unknown));
		}

		try {
			if (Base.isIntentAvailable(activity, "com.google.android.radar.SHOW_RADAR") == true) {
				Intent radarIntent = new Intent("com.google.android.radar.SHOW_RADAR");
				radarIntent.putExtra("latitude", new Float(waypoint.latitude));
				radarIntent.putExtra("longitude", new Float(waypoint.longitude));
				activity.startActivity(radarIntent);
			} else {
				AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
				dialog.setTitle(res.getString(R.string.err_radar_title));
				dialog.setMessage(res.getString(R.string.err_radar_message));
				dialog.setCancelable(true);
				dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						try {
							activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.eclipsim.gpsstatus2")));
							dialog.cancel();
						} catch (Exception e) {
							warning.showToast(res.getString(R.string.err_radar_market));
							Log.e(Settings.tag, "cgeowaypoint.radarTo.onClick: " + e.toString());
						}
					}
				});
				dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				AlertDialog alert = dialog.create();
				alert.show();
			}
		} catch (Exception e) {
			warning.showToast(res.getString(R.string.err_radar_generic));
			Log.e(Settings.tag, "cgeowaypoint.radarTo: " + e.toString());
		}
	}

	private void cachesAround() {
		if (waypoint == null || waypoint.latitude == null || waypoint.longitude == null) {
			warning.showToast(res.getString(R.string.err_location_unknown));
		}

		cacheList cachesActivity = new cacheList();

		Intent cachesIntent = new Intent(activity, cachesActivity.getClass());
		cachesIntent.putExtra("type", "coordinate");
		cachesIntent.putExtra("latitude", waypoint.latitude);
		cachesIntent.putExtra("longitude", waypoint.longitude);
		cachesIntent.putExtra("cachetype", settings.cacheType);

		activity.startActivity(cachesIntent);

		finish();
	}

	private class loadWaypoint extends Thread {

		@Override
		public void run() {
			try {
				waypoint = app.loadWaypoint(id);

				loadWaypointHandler.sendMessage(new Message());
			} catch (Exception e) {
				Log.e(Settings.tag, "cgeowaypoint.loadWaypoint.run: " + e.toString());
			}
		}
	}

	private class update extends UpdateLoc {

		@Override
		public void updateLoc(Geo geo) {
			// nothing
		}
	}

	private class editWaypointListener implements View.OnClickListener {

		private int id = -1;

		public editWaypointListener(int idIn) {
			id = idIn;
		}

		public void onClick(View arg0) {
			Intent editIntent = new Intent(activity, waypointNew.class);
			editIntent.putExtra("waypoint", id);
			activity.startActivity(editIntent);
		}
	}

	private class deleteWaypointListener implements View.OnClickListener {

		private Integer id = null;

		public deleteWaypointListener(int idIn) {
			id = idIn;
		}

		public void onClick(View arg0) {
			if (app.deleteWaypoint(id) == false) {
				warning.showToast(res.getString(R.string.err_waypoint_delete_failed));
			} else {
				app.removeCacheFromCache(geocode);

				finish();
				return;
			}
		}
	}

	public void goHome(View view) {
		base.goHome(activity);
	}

	public void goCompass(View view) {
		if (waypoint == null || waypoint.latitude == null || waypoint.longitude == null) {
			warning.showToast(res.getString(R.string.err_location_unknown));
		}

		Intent navigateIntent = new Intent(activity, navigate.class);
		navigateIntent.putExtra("latitude", waypoint.latitude);
		navigateIntent.putExtra("longitude", waypoint.longitude);
		navigateIntent.putExtra("geocode", waypoint.prefix.trim() + "/" + waypoint.lookup.trim());
		navigateIntent.putExtra("name", waypoint.name);

		if (navigate.coordinates != null) {
			navigate.coordinates.clear();
		}
		navigate.coordinates = new ArrayList<Coord>();
		navigate.coordinates.add(new Coord(waypoint));
		activity.startActivity(navigateIntent);
	}
}