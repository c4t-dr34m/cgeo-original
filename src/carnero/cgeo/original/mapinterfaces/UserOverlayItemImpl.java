package carnero.cgeo.original.mapinterfaces;

import carnero.cgeo.original.models.User;

/**
 * Common functions of the provider-specific
 * UserOverlayItem implementations
 * @author rsudev
 *
 */
public interface UserOverlayItemImpl extends OverlayItemImpl {

	public User getUser();
}
