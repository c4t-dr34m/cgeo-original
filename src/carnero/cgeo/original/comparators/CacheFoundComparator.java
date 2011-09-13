package carnero.cgeo.original.comparators;

import java.util.Comparator;
import android.util.Log;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.libs.Settings;

public class CacheFoundComparator implements Comparator<Cache> {

	public int compare(Cache cache1, Cache cache2) {
		try {
			// 1: 1..2
			// 0: stejne
			// -1: 2..1

			int rating1 = 0;
			int rating2 = 0;
			
			if (cache1.logOffline) {
				rating1 = 1;
			}
			if (cache2.logOffline) {
				rating2 = 1;
			}
			
			if (cache1.found) {
				rating1 = 5;
			}
			if (cache2.found) {
				rating2 = 5;
			}
			
			if (rating1 > rating2) {
				return 1;
			} else if (rating1 < rating2) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			Log.e(Settings.tag, "cgCacheFoundComparator.compare: " + e.toString());
		}
		return 0;
	}
}
