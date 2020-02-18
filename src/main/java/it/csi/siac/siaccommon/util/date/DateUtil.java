/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.date;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {
	private DateUtil() {
		// Prevent instantiation
	}
	
	public static Date getEndYearDay(Date d) {
		return DateUtils.addDays(DateUtils.addYears(d, 1), -1);
	}

	public static boolean nowBetween(Date start, Date end) {
		if (start == null)
			return false;

		Date now = new Date();

		if (now.before(start))
			return false;

		if (end == null)
			return true;

		return now.before(end);
	}
}
