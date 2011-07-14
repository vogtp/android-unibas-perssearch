package ch.almana.android.unibas.perssearch.view.adapter;

import java.util.List;

import android.R;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import ch.almana.android.unibas.perssearch.helper.Settings;
import ch.almana.android.unibas.perssearch.model.DummyPerson;
import ch.almana.android.unibas.perssearch.model.Person;
import ch.almana.android.unibas.perssearch.view.activity.PerssearchActivity;

public class PersonAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

	/**
	 * 
	 */
	private final PerssearchActivity perssearchActivity;
	private final List<Person> persons;
	private final LayoutInflater mInflater;

	public PersonAdapter(PerssearchActivity perssearchActivity, List<Person> persons) {
		this.perssearchActivity = perssearchActivity;
		this.persons = persons;
		mInflater = (LayoutInflater) this.perssearchActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return persons.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TwoLineListItem view = (convertView != null) ? (TwoLineListItem) convertView : createView(parent);
		bindView(view, persons.get(position));
		return view;
	}

	private TwoLineListItem createView(ViewGroup parent) {
		TwoLineListItem item = (TwoLineListItem) mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
		item.getText2().setSingleLine();
		item.getText2().setEllipsize(TextUtils.TruncateAt.END);
		return item;
	}

	private void bindView(TwoLineListItem view, Person person) {
		view.getText1().setText(person.getName());
		if (Settings.getInstance().getAppAppearance() == Settings.APP_APPEARIANCE_UNIBAS_TURQUISE) {
			((TextView) view.findViewById(R.id.text1)).setTextColor(android.R.color.black);

			view.getText1().setTextColor(Color.BLACK);
		}
		view.getText2().setText(person.getDesciption());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Person person = persons.get(position);
		if (person instanceof DummyPerson) {
			DummyPerson dummy = (DummyPerson) person;
			Intent i = new Intent(perssearchActivity, PerssearchActivity.class);
			i.setAction(Intent.ACTION_SEARCH);
			i.putExtra(PerssearchActivity.EXTRA_SEARCH_ALL, true);
			i.putExtra(SearchManager.QUERY, dummy.getQuery());
			perssearchActivity.startActivity(i);
		} else {
			this.perssearchActivity.launchPersonActivity(person.getJsonString());
		}
	}
}