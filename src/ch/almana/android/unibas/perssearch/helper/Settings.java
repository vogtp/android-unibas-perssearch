package ch.almana.android.unibas.perssearch.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

	public enum SearchType {
		ALL, STAFF, STUDENTS
	}

	private static final String TYPE_STAFF = "1";
	private static final String TYPE_STUDENTS = "2";

	private static Settings instance;
	private Context ctx;

	public Settings(Context ctx) {
		super();
		this.ctx = ctx.getApplicationContext();
	}

	public static void initInstance(Context ctx) {
		if (instance == null) {
			instance = new Settings(ctx);
		}
	}

	public static Settings getInstance() {
		return instance;
	}

	protected SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(ctx);
	}

	public SearchType getSearchType() {
		String seachType = getPreferences().getString("prefKeySearchType", "0");
		if (TYPE_STAFF.equals(seachType)) {
			return SearchType.STAFF;
		} else if (TYPE_STUDENTS.equals(seachType)) {
			return SearchType.STUDENTS;
		}
		return SearchType.ALL;
	}

}
