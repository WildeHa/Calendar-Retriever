import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class readerTest {

	String filePath = "C:/Users/oshao/Desktop/holiday.csv";
	String splitBy = ",";
	ArrayList<String> holidays = new ArrayList<String>();

	public void Run() {
		try {
			File fileDir = new File(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] holiday = line.split(splitBy);
				holidays.add(holiday[1]);
				holidays.add(holiday[2]);
				holidays.add(holiday[3]);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(holidays);
	}

	public static void main(String args[]) {
		readerTest obj = new readerTest();
		obj.Run();
	}

}
