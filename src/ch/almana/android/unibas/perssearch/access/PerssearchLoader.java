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
import ch.almana.android.unibas.perssearch.helper.Settings;
import ch.almana.android.unibas.perssearch.helper.Settings.SearchType;
import ch.almana.android.unibas.perssearch.model.DummyPerson;
import ch.almana.android.unibas.perssearch.model.Person;

public class PerssearchLoader {


	private static final String PERSSEARCH_QUERY_URL = "https://perssearch.unibas.ch/?Json=1&query=";
	private static final String PERSSEARCH_QUERY_URL_POST = "https://perssearch.unibas.ch/?Json=1";
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
		// HttpPost request = new HttpPost(PERSSEARCH_QUERY_URL_POST);
		// HttpParams params = request.getParams();
		// params.setParameter("query", "vogt");
		// params.setIntParameter("Json", 1);
		// params.setParameter("s", "STAFF");
		// params.setParameter("sicherheit", "schwach");
		HttpUriRequest request = new HttpGet(uri + "&sicherheit=schwach");
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
			Logger.d("perssearch search " + duration + " s");
		}
	}

	public List<Person> getMatches(String query) {
		return getMatches(query, true);
	}

	@SuppressWarnings("unchecked")
	public List<Person> getMatches(String query, boolean searchAll) {
		if (query == null) {
			return Collections.EMPTY_LIST;
		}
		SearchType searchType = Settings.getInstance().getSearchType();
		if (searchAll) {
			searchType = SearchType.ALL;
		}
		String querytype;
		switch (searchType) {
		case EMPLOYEES:
			querytype = "&s=STAFF";
			break;
		case STUDENTS:
			querytype = "&s=STUDS";
			break;

		default:
			querytype = "";
			break;
		}

		if (query.equals(lastQUery)) {
			return personsList;
		}
		List<Person> list = null;
		if (query != null && query.trim().length() > 2) {
			String payload = getUri(PERSSEARCH_QUERY_URL + Uri.encode(query) + querytype);
			Logger.d("Got payload: " + payload);
			try {
				list = parseJson(payload);

			} catch (JSONException e) {
				Logger.e("Cannot parse payload a json", e);
			}
		}

		lastQUery = query;
		if (list != null) {
			personsList = list;
			DummyPerson dummy;
			switch (searchType) {
			case EMPLOYEES:
				dummy = DummyPerson.PERSON_STUDS_TOO;
				dummy.setQuery(query);
				personsList.add(dummy);
				break;
			case STUDENTS:
				dummy = DummyPerson.PERSON_STAFF_TOO;
				dummy.setQuery(query);
				personsList.add(dummy);
				break;

			default:
				break;
			}
		} else {
			personsList = Collections.EMPTY_LIST;
		}
		return personsList;
	}

	private List<Person> parseJson(String payload) throws JSONException {
		JSONArray jsonArray = new JSONArray(payload);
		List<Person> list = new ArrayList<Person>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject object = (JSONObject) jsonArray.get(i);
			list.add(new Person(object.toString()));
		}
		return list;
	}

}
