package carnero.cgeo.original.comparators;

import java.util.Comparator;
import android.util.Log;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.libs.Settings;

public class CacheDifficultyComparator implements Comparator<Cache> {

	public int compare(Cache cache1, Cache cache2) {
		try {
			if (cache1.difficulty == null || cache2.difficulty == null) {
				return 0;
			}
			
			if (cache1.difficulty > cache2.difficulty) {
				return 1;
			} else if (cache2.difficulty > cache1.difficulty) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			Log.e(Settings.tag, "cgCacheDifficultyComparator.compare: " + e.toString());
		}
		return 0;
	}
}
