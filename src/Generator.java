import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Generator extends ReadRss {

	ReadRss rss = new ReadRss();
		 
	// file name use current time stamp
	static String fileName = new SimpleDateFormat("yyyyMMddhhmm'.txt'").format(new Date());

	static String path = "C:/Users/oshao/Desktop/testfile" + "/" + fileName;
	
	final static String URL_EN = "http://rss.weather.gov.hk/rss/CurrentWeather.xml";
	
	public static void writetoFile() throws Exception {
		File file = new File(path);
		FileWriter fw = new FileWriter(file.getAbsolutePath());
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			readfromLink(URL_EN);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bw.write(lan+title);
		
//		bw.write(map);
		bw.close();
	}

	public static void main(String[] args) {
		
		try {
			readfromLink(URL_EN);
			writetoFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
