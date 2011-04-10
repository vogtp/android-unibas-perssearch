package ch.almana.android.unibas.perssearch.view;



import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ch.almana.android.unibas.perssearch.R;
import ch.almana.android.unibas.perssearch.helper.ContactAddHelper;
import ch.almana.android.unibas.perssearch.model.Person;

/**
 * Displays a word and its definition.
 */
public class PersonDetailsActivity extends ListActivity {
	
	public static final String EXTRA_ID = "id";
	private Person person;
	private TextView tvName;
	private TextView tvDescription;
	private Button buAddContact;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.person_detail);


        Intent intent = getIntent();
		String personString = intent.getStringExtra(EXTRA_ID);

		person = new Person(personString);

		tvName = (TextView) findViewById(R.id.tvName);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		buAddContact = (Button) findViewById(R.id.buAddContact);

		buAddContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContactAddHelper.addContact(PersonDetailsActivity.this, person);
			}
		});

		PersonDetailAdapter personDetailAdapter = new PersonDetailAdapter(this, person);
		getListView().setAdapter(personDetailAdapter);
		getListView().setOnItemClickListener(personDetailAdapter);
		updateView();
    }

	private void updateView() {
		tvName.setText(person.getName());
		tvDescription.setText(person.getDesciption());
	}

	public int getTextHeight() {
		return tvDescription.getHeight() - tvDescription.getPaddingTop() - tvDescription.getPaddingBottom();
	}
}
