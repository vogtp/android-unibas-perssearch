package ch.almana.android.unibas.perssearch.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.widget.TextView;
import ch.almana.android.unibas.perssearch.R;
import ch.almana.android.unibas.perssearch.helper.Logger;
import ch.almana.android.unibas.perssearch.model.Person;

/**
 * Displays a word and its definition.
 */
public class PersonDetailsActivity extends Activity {
	
	public static final String EXTRA_ID = "id";
	private TextView tvName;
	private Person person;
	private TextView tvEmail;
	private TextView tvPhoneWork;
	private TextView tvAddress;
	private TextView tvDebug;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.person_detail);


        Intent intent = getIntent();
		String personString = intent.getStringExtra(EXTRA_ID);

		person = new Person(personString);

		tvName = (TextView) findViewById(R.id.tvName);
		tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvPhoneWork = (TextView) findViewById(R.id.tvPhoneWork);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvDebug = (TextView) findViewById(R.id.tvDebug);

		updateView();
    }

	private void updateView() {
		tvName.setText(person.getName());
		tvEmail.setText(person.getEmail());
		tvPhoneWork.setText(PhoneNumberUtils.formatNumber(person.getPhoneWork()));
		tvAddress.setText(person.getAddress());
		if (Logger.DEBUG) {
			tvDebug.setText(person.toString());
		} else {
			tvDebug.setText("");
		}
	}
}
