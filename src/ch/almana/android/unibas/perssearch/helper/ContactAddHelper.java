package ch.almana.android.unibas.perssearch.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Intents.Insert;
import ch.almana.android.unibas.perssearch.R;
import ch.almana.android.unibas.perssearch.model.Person;

public class ContactAddHelper {

	private static final int EMAIL_INSTITUTE = -2;
	private static int phoneIdx;
	private static int emailIdx;

	// private Context ctx;
	// private Person person;
	//
	// public ContactAddHelper(Context ctx, Person person) {
	// this.ctx = ctx.getApplicationContext();
	// this.person = person;
	// }


	public static void addContact2(Context ctx, Person person) {

		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setData(Contacts.CONTENT_URI);

		intent.putExtra(Contacts.People.NAME, person.getName());
		intent.putExtra(Contacts.Phones.NUMBER, person.getPhoneWork());

// intent.putExtra(Insert.COMPANY, person.getDesciption());
		//
		// addPhone(intent, person.getPhoneWork(), Phone.TYPE_WORK);
		// addPhone(intent, person.getPhoneMobile(), Phone.TYPE_MOBILE);
		// addPhone(intent, person.getPhoneHome(), Phone.TYPE_HOME);
		// addPhone(intent, person.getFaxWork(), Phone.TYPE_FAX_WORK);
		//
		// addEmail(ctx, intent, person.getEmailWork(), Email.TYPE_WORK);
		// addEmail(ctx, intent, person.getEmailHome(), Email.TYPE_HOME);
		// addEmail(ctx, intent, person.getEmailInstitute(), EMAIL_INSTITUTE);
		//
		// intent.putExtra(Insert.POSTAL, person.getAddress());
		// intent.putExtra(Insert.POSTAL_TYPE, StructuredPostal.TYPE_WORK);
		// intent.putExtra(Insert.FULL_MODE, true);

		ctx.startActivity(intent);
	}

	public static void addContact(Context ctx, Person person) {

		phoneIdx = 0;
		emailIdx = 0;

		Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT);
		// Intent intent = new Intent(Insert.ACTION);
		intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);
		intent.putExtra(Insert.ACTION, true);
		intent.setData(Uri.fromParts("mailto", person.getEmailWork(), null));

		String familyName = person.getFamilyName();
		String givenName = person.getGivenName();
		if (Person.hasField(givenName) && Person.hasField(familyName)) {
			intent.putExtra(StructuredName.FAMILY_NAME, familyName);
			intent.putExtra(StructuredName.GIVEN_NAME, givenName);
		}
		intent.putExtra(Insert.NAME, person.getName());

		intent.putExtra(Insert.COMPANY, person.getDesciption());
		
		addPhone(intent, person.getPhoneWork(), Phone.TYPE_WORK);
		addPhone(intent, person.getPhoneMobile(), Phone.TYPE_MOBILE);
		addPhone(intent, person.getPhoneHome(), Phone.TYPE_HOME);
		addPhone(intent, person.getFaxWork(), Phone.TYPE_FAX_WORK);

		addEmail(ctx, intent, person.getEmailWork(), Email.TYPE_WORK);
		addEmail(ctx, intent, person.getEmailHome(), Email.TYPE_HOME);
		addEmail(ctx, intent, person.getEmailInstitute(), EMAIL_INSTITUTE);
		
		intent.putExtra(Insert.POSTAL, person.getAddressWork());
		intent.putExtra(Insert.POSTAL_TYPE, StructuredPostal.TYPE_WORK);
		intent.putExtra(Insert.FULL_MODE, true);

		ctx.startActivity(intent);

	}

	private static void addPhone(Intent intent, String number, int type) {
		if (!Person.hasField(number)) {
			return;
		}
		String extraPhone;
		String extraPhoneType;
		switch (phoneIdx) {
		case 0:
			extraPhone = Insert.PHONE;
			extraPhoneType = Insert.PHONE_TYPE;
			break;
		case 1:
			extraPhone = Insert.SECONDARY_PHONE;
			extraPhoneType = Insert.SECONDARY_PHONE_TYPE;
			break;
		case 2:
			extraPhone = Insert.TERTIARY_PHONE;
			extraPhoneType = Insert.TERTIARY_PHONE_TYPE;
			break;

		default:
			// cannot add more phonenumbers
			// FIXME: toast
			return;
		}

		intent.putExtra(extraPhone, number);
		intent.putExtra(extraPhoneType, type);
		phoneIdx++;
	}

	private static void addEmail(Context ctx, Intent intent, String email, int type) {
		if (!Person.hasField(email)) {
			return;
		}
		String extraEmail;
		String extraEmailType;
		switch (emailIdx) {
		case 0:
			extraEmail = Insert.EMAIL;
			extraEmailType = Insert.EMAIL_TYPE;
			break;
		case 1:
			extraEmail = Insert.SECONDARY_EMAIL;
			extraEmailType = Insert.SECONDARY_EMAIL_TYPE;
			break;
		case 2:
			extraEmail = Insert.TERTIARY_EMAIL;
			extraEmailType = Insert.TERTIARY_EMAIL_TYPE;
			break;

		default:
			// cannot add more emails
			// FIXME: toast
			return;
		}

		
		intent.putExtra(extraEmail, email);
		if (type == EMAIL_INSTITUTE) {
			intent.putExtra(extraEmailType, ctx.getString(R.string.email_institute));
		}else {
			intent.putExtra(extraEmailType, type);
		}
		emailIdx++;
	}

	// public void addContactDirect() {
	// try {
	// ArrayList<ContentProviderOperation> providerOperations = new
	// ArrayList<ContentProviderOperation>();
	// setName();
	// setPhoneWork();
	// setEmailWork();
	// setFaxWork();
	// setAddress();
	// ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY,
	// providerOperations);
	// } catch (Exception e) {
	// Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
	// Logger.e("Could not generate contact entry ", e);
	// }
	// }
	//
	// private void setEmailWork() {
	// setEmail(person.getEmailWork(), Email.TYPE_WORK);
	// }
	//
	// private void setEmail(Object email, Object type) {
	// Builder builder = getBuilder();
	// builder.withValue(ContactsContract.Data.MIMETYPE,
	// Email.CONTENT_ITEM_TYPE);
	// builder.withValue(Email.DATA, email);
	// builder.withValue(Email.TYPE, type);
	// addToOperations(builder);
	// }
	//
	//
	// private void setFaxWork() {
	// setPhone(person.getFaxWork(), Phone.TYPE_FAX_WORK);
	// }
	// private void setPhoneWork() {
	// setPhone(person.getPhoneWork(), Phone.TYPE_WORK);
	// }
	//
	// private void setPhone(Object number, Object type) {
	// Builder builder = getBuilder();
	// builder.withValue(ContactsContract.Data.MIMETYPE,
	// Phone.CONTENT_ITEM_TYPE);
	// builder.withValue(Phone.NUMBER, number);
	// builder.withValue(Phone.TYPE, type);
	// addToOperations(builder);
	// }
	//
	// private void setAddress() {
	// Builder builder = getBuilder();
	// builder.withValue(ContactsContract.Data.MIMETYPE,
	// StructuredPostal.CONTENT_ITEM_TYPE);
	// builder.withValue(StructuredPostal.FORMATTED_ADDRESS,
	// person.getAddress());
	// builder.withValue(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK);
	// addToOperations(builder);
	// }
	//
	// private void setName() {
	// Builder builder = getBuilder();
	// builder.withValue(ContactsContract.Data.MIMETYPE,
	// StructuredName.CONTENT_ITEM_TYPE);
	// builder.withValue(StructuredName.DISPLAY_NAME, person.getName());
	// addToOperations(builder);
	// }
	//
	// private Builder getBuilder() {
	// Builder builder =
	// ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
	// // builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
	// // 0);
	// return builder;
	// }
	//
	// private void addToOperations(Builder builder,
	// ArrayList<ContentProviderOperation> providerOperations) {
	// providerOperations.add(builder.build());
	// }
	//
	// private void addToAccount(String type, String name) {
	// Builder builder =
	// ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
	// builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, type);
	// builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, name);
	// addToOperations(builder);
	// }

}
