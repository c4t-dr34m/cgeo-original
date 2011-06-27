package carnero.cgeo.original.filter;

import java.util.ArrayList;
import java.util.List;

import carnero.cgeo.original.cgCache;

public abstract class cgFilter {
	abstract boolean applyFilter(cgCache cache);
	
	public void filter(List<cgCache> list){
		List<cgCache> itemsToRemove = new ArrayList<cgCache>();
		for(cgCache item : list){
			if(!applyFilter(item)){
				itemsToRemove.add(item);
			}
		}
		list.removeAll(itemsToRemove);
	}
}
