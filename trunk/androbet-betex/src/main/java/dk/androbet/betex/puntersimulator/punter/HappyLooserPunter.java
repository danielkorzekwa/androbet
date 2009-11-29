package dk.androbet.betex.puntersimulator.punter;

import java.util.List;
import java.util.Random;

import dk.androbet.betex.Betex;
import dk.androbet.betex.model.BetType;
import dk.androbet.betex.utils.PriceUtil;
import dk.androbet.betex.utils.PriceUtil.PriceRange;

public class HappyLooserPunter implements PunterStrategy {

	private Random random = new Random(new Random(System.currentTimeMillis()).nextLong());
	private List<PriceRange> priceRanges = PriceUtil.getPriceRanges();

	@Override
	public void process(int userId, double realRunnerPrice, byte runnerPriceTrend,Betex betex) {

		double priceToBack1 = PriceUtil.validatePrice(priceRanges, realRunnerPrice-0.03, PriceUtil.ROUND_DOWN);
		double priceToBack2 = PriceUtil.validatePrice(priceRanges, priceToBack1-0.03, PriceUtil.ROUND_DOWN);
		double priceToBack3 = PriceUtil.validatePrice(priceRanges, priceToBack2-0.03, PriceUtil.ROUND_DOWN);
		
		double priceToLay1 = PriceUtil.validatePrice(priceRanges,realRunnerPrice+0.03, PriceUtil.ROUND_UP);
		double priceToLay2 = PriceUtil.validatePrice(priceRanges, priceToLay1+0.04, PriceUtil.ROUND_UP);
		double priceToLay3 = PriceUtil.validatePrice(priceRanges, priceToLay2+0.06, PriceUtil.ROUND_UP);
		
		int nextInt = random.nextInt(6);
		
		if(nextInt==0) betex.placeBet(userId, BetType.B, 2 + random.nextInt(2),priceToBack1);
		if(nextInt==1) betex.placeBet(userId, BetType.B, 2 + random.nextInt(2),priceToBack2);
		if(nextInt==2) betex.placeBet(userId, BetType.B, 2 + random.nextInt(2),priceToBack3);
		
		if(nextInt==3) betex.placeBet(userId, BetType.L, 2 + random.nextInt(2), priceToLay1);	
		if(nextInt==4) betex.placeBet(userId, BetType.L, 2 + random.nextInt(2), priceToLay2);	
		if(nextInt==5) betex.placeBet(userId, BetType.L, 2 + random.nextInt(2), priceToLay3);	
	}

}
