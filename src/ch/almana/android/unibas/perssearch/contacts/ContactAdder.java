package ch.almana.android.unibas.perssearch.contacts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Contacts.Data;
import android.widget.Toast;
import ch.almana.android.unibas.perssearch.helper.Logger;
import ch.almana.android.unibas.perssearch.model.Person;

public class ContactAdder {
	private Activity act;
	private Person person;
	private ArrayList<ContentProviderOperation> providerOperations;
	private int contactId;

	public ContactAdder(Activity act, Person person) {
		this.act = act;
		this.person = person;
		this.contactId = getContactId();
	}

	public void addContactDirect() {
		try {
			providerOperations = new ArrayList<ContentProviderOperation>();

			setName();

			setEmail(person.getEmailWork(), Email.TYPE_WORK);
			setEmail(person.getEmailHome(), Email.TYPE_HOME);

			setEmail(person.getEmailInstitute(), Email.TYPE_OTHER);

			setPhone(person.getPhoneMobile(), Phone.TYPE_MOBILE);
			setPhone(person.getPhoneHome(), Phone.TYPE_HOME);
			setPhone(person.getPhoneWork(), Phone.TYPE_WORK);
			setPhone(person.getFaxWork(), Phone.TYPE_FAX_WORK);
			setPhone(person.getFaxHome(), Phone.TYPE_FAX_HOME);

			setAddress(person.getAddressWork(), StructuredPostal.TYPE_WORK);
			setAddress(person.getAddressHome(), StructuredPostal.TYPE_HOME);
			
			 setWebpage();
			
			act.getContentResolver().applyBatch(ContactsContract.AUTHORITY, providerOperations);
		} catch (Exception e) {
			Toast.makeText(act, e.getMessage(), Toast.LENGTH_LONG).show();
			Logger.e("Could not generate contact entry ", e);
		}
	}

	private int getContactId() {
		Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String[] projection = new String[] { Data._ID, CommonDataKinds.Email.DATA };
		String selection = CommonDataKinds.Email.DATA + " = '" + person.getEmailWork() + "'";
		String[] selectionArgs = null;

		Cursor c = null;
		try {
			c = act.managedQuery(uri, projection, selection, selectionArgs, null);
			if (c.moveToFirst()) {
				return c.getInt(c.getColumnIndex(Data._ID));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return -1;
	}

	private void setEmail(Object email, Object type) {
		if (email == null) {
			return;
		}
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		builder.withValue(Email.DATA, email);
		builder.withValue(Email.TYPE, type);
		addToOperations(builder);
	}

	private void setPhone(Object number, Object type) {
		if (number == null) {
			return;
		}
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		builder.withValue(Phone.NUMBER, number);
		builder.withValue(Phone.TYPE, type);
		addToOperations(builder);
	}

	private void setAddress(Object address, Object type) {
		if (address == null) {
			return;
		}
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
		builder.withValue(StructuredPostal.FORMATTED_ADDRESS, address);
		builder.withValue(StructuredPostal.TYPE, type);
		addToOperations(builder);
	}

	private void setName() {
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		builder.withValue(StructuredName.DISPLAY_NAME, person.getName());
		String familyName = StructuredName.FAMILY_NAME;
		if (familyName != null) {
			builder.withValue(familyName, person.getFamilyName());
		}
		String givenName = person.getGivenName();
		if (givenName != null) {
			builder.withValue(StructuredName.GIVEN_NAME, givenName);
		}
		addToOperations(builder);
	}

	private void setWebpage() {
		String webpage = person.getWebpage();
		if (webpage == null) {
			return;
		}
		Builder builder = getBuilder();
		builder.withValue(ContactsContract.Data.MIMETYPE, Website.CONTENT_ITEM_TYPE);
		builder.withValue(Website.URL, webpage);
		addToOperations(builder);
	}
	
	private Builder getBuilder() {
		Builder builder;
		// if (contactId == -1) {
			builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
		builder = builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
		// } else {
		// builder =
		// ContentProviderOperation.newUpdate(Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI,
		// contactId + ""));
		// //
		// builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
		// contactId);
		// }
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