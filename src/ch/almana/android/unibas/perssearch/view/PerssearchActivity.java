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


public class PerssearchActivity extends Activity {

    private static final int MENU_SEARCH = 1;

    private TextView mTextView;
    private ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();

        setContentView(R.layout.main);
        mTextView = (TextView) findViewById(R.id.textField);
        mList = (ListView) findViewById(R.id.list);

        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// from click on search results
			launchPersonActivity(intent.getDataString());
            finish();
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mTextView.setText(getString(R.string.search_results, query));
            PersonAdapter wordAdapter = new PersonAdapter(this, PerssearchLoader.getInstance().getMatches(query));
            mList.setAdapter(wordAdapter);
            mList.setOnItemClickListener(wordAdapter);
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

	// @Override
	// protected void onResume() {
	// super.onResume();
	// KeyEvent eventUp = new KeyEvent(KeyEvent.ACTION_UP,
	// KeyEvent.KEYCODE_SEARCH);
	// KeyEvent eventDown = new KeyEvent(KeyEvent.ACTION_DOWN,
	// KeyEvent.KEYCODE_SEARCH);
	// DispatcherState state = new DispatcherState();
	// state.startTracking(eventDown, this);
	// state.handleUpEvent(eventDown);
	// // eventDown.dispatch(this, state, this);
	// //
	// // eventUp.dispatch(this, state, this);
	//
	// // google: getKeyDispatcherState().startTracking(event, this);
	//
	// // getWindow().superDispatchKeyEvent(event);
	// // dispatchKeyEvent(event);
	// // KeyEvent.KEYCODE_SEARCH));
	// // onKeyUp(KeyEvent.KEYCODE_SEARCH, new KeyEvent(KeyEvent.ACTION_UP,
	// // KeyEvent.KEYCODE_SEARCH));
	// }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_SEARCH, 0, R.string.menu_search)
                .setIcon(android.R.drawable.ic_search_category_default)
                .setAlphabeticShortcut(SearchManager.MENU_KEY);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SEARCH:
                onSearchRequested();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	void launchPersonActivity(String personId) {
        Intent next = new Intent();
        next.setClass(this, PersonDetailsActivity.class);
		next.putExtra(PersonDetailsActivity.EXTRA_ID, personId);
        startActivity(next);
    }
}
