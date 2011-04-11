package ch.almana.android.unibas.perssearch.model;

public class DummyPerson extends Person {

	public static final DummyPerson PERSON_STUDS_TOO = new DummyPerson("Show students too");
	public static final DummyPerson PERSON_STAFF_TOO = new DummyPerson("Show staff too");
	private String query;
	private String label;

	public DummyPerson(String label) {
		super("{}");
		this.label = label;
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
		return "Search again with all results included";
	}
}
