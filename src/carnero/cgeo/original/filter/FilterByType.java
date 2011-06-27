package carnero.cgeo.original.filter;

import carnero.cgeo.original.models.Cache;

public class FilterByType extends Filter {	
	private String type;

	public FilterByType(String type){
		this.type = type;
	}
	
	@Override
	boolean applyFilter(Cache cache) {
		return cache.type.equals(type);
	}

}
