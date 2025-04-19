/**
 * File: DateUtil.java
 * Author: Khilfi
 * Description:
 * This file is used for handling date data type.
 */
package com.delluna.dellunahotel.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd"; // Standard format (adjust as needed)

    // 1️⃣ Get current date as a formatted string
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }

    // 2️⃣ Format a Date object into a string
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    // 3️⃣ Parse a string into a Date object
    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.parse(dateStr);
    }

    // 4️⃣ Calculate the difference (in days) between two dates
    public static long getDaysDifference(Date startDate, Date endDate) {
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return diffInMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days
    }
}
