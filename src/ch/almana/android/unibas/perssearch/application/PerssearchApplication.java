package ch.almana.android.unibas.perssearch.application;

import android.app.Application;
import ch.almana.android.unibas.perssearch.helper.Settings;

public class PerssearchApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Settings.initInstance(this);
	}

}
