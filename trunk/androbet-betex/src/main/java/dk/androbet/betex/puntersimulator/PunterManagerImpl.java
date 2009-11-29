package dk.androbet.betex.puntersimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import dk.androbet.betex.Betex;
import dk.androbet.betex.puntersimulator.punter.Punter;
import dk.androbet.betex.utils.PriceUtil;
import dk.androbet.betex.utils.PriceUtil.PriceRange;

public class PunterManagerImpl implements PunterManager {

	private Random random = new Random(new Random(System.currentTimeMillis()).nextLong());
	private List<PriceRange> priceRanges = PriceUtil.getPriceRanges();
	
	private final Betex betex;

	/** Represents the real runner price, allows to simulate the trend on a betting exchange. */
	private double realRunnerPrice = 2;

	/** 0 - going down, 1 - drifting, 2 - going up. */
	private byte runnerPriceTrend = (byte)random.nextInt(3);

	private List<Punter> punters = new ArrayList<Punter>();

	public PunterManagerImpl(Betex betex) {
		this.betex = betex;
		 System.out.println(runnerPriceTrend);
	}

	@Override
	public void addPunter(Punter punter) {
		punters.add(punter);
	}

	@Override
	public void process(int numOfTimes) {

		for (int i = 0; i < numOfTimes; i++) {

			int trendChange = random.nextInt(1800);
			if (trendChange < 3) {
				 runnerPriceTrend=(byte)trendChange;
				 System.out.println(runnerPriceTrend);
			}

			boolean priceIsChanged=false;
			/** update real runner price */
			if (random.nextInt(20) == 0) {
				if (runnerPriceTrend == 0) {
					realRunnerPrice = PriceUtil.validatePrice(priceRanges, realRunnerPrice-0.01, PriceUtil.ROUND_DOWN);
				} else if (runnerPriceTrend == 1) {
					// do nothing, price is drifting
				} else if (runnerPriceTrend == 2) {
					realRunnerPrice = PriceUtil.validatePrice(priceRanges, realRunnerPrice+0.01, PriceUtil.ROUND_UP);
				}
				priceIsChanged=true;
			}

			/**Call punters.*/
			for (Punter punter : punters) {
				
				if (priceIsChanged || random.nextFloat() < punter.getPunterCallProb()) {
					punter.getPunterStrategy().process(punter.getUserId(), realRunnerPrice, runnerPriceTrend,betex);
				}
			}
		}
	}

}
