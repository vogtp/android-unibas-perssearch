package ch.almana.android.unibas.perssearch.view;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TwoLineListItem;
import ch.almana.android.unibas.perssearch.model.Person;

class PersonAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    /**
	 * 
	 */
	private final PerssearchActivity perssearchActivity;
	private final List<Person> persons;
    private final LayoutInflater mInflater;

	public PersonAdapter(PerssearchActivity perssearchActivity, List<Person> persons) {
		this.perssearchActivity = perssearchActivity;
		this.persons = persons;
		mInflater = (LayoutInflater) this.perssearchActivity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
		return persons.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TwoLineListItem view = (convertView != null) ? (TwoLineListItem) convertView :
                createView(parent);
		bindView(view, persons.get(position));
        return view;
    }

    private TwoLineListItem createView(ViewGroup parent) {
        TwoLineListItem item = (TwoLineListItem) mInflater.inflate(
                android.R.layout.simple_list_item_2, parent, false);
        item.getText2().setSingleLine();
        item.getText2().setEllipsize(TextUtils.TruncateAt.END);
        return item;
    }

	private void bindView(TwoLineListItem view, Person person) {
		view.getText1().setText(person.getName());
		view.getText2().setText(person.getEmail());
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.perssearchActivity.launchPersonActivity(persons.get(position).getId());
    }
}