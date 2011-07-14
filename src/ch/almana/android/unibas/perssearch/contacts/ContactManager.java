/*
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ch.almana.android.unibas.perssearch.contacts;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.SyncStateContract.Constants;
import ch.almana.android.unibas.perssearch.helper.Logger;
import ch.almana.android.unibas.perssearch.model.Person;

/**
 * Class for managing contacts sync related mOperations
 */
public class ContactManager {

	/**
	 * Synchronize raw contacts
	 * 
	 * @param context
	 *            The context of Authenticator Activity
	 * @param accounts
	 *            The username for the account
	 * @param users
	 *            The list of users
	 */
	public static synchronized void addToContacts(Context context, Account accounts,
			Person person) {
		long userId;
		long rawContactId = 0;
		final ContentResolver resolver = context.getContentResolver();
		final BatchOperation batchOperation = new BatchOperation(context, resolver);
		userId = person.getUserId();
		// Check to see if the contact needs to be inserted or updated
		rawContactId = lookupRawContact(resolver, userId);
		boolean contactAdded = false;
		if (rawContactId != 0) {
			Logger.d("update contact");
			contactAdded = updateContact(context, resolver, accounts, person, rawContactId, batchOperation);
		}
		if (!contactAdded) {
			Logger.d("add new contact");
			addContact(context, accounts, person, batchOperation);
		}

		batchOperation.execute();
		// showContact(context, person);
	}

	private static void showContact(Context context, Person person) {
		Intent intent = new Intent(ContactsContract.Intents.SHOW_OR_CREATE_CONTACT);
		// FIXME use all possible combinations
		intent.setData(Uri.fromParts("mailto", person.getEmailWork(), null));
		context.startActivity(intent);
	}

	/**
	 * Adds a single contact to the platform contacts provider.
	 * 
	 * @param context
	 *            the Authenticator Activity context
	 * @param accounts
	 *            the account the contact belongs to
	 * @param user
	 *            the sample SyncAdapter User object
	 */
	private static void addContact(Context context, Account accounts, Person person, BatchOperation batchOperation) {
		// Put the data in the contacts provider
		final ContactOperations contactOp = ContactOperations.createNewContact(context, person.getUserId(),
		/* accountName, */batchOperation);
		contactOp.addName(person.getGivenName(), person.getFamilyName());
		contactOp.addEmail(person.getEmailWork(), Email.TYPE_WORK);
		contactOp.addPhone(person.getPhoneMobile(), Phone.TYPE_MOBILE);
		contactOp.addPhone(person.getPhoneWork(), Phone.TYPE_WORK);
		contactOp.addPhone(person.getPhoneHome(), Phone.TYPE_HOME);
		// FIXME add all
		contactOp.addProfileAction(person.getUserId());
	}

	/**
	 * Updates a single contact to the platform contacts provider.
	 * 
	 * @param context
	 *            the Authenticator Activity context
	 * @param resolver
	 *            the ContentResolver to use
	 * @param accounts
	 *            the account the contact belongs to
	 * @param user
	 *            the sample SyncAdapter contact object.
	 * @param rawContactId
	 *            the unique Id for this rawContact in contacts provider
	 * @return
	 */
	private static boolean updateContact(Context context, ContentResolver resolver, Account accounts, Person person, long rawContactId, BatchOperation batchOperation) {
		Uri uri;
		String cellPhone = null;
		String homePhone = null;
		String workPhone = null;
		String email = null;

		final Cursor c = resolver.query(Data.CONTENT_URI, DataQuery.PROJECTION, DataQuery.SELECTION, new String[] { String.valueOf(rawContactId) }, null);
		final ContactOperations contactOp = ContactOperations.updateExistingContact(context, rawContactId, batchOperation);

		try {

			if (c.moveToNext()) {
				final long id = c.getLong(DataQuery.COLUMN_ID);
				final String mimeType = c.getString(DataQuery.COLUMN_MIMETYPE);
				uri = ContentUris.withAppendedId(Data.CONTENT_URI, id);

				if (mimeType.equals(StructuredName.CONTENT_ITEM_TYPE)) {
					final String lastName = c.getString(DataQuery.COLUMN_FAMILY_NAME);
					final String firstName = c.getString(DataQuery.COLUMN_GIVEN_NAME);
					contactOp.updateName(uri, firstName, lastName, person.getGivenName(), person.getFamilyName());
				}

				else if (mimeType.equals(Phone.CONTENT_ITEM_TYPE)) {
					final int type = c.getInt(DataQuery.COLUMN_PHONE_TYPE);

					if (type == Phone.TYPE_MOBILE) {
						cellPhone = c.getString(DataQuery.COLUMN_PHONE_NUMBER);
						contactOp.updatePhone(cellPhone, person.getPhoneMobile(), uri);
					} else if (type == Phone.TYPE_HOME) {
						homePhone = c.getString(DataQuery.COLUMN_PHONE_NUMBER);
						contactOp.updatePhone(homePhone, person.getPhoneHome(), uri);
					} else if (type == Phone.TYPE_WORK) {
						workPhone = c.getString(DataQuery.COLUMN_PHONE_NUMBER);
						contactOp.updatePhone(workPhone, person.getPhoneWork(), uri);
					}
				}

				else if (Data.MIMETYPE.equals(Email.CONTENT_ITEM_TYPE)) {
					final int type = c.getInt(DataQuery.COLUMN_EMAIL_TYPE);
					if (type == Email.TYPE_HOME) {
						email = c.getString(DataQuery.COLUMN_EMAIL_ADDRESS);
						contactOp.updateEmail(person.getEmailHome(), email, uri);
					} else if (type == Email.TYPE_WORK) {
						email = c.getString(DataQuery.COLUMN_EMAIL_ADDRESS);
						contactOp.updateEmail(person.getEmailWork(), email, uri);
					} else if (type == Email.TYPE_OTHER) {
						email = c.getString(DataQuery.COLUMN_EMAIL_ADDRESS);
						contactOp.updateEmail(person.getEmailInstitute(), email, uri);
					}

				}
			} else {
				return false;
			}
		} finally {
			c.close();
		}

		if (cellPhone == null) {
			contactOp.addPhone(person.getPhoneMobile(), Phone.TYPE_MOBILE);
		}

		if (homePhone == null) {
			contactOp.addPhone(person.getPhoneHome(), Phone.TYPE_HOME);
		}

		if (workPhone == null) {
			contactOp.addPhone(person.getPhoneWork(), Phone.TYPE_WORK);
		}

		// FIXME add all

		// FIXME
		// // Add the email address, if present and not updated above
		// if (email == null) {
		// contactOp.addEmail(user.getEmail());
		// }

		return true;
	}

	/**
	 * Returns the RawContact id for a sample SyncAdapter contact, or 0 if the
	 * sample SyncAdapter user isn't found.
	 * 
	 * @param context
	 *            the Authenticator Activity context
	 * @param userId
	 *            the sample SyncAdapter user ID to lookup
	 * @return the RawContact id, or 0 if not found
	 */
	private static long lookupRawContact(ContentResolver resolver, long userId) {
		long authorId = 0;
		final Cursor c = resolver.query(RawContacts.CONTENT_URI, UserIdQuery.PROJECTION, UserIdQuery.SELECTION, new String[] { String.valueOf(userId) }, null);
		try {
			if (c.moveToFirst()) {
				int deleted = c.getInt(c.getColumnIndex(RawContacts.DELETED));
				if (deleted == 0) {
					authorId = c.getLong(UserIdQuery.COLUMN_ID);
				}
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return authorId;
	}

	/**
	 * Returns the Data id for a sample SyncAdapter contact's profile row, or 0
	 * if the sample SyncAdapter user isn't found.
	 * 
	 * @param resolver
	 *            a content resolver
	 * @param userId
	 *            the sample SyncAdapter user ID to lookup
	 * @return the profile Data row id, or 0 if not found
	 */
	private static long lookupProfile(ContentResolver resolver, long userId) {
		long profileId = 0;
		final Cursor c = resolver.query(Data.CONTENT_URI, ProfileQuery.PROJECTION, ProfileQuery.SELECTION, new String[] { String.valueOf(userId) }, null);
		try {
			if (c != null && c.moveToFirst()) {
				profileId = c.getLong(ProfileQuery.COLUMN_ID);
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return profileId;
	}

	/**
	 * Constants for a query to find a contact given a sample SyncAdapter user
	 * ID.
	 */
	private interface ProfileQuery {
		public final static String[] PROJECTION = new String[] { Data._ID };

		public final static int COLUMN_ID = 0;

		public static final String SELECTION = Data.MIMETYPE + "='" + ContactOperations.MIME_PROFILE + "' AND " + ContactOperations.DATA_PID + "=?";
	}

	/**
	 * Constants for a query to find a contact given a sample SyncAdapter user
	 * ID.
	 */
	private interface UserIdQuery {
		public final static String[] PROJECTION = new String[] { RawContacts._ID, RawContacts.DELETED };

		public final static int COLUMN_ID = 0;

		public static final String SELECTION = RawContacts.ACCOUNT_TYPE + "='" + Constants.ACCOUNT_TYPE + "' AND " + RawContacts._ID + "=?";
	}

	/**
	 * Constants for a query to get contact data for a given rawContactId
	 */
	private interface DataQuery {
		public static final String[] PROJECTION = new String[] { Data._ID, Data.MIMETYPE, Data.DATA1, Data.DATA2, Data.DATA3, };

		public static final int COLUMN_ID = 0;
		public static final int COLUMN_MIMETYPE = 1;
		public static final int COLUMN_DATA1 = 2;
		public static final int COLUMN_DATA2 = 3;
		public static final int COLUMN_DATA3 = 4;
		public static final int COLUMN_PHONE_NUMBER = COLUMN_DATA1;
		public static final int COLUMN_PHONE_TYPE = COLUMN_DATA2;
		public static final int COLUMN_EMAIL_ADDRESS = COLUMN_DATA1;
		public static final int COLUMN_EMAIL_TYPE = COLUMN_DATA2;
		public static final int COLUMN_GIVEN_NAME = COLUMN_DATA2;
		public static final int COLUMN_FAMILY_NAME = COLUMN_DATA3;

		public static final String SELECTION = Data.RAW_CONTACT_ID + "=?";
	}
}
