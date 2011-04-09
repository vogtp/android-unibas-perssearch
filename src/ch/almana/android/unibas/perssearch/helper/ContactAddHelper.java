package ch.almana.android.unibas.perssearch.helper;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.Context;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.widget.Toast;
import ch.almana.android.unibas.perssearch.model.Person;

public class ContactAddHelper {

	private Context ctx;
	private Person person;
	private ArrayList<ContentProviderOperation> providerOperations;

	public ContactAddHelper(Context ctx, Person person) {
		this.ctx = ctx.getApplicationContext();
		this.person = person;
		providerOperations = new ArrayList<ContentProviderOperation>();
	}

	public void addContact() {
		try {
			setName();
			setPhoneWork();
			setEmailWork();
			setFaxWork();
			setAddress();
			ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, providerOperations);
		} catch (Exception e) {
			Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
			Logger.e("Could not generate contact entry ", e);
		}
	}

	private void setEmailWork() {
		setEmail(person.getEmailWork(), Email.TYPE_WORK);
	}

	private void setEmail(Object email, Object type) {
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		builder.withValue(Email.DATA, email);
		builder.withValue(Email.TYPE, type);
		addToOperations(builder);
	}


	private void setFaxWork() {
		setPhone(person.getFaxWork(), Phone.TYPE_FAX_WORK);
	}
	private void setPhoneWork() {
		setPhone(person.getPhoneWork(), Phone.TYPE_WORK);
	}

	private void setPhone(Object number, Object type) {
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		builder.withValue(Phone.NUMBER, number);
		builder.withValue(Phone.TYPE, type);
		addToOperations(builder);
	}

	private void setAddress() {
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
		builder.withValue(StructuredPostal.FORMATTED_ADDRESS, person.getAddress());
		builder.withValue(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK);
		addToOperations(builder);
	}

	private void setName() {
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		builder.withValue(StructuredName.DISPLAY_NAME, person.getName());
		addToOperations(builder);
	}

	private Builder getBuilder() {
		Builder builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		// builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
		// 0);
		return builder;
	}

	private void addToOperations(Builder builder) {
		providerOperations.add(builder.build());
	}

	private void addToAccount(String type, String name) {
		Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
		builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, type);
		builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, name);
		addToOperations(builder);
	}

}
