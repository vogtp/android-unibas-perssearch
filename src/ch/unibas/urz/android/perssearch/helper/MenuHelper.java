package ch.unibas.urz.android.perssearch.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import ch.unibas.urz.android.perssearch.R;
import ch.unibas.urz.android.perssearch.view.preferences.SettingsActivity;

public class MenuHelper {

	public static boolean onOptionsItemSelected(Activity act, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemSearch:
			act.onSearchRequested();
			return true;
		case R.id.itemSettings:
			Intent i = new Intent(act, SettingsActivity.class);
			act.startActivity(i);
			return true;

		default:
			return false;

		}
	}

	public static void onCreateOptionsMenu(Activity act, Menu menu) {
		act.getMenuInflater().inflate(R.menu.options_general, menu);
	}

}
