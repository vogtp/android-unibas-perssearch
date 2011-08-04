package ch.unibas.urz.android.perssearch.application;

import android.app.Application;
import ch.unibas.urz.android.perssearch.helper.Settings;

public class PerssearchApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Settings.initInstance(this);
	}

}
