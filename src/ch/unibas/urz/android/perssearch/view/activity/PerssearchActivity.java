package ch.unibas.urz.android.perssearch.view.activity;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import ch.unibas.urz.android.perssearch.R;
import ch.unibas.urz.android.perssearch.access.PerssearchLoader;
import ch.unibas.urz.android.perssearch.helper.Debugger;
import ch.unibas.urz.android.perssearch.helper.MenuHelper;
import ch.unibas.urz.android.perssearch.helper.Settings;
import ch.unibas.urz.android.perssearch.view.adapter.PersonAdapter;


public class PerssearchActivity extends Activity {

	public static final String EXTRA_SEARCH_ALL = "searchAll";
	private TextView mTextView;
    private ListView mList;
	private boolean search;
	private int currentAppAppearance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		if (Debugger.DEBUG) {
			setTitle(getTitle() + " - DEBUG");
		}

		updateView();

    }

	private void displayData() {
		Intent intent = getIntent();
		mTextView = (TextView) findViewById(R.id.textField);
        mList = (ListView) findViewById(R.id.list);

		search = true;
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// from click on search results
			launchPersonActivity(intent.getDataString());
            finish();
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
			boolean searchAll = intent.getExtras().getBoolean(EXTRA_SEARCH_ALL, false);
			String type = getResources().getStringArray(R.array.prefSearchTypeEntries)[0];
			if (!searchAll) {
				switch (Settings.getInstance().getSearchType()) {
				case STAFF:
					type = getResources().getStringArray(R.array.prefSearchTypeEntries)[1];
					break;
				case STUDENTS:
					type = getResources().getStringArray(R.array.prefSearchTypeEntries)[2];
					break;
				}
			}
			mTextView.setText(getString(R.string.search_results, query, type));
			PersonAdapter personAdapter = new PersonAdapter(this, PerssearchLoader.getInstance().getMatches(this, query, searchAll));
			mList.setAdapter(personAdapter);
			mList.setOnItemClickListener(personAdapter);
			search = false;
		} else if (Debugger.DEBUG) {
			switch (Debugger.DUMMY_QUERY) {
			case FIXED_QUERY:
				launchPersonActivity(PerssearchLoader.getInstance().getMatches(this, Debugger.QUERY_STRING).get(0).getJsonString());
				break;
			case LOCAL_DATA:
				launchPersonActivity(Debugger.JSON_STRING);
				break;
			}
		}

        Log.d("dict", intent.toString());
        if (intent.getExtras() != null) {
            Log.d("dict", intent.getExtras().keySet().toString());
        }
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (search) {
			onSearchRequested();
		}
		updateView();
	}

	private void updateView() {
		int appAppearance = Settings.getInstance().getAppAppearance();
		if (appAppearance == currentAppAppearance) {
			return;
		}
		if (appAppearance == Settings.APP_APPEARIANCE_ANDROID) {
			setTheme(R.style.android);
			setContentView(R.layout.main_android);
		} else {
			setTheme(R.style.unibas_turquoise);
			setContentView(R.layout.main_unibas);
		}
		currentAppAppearance = appAppearance;
		displayData();
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

	public void launchPersonActivity(String personId) {
        Intent next = new Intent();
        next.setClass(this, PersonDetailsActivity.class);
		next.putExtra(PersonDetailsActivity.EXTRA_ID, personId);
        startActivity(next);
    }

}
