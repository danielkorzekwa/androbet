package dk.androbet.betex.utils;

import java.util.List;

import dk.androbet.betex.model.LadderPrices;
import dk.androbet.betex.model.RunnerPrice;

/** Returns avg price based on best back/lay prices
 * 
 * @author korzekwad
 *
 */
public class AvgPriceUtil {

	/** Returns avg price based on best back/lay prices. */
	public static double getAvgPrice(List<RunnerPrice> runnerPrices) {
		LadderPrices ladderPrices = LadderPricesUtil.getThreeBackLayRunnerPrices(runnerPrices);

		double bestToLay = ladderPrices.getBestPriceToLay().getPrice();
		double bestToBack = ladderPrices.getBestPriceToBack().getPrice();
		double currentProb = (((1 / bestToBack) + (1 / bestToLay)) / 2);
		
		return 1/currentProb;
	}
}
