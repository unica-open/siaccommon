/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {
	private DateUtil() {}
	
	private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
	
	private static final ThreadLocal<Map<String, DateFormat>> TL_DATE_FORMATS = new ThreadLocal<Map<String,DateFormat>>() {
		@Override
		protected Map<String,DateFormat> initialValue() {
			return new HashMap<String, DateFormat>();
		}
	};
	
	public static Date parseDate(String value) {
		return parseDate(value, DEFAULT_DATE_PATTERN);
	}
	
	public static Date parseDate(String value, String pattern) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		Date result = null;
		
		try {
			result = getDateFormat(pattern).parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Conversione in java.util.Date fallita per il valore " + value, e);
		}

		Date date1900 = new GregorianCalendar(1900, Calendar.DECEMBER, 31).getTime();
		
		if (!result.after(date1900)) {
			throw new IllegalArgumentException("Conversione in java.util.Date fallita per il valore " + value);
		}

		return result;
	}

	public static String formatDate(Date value) {
		try {
			return getDateFormat(DEFAULT_DATE_PATTERN).format(value);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private static DateFormat getDateFormat(String pattern) {
		Map<String, DateFormat> dfs = TL_DATE_FORMATS.get();
		DateFormat df = dfs.get(pattern);
		if(df == null) {
			df = new SimpleDateFormat(pattern, Locale.ITALY);
			dfs.put(pattern, df);
		}
		return df;
	}
	
	
	
	public static Date getEndYearDay(Date d) {
		return DateUtils.addDays(DateUtils.addYears(d, 1), -1);
	}

	public static Date now() {
		return new Date();
	}
	
	public static Date today() {
		return truncateAtDay(now());
	}

	public static Date truncateAtDay(Date d) {
		return DateUtils.truncate(d, Calendar.DAY_OF_MONTH);
	}
	
	public static Date createDate(int day, int month, int year) {
		return new GregorianCalendar(year, month-1, day).getTime();
	}
	
	
	
	
	
	public static boolean nowBetween(Date d1, Date d2) {
		Date now = now();
		return !before(now, d1) && !after(now, d2);
	}
	
	public static boolean beforeNow(Date d) {
		return compareToNow(d) < 0;
	}
	
	public static boolean afterNow(Date d) {
		return compareToNow(d) > 0;
	}
	
	public static int compareToNow(Date d) {
		return compareTo(d, now());
	}
	

	
	
	
	
	public static boolean todayBetween(Date d1, Date d2) {
		Date today = today();
		return !before(today, d1) && !after(today, d2);
	}
	
	public static boolean beforeToday(Date d) {
		return compareToToday(d) < 0;
	}
	
	public static boolean afterToday(Date d) {
		return compareToToday(d) > 0;
	}
	
	public static int compareToToday(Date d) {
		return compareTo(d, today());
	}
	
	

	public static boolean todayBetweenDays(Date d1, Date d2) {
		return todayBetween(truncateAtDay(d1), truncateAtDay(d2));
	}
	
	public static boolean dayBeforeToday(Date d) {
		return beforeToday(truncateAtDay(d));
	}
	
	public static boolean dayWithinToday(Date d) {
		return !afterToday(d);
	}
	
	public static boolean dayAfterToday(Date d) {
		return afterToday(truncateAtDay(d));
	}
	
	public static boolean dayFromToday(Date d) {
		return !beforeToday(d);
	}
	
	public static int dayCompareToToday(Date d) {
		return compareToToday(truncateAtDay(d));
	}
	
	public static boolean dayEqualsToday(Date d) {
		return dayCompareToToday(d) == 0;
	}
	
	
	
	
	public static boolean betweenDays(Date d, Date d1, Date d2) {
		return between(truncateAtDay(d), truncateAtDay(d1), truncateAtDay(d2));
	}
	
	public static boolean dayBeforeDay(Date d1, Date d2) {
		return before(truncateAtDay(d1), truncateAtDay(d2));
	}
	
	public static boolean dayWithinDay(Date d1, Date d2) {
		return !dayAfterDay(d1, d2);
	}
	
	public static boolean dayAfterDay(Date d1, Date d2) {
		return after(truncateAtDay(d1), truncateAtDay(d2));
	}
	
	public static boolean dayFromDay(Date d1, Date d2) {
		return !dayBeforeDay(d1, d2);
	}
	
	public static int dayCompareToDay(Date d1, Date d2) {
		return compareTo(truncateAtDay(d1), truncateAtDay(d2));
	}

	public static boolean daysEqual(Date d1, Date d2) {
		return dayCompareToDay(d1, d2) == 0;
	}
	
	
	
	
	public static boolean between(Date d, Date d1, Date d2) {
		return !before(d, d1) && !after(d, d2);
	}

	public static boolean before(Date d1, Date d2) {
		return compareTo(d1, d2) < 0;
	}
	
	public static boolean after(Date d1, Date d2) {
		return compareTo(d1, d2) > 0;
	}
	
	public static int compareTo(Date d1, Date d2) {
		if (d1 == null && d2 == null) {
			return 0;
		}

		if (d1 == null) {
			return -1;
		}

		if (d2 == null) {
			return +1;
		}
		
		return d1.compareTo(d2);
	}
	
	public static boolean equals(Date d1, Date d2) {
		return compareTo(d1, d2) == 0;
	}
	
	
	
	
	public static Calendar getCalendarInstanceForDate(Date d) {
		if (d == null) {
			return null;
		}
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(d);
		
		return c;
	}

	public static Date getFirstDayInMonth(Date d) {
		if (d == null) {
			return null;
		}
		
		Calendar c = getCalendarInstanceForDate(d);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	public static Integer getYear(Date d) {
		return d == null ? null : getCalendarInstanceForDate(d).get(Calendar.YEAR);
	}

	public static Integer getMonth(Date d) {
		return d == null ? null : getCalendarInstanceForDate(d).get(Calendar.MONTH) + 1;
	}
	
	public static Date min(Date d1, Date d2) {
		return compareTo(d1, d2) < 0 ? d1 : d2;
	}
	
	public static Date max(Date d1, Date d2) {
		return compareTo(d1, d2) > 0 ? d1 : d2;
	}
}
