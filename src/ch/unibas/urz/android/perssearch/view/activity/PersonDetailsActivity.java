package ch.unibas.urz.android.perssearch.view.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import ch.unibas.urz.android.perssearch.R;
import ch.unibas.urz.android.perssearch.contacts.ContactAdderVcf;
import ch.unibas.urz.android.perssearch.helper.MenuHelper;
import ch.unibas.urz.android.perssearch.helper.Settings;
import ch.unibas.urz.android.perssearch.model.Person;
import ch.unibas.urz.android.perssearch.view.adapter.PersonDetailAdapter;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

/**
 * Displays a word and its definition.
 */
public class PersonDetailsActivity extends ListActivity {

	public static final String EXTRA_ID = "id";
	private Person person;
	private TextView tvName;
	private TextView tvDescription;
	private int currentAppAppearance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Settings.getInstance().getAppAppearance() != Settings.APP_APPEARIANCE_ANDROID) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		updateView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateView();
	}

	private void updateView() {
		int appAppearance = Settings.getInstance().getAppAppearance();
		if (appAppearance == currentAppAppearance) {
			return;
		}
		if (appAppearance == Settings.APP_APPEARIANCE_ANDROID) {
			setTheme(R.style.android);
			setContentView(R.layout.person_detail_android);
		} else {
			setTheme(R.style.unibas_turquoise);
			setContentView(R.layout.person_detail_unibas);
			ActionBar actionBar = (ActionBar) findViewById(R.id.actionBar1);
			actionBar.setTitle(R.string.app_name);
			actionBar.addAction(new Action() {
				@Override
				public void performAction(View view) {
					ContactAdderVcf.addContact(PersonDetailsActivity.this, person);
				}

				@Override
				public int getDrawable() {
					return android.R.drawable.ic_menu_add;
				}
			});
			actionBar.addAction(new Action() {
				@Override
				public void performAction(View view) {
					searchAgain();
				}

				@Override
				public int getDrawable() {
					return android.R.drawable.ic_menu_search;
				}
			});
		}
		currentAppAppearance = appAppearance;
		tvName = (TextView) findViewById(R.id.tvName);
		tvDescription = (TextView) findViewById(R.id.tvDescription);

		tvDescription.setLines(1);
		tvDescription.setEllipsize(TruncateAt.END);
		displayData();
	}

	private void displayData() {
		Intent intent = getIntent();
		String personString = intent.getStringExtra(EXTRA_ID);
		person = new Person(personString);
		PersonDetailAdapter personDetailAdapter = new PersonDetailAdapter(this, person);
		getListView().setAdapter(personDetailAdapter);
		getListView().setOnItemClickListener(personDetailAdapter);
		tvName.setText(person.getName());
		tvDescription.setText(person.getDesciption());
	}

	public int getTextHeight() {
		return tvDescription.getHeight() - tvDescription.getPaddingTop() - tvDescription.getPaddingBottom();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_contact, menu);
		MenuHelper.onCreateOptionsMenu(this, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (MenuHelper.onOptionsItemSelected(this, item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.itemAddContacto:
			// AccountManager accountManager = AccountManager.get(this);
			// Account[] accounts = accountManager.getAccounts();
			// if (accounts.length > 0) {
			// // FIXME show accounts chooser
			// ContactManager.addToContacts(this, accounts[0], person);
			// }
			ContactAdderVcf.addContact(this, person);
			return true;
		case R.id.itemSearch:
			searchAgain();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void searchAgain() {
		Intent i = new Intent(this, PerssearchActivity.class);
		// i.setAction(Intent.ACTION_SEARCH);
		startActivity(i);
	}
}
