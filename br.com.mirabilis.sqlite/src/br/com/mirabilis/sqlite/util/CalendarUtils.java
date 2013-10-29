package br.com.mirabilis.sqlite.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class utils for calendar.
 * 
 * @author Rodrigo Simões Rosa
 */
public class CalendarUtils {

	/**
	 * Return {@link String} that one {@link Calendar}
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getString(Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(calendar.getTime());
	}
}
