package carnero.cgeo.original.filter;

import java.util.ArrayList;
import java.util.List;

import carnero.cgeo.original.models.Cache;

public abstract class Filter {
	abstract boolean applyFilter(Cache cache);
	
	public void filter(List<Cache> list){
		List<Cache> itemsToRemove = new ArrayList<Cache>();
		for(Cache item : list){
			if(!applyFilter(item)){
				itemsToRemove.add(item);
			}
		}
		list.removeAll(itemsToRemove);
	}
}
