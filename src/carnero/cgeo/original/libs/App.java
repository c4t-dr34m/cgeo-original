package carnero.cgeo.original.libs;

import carnero.cgeo.original.models.Waypoint;
import carnero.cgeo.original.models.Search;
import carnero.cgeo.original.models.Trackable;
import carnero.cgeo.original.models.Spoiler;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.models.CachesList;
import carnero.cgeo.original.models.CacheLog;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class App extends Application {

	private Data storage = null;
	private String action = null;
	private Double lastLatitude = null;
	private Double lastLongitude = null;
	private Geo geo = null;
	private boolean geoInUse = false;
	private Direction dir = null;
	private boolean dirInUse = false;
	final private HashMap<Long, Search> searches = new HashMap<Long, Search>(); // information about searches
	final private HashMap<String, Cache> cachesCache = new HashMap<String, Cache>(); // caching caches into memory
	public boolean firstRun = true; // c:geo is just launched
	public boolean warnedLanguage = false; // user was warned about different language settings on geocaching.com
	private boolean databaseCleaned = false; // database was cleaned

	public App() {
		if (storage == null) {
			storage = new Data(this);
		}
	}

	@Override
	public void onLowMemory() {
		Log.i(Settings.tag, "Cleaning applications cache.");

		cachesCache.clear();
	}

	@Override
	public void onTerminate() {
		Log.d(Settings.tag, "Terminating c:geo...");

		if (geo != null) {
			geo.closeGeo();
			geo = null;
		}

		if (dir != null) {
			dir.closeDir();
			dir = null;
		}

		if (storage != null) {
			storage.clean();
			storage.closeDb();
			storage = null;
		}

		super.onTerminate();
	}

	public String backupDatabase() {
		return storage.backupDatabase();
	}

	public File isRestoreFile() {
		return storage.isRestoreFile();
	}

	public boolean restoreDatabase() {
		return storage.restoreDatabase();
	}

	public void cleanGeo() {
		if (geo != null) {
			geo.closeGeo();
			geo = null;
		}
	}

	public void cleanDir() {
		if (dir != null) {
			dir.closeDir();
			dir = null;
		}
	}

	public boolean storageStatus() {
		if (storage.status() == false) {
			return false;
		}

		return true;
	}

	public Geo startGeo(Context context, UpdateLoc geoUpdate, Base base, Settings settings, Warning warning, int time, int distance) {
		if (geo == null) {
			geo = new Geo(context, this, geoUpdate, base, settings, warning, time, distance);
			geo.initGeo();

			Log.i(Settings.tag, "Location service started");
		}

		geo.replaceUpdate(geoUpdate);
		geoInUse = true;

		return geo;
	}

	public Geo removeGeo() {
		if (geo != null) {
			geo.replaceUpdate(null);
		}
		geoInUse = false;

		(new removeGeoThread()).start();

		return null;
	}

	private class removeGeoThread extends Thread {

		@Override
		public void run() {
			try {
				sleep(2500);
			} catch (Exception e) {
				// nothing
			}

			if (geoInUse == false && geo != null) {
				geo.closeGeo();
				geo = null;

				Log.i(Settings.tag, "Location service stopped");
			}
		}
	}

	public Direction startDir(Context context, UpdateDir dirUpdate, Warning warning) {
		if (dir == null) {
			dir = new Direction(this, context, dirUpdate, warning);
			dir.initDir();

			Log.i(Settings.tag, "Direction service started");
		}

		dir.replaceUpdate(dirUpdate);
		dirInUse = true;

		return dir;
	}

	public Direction removeDir() {
		if (dir != null) {
			dir.replaceUpdate(null);
		}
		dirInUse = false;

		(new removeDirThread()).start();

		return null;
	}

	private class removeDirThread extends Thread {

		@Override
		public void run() {
			try {
				sleep(2500);
			} catch (Exception e) {
				// nothing
			}

			if (dirInUse == false && dir != null) {
				dir.closeDir();
				dir = null;

				Log.i(Settings.tag, "Direction service stopped");
			}
		}
	}

	public void cleanDatabase(boolean more) {
		if (databaseCleaned == true) {
			return;
		}

		if (storage == null) {
			storage = new Data(this);
		}
		storage.clean(more);
		databaseCleaned = true;
	}

	public Boolean isThere(String geocode, String guid, boolean detailed, boolean checkTime) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.isThere(geocode, guid, detailed, checkTime);
	}

	public Boolean isOffline(String geocode, String guid) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.isOffline(geocode, guid);
	}

	public String getGeocode(String guid) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.getGeocodeForGuid(guid);
	}

	public String getCacheid(String geocode) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.getCacheidForGeocode(geocode);
	}

	public String getError(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return null;
		}

		return searches.get(searchId).error;
	}

	public boolean setError(Long searchId, String error) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return false;
		}

		searches.get(searchId).error = error;

		return true;
	}

	public String getUrl(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return null;
		}

		return searches.get(searchId).url;
	}

	public boolean setUrl(Long searchId, String url) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return false;
		}

		searches.get(searchId).url = url;

		return true;
	}

	public String getViewstate(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return null;
		}

		return searches.get(searchId).viewstate;
	}

	public String getViewstate1(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return null;
		}

		return searches.get(searchId).viewstate1;
	}

	public boolean setViewstate(Long searchId, String viewstate) {
		if (viewstate == null || viewstate.length() == 0) {
			return false;
		}
		if (searchId == null || searches.containsKey(searchId) == false) {
			return false;
		}

		searches.get(searchId).viewstate = viewstate;

		return true;
	}

	public boolean setViewstate1(Long searchId, String viewstate1) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return false;
		}

		searches.get(searchId).viewstate1 = viewstate1;

		return true;
	}

	public Integer getTotal(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return null;
		}

		return searches.get(searchId).totalCnt;
	}

	public Integer getCount(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return 0;
		}

		return searches.get(searchId).getCount();
	}

	public Integer getNotOfflineCount(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return 0;
		}

		int count = 0;
		ArrayList<String> geocodes = searches.get(searchId).getGeocodes();
		if (geocodes != null) {
			for (String geocode : geocodes) {
				if (isOffline(geocode, null) == false) {
					count++;
				}
			}
		}

		return count;
	}

	public Cache getCacheByGeocode(String geocode) {
		return getCacheByGeocode(geocode, false, true, false, false, false, false);
	}

	public Cache getCacheByGeocode(String geocode, boolean loadA, boolean loadW, boolean loadS, boolean loadL, boolean loadI, boolean loadO) {
		if (geocode == null || geocode.length() == 0) {
			return null;
		}

		Cache cache = null;
		if (cachesCache.containsKey(geocode) == true) {
			cache = cachesCache.get(geocode);
		} else {
			if (storage == null) {
				storage = new Data(this);
			}
			cache = storage.loadCache(geocode, null, loadA, loadW, loadS, loadL, loadI, loadO);

			if (cache != null && cache.detailed == true && loadA == true && loadW == true && loadS == true && loadL == true && loadI == true) {
				putCacheInCache(cache);
			}
		}

		return cache;
	}

	public Trackable getTrackableByGeocode(String geocode) {
		if (geocode == null || geocode.length() == 0) {
			return null;
		}

		Trackable trackable = null;
		trackable = storage.loadTrackable(geocode);

		return trackable;
	}

	public void removeCacheFromCache(String geocode) {
		if (geocode != null && cachesCache.containsKey(geocode) == true) {
			cachesCache.remove(geocode);
		}
	}

	public void putCacheInCache(Cache cache) {
		if (cache == null || cache.geocode == null) {
			return;
		}

		if (cachesCache.containsKey(cache.geocode) == true) {
			cachesCache.remove(cache.geocode);
		}

		cachesCache.put(cache.geocode, cache);
	}

	public String[] geocodesInCache() {
		if (storage == null) {
			storage = new Data(this);
		}

		return storage.allDetailedThere();
	}

	public Waypoint getWaypointById(Integer id) {
		if (id == null || id == 0) {
			return null;
		}

		if (storage == null) {
			storage = new Data(this);
		}
		return storage.loadWaypoint(id);
	}

	public ArrayList<Object> getBounds(String geocode) {
		if (geocode == null) {
			return null;
		}
		
		List<String> geocodeList = new ArrayList<String>();
		geocodeList.add(geocode);
		
		return getBounds(geocodeList);
	}
		
	public ArrayList<Object> getBounds(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return null;
		}
		
		if (storage == null) {
			storage = new Data(this);
		}
		
		final Search search = searches.get(searchId);
		final ArrayList<String> geocodeList = search.getGeocodes();
		
		return getBounds(geocodeList);
	}
	
	public ArrayList<Object> getBounds(List<String> geocodes) {
		if (geocodes == null || geocodes.isEmpty()) {
			return null;
		}
		
		if (storage == null) {
			storage = new Data(this);
		}
		
		return storage.getBounds(geocodes.toArray());
	}

	public Cache getCache(Long searchId) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			return null;
		}

		Search search = searches.get(searchId);
		ArrayList<String> geocodeList = search.getGeocodes();

		return getCacheByGeocode(geocodeList.get(0), true, true, true, true, true, true);
	}

	public ArrayList<Cache> getCaches(Long searchId) {
		return getCaches(searchId, null, null, null, null, false, true, false, false, false, true);
	}

	public ArrayList<Cache> getCaches(Long searchId, boolean loadA, boolean loadW, boolean loadS, boolean loadL, boolean loadI, boolean loadO) {
		return getCaches(searchId, null, null, null, null, loadA, loadW, loadS, loadL, loadI, loadO);
	}
		
	public ArrayList<Cache> getCaches(Long searchId, Long centerLat, Long centerLon, Long spanLat, Long spanLon) {
		return getCaches(searchId, centerLat, centerLon, spanLat, spanLon, false, true, false, false, false, true);
	}

	public ArrayList<Cache> getCaches(Long searchId, Long centerLat, Long centerLon, Long spanLat, Long spanLon, boolean loadA, boolean loadW, boolean loadS, boolean loadL, boolean loadI, boolean loadO) {
		if (searchId == null || searches.containsKey(searchId) == false) {
			ArrayList<Cache> cachesOut = new ArrayList<Cache>();
			
			final ArrayList<Cache> cachesPre = storage.loadCaches(null , null, centerLat, centerLon, spanLat, spanLon, loadA, loadW, loadS, loadL, loadI, loadO);
			
			if (cachesPre != null) {
				cachesOut.addAll(cachesPre);
			}
			
			return cachesOut; 
		}

		ArrayList<Cache> cachesOut = new ArrayList<Cache>();

		Search search = searches.get(searchId);
		ArrayList<String> geocodeList = search.getGeocodes();
		
		if (storage == null) {
			storage = new Data(this);
		}
		
		final ArrayList<Cache> cachesPre = storage.loadCaches(geocodeList.toArray(), null, centerLat, centerLon, spanLat, spanLon, loadA, loadW, loadS, loadL, loadI, loadO);
		if (cachesPre != null) {
			cachesOut.addAll(cachesPre);
		}

		return cachesOut;
	}

	public Search getBatchOfStoredCaches(boolean detailedOnly, Double latitude, Double longitude, String cachetype, int list) {
		if (storage == null) {
			storage = new Data(this);
		}
		Search search = new Search();

		ArrayList<String> geocodes = storage.loadBatchOfStoredGeocodes(detailedOnly, latitude, longitude, cachetype, list);
		if (geocodes != null && geocodes.isEmpty() == false) {
			for (String gccode : geocodes) {
				search.addGeocode(gccode);
			}
		}
		searches.put(search.getCurrentId(), search);

		return search;
	}

	public Search getHistoryOfCaches(boolean detailedOnly, String cachetype) {
		if (storage == null) {
			storage = new Data(this);
		}
		Search search = new Search();

		ArrayList<String> geocodes = storage.loadBatchOfHistoricGeocodes(detailedOnly, cachetype);
		if (geocodes != null && geocodes.isEmpty() == false) {
			for (String gccode : geocodes) {
				search.addGeocode(gccode);
			}
		}
		searches.put(search.getCurrentId(), search);

		return search;
	}

	public Long getCachedInViewport(Long centerLat, Long centerLon, Long spanLat, Long spanLon, String cachetype) {
		if (storage == null) {
			storage = new Data(this);
		}
		Search search = new Search();

		ArrayList<String> geocodes = storage.getCachedInViewport(centerLat, centerLon, spanLat, spanLon, cachetype);
		if (geocodes != null && geocodes.isEmpty() == false) {
			for (String gccode : geocodes) {
				search.addGeocode(gccode);
			}
		}
		searches.put(search.getCurrentId(), search);

		return search.getCurrentId();
	}

	public Long getStoredInViewport(Long centerLat, Long centerLon, Long spanLat, Long spanLon, String cachetype) {
		if (storage == null) {
			storage = new Data(this);
		}
		Search search = new Search();

		ArrayList<String> geocodes = storage.getStoredInViewport(centerLat, centerLon, spanLat, spanLon, cachetype);
		if (geocodes != null && geocodes.isEmpty() == false) {
			for (String gccode : geocodes) {
				search.addGeocode(gccode);
			}
		}
		searches.put(search.getCurrentId(), search);

		return search.getCurrentId();
	}

	public Long getOfflineAll(String cachetype) {
		if (storage == null) {
			storage = new Data(this);
		}
		Search search = new Search();

		ArrayList<String> geocodes = storage.getOfflineAll(cachetype);
		if (geocodes != null && geocodes.isEmpty() == false) {
			for (String gccode : geocodes) {
				search.addGeocode(gccode);
			}
		}
		searches.put(search.getCurrentId(), search);

		return search.getCurrentId();
	}

	public int getAllStoredCachesCount(boolean detailedOnly, String cachetype, Integer list) {
		if (storage == null) {
			storage = new Data(this);
		}

		return storage.getAllStoredCachesCount(detailedOnly, cachetype, list);
	}

	public int getAllHistoricCachesCount(boolean detailedOnly, String cachetype) {
		if (storage == null) {
			storage = new Data(this);
		}

		return storage.getAllHistoricCachesCount(detailedOnly, cachetype);
	}

	public void markStored(String geocode, int listId) {
		if (storage == null) {
			storage = new Data(this);
		}
		storage.markStored(geocode, listId);
	}

	public boolean markDropped(String geocode) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.markDropped(geocode);
	}

	public boolean markFound(String geocode) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.markFound(geocode);
	}

	public boolean saveWaypoints(String geocode, ArrayList<Waypoint> waypoints, boolean drop) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.saveWaypoints(geocode, waypoints, drop);
	}

	public boolean saveOwnWaypoint(int id, String geocode, Waypoint waypoint) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.saveOwnWaypoint(id, geocode, waypoint);
	}

	public boolean deleteWaypoint(int id) {
		if (storage == null) {
			storage = new Data(this);
		}
		return storage.deleteWaypoint(id);
	}

	public boolean saveTrackable(Trackable trackable) {
		if (storage == null) {
			storage = new Data(this);
		}

		final ArrayList<Trackable> list = new ArrayList<Trackable>();
		list.add(trackable);

		return storage.saveInventory("---", list);
	}

	public void addGeocode(Long searchId, String geocode) {
		if (this.searches.containsKey(searchId) == false || geocode == null || geocode.length() == 0) {
			return;
		}

		this.searches.get(searchId).addGeocode(geocode);
	}

	public Long addSearch(Long searchId, ArrayList<Cache> cacheList, Boolean newItem, int reason) {
		if (this.searches.containsKey(searchId) == false) {
			return null;
		}

		Search search = this.searches.get(searchId);

		return addSearch(search, cacheList, newItem, reason);
	}

	public Long addSearch(Search search, ArrayList<Cache> cacheList, Boolean newItem, int reason) {
		if (cacheList == null || cacheList.isEmpty()) {
			return null;
		}

		final long searchId = search.getCurrentId();
		searches.put(searchId, search);

		if (storage == null) {
			storage = new Data(this);
		}
		if (newItem == true) {
			// save only newly downloaded data
			for (Cache oneCache : cacheList) {
				String oneGeocode = oneCache.geocode.toUpperCase();
				String oneGuid = oneCache.guid.toLowerCase();

				oneCache.reason = reason;

				if (storage.isThere(oneGeocode, oneGuid, false, false) == false || reason >= 1) {
					// cache is not saved, new data are for storing
					storage.saveCache(oneCache);
				} else {
					Cache mergedCache = oneCache.merge(storage);

					storage.saveCache(mergedCache);
				}
			}
		}

		return searchId;
	}

	public boolean addCacheToSearch(Search search, Cache cache) {
		if (search == null || cache == null) {
			return false;
		}

		final long searchId = search.getCurrentId();

		if (searches.containsKey(searchId) == false) {
			searches.put(searchId, search);
		}

		String geocode = cache.geocode.toUpperCase();
		String guid = cache.guid.toLowerCase();

		boolean status = false;
		
		if (storage.isThere(geocode, guid, false, false) == false || cache.reason >= 1) { // if for offline, do not merge
			status = storage.saveCache(cache);
		} else {
			Cache mergedCache = cache.merge(storage);

			status = storage.saveCache(mergedCache);
		}

		if (status == true) {
			search.addGeocode(cache.geocode);
		}

		return status;
	}

	public void dropStored(int listId) {
		if (storage == null) {
			storage = new Data(this);
		}
		storage.dropStored(listId);
	}

	public ArrayList<Trackable> loadInventory(String geocode) {
		return storage.loadInventory(geocode);
	}

	public ArrayList<Spoiler> loadSpoilers(String geocode) {
		return storage.loadSpoilers(geocode);
	}

	public Waypoint loadWaypoint(int id) {
		return storage.loadWaypoint(id);
	}

	public void setAction(String act) {
		action = act;
	}

	public String getAction() {
		if (action == null) {
			return "";
		}
		return action;
	}

	public boolean addLog(String geocode, CacheLog log) {
		if (geocode == null || geocode.length() == 0) {
			return false;
		}
		if (log == null) {
			return false;
		}

		ArrayList<CacheLog> list = new ArrayList<CacheLog>();
		list.add(log);

		return storage.saveLogs(geocode, list, false);
	}

	public void setLastLoc(Double lat, Double lon) {
		lastLatitude = lat;
		lastLongitude = lon;
	}

	public Double getLastLat() {
		return lastLatitude;
	}

	public Double getLastLon() {
		return lastLongitude;
	}

	public boolean saveLogOffline(String geocode, Date date, int logtype, String log) {
		return storage.saveLogOffline(geocode, date, logtype, log);
	}

	public CacheLog loadLogOffline(String geocode) {
		return storage.loadLogOffline(geocode);
	}

	public void clearLogOffline(String geocode) {
		storage.clearLogOffline(geocode);
	}

	public void saveVisitDate(String geocode) {
		storage.saveVisitDate(geocode);
	}

	public ArrayList<CachesList> getLists() {
		return storage.getLists(getResources());
	}
	
	public CachesList getList(int id) {
		return storage.getList(id, getResources());
	}
	
	public int createList(String title) {
		return storage.createList(title);
	}
	
	public boolean removeList(int id) {
		return storage.removeList(id);
	}
	
	public void moveToList(String geocode, int listId) {
		storage.moveToList(geocode, listId);
	}
}
