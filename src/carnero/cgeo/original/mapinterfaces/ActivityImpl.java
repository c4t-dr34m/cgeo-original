package carnero.cgeo.original.mapinterfaces;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Defines the common functions of the provider-specific
 * MapActivity implementations.
 * @author rsudev
 *
 */
public interface ActivityImpl {

	Resources getResources();

	Activity getActivity();

	void superOnCreate(Bundle savedInstanceState);

	void superOnResume();

	void superOnDestroy();

	boolean superOnCreateOptionsMenu(Menu menu);

	boolean superOnPrepareOptionsMenu(Menu menu);

	boolean superOnOptionsItemSelected(MenuItem item);
	
	void switchMyLoc(View view);
	void goHome(View view);
}
