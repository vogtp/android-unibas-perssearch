package ch.almana.android.unibas.perssearch.view;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import ch.almana.android.unibas.perssearch.R;
import ch.almana.android.unibas.perssearch.access.PerssearchLoader;
import ch.almana.android.unibas.perssearch.helper.Debugger;
import ch.almana.android.unibas.perssearch.helper.MenuHelper;


public class PerssearchActivity extends Activity {

	public static final String EXTRA_SEARCH_ALL = "searchAll";
	private TextView mTextView;
    private ListView mList;
	private boolean search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();

        setContentView(R.layout.main);
        mTextView = (TextView) findViewById(R.id.textField);
        mList = (ListView) findViewById(R.id.list);

		search = true;
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// from click on search results
			launchPersonActivity(intent.getDataString());
            finish();
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mTextView.setText(getString(R.string.search_results, query));
			PersonAdapter personAdapter = new PersonAdapter(this, PerssearchLoader.getInstance().getMatches(query, intent.getExtras().getBoolean(EXTRA_SEARCH_ALL, false)));
			mList.setAdapter(personAdapter);
			mList.setOnItemClickListener(personAdapter);
			search = false;
		} else if (Debugger.DEBUG) {
			switch (Debugger.DUMMY_QUERY) {
			case FIXED_QUERY:
				launchPersonActivity(PerssearchLoader.getInstance().getMatches(Debugger.QUERY_STRING).get(0).getJsonString());
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

	void launchPersonActivity(String personId) {
        Intent next = new Intent();
        next.setClass(this, PersonDetailsActivity.class);
		next.putExtra(PersonDetailsActivity.EXTRA_ID, personId);
        startActivity(next);
    }

}
