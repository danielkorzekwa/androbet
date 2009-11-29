package dk.androbet.betex.model;

import java.util.Collections;
import java.util.List;

/** Best back and lay prices represented as a ladder.*/
public class LadderPrices {

	private final List<RunnerPrice> pricesToLay;
	private final List<RunnerPrice> pricesToBack;

	public LadderPrices(List<RunnerPrice> pricesToLay, List<RunnerPrice> pricesToBack) {
		this.pricesToLay = pricesToLay;
		this.pricesToBack = pricesToBack;
		
		Collections.sort(pricesToLay);
		Collections.sort(pricesToBack);
	}

	public List<RunnerPrice> getPricesToLay() {
		return pricesToLay;
	}

	public List<RunnerPrice> getPricesToBack() {
		return pricesToBack;
	}
	
	public RunnerPrice getBestPriceToBack() {
		return pricesToBack.get(pricesToBack.size() - 1);
	}
	public RunnerPrice getBestPriceToLay() {
		return pricesToLay.get(0);
	}
}
