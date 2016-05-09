import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class test {

	public static Date translate(String date){
		Date currentDate = new Date();
		DateFormat format = new SimpleDateFormat("yyyyMMdd",Locale.ENGLISH);
		try {
			currentDate = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date);
		return currentDate;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		translate("2015-02-02");
	}

}
