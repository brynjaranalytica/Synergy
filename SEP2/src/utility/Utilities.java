package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Nicolai Onov on 5/12/2017.
 */
public class Utilities {

    public static java.util.Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-d").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
