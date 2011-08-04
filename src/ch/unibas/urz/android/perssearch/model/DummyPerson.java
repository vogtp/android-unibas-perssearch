package ch.unibas.urz.android.perssearch.model;

import android.content.Context;
import ch.unibas.urz.android.perssearch.R;

public class DummyPerson extends Person {

	public enum DummyType {
		STAFF_TOO, STUD_TOO
	};

	private static DummyPerson PERSON_STUDS_TOO;
	private static DummyPerson PERSON_STAFF_TOO;
	private String query;
	private final String label;
	private final String desciption;

	public static DummyPerson getDummy(Context ctx, DummyType type) {
		switch (type) {
		case STAFF_TOO:
			if (PERSON_STAFF_TOO == null) {
				PERSON_STAFF_TOO = new DummyPerson(ctx.getString(R.string.label_staff_too), ctx.getString(R.string.label_search_all_included));
			}
			return PERSON_STAFF_TOO;
		case STUD_TOO:
			if (PERSON_STUDS_TOO == null) {
				PERSON_STUDS_TOO = new DummyPerson(ctx.getString(R.string.label_students_too), ctx.getString(R.string.label_search_all_included));
			}
			return PERSON_STUDS_TOO;

		default:
			// should never happen
			return new DummyPerson("", "");
		}
	}

	public DummyPerson(String label, String desciption) {
		super("{}");
		this.label = label;
		this.desciption = desciption;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	@Override
	public String getName() {
		return label;
	}

	@Override
	public String getDesciption() {
		return desciption;
	}
}
