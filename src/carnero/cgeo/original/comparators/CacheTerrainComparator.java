package carnero.cgeo.original.comparators;

import java.util.Comparator;
import android.util.Log;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.libs.Settings;

public class CacheTerrainComparator implements Comparator<Cache> {

	public int compare(Cache cache1, Cache cache2) {
		try {
			if (cache1.terrain == null || cache2.terrain == null) {
				return 0;
			}
			
			if (cache1.terrain > cache2.terrain) {
				return 1;
			} else if (cache2.terrain > cache1.terrain) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			Log.e(Settings.tag, "cgCacheTerrainComparator.compare: " + e.toString());
		}
		return 0;
	}
}
