package dk.androbet.betex.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.androbet.betex.model.LadderPrices;
import dk.androbet.betex.model.RunnerPrice;

/**
 * Returns always 3 best prices (three back and three lay prices), if there are no 3 lay or 3 back prices then extra prices with nothing to bet are returned.
 * 
 * @author korzekwad
 * 
 */
public class LadderPricesUtil {

	/**
	 * @param runnerPrices
	 *            all runner back/lay prices
	 * @return Returns always 3 best prices (three back and three lay prices).
	 */
	public static LadderPrices getThreeBackLayRunnerPrices(List<RunnerPrice> runnerPrices) {

		Collections.sort(runnerPrices);
		
		/**Prepare best prices to lay*/
		List<RunnerPrice> pricesToLay = new ArrayList<RunnerPrice>();
		Integer bestPriceToLayIndex = getBestPriceToLayIndex(runnerPrices);
		if(bestPriceToLayIndex!=null) {
			while(bestPriceToLayIndex<runnerPrices.size() && pricesToLay.size()<3) {
				RunnerPrice runnerPrice = runnerPrices.get(bestPriceToLayIndex);
				pricesToLay.add(runnerPrice);
				bestPriceToLayIndex++;
			}
			while(pricesToLay.size()<3) {
				double nextPrice = PriceUtil.validatePrice(PriceUtil.getPriceRanges(), pricesToLay.get(pricesToLay.size()-1).getPrice()+0.01, PriceUtil.ROUND_UP);
				pricesToLay.add(new RunnerPrice(nextPrice,0,0));
			}
			
		}
		else {
			double lastPrice=Integer.MAX_VALUE;
			for(int i=0;i<3;i++) {
				lastPrice = PriceUtil.validatePrice(PriceUtil.getPriceRanges(), lastPrice-0.01d, PriceUtil.ROUND_DOWN);
				pricesToLay.add(new RunnerPrice(lastPrice,0,0));
			}
		}
		
		/**Prepare best prices to back*/
		List<RunnerPrice> pricesToBack = new ArrayList<RunnerPrice>();
		Integer bestPriceToBackIndex = getBestPriceToBackIndex(runnerPrices);
		if(bestPriceToBackIndex!=null) {
			while(bestPriceToBackIndex>-1 && pricesToBack.size()<3) {
				RunnerPrice runnerPrice = runnerPrices.get(bestPriceToBackIndex);
				pricesToBack.add(runnerPrice);
				bestPriceToBackIndex--;
			}
			while(pricesToBack.size()<3) {
				double nextPrice = PriceUtil.validatePrice(PriceUtil.getPriceRanges(), pricesToBack.get(pricesToBack.size()-1).getPrice()-0.01, PriceUtil.ROUND_DOWN);
				pricesToBack.add(new RunnerPrice(nextPrice,0,0));
			}
		}
		else {
			double lastPrice=0;
			for(int i=0;i<3;i++) {
				lastPrice = PriceUtil.validatePrice(PriceUtil.getPriceRanges(), lastPrice+0.01, PriceUtil.ROUND_UP);
				pricesToBack.add(new RunnerPrice(lastPrice,0,0));
			}
		}
		
		return new LadderPrices(pricesToLay,pricesToBack);
	}

	/**Returns index of best price to back. 
	 * 
	 * @param smallerThan
	 * @param runnerPrices must be sorted
	 * @return null if nothing to back is available
	 */
	private static Integer getBestPriceToBackIndex(List<RunnerPrice> runnerPrices) {
		Integer bestPriceIndex=null;
		for (int i=0;i<runnerPrices.size();i++) {
			RunnerPrice price = runnerPrices.get(i);
			if (price.getTotalToBack()>0 && (bestPriceIndex == null || price.getPrice() >= runnerPrices.get(bestPriceIndex).getPrice())) {
				bestPriceIndex = i;
			}
		}
		return bestPriceIndex;
	}

	/**Returns index of best price to lay.
	 * 
	 * @param runnerPrices must be sorted
	 * @return null if nothing to lay is available
	 */
	private static Integer getBestPriceToLayIndex(List<RunnerPrice> runnerPrices) {
		Integer bestPriceIndex=null;

		for (int i=0;i<runnerPrices.size();i++) {
			RunnerPrice price = runnerPrices.get(i);
			if (price.getTotalToLay()>0 && (bestPriceIndex == null || price.getPrice() <= runnerPrices.get(bestPriceIndex).getPrice())) {
				bestPriceIndex = i;
			}
		}
		return bestPriceIndex;
	}

}
