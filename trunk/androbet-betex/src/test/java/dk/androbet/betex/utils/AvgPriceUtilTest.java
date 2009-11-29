package dk.androbet.betex.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.androbet.betex.model.RunnerPrice;
import dk.androbet.betex.utils.AvgPriceUtil;

public class AvgPriceUtilTest {

	private List<RunnerPrice> runnerPrices = new ArrayList<RunnerPrice>();

	@Before
	public void setUp() {
		/** Add to back prices */
		runnerPrices.add(new RunnerPrice(2, 5, 0));
		runnerPrices.add(new RunnerPrice(2.1, 5, 0));

		/** Add to lay prices */
		runnerPrices.add(new RunnerPrice(2.3, 0, 5));
		runnerPrices.add(new RunnerPrice(2.4, 0, 5));
	}

	@Test
	public void testGetAvgPrice() {
		assertEquals(2.195, AvgPriceUtil.getAvgPrice(runnerPrices), 3);
	}

}
