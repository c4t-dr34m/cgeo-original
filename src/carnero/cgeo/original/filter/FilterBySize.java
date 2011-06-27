package carnero.cgeo.original.filter;

import carnero.cgeo.original.models.Cache;

public class FilterBySize extends Filter {
	private String size;

	public FilterBySize(String size){
		this.size = size;
	}
	
	@Override
	boolean applyFilter(Cache cache) {
		return cache.size.equals(size);
	}

}
