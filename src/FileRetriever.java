import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileRetriever {

	String pubPath = "C:/Users/oshao/Desktop/publicholiday.csv";
	String lunPath = "C:/Users/oshao/Desktop/lunarholiday.csv";
	String splitBy = ",";
	ArrayList<String> pubholidays = new ArrayList<String>();
	ArrayList<String> lunholidays = new ArrayList<String>();

	public ArrayList<String> getPubStr() {
		
		ArrayList<String> pubholidays = new ArrayList<String>();
		
		File pubFile = new File(pubPath);
		
		try {
			BufferedReader pubBf = new BufferedReader(new InputStreamReader(new FileInputStream(pubFile), "UTF-8"));
			String line = null;
			while ((line = pubBf.readLine()) != null) {
				pubholidays.add(line);
			}
			pubBf.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pubholidays;
	}

	public ArrayList<String> getLunStr() {
		
		ArrayList<String> lunholidays = new ArrayList<String>();
		
		File pubFile = new File(pubPath);
		
		try {
			BufferedReader lunBf = new BufferedReader(new InputStreamReader(new FileInputStream(pubFile), "UTF-8"));
			String line = null;
			while ((line = lunBf.readLine()) != null) {
				lunholidays.add(line);
			}
			lunBf.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pubholidays;
	}

	public static void main(String args[]) {

	}

}
