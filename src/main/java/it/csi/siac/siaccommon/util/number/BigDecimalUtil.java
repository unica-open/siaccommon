/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.number;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public final class BigDecimalUtil {
		
	private BigDecimalUtil() {
	}

	public static BigDecimal parseBigDecimal(String value) {
		try {
			return StringUtils.isNotBlank(value) ? new BigDecimal(value) : null;
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static BigDecimal getDefaultZero(BigDecimal bigDecimal) {
		return getDefault(bigDecimal, BigDecimal.ZERO);
	}
	
	public static BigDecimal getDefault(BigDecimal bigDecimal, BigDecimal defaultValue) {
		return bigDecimal == null ? defaultValue : bigDecimal;
	}
	
	public static BigDecimal sum(BigDecimal...bigDecimals) {
		return bigDecimals == null ? null : sum(Arrays.asList(bigDecimals));
	}

	public static BigDecimal neg(BigDecimal bigDecimal) {
		return bigDecimal == null ? null : bigDecimal.negate();
	}

	public static BigDecimal sum(Collection<BigDecimal> bigDecimals) {

		if (bigDecimals == null || bigDecimals.isEmpty()) {
			return null;
		}
		
		BigDecimal sum = BigDecimal.ZERO;
		
		for (BigDecimal n: bigDecimals) {
			if (n == null) {
				return null;
			}

			sum = sum.add(n);
		}
		
		return sum;
	}

	public static BigDecimal sum(BigDecimal[] bigDecimalArr, BigDecimal...bigDecimals) {
		return sum(bigDecimalArr).add(sum(bigDecimals));
	}
	
	public static BigDecimal sum(Collection<BigDecimal> bigDecimalColl, BigDecimal...bigDecimals) {
		BigDecimal a = sum(bigDecimalColl);
		
		return a == null ? null : sum(bigDecimals, a);
	}
	
	public static BigDecimal percent(BigDecimal bigDecimal) {
		return bigDecimal == null ? null : bigDecimal.divide(BigDecimal.valueOf(100));
	}
	
	public static BigDecimal percent(double value) {
		return percent(BigDecimal.valueOf(value));
	}
	
	public static BigDecimal product(BigDecimal...bigDecimals) {
		
		BigDecimal product = BigDecimal.ONE;
		
		for (BigDecimal n: bigDecimals) {
			if (n == null) {
				return null;
			}
			
			product.multiply(n);
		}
		
		return product;
	}
	
	public static BigDecimal inv(BigDecimal bigDecimal, MathContext mc) {
		return bigDecimal == null ? null : BigDecimal.ONE.divide(bigDecimal, mc);
	}

	public static BigDecimal log(BigDecimal bigDecimal) {
		return bigDecimal == null ? null : new BigDecimal(Math.log(bigDecimal.doubleValue()));
	}

	public static BigDecimal exp(BigDecimal bigDecimal) {
		return bigDecimal == null ? null : new BigDecimal(Math.exp(bigDecimal.doubleValue()));
	}

	public static BigDecimal pow(BigDecimal base, BigDecimal exp) {
		if (base == null || exp == null) {
			return null;
		}
		
		double pow = Math.pow(base.doubleValue(), exp.doubleValue());

		if (Double.isNaN(pow)) {
			throw new IllegalArgumentException(String.format("Bad arguments %f %f, result is NaN", base, exp) );
		}
		
		return new BigDecimal(pow);
	}

	public static BigDecimal round2HalfUp(BigDecimal bigDecimal) {
		return roundHalfUp(bigDecimal, 2);
	}

	public static BigDecimal roundHalfUp(BigDecimal bigDecimal, int scale) {
		return bigDecimal == null ? null : bigDecimal.setScale(2, RoundingMode.HALF_UP);
	}
	
	public static boolean equalWithinTolerance(BigDecimal bigDecimal1, BigDecimal bigDecimal2, double tolerance) {
		return bigDecimal1.subtract(bigDecimal2).abs().compareTo(BigDecimal.valueOf(tolerance)) < 0;
	}
}
