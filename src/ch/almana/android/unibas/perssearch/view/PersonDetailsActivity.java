package ch.almana.android.unibas.perssearch.view;



import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import ch.almana.android.unibas.perssearch.R;
import ch.almana.android.unibas.perssearch.model.Person;

/**
 * Displays a word and its definition.
 */
public class PersonDetailsActivity extends ListActivity {
	
	public static final String EXTRA_ID = "id";
	private Person person;
	private TextView tvName;
	private TextView tvDescription;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.person_detail);


        Intent intent = getIntent();
		String personString = intent.getStringExtra(EXTRA_ID);

		person = new Person(personString);

		tvName = (TextView) findViewById(R.id.tvName);
		tvDescription = (TextView) findViewById(R.id.tvDescription);

		getListView().setAdapter(new PersonDetailAdapter(this, person));
		
		updateView();
    }

	private void updateView() {
		tvName.setText(person.getName());
		tvDescription.setText(person.getDesciption());
	}
}
