package carnero.cgeo.original.comparators;

import java.util.Comparator;
import android.util.Log;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.libs.Settings;

public class CacheNameComparator implements Comparator<Cache> {

	public int compare(Cache cache1, Cache cache2) {
		try {
			if (cache1.name == null || cache2.name == null) {
				return 0;
			}
			
			return cache1.name.compareToIgnoreCase(cache2.name);
		} catch (Exception e) {
			Log.e(Settings.tag, "cgCacheNameComparator.compare: " + e.toString());
		}
		return 0;
	}
}
