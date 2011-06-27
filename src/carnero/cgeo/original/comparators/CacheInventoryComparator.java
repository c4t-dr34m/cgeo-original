package carnero.cgeo.original.comparators;

import java.util.Comparator;
import android.util.Log;
import carnero.cgeo.original.models.Cache;
import carnero.cgeo.original.libs.Settings;

/**
 * compares by number of items in inventory
 * @author bananeweizen
 *
 */
public class CacheInventoryComparator implements Comparator<Cache> {

	public int compare(Cache cache1, Cache cache2) {
		try {
			int itemCount1 = 0;
			int itemCount2 = 0;
			if (cache1.difficulty != null) {
				itemCount1 = cache1.inventoryItems;
			}
			if (cache2.difficulty != null) {
				itemCount2 = cache2.inventoryItems;
			}

			if (itemCount1 < itemCount2) {
				return 1;
			} else if (itemCount2 < itemCount1) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			Log.e(Settings.tag, "cgCacheInventoryComparator.compare: " + e.toString());
		}
		return 0;
	}
}
