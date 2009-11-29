package dk.androbet.betex.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dk.androbet.betex.model.Bet;
import dk.androbet.betex.model.BetStatus;
import dk.androbet.betex.model.BetType;
import dk.androbet.betex.utils.ProfitLossUtil;

public class ProfitLossUtilTest {

	@Test
	public void testCalculateProfit1() {
		List<Bet> bets = new ArrayList<Bet>();
		bets.add(createBet(BetType.B, 3, 2));
		
		assertEquals(4,ProfitLossUtil.calculateProfit(bets, true),0);
		assertEquals(-2,ProfitLossUtil.calculateProfit(bets, false),0);
	}
	
	@Test
	public void testCalculateProfit2() {
		List<Bet> bets = new ArrayList<Bet>();
		bets.add(createBet(BetType.L, 3, 2));
		
		assertEquals(-4,ProfitLossUtil.calculateProfit(bets, true),0);
		assertEquals(2,ProfitLossUtil.calculateProfit(bets, false),0);
	}
	
	@Test
	public void testCalculateProfit3() {
		List<Bet> bets = new ArrayList<Bet>();
		bets.add(createBet(BetType.B, 3, 2));
		bets.add(createBet(BetType.L, 3, 2));
		
		assertEquals(0,ProfitLossUtil.calculateProfit(bets, true),0);
		assertEquals(0,ProfitLossUtil.calculateProfit(bets, false),0);
	}
	
	@Test 
	public void testCalculateAvgPrice() {
		
		assertEquals(3,ProfitLossUtil.calculateAvgPrice(4,-2),0);
		assertEquals(3,ProfitLossUtil.calculateAvgPrice(-4,2),0);
		
		assertEquals(4,ProfitLossUtil.calculateAvgPrice(3,-1),0);
		assertEquals(4,ProfitLossUtil.calculateAvgPrice(-3,1),0);
		
		assertEquals(-1,ProfitLossUtil.calculateAvgPrice(-1,-1),0);
		assertEquals(-1,ProfitLossUtil.calculateAvgPrice(-1,0),0);
		assertEquals(-1,ProfitLossUtil.calculateAvgPrice(0,-1),0);
		assertEquals(-1,ProfitLossUtil.calculateAvgPrice(0,0),0);
		assertEquals(-1,ProfitLossUtil.calculateAvgPrice(0,1),0);
		assertEquals(-1,ProfitLossUtil.calculateAvgPrice(1,1),0);
		assertEquals(-1,ProfitLossUtil.calculateAvgPrice(1,0),0);	
	}
	
	private Bet createBet(BetType type,double price, double size) {
		Bet bet = new Bet();
		bet.setBetType(type);
		bet.setBetStatus(BetStatus.M);
		bet.setPrice(price);
		bet.setSize(size);
		
		return bet;
	}
}
