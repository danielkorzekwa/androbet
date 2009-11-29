package dk.androbet.betex.puntersimulator;

import org.junit.Before;
import org.junit.Test;

import dk.androbet.betex.BetexImpl;
import dk.androbet.betex.model.LadderPrices;
import dk.androbet.betex.puntersimulator.punter.FillEmptyPricesPunter;
import dk.androbet.betex.puntersimulator.punter.HappyLooserPunter;
import dk.androbet.betex.puntersimulator.punter.Punter;
import dk.androbet.betex.puntersimulator.punter.TradingPunter;
import dk.androbet.betex.utils.AvgPriceUtil;
import dk.androbet.betex.utils.LadderPricesUtil;
import dk.androbet.betex.utils.RoundUtil;

public class PunterManagerImplTest {

	private BetexImpl betex = new BetexImpl();

	/** Virtual punters that simulate liquidity. */
	private PunterManager punterManager = new PunterManagerImpl(betex);

	@Before
	public void setUp() {
		punterManager.addPunter(new Punter(102,0.05,new TradingPunter()));	
		punterManager.addPunter(new Punter(101,0.05,new HappyLooserPunter()));	
		punterManager.addPunter(new Punter(103,0.5,new FillEmptyPricesPunter()));			
	}

	@Test
	public void testProcess() {
		for (int i = 0; i < 1800; i++) {
			System.out.println("\nAvg price: " + RoundUtil.round(AvgPriceUtil.getAvgPrice(betex.getRunnerPrices()), 2));
			punterManager.process(1);
		}

		/** Display ladder price */
		System.out.println("Ladder prices:");
		LadderPrices ladderPrices = LadderPricesUtil.getThreeBackLayRunnerPrices(betex.getRunnerPrices());
		for (int i = 2; i >= 0; i--) {
			System.out.println(ladderPrices.getPricesToLay().get(i).getPrice() + ":" + ladderPrices.getPricesToLay().get(i).getTotalToLay());
		}
		for (int i = 2; i >= 0; i--) {
			System.out.println(ladderPrices.getPricesToBack().get(i).getPrice() + ":" + ladderPrices.getPricesToBack().get(i).getTotalToBack());
		}

		System.out.println("\nAvg price: " + RoundUtil.round(AvgPriceUtil.getAvgPrice(betex.getRunnerPrices()), 2));
		System.out.println("\nTotal matched: " + betex.getTotalMatched());
	}

}
