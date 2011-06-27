package carnero.cgeo.original.comparators;

import java.util.Comparator;
import android.util.Log;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.libs.Settings;

public class CacheGeocodeComparator implements Comparator<Cache> {

	public int compare(Cache cache1, Cache cache2) {
		try {
			if (cache1.geocode == null || cache1.geocode.length() <= 0 || cache2.geocode == null || cache2.geocode.length() <= 0) {
				return 0;
			}
			
			if (cache1.geocode.length() > cache2.geocode.length()) {
				return 1;
			} else if (cache2.geocode.length() > cache1.geocode.length()) {
				return -1;
			} else {
				return cache1.geocode.compareToIgnoreCase(cache2.geocode);
			}
		} catch (Exception e) {
			Log.e(Settings.tag, "cgCacheGeocodeComparator.compare: " + e.toString());
		}
		return 0;
	}
}
