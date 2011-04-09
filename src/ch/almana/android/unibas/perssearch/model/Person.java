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
	public static final String KEY_MAIL_WORK = "mail";
	public static final String KEY_MAIL_INSTITUTE = "mailalternateaddress";
	public static final String KEY_PHONE_WORK = "telephonenumber";
	public static final String KEY_FAX_WORK = "facsimiletelephonenumber";

	private static final String KEY_PHONE_HOME = "homephone";
	private static final String KEY_PHONE_MOBILE = "unibaschhomemobile";
	// private static final String KEY_PHONE_FAX = null;
	public static final String KEY_ADDRESS = "postaladdress";
	public static final String KEY_STUDENT_TYPE = "edupersonaffiliation";
	public static final String KEY_WEBPAGE = "labeleduri";

	public static enum FieldTypes {
		MAIL_WORK, MAIL_INSTITUTE, PHONE_WORK, PHONE_MOBILE, PHONE_HOME, FAX_WORK, ADDRESS, WEBPAGE
	};

	private static final String ADDRESS_SEPARATOR = "$";

	private JSONObject jsonObject;

	private String jsonString;

	private String streetCity;

	private String phoneWork;

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

	private String getJsonEntryPhone(String keyName) {
		try {
			String d = jsonObject.getString(keyName);
			int idx = d.lastIndexOf("E");
			if (idx > -1) {
				StringBuilder s = new StringBuilder();
				s.append(d.substring(0, 1));
				s.append(d.substring(2, d.lastIndexOf("E")));
				d = s.toString();
			}
			idx = d.indexOf(" ");
			if (idx == -1) {
				int lenght = d.length();
				StringBuilder s = new StringBuilder(d);
				if (lenght == 11) {
					s.insert(2, " ").insert(5, " ").insert(9, " ").insert(12, " ");
					s.insert(0, "+");
				} else if (lenght == 9) {
					s.insert(2, " ").insert(6, " ").insert(9, " ");
					s.insert(0, "0");
				}
				d = s.toString();
			}
			return d;
		} catch (Exception e) {
			return NOT_GIVEN;
		}
	}

	public String getName() {
		return getJsonEntry(KEY_NAME);
	}

	public String getEmailWork() {
		return getJsonEntry(KEY_MAIL_WORK);
	}

	public String getEmailInstitute() {
		return getJsonEntry(KEY_MAIL_INSTITUTE);
	}

	public String getPhoneWork() {
		if (phoneWork == null) {
			phoneWork = getJsonEntryPhone(KEY_PHONE_WORK);
		}
		return phoneWork;
	}

	public String getPhoneHome() {
		return getJsonEntry(KEY_PHONE_HOME);
	}

	public String getPhoneMobile() {
		return getJsonEntry(KEY_PHONE_MOBILE);
	}

	public String getAddress() {
		return getJsonEntry(KEY_ADDRESS).replace(ADDRESS_SEPARATOR, "\n");
	}

	public String getStudentType() {
		return getJsonEntry(KEY_STUDENT_TYPE);
	}

	public String getWebpage() {
		return getJsonEntry(KEY_WEBPAGE);
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
			description = getEmailWork();
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

	public List<FieldTypes> getNonblankFields() {
		ArrayList<FieldTypes> fields = new ArrayList<FieldTypes>();
		if (hasField(getEmailWork())) {
			fields.add(FieldTypes.MAIL_WORK);
		}
		if (hasField(getEmailInstitute())) {
			fields.add(FieldTypes.MAIL_INSTITUTE);
		}
		if (hasField(getPhoneWork())) {
			fields.add(FieldTypes.PHONE_WORK);
		}
		if (hasField(getPhoneMobile())) {
			fields.add(FieldTypes.PHONE_MOBILE);
		}
		if (hasField(getPhoneHome())) {
			fields.add(FieldTypes.PHONE_HOME);
		}
		if (hasField(getFaxWork())) {
			fields.add(FieldTypes.FAX_WORK);
		}
		if (hasField(getAddress())) {
			fields.add(FieldTypes.ADDRESS);
		}
		if (hasField(getWebpage())) {
			fields.add(FieldTypes.WEBPAGE);
		}
		return fields;
	}

	private boolean hasField(String value) {
		return value != null && !TextUtils.isEmpty(value.trim()) && !value.equals(Person.NOT_GIVEN);
	}

	public String getFaxWork() {
		return getJsonEntry(KEY_FAX_WORK);
	}

	public String getStreetCity() {
		if (streetCity == null) {
			String a = getAddress();
			int idx = a.lastIndexOf("\n", a.lastIndexOf("\n") - 1);
			streetCity = a.substring(idx + 1, a.length());
		}
		return streetCity;
	}

}
