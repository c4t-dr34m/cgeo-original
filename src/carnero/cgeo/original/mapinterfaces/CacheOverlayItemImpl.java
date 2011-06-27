package carnero.cgeo.original.mapinterfaces;

import carnero.cgeo.original.models.Coord;

/**
 * Covers the common functions of the provider-specific
 * CacheOverlayItem implementations 
 * @author rsudev
 *
 */
public interface CacheOverlayItemImpl extends OverlayItemImpl {

	public Coord getCoord();
	
	public String getType();

}
