package ch.almana.android.unibas.perssearch.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import ch.almana.android.unibas.perssearch.helper.Logger;
import ch.almana.android.unibas.perssearch.model.Person;

public class PerssearchLoader {


	private static final String PERSSEARCH_QUERY_URL = "https://perssearch.unibas.ch/?Json=1&query=";
	private static final PerssearchLoader instance = new PerssearchLoader();

	private List<Person> personsList;
	private String lastQUery;

	public static PerssearchLoader getInstance() {
		return instance;
	}

	private PerssearchLoader() {
		super();
	}

	private String getUri(String uri) {
		Logger.v("Loging >" + uri + "<");
		long start = System.currentTimeMillis();
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = new HttpGet(uri);
		BufferedHttpEntity bhe = null;
		BufferedReader content = null;
		try {
			HttpResponse response = httpClient.execute(request);
			bhe = new BufferedHttpEntity(response.getEntity());
			content = new BufferedReader(new InputStreamReader(bhe.getContent()));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = content.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();

		} catch (Exception e) {
			Logger.e("Error on perssearch", e);
			return "";
		} finally {

			if (bhe != null) {
				try {
					bhe.consumeContent();
				} catch (IOException e) {
					Logger.e("Error closing");
				}
			}
			if (content != null) {
				try {
					content.close();
				} catch (IOException e) {
					Logger.e("Error closing");
				}
			}

			long duration = System.currentTimeMillis() - start;
			duration /= 1000l;
			Logger.w("perssearch search " + duration + " s");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Person> getMatches(String query) {
		if (query == null) {
			return Collections.EMPTY_LIST;
		}
		if (query.equals(lastQUery)) {
			return personsList;
		}
		List<Person> list = null;
		if (query != null && query.trim().length() > 2) {
			String payload = getUri(PERSSEARCH_QUERY_URL + Uri.encode(query));
			Logger.d("Got payload: " + payload);
			try {
				list = parseJson(payload);

			} catch (JSONException e) {
				Logger.e("Cannot parse payload a json", e);
			}
		}

		lastQUery = query;
		personsList = list == null ? Collections.EMPTY_LIST : list;
		return personsList;
	}

	private List<Person> parseJson(String payload) throws JSONException {
		JSONArray jsonArray = new JSONArray(payload);
		List<Person> list = new ArrayList<Person>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			list.add(new Person(object));
		}
		return list;
	}

	public Person getPersonById(String id) {
		if (id == null) {
			return Person.EMPTY;
		}
		for (Person person : personsList) {
			if (id.equals(person.getId())) {
				return person;
			}
		}
		return Person.EMPTY;
	}

}
