package ch.almana.android.unibas.perssearch.view;



import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import ch.almana.android.unibas.perssearch.R;
import ch.almana.android.unibas.perssearch.helper.MenuHelper;
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
		
		tvDescription.setLines(1);
		tvDescription.setEllipsize(TruncateAt.END);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuHelper.onCreateOptionsMenu(this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MenuHelper.onOptionsItemSelected(this, item);
		return super.onOptionsItemSelected(item);
	}
}
