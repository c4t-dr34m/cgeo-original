package carnero.cgeo.original.comparators;

import java.util.Comparator;
import android.util.Log;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.libs.Settings;

public class CacheVisitComparator implements Comparator<Cache> {

	public int compare(Cache cache1, Cache cache2) {
		try {
			if (cache1.visitedDate == null || cache1.visitedDate <= 0 || cache2.visitedDate == null || cache2.visitedDate <= 0) {
				return 0;
			}

			if (cache1.visitedDate > cache2.visitedDate) {
				return -1;
			} else if (cache1.visitedDate < cache2.visitedDate) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			Log.e(Settings.tag, "cgCacheVisitComparator.compare: " + e.toString());
		}

		return 0;
	}
}
