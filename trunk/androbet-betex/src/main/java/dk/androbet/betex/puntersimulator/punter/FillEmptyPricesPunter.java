package dk.androbet.betex.puntersimulator.punter;

import java.util.List;
import java.util.Random;

import dk.androbet.betex.Betex;
import dk.androbet.betex.model.BetType;
import dk.androbet.betex.model.LadderPrices;
import dk.androbet.betex.model.RunnerPrice;
import dk.androbet.betex.utils.LadderPricesUtil;
import dk.androbet.betex.utils.PriceUtil;
import dk.androbet.betex.utils.PriceUtil.PriceRange;

public class FillEmptyPricesPunter implements PunterStrategy {

	private List<PriceRange> priceRanges = PriceUtil.getPriceRanges();
	private Random random = new Random(new Random(System.currentTimeMillis()).nextLong());

	@Override
	public void process(int userId, double realRunnerPrice, byte runnerPriceTrend, Betex betex) {
		List<RunnerPrice> runnerPrices = betex.getRunnerPrices();
		LadderPrices ladderPrice = LadderPricesUtil.getThreeBackLayRunnerPrices(runnerPrices);

		if (runnerPrices.size() == 0) {
			betex.placeBet(userId, BetType.B, 2 + random.nextInt(2), PriceUtil.validatePrice(priceRanges, 2.01,
					PriceUtil.ROUND_UP));
			betex.placeBet(userId, BetType.L, 2 + random.nextInt(2), PriceUtil.validatePrice(priceRanges, 1.99,
					PriceUtil.ROUND_DOWN));
			return;
		}

		for (RunnerPrice runnerPrice : ladderPrice.getPricesToBack()) {
			if (runnerPrice.getTotalToBack() == 0) {
				try {
					betex.placeBet(userId, BetType.L, 2 + random.nextInt(2), runnerPrice.getPrice());
				} catch (Exception e) {
				}
			}
		}
		for (RunnerPrice runnerPrice : ladderPrice.getPricesToLay()) {
			if (runnerPrice.getTotalToLay() == 0) {
				try {
					betex.placeBet(userId, BetType.B, 2 + random.nextInt(2), runnerPrice.getPrice());
				} catch (Exception e) {
				}
			}
		}

		/** price is going down */
		if (runnerPriceTrend == 0) {
			if ((1 / ladderPrice.getBestPriceToBack().getPrice() - 1 / ladderPrice.getBestPriceToLay().getPrice()) > 0.01) {
				try {
					betex.placeBet(userId, BetType.B, 2 + random.nextInt(2), PriceUtil.validatePrice(priceRanges,
							ladderPrice.getBestPriceToBack().getPrice() + 0.01, PriceUtil.ROUND_UP));
				} catch (Exception e) {
				}
			}
		}
		/** price is going up. */
		else if (runnerPriceTrend == 2) {
			if ((1 / ladderPrice.getBestPriceToBack().getPrice() - 1 / ladderPrice.getBestPriceToLay().getPrice()) > 0.01) {
				try {
					betex.placeBet(userId, BetType.L, 2 + random.nextInt(2), PriceUtil.validatePrice(priceRanges,
							ladderPrice.getBestPriceToLay().getPrice() - 0.01, PriceUtil.ROUND_DOWN));
				} catch (Exception e) {
				}
			}
		}

	}
}
