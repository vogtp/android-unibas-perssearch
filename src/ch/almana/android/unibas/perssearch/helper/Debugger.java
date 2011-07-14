package ch.almana.android.unibas.perssearch.helper;

public class Debugger {

	public static boolean DEBUG = false;

	public enum QueryType {
		DEFAULT, LOCAL_DATA, FIXED_QUERY
	};

	public static final QueryType DUMMY_QUERY = QueryType.LOCAL_DATA;
	public static final String QUERY_STRING = "patrick vogt";

	public static final String JSON_STRING = "[{\"apphtmlurl\":\"http:\\/\\/perssearch.unibas.ch\",\"nappexist\":1,\"appdesc\":\"Personensuche der Universität Basel\",\"lastchange\":\"July, 12 2011 00:00:00\",\"appid\":3,\"appname\":\"PersSearch\",\"appicon\":\"perssearch.png\",\"NativeApp\":{\"ndata\":\"\",\"nlastchange\":\"July, 12 2011 00:00:00\",\"napploc\":\"ch.almana.android.unibas.perssearch\",\"nappid\":1,\"appid\":3,\"nappsystem\":\"Android\"},\"applabel\":\"PersSearch\"},{\"apphtmlurl\":\"http:\\/\\/www.twitter.com\\/unibasel\",\"nappexist\":0,\"appdesc\":\"Twitter Unibasel\",\"lastchange\":\"July, 12 2011 00:00:00\",\"appid\":4,\"appname\":\"Twitter\",\"appicon\":\"twitter.png\",\"applabel\":\"Twitter\"},{\"apphtmlurl\":\"http:\\/\\/www.facebook.com\\/unibas\",\"nappexist\":1,\"appdesc\":\"FaceBook Unibasel\",\"lastchange\":\"July, 12 2011 00:00:00\",\"appid\":6,\"appname\":\"Facebook\",\"appicon\":\"facebook.png\",\"applabel\":\"Facebook\"},{\"apphtmlurl\":\"http:\\/\\/flexiform2.unibas.ch\",\"nappexist\":0,\"appdesc\":\"FlexiForm\",\"lastchange\":\"July, 12 2011 00:00:00\",\"appid\":7,\"appname\":\"FlexiForm\",\"appicon\":\"flexiform.png\",\"applabel\":\"FlexiForm\"},{\"apphtmlurl\":1.0,\"nappexist\":1,\"appdesc\":\"Raumverwaltung\",\"lastchange\":\"July, 12 2011 00:00:00\",\"appid\":11,\"appname\":\"Raumverwaltung (LZM)\",\"appicon\":\"raum.png\",\"applabel\":\"Raumverwaltung\"}] ";
	public static final String JSON_STRING2 = " {\"registeredaddress\":\"\",\"unibaschhomefax\":\"\",\"homepostaladdress\":\"\",\"sn\":\"Vogt\",\n"
			+ "	 \"postaladdress\":\"Universitätsrechenzentrum$Linux Gruppe$Mail Team$Klingelbergstrasse 70$CH-4056 Basel\",\n"
			+ "	 \"dn\":\"uid=vogtp, ou=people, ou=intern, ou=psearch, dc=unibas, dc=ch\",\"uid\":\"vogtp\",\n"
			+ "	 \"facsimiletelephonenumber\":\"+41 61 267 22 82\",\"unibaschsalutation\":\"M\",\"labeleduri\":\"\",\"rating\":1.0,\n"
			+ "	 \"telephonenumber\":4.1612671522E10,\"cn\":\"Patrick Vogt\",\"mail\":\"Patrick.Vogt@unibas.ch\",\n"
			+ "	 \"displayname\":\"Patrick Vogt\",\"unibaschhomemail\":\"\",\"edupersonaffiliation\":\"\",\"mailalternateaddress\":\"\",\n"
			+ "	 \"givenname\":\"Patrick\",\"unibaschorgroledisplay\":\"\",\"employeetype\":\"\",\"homephone\":\"\",\n"
			+ "	 \"unibaschhomemobile\":\"\",\"edupersonorgunitdn\":\"ou=urz,ou=units,ou=psearch,dc=unibas,dc=ch\"},";
}
