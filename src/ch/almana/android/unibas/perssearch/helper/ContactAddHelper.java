package ch.almana.android.unibas.perssearch.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import ch.almana.android.unibas.perssearch.R;
import ch.almana.android.unibas.perssearch.model.Person;

public class ContactAddHelper {

	// private Context ctx;
	// private Person person;
	//
	// public ContactAddHelper(Context ctx, Person person) {
	// this.ctx = ctx.getApplicationContext();
	// this.person = person;
	// }

	public static void addContact(Context ctx, Person person) {

		Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT);
		intent.setData(Uri.fromParts("mailto", person.getEmailWork(), null));
		intent.putExtra(ContactsContract.Intents.Insert.NAME, person.getName());
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, person.getPhoneWork());
		intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
		intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, person.getFaxWork());
		intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ctx.getString(R.string.work_fax));
		// intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE,
		// person.get);
		// intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE,
		// ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL, person.getEmailWork());
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
		intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, person.getEmailInstitute());
		intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE, ctx.getString(R.string.institute));
		intent.putExtra(ContactsContract.Intents.Insert.POSTAL, person.getAddress());
		intent.putExtra(ContactsContract.Intents.Insert.POSTAL_TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK);
		intent.putExtra(ContactsContract.Intents.Insert.FULL_MODE, true);

		ctx.startActivity(intent);

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
