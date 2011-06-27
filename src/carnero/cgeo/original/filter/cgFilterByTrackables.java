package carnero.cgeo.original.filter;

import carnero.cgeo.original.cgCache;

public class cgFilterByTrackables extends cgFilter {

	@Override
	boolean applyFilter(cgCache cache) {
		return cache.hasTrackables();
	}

}
