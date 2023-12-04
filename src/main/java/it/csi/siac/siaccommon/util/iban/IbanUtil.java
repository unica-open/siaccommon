/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.iban;

import org.apache.commons.lang3.StringUtils;

public final class IbanUtil {
	private IbanUtil() {
		// Prevent instantiation
	}
	
	public static String checkIban(String iban) {
		if (StringUtils.isBlank(iban) || iban.length() < 5)
			return "lunghezza minore di 5 caratteri";

		String s = iban.substring(4) + iban.substring(0, 4);

		int i, r;

		for (i = 0, r = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			int k;
			
			if ('0' <= c && c <= '9') {
				if (i == s.length() - 4 || i == s.length() - 3)
					return "i primi due caratteri devono essere lettere";

				k = c - '0';
			} else if ('A' <= c && c <= 'Z') {
				if (i == s.length() - 2 || i == s.length() - 1)
					return "terzo e quarto carattere devono essere numeri";

				k = c - ('A' - 10);
			} else
				return "sono ammessi soltanto numeri e lettere maiuscole";

			r = (k > 9) ? (100 * r + k) % 97 : (10 * r + k) % 97;
		}

		return r == 1 ? null : "codice di controllo errato";
	}
}
