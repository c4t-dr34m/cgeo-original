package carnero.cgeo.original.filter;

import carnero.cgeo.original.models.Cache;

public class FilterByTrackables extends Filter {

	@Override
	boolean applyFilter(Cache cache) {
		return cache.hasTrackables();
	}

}
