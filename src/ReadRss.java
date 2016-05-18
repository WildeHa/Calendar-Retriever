
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;

/*
 *  Just Follow the Comment 
 */
public class ReadRss {

	static String lan = new String();
	static String title = new String();
	static String weather = new String();
	static Date releasetime = new Date();
	static String meanUV = new String();
	static String radiation = new String();
	
	static LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
	

	public static LinkedHashMap<String, String> readfromLink(String link) throws Exception {

		ArrayList<String> locale = new ArrayList<String>();
		ArrayList<String> temperature = new ArrayList<String>();

		SyndFeed feed = null;
		URL url = new URL(link);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream is = connection.getInputStream();

		if ("gzip".equals(connection.getContentEncoding())) {
			is = new GZIPInputStream(is);
		}
		InputSource source = new InputSource(is);
		SyndFeedInput input = new SyndFeedInput();
		feed = input.build(source);

		// Iterate to get entry
		SyndEntry entry = null;
		for (Iterator<?> iter = feed.getEntries().iterator(); iter.hasNext();) {
			entry = (SyndEntry) iter.next();
		}
		// Language
		lan = feed.getLanguage();

		// title
		title = feed.getTitle();

		// Weather
		weather = entry.getDescription().getValue();

		// publish time
		releasetime = entry.getPublishedDate();

		// Remove html tags
		String plaintext = weather.replaceAll("＼＼&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]",
				"");

		// get temperature locally
		String[] strMap = plaintext.split(";");

		// get temperature of observatory
		String[] summary = strMap[0].split(":");

		StringBuffer sb = new StringBuffer();
		for (int m = 0; m < summary.length - 1; m++) {

			sb.append(summary[m]);

		}
		String weSummary = sb.toString(); // rest string besides observatory
		String weNumber = weSummary.replaceAll("[^-?0-9]+", " ");
		ArrayList<String> suNumberList = new ArrayList<String>(Arrays.asList(weNumber.split(" ")));

		// para in weather summary
		String time = suNumberList.get(1);
		String temp = suNumberList.get(2);
		String humidity = suNumberList.get(3);

		// get meanUV

		StringBuffer nubf = new StringBuffer();
		String last = new String();

		// if mean uv less than 1, the size will plus one, use sb to forge "0.x"
		if (suNumberList.size() == 6) {
			int index = suNumberList.size() - 1;
			last = suNumberList.get(index);
			nubf.append("0.").append(last);
			meanUV = nubf.toString();
		} else {
			meanUV = suNumberList.get(suNumberList.size() - 1);
		}

		/*
		 * Get Radiation from sub-string of the 5th of summary it is separate by
		 * enter and get the first word is result
		 */
		String[] subforRa = summary[5].split("\n", 2);
		// Radiation in summary
		radiation = subforRa[0];

		// observatory temperature is last one in summary
		String observatory = summary[summary.length - 1];

		char[] obcrs = observatory.toCharArray();
		int obindex = 0;
		for (int l = 1; l < obcrs.length; l++) {

			// index of first number appearance
			if (Character.isDigit(obcrs[l])) {
				obindex = l - 1;
			}

		}
		locale.add(observatory.substring(0, obindex));
		temperature.add(observatory.substring(obindex));

		for (int i = 1; i < strMap.length; i++) {

			// index of first number appearance
			int index = 0;

			char[] crs = strMap[i].toCharArray();
			for (int j = 0; j < crs.length; j++) {
				if (Character.isDigit(crs[j])) {
					index = j - 1;
				}
			}
			locale.add(strMap[i].substring(0, index));
			temperature.add(strMap[i].substring(index));
		}
		for (int k = 0; k < locale.size(); k++) {
			map.put(locale.get(k), temperature.get(k));
		}
		System.out.println(title);
		System.out.println(lan);
		System.out.println(releasetime);
//		System.out.print(weSummary);
		System.out.println(map);
		return map;
	}

	public static void main(String[] args) throws Exception {
		final String URL_EN = "http://rss.weather.gov.hk/rss/CurrentWeather.xml";
		final String URL_ZH = "http://rss.weather.gov.hk/rss/CurrentWeather_uc.xml";
		readfromLink(URL_EN);
	}
}
