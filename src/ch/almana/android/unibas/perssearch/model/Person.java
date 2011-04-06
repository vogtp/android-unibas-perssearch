package ch.almana.android.unibas.perssearch.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import ch.almana.android.unibas.perssearch.helper.Logger;

public class Person {

	// {"registeredaddress":"","unibaschhomefax":"","homepostaladdress":"","sn":"Vogt",
	// "postaladdress":"Universitätsrechenzentrum$Klingelbergstrasse 70$CH-4056 Basel",
	// "dn":"uid=vogtp, ou=people, ou=intern, ou=psearch, dc=unibas, dc=ch","uid":"vogtp",
	// "facsimiletelephonenumber":"+41 61 267 22 82","unibaschsalutation":"M","labeleduri":"","rating":1.0,
	// "telephonenumber":4.1612671522E10,"cn":"Patrick Vogt","mail":"Patrick.Vogt@unibas.ch",
	// "displayname":"Patrick Vogt","unibaschhomemail":"","edupersonaffiliation":"","mailalternateaddress":"",
	// "givenname":"Patrick","unibaschorgroledisplay":"","employeetype":"","homephone":"",
	// "unibaschhomemobile":"","edupersonorgunitdn":"ou=urz,ou=units,ou=psearch,dc=unibas,dc=ch"},


	public static final Person EMPTY = new Person();
	
	public static final String NOT_GIVEN = "not given";
	
	public static final String KEY_NAME = "displayname";
	public static final String KEY_MAIL = "mail";
	public static final String KEY_PHONE_WORK = "facsimiletelephonenumber";

	// private static final String KEY_PHONE_HOME = null;
	// private static final String KEY_PHONE_MOBILE = null;
	// private static final String KEY_PHONE_FAX = null;
	public static final String KEY_ADDRESS = "postaladdress";
	public static final String KEY_STUDENT_TYPE = "edupersonaffiliation";
	
	public static enum FieldTypes {
		MAIL, PHONE_WORK, ADDRESS, STUDENT_TYPE
	};

	private static final String ADDRESS_SEPARATOR = "$";

	private JSONObject jsonObject;

	private String jsonString;

	public Person(String jsonString) {
		super();
		try {
			this.jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			jsonObject = new JSONObject();
			Logger.e("Cannot initalise person from sting " + jsonString, e);
		}
		this.jsonString = jsonString;
	}

	private Person() {
		try {
			this.jsonObject = new JSONObject("{ \"displayname\":\"Not found\"}");
		} catch (JSONException e) {
			Logger.e("Cannot initalise empty person", e);
		}
	}

	private String getJsonEntry(String keyName) {
		try {
			return jsonObject.getString(keyName);
		} catch (JSONException e) {
			return NOT_GIVEN;
		}
	}

	public String getName() {
		return getJsonEntry(KEY_NAME);
	}

	public String getEmail() {
		return getJsonEntry(KEY_MAIL);
	}

	public String getPhoneWork() {
		return getJsonEntry(KEY_PHONE_WORK);
	}

	// public String getPhoneHome() {
	// return getJsonEntry(KEY_PHONE_HOME);
	// }
	//
	// public String getPhoneMobile() {
	// return getJsonEntry(KEY_PHONE_MOBILE);
	// }
	//
	// public String getFax() {
	// return getJsonEntry(KEY_PHONE_FAX);
	// }

	public String getAddress() {
		return getJsonEntry(KEY_ADDRESS).replace(ADDRESS_SEPARATOR, "\n");
	}

	public String getStudentType() {
		return getJsonEntry(KEY_STUDENT_TYPE);
	}

	public String[] getValuesAarry() {
		String ID = "1";// getId();
		String intentData = jsonString;
		return new String[] { ID, getName(), getDesciption(), intentData, };
	}

	public String getDesciption() {
		String description = getStudentType();
		if (description == NOT_GIVEN || TextUtils.isEmpty(description)) {
			String adr = getJsonEntry(KEY_ADDRESS);
			int idx = adr.indexOf(ADDRESS_SEPARATOR);
			if (idx > 0) {
				description = adr.substring(0, idx);
			}
		}
		if (description == NOT_GIVEN || TextUtils.isEmpty(description)) {
			description = getEmail();
		}
		return description;
	}

	public String getId() {
		return Integer.toString(jsonString.hashCode());
	}

	public String getJsonString() {
		return jsonString;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		JSONArray keys = jsonObject.names();
		for (int i = 0; i < keys.length(); i++) {
			try {
				String key = keys.getString(i);
				sb.append(key).append(" -> ").append(getJsonEntry(key)).append("\n");
			} catch (JSONException e) {
				sb.append(e.getMessage());
				Logger.w("Error unmarshalling json", e);
			}
		}
		return sb.toString();
	}

	public void getDataFields() {
		// TODO Auto-generated method stub
		
	}

	public List<FieldTypes> getNonblankFields() {
		ArrayList<FieldTypes> fields = new ArrayList<FieldTypes>();
		if (hasField(getEmail())) {
			fields.add(FieldTypes.MAIL);
		}
		if (hasField(getPhoneWork())) {
			fields.add(FieldTypes.PHONE_WORK);
		}
		if (hasField(getAddress())) {
			fields.add(FieldTypes.ADDRESS);
		}
		return fields;
	}

	private boolean hasField(String value) {
		return value != null && !TextUtils.isEmpty(value.trim()) && !value.equals(Person.NOT_GIVEN);
	}


}
