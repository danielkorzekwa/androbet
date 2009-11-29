package dk.androbet.betex.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dk.androbet.betex.utils.PriceUtil;
import dk.androbet.betex.utils.PriceUtil.PriceRange;

public class PriceUtilTest {

	@Test
	public void tesValidatePrice() {
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
		
		assertEquals(990, PriceUtil.validatePrice(priceRanges, 999.99, PriceUtil.ROUND_DOWN), 0);
		
		assertEquals(1.64, PriceUtil.validatePrice(priceRanges, 1.6317991631799162, PriceUtil.ROUND_UP), 0);
		
		assertEquals(1.88, PriceUtil.validatePrice(priceRanges, 1.88, PriceUtil.ROUND_DOWN), 0);
		assertEquals(1.87, PriceUtil.validatePrice(priceRanges, 1.87, PriceUtil.ROUND_DOWN), 0);
		
		
		assertEquals(1.01, PriceUtil.validatePrice(priceRanges, 0.5, PriceUtil.ROUND_UP), 0);
		assertEquals(1.01, PriceUtil.validatePrice(priceRanges, 0.5, PriceUtil.ROUND_DOWN), 0);
		
		assertEquals(1000, PriceUtil.validatePrice(priceRanges, 2000, PriceUtil.ROUND_UP), 0);
		assertEquals(1000, PriceUtil.validatePrice(priceRanges, 2000, PriceUtil.ROUND_DOWN), 0);
		
		assertEquals(1.01, PriceUtil.validatePrice(priceRanges, 1.01, PriceUtil.ROUND_UP), 0);
		assertEquals(1.01, PriceUtil.validatePrice(priceRanges, 1.01, PriceUtil.ROUND_DOWN), 0);
		
		assertEquals(1000, PriceUtil.validatePrice(priceRanges, 1000, PriceUtil.ROUND_UP), 0);
		assertEquals(1000, PriceUtil.validatePrice(priceRanges, 1000, PriceUtil.ROUND_DOWN), 0);
			
		assertEquals(2.0, PriceUtil.validatePrice(priceRanges, 2.0, PriceUtil.ROUND_UP), 0);
		assertEquals(2.0, PriceUtil.validatePrice(priceRanges, 2.0, PriceUtil.ROUND_DOWN), 0);
		
		assertEquals(2.02, PriceUtil.validatePrice(priceRanges, 2.01, PriceUtil.ROUND_UP), 0);
		assertEquals(2.0, PriceUtil.validatePrice(priceRanges, 2.01, PriceUtil.ROUND_DOWN), 0);
		
		assertEquals(44, PriceUtil.validatePrice(priceRanges, 43, PriceUtil.ROUND_UP), 0);
		assertEquals(44, PriceUtil.validatePrice(priceRanges, 44, PriceUtil.ROUND_UP), 0);
		assertEquals(46, PriceUtil.validatePrice(priceRanges, 45, PriceUtil.ROUND_UP), 0);
		
		assertEquals(860, PriceUtil.validatePrice(priceRanges, 856, PriceUtil.ROUND_UP), 0);
		
		assertEquals(1000, PriceUtil.validatePrice(priceRanges, 999.99, PriceUtil.ROUND_UP), 0);
		assertEquals(990, PriceUtil.validatePrice(priceRanges, 999.99, PriceUtil.ROUND_DOWN), 0);
		
	}


}
