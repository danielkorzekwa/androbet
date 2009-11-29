package dk.androbet.betex.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns the closest valid price
 * 
 * @author korzekwad
 * 
 */
public class PriceUtil {

	public static final int ROUND_DOWN = 0;
	public static final int ROUND_UP = 1;
	
	/**
	 * Checks if price is validate. If not, it returns the closest correct value.
	 * 
	 * @param priceRanges
	 *            - Ranges must be closed, 1-2, 2-4, 4-6 and so on
	 * @param price
	 *            - price to be validated
	 * @param roundingMode
	 *            - define price rounding mode.
	 * @return validated price
	 */
	public static double validatePrice(List<PriceRange> priceRanges, double price, int roundingMode) {

	if (priceRanges.size() > 0) {

			// Check if price is out of bounds (below minimum or maximum of
			// priceRanges
			if (price < priceRanges.get(0).getMinimum()) {
				return priceRanges.get(0).getMinimum();
			} else if (price > priceRanges.get(priceRanges.size() - 1).getMaximum()) {
				return priceRanges.get(priceRanges.size() - 1).getMaximum();
			}

			for (PriceRange priceRange : priceRanges) {
				if (price >= priceRange.getMinimum() && price <= priceRange.getMaximum()) {
					// If there is no reminder, it means that price is OK
					double reminder = price - (priceRange.getIncrRate() * (int) RoundUtil.round((price / priceRange.getIncrRate()),5));
					if (RoundUtil.round(reminder,5) == 0) {
						return RoundUtil.round(price, 2);
					} else {
						if (roundingMode == ROUND_DOWN) {
							return RoundUtil.round(price - reminder, 2);
						} else if (roundingMode == ROUND_UP) {
							return RoundUtil.round(price - reminder + priceRange.getIncrRate(), 2);
						} else {
							throw new IllegalArgumentException("Rounding mode not recognized: " + roundingMode + ".");
						}
					}
				}
			}

			throw new IllegalArgumentException("Price range for price not found. Price: " + price + ".");
		} else {
			throw new IllegalArgumentException("Price range list is empty.");
		}
	}

	/** List of valid BetFair bet prices */
	public static List<PriceRange> getPriceRanges() {
		List<PriceRange> priceRanges = new ArrayList<PriceRange>();
		PriceRange pr1 = new PriceRange(1.01, 2.0, 0.01);
		PriceRange pr2 = new PriceRange(2.0, 3.0, 0.02);
		PriceRange pr3 = new PriceRange(3.0, 4.0, 0.05);
		PriceRange pr4 = new PriceRange(4.0, 6.0, 0.1);
		PriceRange pr5 = new PriceRange(6.0, 10.0, 0.2);
		PriceRange pr6 = new PriceRange(10.0, 20.0, 0.5);
		PriceRange pr7 = new PriceRange(20.0, 30.0, 1.0);
		PriceRange pr8 = new PriceRange(30.0, 50.0, 2.0);
		PriceRange pr9 = new PriceRange(50.0, 100.0, 5.0);
		PriceRange pr10 = new PriceRange(100.0, 1000.0, 10.0);

		priceRanges.add(pr1);
		priceRanges.add(pr2);
		priceRanges.add(pr3);
		priceRanges.add(pr4);
		priceRanges.add(pr5);
		priceRanges.add(pr6);
		priceRanges.add(pr7);
		priceRanges.add(pr8);
		priceRanges.add(pr9);
		priceRanges.add(pr10);

		return priceRanges;
	}
	
	public static class PriceRange {

		private double minimum;
		private double maximum;
		private double incrRate;

		public PriceRange(double minimum, double maximum, double incrRate) {
			this.minimum = minimum;
			this.maximum = maximum;
			this.incrRate = incrRate;
		}

		public double getMinimum() {
			return minimum;
		}

		public void setMinimum(double minimum) {
			this.minimum = minimum;
		}

		public double getMaximum() {
			return maximum;
		}

		public void setMaximum(double maximum) {
			this.maximum = maximum;
		}

		public double getIncrRate() {
			return incrRate;
		}

		public void setIncrRate(double incrRate) {
			this.incrRate = incrRate;
		}

	}

}
