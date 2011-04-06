package ch.almana.android.unibas.perssearch.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import ch.almana.android.unibas.perssearch.R;
import ch.almana.android.unibas.perssearch.model.Person;
import ch.almana.android.unibas.perssearch.model.Person.FieldTypes;

class PersionDetailAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

	private final PersonDetailsActivity personDetailsActivity;
	private final Person person;
	private final LayoutInflater inflater;
	private List<FieldTypes> fields;

	public PersionDetailAdapter(PersonDetailsActivity personDetailsActivity, Person person) {
		this.personDetailsActivity = personDetailsActivity;
		this.person = person;
		fields = person.getNonblankFields();
		inflater = (LayoutInflater) personDetailsActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return fields.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = (convertView != null) ? convertView : createView(parent);
		bindView(view, fields.get(position));
		return view;
	}

	private View createView(ViewGroup parent) {
		return inflater.inflate(R.layout.person_detail_list_item, parent, false);
	}

	private void bindView(View view, FieldTypes field) {

		int labelResId;
		String value;
		int buttonImageResId = -1;
		OnClickListener clickListener = null;
		TextView tvValue = (TextView) view.findViewById(R.id.tvValue);

		switch (field) {
		case MAIL:
			labelResId = R.string.email;
			value = person.getEmail();
			buttonImageResId = android.R.drawable.sym_action_email;
			clickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					String email = person.getEmail();
					intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
					// intent.putExtra(Intent.EXTRA_SUBJECT, "test ");
					// intent.putExtra(Intent.EXTRA_TEXT, " test");
					personDetailsActivity.startActivity(Intent.createChooser(intent, personDetailsActivity.getString(R.string.email)));
				}
			};
			break;
		case PHONE_WORK:
			labelResId = R.string.phone_work;
			value = person.getPhoneWork();
			buttonImageResId = android.R.drawable.sym_action_call;
			clickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + person.getPhoneWork()));
					personDetailsActivity.startActivity(intent);
				}
			};
			break;
		case ADDRESS:
			labelResId = R.string.work_address;
			value = person.getAddress();
			buttonImageResId = android.R.drawable.ic_menu_directions;
			tvValue.setLines(5);
			clickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("geo:0,0?q=" + person.getAddress()));
					personDetailsActivity.startActivity(intent);
				}
			};
			break;

		default:
			labelResId = R.string.unknown;
			value = "";
			break;
		}

		((TextView) view.findViewById(R.id.tvLabel)).setText(personDetailsActivity.getString(labelResId));
		tvValue.setText(value);
		if (buttonImageResId != -1) {
			Button buAction = ((Button) view.findViewById(R.id.buAction));
			buAction.setBackgroundDrawable(personDetailsActivity.getResources().getDrawable(buttonImageResId));
			buAction.setOnClickListener(clickListener);
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// this.perssearchActivity.launchPersonActivity(person.get(position).getJsonString());
	}
}