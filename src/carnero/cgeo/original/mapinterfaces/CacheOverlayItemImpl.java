package carnero.cgeo.original.mapinterfaces;

import carnero.cgeo.original.cgCoord;

/**
 * Covers the common functions of the provider-specific
 * CacheOverlayItem implementations 
 * @author rsudev
 *
 */
public interface CacheOverlayItemImpl extends OverlayItemImpl {

	public cgCoord getCoord();
	
	public String getType();

}
