package br.com.eztest.core.util

import java.text.DateFormat
import java.text.ParseException

public class DateTimeUtil {

    public static Calendar toCalendar(String source, DateFormat format) {
        Calendar date = Calendar.getInstance();
        try {
            date.setTimeInMillis(format.parse(source).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        return date;
    }

}
