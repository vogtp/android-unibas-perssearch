package ch.almana.android.unibas.perssearch.view;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import ch.almana.android.unibas.perssearch.R;

public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
	}
}
