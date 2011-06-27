package carnero.cgeo.original.comparators;

import java.util.Comparator;
import android.util.Log;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.libs.Settings;

public class CachePopularityComparator implements Comparator<Cache> {

	public int compare(Cache cache1, Cache cache2) {
		try {
			if (cache1.favouriteCnt == null || cache2.favouriteCnt == null) {
				return 0;
			}
			
			if (cache1.favouriteCnt < cache2.favouriteCnt) {
				return 1;
			} else if (cache2.favouriteCnt < cache1.favouriteCnt) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			Log.e(Settings.tag, "cgCachePopularityComparator.compare: " + e.toString());
		}
		return 0;
	}
}
