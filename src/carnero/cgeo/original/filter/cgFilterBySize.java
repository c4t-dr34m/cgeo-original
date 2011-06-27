package carnero.cgeo.original.filter;

import carnero.cgeo.original.cgCache;

public class cgFilterBySize extends cgFilter {
	private String size;

	public cgFilterBySize(String size){
		this.size = size;
	}
	
	@Override
	boolean applyFilter(cgCache cache) {
		return cache.size.equals(size);
	}

}
