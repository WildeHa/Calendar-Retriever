import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public class CalendarReader {

	private static final String URL_CHINESE = "https://www.googleapis.com/calendar/v3/calendars/94ieggkmksdc6kp89kbikon26b55icfi@import.calendar.google.com/events?key=AIzaSyDZHjQaA6zKJLet4ydvZfk7nJ7YnQ_DpZg";
	private static final String URL_EN = "https://www.googleapis.com/calendar/v3/calendars/en.hong_kong%23holiday@group.v.calendar.google.com/events?key=AIzaSyDZHjQaA6zKJLet4ydvZfk7nJ7YnQ_DpZg";
	private static final String URL_SIM = "https://www.googleapis.com/calendar/v3/calendars/1cqaqs2oqsh65iagv0ec9e1p7recnvbo@import.calendar.google.com/events?key=AIzaSyCfvHtX98SRZNosJrbv9TrzUNwF9srSwV0";

	private static final String URL_GOOGLE = "https://www.googleapis.com/calendar/v3/calendars/zh.hong_kong%23holiday@group.v.calendar.google.com/events?key=AIzaSyDZHjQaA6zKJLet4ydvZfk7nJ7YnQ_DpZg";
	private static final String URL_GOOGLE_ENG = "https://www.googleapis.com/calendar/v3/calendars/en.hong_kong%23holiday@group.v.calendar.google.com/events?key=AIzaSyDZHjQaA6zKJLet4ydvZfk7nJ7YnQ_DpZg";

	private static final int EN = 0;
	private static final int CT = 1;
	private static final int CS = 2;

	public static ArrayList<String> holiday = new ArrayList<>();

	private static String[][] publicHoliday = { { "0101", "New Year's Day", "一月一日", "元旦" },
			{ "0214", "Valentine's Day", "情人節", "情人节" }, { "0401", "April Fool's Day", "愚人節", "愚人节" },
			{ "0404", "Children's Day", "兒童節", "儿童节" }, { "0501", "Labour Day", "勞動節", "劳动节" },
			{ "0701", "Hong Kong Special Administrative Region Establishment Day", "香港特別行政區成立紀念日", "香港特别行政区成立纪念日" },
			{ "1001", "National Day of the People's Republic of China", "國慶日", "国庆节" },
			{ "1225", "Christmas Day", "聖誕節", "圣诞节" }, { "1226", "Boxing Day", "節禮日", "节礼日" },
			{ "1231", "New Year's Eve", "新年前夕", "新年前夕" } };

	private static String[][] lunarHoliday = { { "0101", "Chinese Lunar New Year's Day", "農曆年初一", "农历年初一" },
			{ "0102", "Second day of Chinese Lunar New Year", "農曆年初二", "农历年初二" },
			{ "0103", "Third day of Chinese Lunar New Year", "農曆年初三", "农历年初三" },
			{ "0505", "Dragon Boat Festival", "端午節", "端午节" }, { "0815", "MidAutumn Festival", "中秋節", "中秋节" },
			{ "0909", "Double Ninth Festival", "重陽節", "重阳节" }, { "0408", "Buddha's Birthday", "佛誕節", "佛诞节" },
			{ "0115", "Lantern Festival", "元宵節", "元宵节" }, { "0707", "Chinese Valentine's Day", "七夕情人節", "七夕情人节" },
			{ "0715", "Spirit festival", "鬼節", "鬼节" }, { "1228", "", "", "" } };

	// get map day = holiday from json based on url
	public static LinkedHashMap<String, String> getMapfromUrl(String urlPath) throws Exception {

		URL url = new URL(urlPath);

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		List<String> holidays = new ArrayList<String>();
		List<String> dates = new ArrayList<String>();

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		StringBuffer sb = new StringBuffer();

		while ((str = bufferedReader.readLine()) != null) {
			if (str.contains("start")) {

				str = bufferedReader.readLine();
				dates.add(getTargetString(str));

			}

			if (str.contains("summary")) {

				String hosub = str.substring(0, str.length() - 1);
				holidays.add(getTargetString(hosub) + "\n");

			}
			sb.append(str + "\n");
		}

		for (int i = 0; i < dates.size(); i++) {
			map.put(dates.get(i), holidays.get(i + 1));
		}

		reader.close();
		connection.disconnect();
		return map;
	}

	// get key map of day = holiday from hard code
	public static LinkedHashMap<String, ArrayList<String>> initHardCode(String[][] str) {

		LinkedHashMap<String, ArrayList<String>> hardMap = new LinkedHashMap<String, ArrayList<String>>();
		for (int i = 0; i < str.length; i++) {

			for (int j = 0; j < str[i].length - 1; j++) {

				holiday.add(str[i][j + 1]);

			}

			hardMap.put(str[i][0], holiday);
			holiday = new ArrayList<>();
		}

		return hardMap;
	}

	public static LinkedHashMap<String, ArrayList<String>> initPulicHoliday(String date) {

		LinkedHashMap<String, ArrayList<String>> daHoMap = initHardCode(publicHoliday);
		LinkedHashMap<String, String> goMap = new LinkedHashMap<String, String>();
		try {
			goMap = getMapfromUrl(URL_GOOGLE_ENG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> easterList = new ArrayList<String>();
		ArrayList<String> chingList = new ArrayList<String>();

		ArrayList<String> moDayList = new ArrayList<String>();
		moDayList.add("Mother's Day");
		moDayList.add("母親節");
		moDayList.add("母亲节");
		String modate = getMotherDay(date);
		daHoMap.put(modate, moDayList);

		ArrayList<String> faDayList = new ArrayList<String>();
		faDayList.add("Father's Day");
		faDayList.add("父親節");
		faDayList.add("父亲节");
		String fadate = getFathersDay(date);
		daHoMap.put(fadate, faDayList);

		String easterDate = getEasternDay(date, goMap);
		easterList.add("Easter Sunday");
		easterList.add("復活節");
		easterList.add("复活节");
		daHoMap.put(easterDate, easterList);

		String chingDate = getChingDay(date, goMap);
		chingList.add("Ching Ming Festival");
		chingList.add("清明節");
		chingList.add("清明节");
		daHoMap.put(chingDate, chingList);

		return daHoMap;
	}

	public static LinkedHashMap<String, ArrayList<String>> initLunarHoliday(String date) {

		LinkedHashMap<String, ArrayList<String>> lunarMap = initHardCode(lunarHoliday);

		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		LunarConverter lc = new LunarConverter();

		String lunarDate = lc.Converter(date);
		String lunMonth = lunarDate.substring(0, 2);

		// check days of lunar 12th month has 29 or 30 days
		if (lunMonth.equals("12")) {
			if (LunarConverter.monthDays(Integer.parseInt(year), Integer.parseInt(month)) == 29) {
				ArrayList<String> newYearList = new ArrayList<String>();
				newYearList.add("Chinese New Year's Eve ");
				newYearList.add("除夕");
				newYearList.add("除夕");
				lunarMap.put("1229", newYearList);
			} else if (LunarConverter.monthDays(Integer.parseInt(year), Integer.parseInt(month)) == 30) {
				ArrayList<String> newYearList = new ArrayList<String>();
				newYearList.add("Chinese New Year's Eve ");
				newYearList.add("除夕");
				newYearList.add("除夕");
				lunarMap.put("1230", newYearList);
			}
		}
		return lunarMap;
	}

	public static List<Map<String, String>> getHolidayFromDate_EN(String date) throws Exception {

		LinkedHashMap<String, ArrayList<String>> pubDaMap = initPulicHoliday(date);
		LinkedHashMap<String, ArrayList<String>> luDaMap = initLunarHoliday(date);

		LinkedHashMap<String, String> pubResult = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> luResult = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> speResult = new LinkedHashMap<String, String>();

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		ArrayList<String> enHoliday = new ArrayList<>();
		
		// get lunar date
		LunarConverter lc = new LunarConverter();
		String lunDate = lc.Converter(date);
		//get date for hard code 
		String puDay = date.substring(4);

		// lunar Holiday
		if (luDaMap.containsKey(lunDate)) {

			enHoliday = luDaMap.get(lunDate);
			luResult.put("EN", enHoliday.get(EN));
			luResult.put("CS", enHoliday.get(CS));
			luResult.put("CT", enHoliday.get(CT));
			result.add(luResult);
		}

		// public holiday with format mmdd the key is different from lunar
		if (pubDaMap.containsKey(puDay)) {

			enHoliday = pubDaMap.get(puDay);
			pubResult.put("EN", enHoliday.get(EN));
			pubResult.put("CS", enHoliday.get(CS));
			pubResult.put("CT", enHoliday.get(CT));
			result.add(pubResult);
		}
		// check overlap holiday
		if (pubDaMap.containsKey(date)) {
			enHoliday = pubDaMap.get(date);
			speResult.put("EN", enHoliday.get(EN));
			speResult.put("CS", enHoliday.get(CS));
			speResult.put("CT", enHoliday.get(CT));
			result.add(speResult);
		}
		System.out.println(result);
		return result;
	}

	// get wanted string from url json
	public static String getTargetString(String summary) {

		summary = summary.replace("\"", "");
		summary = summary.replace("-", "");
		String[] split = summary.split(":");
		return split[1].trim();

	}

	// get father's day according to date's year
	public static String getFathersDay(String date) {

		int year = Integer.parseInt(date.substring(0, 4));
		String month = date.substring(4, 6);
		String day = date.substring(date.length() - 2);

		Calendar cal = Calendar.getInstance();
		cal.set(year, 05, 01); // month start from 0!!!

		int dayofWeek = cal.get(Calendar.DAY_OF_WEEK);
		int thSunday = -(dayofWeek) + 7 + 2 + 14;

		date = date.replace(month, "06");
		date = date.replace(day, String.format("%02d", thSunday));
		return date;
	}

	// get mother's day according to date's year
	public static String getMotherDay(String date) {

		int year = Integer.parseInt(date.substring(0, 4));
		String month = date.substring(4, 6);

		String day = date.substring(date.length() - 2);

		Calendar cal = Calendar.getInstance();
		cal.set(year, 04, 01);// month start from 0!!!!

		int dayofWeek = cal.get(Calendar.DAY_OF_WEEK);
		int thSunday = -(dayofWeek) + 7 + 2;

		date = date.replace(month, "05");
		date = date.replace(day, String.format("%02d", thSunday));
		return date;
	}

	public static String getEasternDay(String date, LinkedHashMap<String, String> map) {

		String year = date.substring(0, 4);
		String result = new String();
		for (String str : map.keySet()) {
			String value = map.get(str);
			if (value.contains("Easter Sunday")) {
				if (str.contains(year)) {
					result = str;
				}
			}
		}
		return result;
	}

	public static String getChingDay(String date, LinkedHashMap<String, String> map) {

		String year = date.substring(0, 4);
		String result = new String();
		for (String str : map.keySet()) {
			String value = map.get(str);
			if (value.contains("Ching Ming Festival")) {
				if (str.contains(year)) {
					result = str;
				}
			}
		}
		return result;
	}

	// public static String getDzDay(String date) {
	//
	// int difDay = 108;
	// Calendar cal = Calendar.getInstance();
	// SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd",
	// Locale.ENGLISH);
	// Date curDate = new Date();
	// try {
	// curDate = format.parse(date);
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block

	// e.printStackTrace();
	// }
	//
	// cal.setTime(curDate);
	// cal.set(Calendar.DATE, cal.get(Calendar.DATE) - difDay);
	// String dzDate = cal.getTime().toString();
	// System.out.println(dzDate);
	// return dzDate;
	// }

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String testDate = "20160404";
		getHolidayFromDate_EN(testDate);
	}

}
