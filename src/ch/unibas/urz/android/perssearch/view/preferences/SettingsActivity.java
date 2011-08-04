package ch.unibas.urz.android.perssearch.view.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import ch.unibas.urz.android.perssearch.R;

public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
	}
}
