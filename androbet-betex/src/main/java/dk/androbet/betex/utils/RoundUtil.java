package dk.androbet.betex.utils;

import java.math.BigDecimal;

public class RoundUtil {

	public static double round(double x, int scale) {
		 try {
	            return (new BigDecimal
	                   (Double.toString(x))
	                   .setScale(scale, BigDecimal.ROUND_HALF_UP))
	                   .doubleValue();
	        } catch (NumberFormatException ex) {
	            if (Double.isInfinite(x)) {
	                return x;          
	            } else {
	                return Double.NaN;
	            }
	        }
	}
}
