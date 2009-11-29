package dk.androbet.betex.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dk.androbet.betex.model.LadderPrices;
import dk.androbet.betex.model.RunnerPrice;
import dk.androbet.betex.utils.LadderPricesUtil;

public class LadderPricesUtilTest {

	@Test
	public void testNoRunnerPrices() {
		LadderPrices prices = LadderPricesUtil.getThreeBackLayRunnerPrices(new ArrayList<RunnerPrice>());
		assertEquals(3, prices.getPricesToBack().size());
		assertEquals(3, prices.getPricesToLay().size());
		
		assertEquals(980, prices.getPricesToLay().get(0).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(0).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToLay().get(0).getTotalToLay(),0);
		
		assertEquals(990, prices.getPricesToLay().get(1).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(1).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToLay().get(1).getTotalToLay(),0);
		
		assertEquals(1000, prices.getPricesToLay().get(2).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(2).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToLay().get(2).getTotalToLay(),0);
		
		assertEquals(1.01, prices.getPricesToBack().get(0).getPrice(),0);
		assertEquals(0, prices.getPricesToBack().get(0).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(0).getTotalToLay(),0);
		
		assertEquals(1.02, prices.getPricesToBack().get(1).getPrice(),0);
		assertEquals(0, prices.getPricesToBack().get(1).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(1).getTotalToLay(),0);
		
		assertEquals(1.03, prices.getPricesToBack().get(2).getPrice(),0);
		assertEquals(0, prices.getPricesToBack().get(2).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(2).getTotalToLay(),0);
	}
	
	@Test
	public void test1ToLayAndTwoToBackPrices() {
		
		List<RunnerPrice> runnerPrices = new ArrayList<RunnerPrice>();
		runnerPrices.add(new RunnerPrice(2.2,0,5));
		runnerPrices.add(new RunnerPrice(2.0,3,0));
		runnerPrices.add(new RunnerPrice(1.9,8,0));
		
		LadderPrices prices = LadderPricesUtil.getThreeBackLayRunnerPrices(runnerPrices);
		assertEquals(3, prices.getPricesToBack().size());
		assertEquals(3, prices.getPricesToLay().size());
		
		assertEquals(2.2, prices.getPricesToLay().get(0).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(0).getTotalToBack(),0);
		assertEquals(5, prices.getPricesToLay().get(0).getTotalToLay(),0);
		
		assertEquals(2.22, prices.getPricesToLay().get(1).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(1).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToLay().get(1).getTotalToLay(),0);
		
		assertEquals(2.24, prices.getPricesToLay().get(2).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(2).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToLay().get(2).getTotalToLay(),0);
		
		assertEquals(1.89, prices.getPricesToBack().get(0).getPrice(),0);
		assertEquals(0, prices.getPricesToBack().get(0).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(0).getTotalToLay(),0);
		
		assertEquals(1.9, prices.getPricesToBack().get(1).getPrice(),0);
		assertEquals(8, prices.getPricesToBack().get(1).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(1).getTotalToLay(),0);
		
		assertEquals(2.0, prices.getPricesToBack().get(2).getPrice(),0);
		assertEquals(3, prices.getPricesToBack().get(2).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(2).getTotalToLay(),0);
		
	}
	
	@Test
	public void test3ToLayAnd3ToBackPrices() {
		
		List<RunnerPrice> runnerPrices = new ArrayList<RunnerPrice>();
		runnerPrices.add(new RunnerPrice(2.3,0,5));
		runnerPrices.add(new RunnerPrice(2.2,0,10));
		runnerPrices.add(new RunnerPrice(2.1,0,15));
		runnerPrices.add(new RunnerPrice(2.0,3,0));
		runnerPrices.add(new RunnerPrice(1.9,6,0));
		runnerPrices.add(new RunnerPrice(1.5,9,0));
		
		LadderPrices prices = LadderPricesUtil.getThreeBackLayRunnerPrices(runnerPrices);
		assertEquals(3, prices.getPricesToBack().size());
		assertEquals(3, prices.getPricesToLay().size());
		
		assertEquals(2.1, prices.getPricesToLay().get(0).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(0).getTotalToBack(),0);
		assertEquals(15, prices.getPricesToLay().get(0).getTotalToLay(),0);
		
		assertEquals(2.2, prices.getPricesToLay().get(1).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(1).getTotalToBack(),0);
		assertEquals(10, prices.getPricesToLay().get(1).getTotalToLay(),0);
		
		assertEquals(2.3, prices.getPricesToLay().get(2).getPrice(),0);
		assertEquals(0, prices.getPricesToLay().get(2).getTotalToBack(),0);
		assertEquals(5, prices.getPricesToLay().get(2).getTotalToLay(),0);
		
		assertEquals(1.5, prices.getPricesToBack().get(0).getPrice(),0);
		assertEquals(9, prices.getPricesToBack().get(0).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(0).getTotalToLay(),0);
		
		assertEquals(1.9, prices.getPricesToBack().get(1).getPrice(),0);
		assertEquals(6, prices.getPricesToBack().get(1).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(1).getTotalToLay(),0);
		
		assertEquals(2.0, prices.getPricesToBack().get(2).getPrice(),0);
		assertEquals(3, prices.getPricesToBack().get(2).getTotalToBack(),0);
		assertEquals(0, prices.getPricesToBack().get(2).getTotalToLay(),0);
		
	}
}
