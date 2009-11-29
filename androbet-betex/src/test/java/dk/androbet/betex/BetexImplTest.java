package dk.androbet.betex;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import dk.androbet.betex.model.Bet;
import dk.androbet.betex.model.BetStatus;
import dk.androbet.betex.model.BetType;
import dk.androbet.betex.model.RunnerPrice;

public class BetexImplTest {

	private Betex betex = new BetexImpl();

	
	@Test
	public void testGetTotalMatched() {
		betex.placeBet(1, BetType.B, 4, 3);
		betex.placeBet(1, BetType.L, 4, 3);
		
		assertEquals(8,betex.getTotalMatched(),0);
	}
	/** Place some bets and then check if getRunnerPrices returns correct data. */
	@Test
	public void testGetRunnerPrices() {

		betex.placeBet(1, BetType.L, 13, 2.1);
		betex.placeBet(1, BetType.L, 3, 2.2);
		betex.placeBet(1, BetType.L, 5, 2.2);
		
		betex.placeBet(1, BetType.B, 8, 2.4);
		betex.placeBet(1, BetType.B, 25, 2.5);
		
		List<RunnerPrice> runnerPrices = betex.getRunnerPrices();
		
		assertEquals(4, runnerPrices.size());
		
		assertEquals(2.1, runnerPrices.get(0).getPrice(),0);
		assertEquals(13, runnerPrices.get(0).getTotalToBack(),0);
		assertEquals(0, runnerPrices.get(0).getTotalToLay(),0);
		
		assertEquals(2.2, runnerPrices.get(1).getPrice(),0);
		assertEquals(8, runnerPrices.get(1).getTotalToBack(),0);
		assertEquals(0, runnerPrices.get(1).getTotalToLay(),0);
		
		assertEquals(2.4, runnerPrices.get(2).getPrice(),0);
		assertEquals(0, runnerPrices.get(2).getTotalToBack(),0);
		assertEquals(8, runnerPrices.get(2).getTotalToLay(),0);
		
		assertEquals(2.5, runnerPrices.get(3).getPrice(),0);
		assertEquals(0, runnerPrices.get(3).getTotalToBack(),0);
		assertEquals(25, runnerPrices.get(3).getTotalToLay(),0);
	}

	@Test
	public void testGetRunnerPricesLayAndBackBetsonTheSamePrice() {
		betex.placeBet(1, BetType.B, 5, 2.4);
		betex.placeBet(1, BetType.L, 8, 2.4);
		
		List<RunnerPrice> runnerPrices = betex.getRunnerPrices();
		
		assertEquals(1, runnerPrices.size());
		assertEquals(2.4, runnerPrices.get(0).getPrice(),0);
		assertEquals(3, runnerPrices.get(0).getTotalToBack(),0);
		assertEquals(0, runnerPrices.get(0).getTotalToLay(),0);
	}
	
	/**Place some bets then check user bets.*/
	@Test
	public void testGetMUBets() {
		betex.placeBet(2, BetType.L, 13, 2.1);
		betex.placeBet(1, BetType.L, 3, 2.2);
		betex.placeBet(2, BetType.L, 5, 2.2);
		
		betex.placeBet(1, BetType.B, 8, 2.4);
		betex.placeBet(2, BetType.B, 25, 2.5);
		
		List<Bet> bets = betex.getMUBets(1);
		
		assertEquals(2, bets.size());
		
		assertEquals(1,bets.get(0).getUserId());
		assertEquals(BetType.B,bets.get(0).getBetType());
		assertEquals(8,bets.get(0).getSize(),0);
		assertEquals(2.4,bets.get(0).getPrice(),0);
		
		assertEquals(1,bets.get(1).getUserId());
		assertEquals(BetType.L,bets.get(1).getBetType());
		assertEquals(3,bets.get(1).getSize(),0);
		assertEquals(2.2,bets.get(1).getPrice(),0);
	}

	@Test
	public void testPlaceBackBet() {
		long betId = betex.placeBet(1, BetType.B, 3, 2.4);

		List<Bet> bets = betex.getMUBets(1);

		assertEquals(1, bets.size());
		assertEquals(betId, bets.get(0).getBetId());
		assertEquals(BetType.B, bets.get(0).getBetType());
		assertEquals(3, bets.get(0).getSize(), 0);
		assertEquals(2.4, bets.get(0).getPrice(), 0);
	}

	@Test
	public void testPlaceLayBet() {
		long betId = betex.placeBet(1, BetType.L, 3, 2.4);

		List<Bet> bets = betex.getMUBets(1);

		assertEquals(1, bets.size());
		assertEquals(betId, bets.get(0).getBetId());
		assertEquals(BetType.L, bets.get(0).getBetType());
		assertEquals(3, bets.get(0).getSize(), 0);
		assertEquals(2.4, bets.get(0).getPrice(), 0);
	}
	
	@Test 
	public void testMatchingPlaceBackThenLay() {
		betex.placeBet(1, BetType.B, 2, 5);
		betex.placeBet(1, BetType.B, 2, 4);
		
		betex.placeBet(1, BetType.L,2, 7);
		
		List<Bet> bets = betex.getMUBets(1);
		
		assertEquals(3, bets.size());
		
		assertEquals(BetStatus.U, bets.get(0).getBetStatus());
		assertEquals(BetType.B, bets.get(0).getBetType());
		assertEquals(2, bets.get(0).getSize(),0);
		assertEquals(5, bets.get(0).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(1).getBetStatus());
		assertEquals(BetType.L, bets.get(1).getBetType());
		assertEquals(2, bets.get(1).getSize(),0);
		assertEquals(4, bets.get(1).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(2).getBetStatus());
		assertEquals(BetType.B, bets.get(2).getBetType());
		assertEquals(2, bets.get(2).getSize(),0);
		assertEquals(4, bets.get(2).getPrice(),0);	
	}

	@Test 
	public void testMatchingPlaceLayThenBack() {
	
		betex.placeBet(1, BetType.L,2, 6);
		betex.placeBet(1, BetType.L,2, 7);
		
		betex.placeBet(1, BetType.B, 2, 5);		
		
		List<Bet> bets = betex.getMUBets(1);
		
		assertEquals(3, bets.size());
		
		assertEquals(BetStatus.U, bets.get(0).getBetStatus());
		assertEquals(BetType.L, bets.get(0).getBetType());
		assertEquals(2, bets.get(0).getSize(),0);
		assertEquals(6, bets.get(0).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(1).getBetStatus());
		assertEquals(BetType.B, bets.get(1).getBetType());
		assertEquals(2, bets.get(1).getSize(),0);
		assertEquals(7, bets.get(1).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(2).getBetStatus());
		assertEquals(BetType.L, bets.get(2).getBetType());
		assertEquals(2, bets.get(2).getSize(),0);
		assertEquals(7, bets.get(2).getPrice(),0);	
	}
	
	@Test 
	public void testMatchingMatchLayBetWithTwoBackBets() {
		betex.placeBet(1, BetType.B, 2, 5);
		betex.placeBet(1, BetType.B, 2, 4);
		
		betex.placeBet(1, BetType.L,4, 7);
		
		List<Bet> bets = betex.getMUBets(1);
		
		assertEquals(4, bets.size());
		
		assertEquals(BetStatus.M, bets.get(0).getBetStatus());
		assertEquals(BetType.L, bets.get(0).getBetType());
		assertEquals(2, bets.get(0).getSize(),0);
		assertEquals(4, bets.get(0).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(1).getBetStatus());
		assertEquals(BetType.B, bets.get(1).getBetType());
		assertEquals(2, bets.get(1).getSize(),0);
		assertEquals(4, bets.get(1).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(2).getBetStatus());
		assertEquals(BetType.L, bets.get(2).getBetType());
		assertEquals(2, bets.get(2).getSize(),0);
		assertEquals(5, bets.get(2).getPrice(),0);	
		
		assertEquals(BetStatus.M, bets.get(3).getBetStatus());
		assertEquals(BetType.B, bets.get(3).getBetType());
		assertEquals(2, bets.get(3).getSize(),0);
		assertEquals(5, bets.get(3).getPrice(),0);	
	}
	
	@Test 
	public void testMatchingMatchBackBetWithTwoLayBets() {
		betex.placeBet(1, BetType.L, 2, 5);
		betex.placeBet(1, BetType.L, 2, 4);
		
		betex.placeBet(1, BetType.B,4, 3);
		
		List<Bet> bets = betex.getMUBets(1);
		
		assertEquals(4, bets.size());
		
		assertEquals(BetStatus.M, bets.get(0).getBetStatus());
		assertEquals(BetType.B, bets.get(0).getBetType());
		assertEquals(2, bets.get(0).getSize(),0);
		assertEquals(5, bets.get(0).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(1).getBetStatus());
		assertEquals(BetType.L, bets.get(1).getBetType());
		assertEquals(2, bets.get(1).getSize(),0);
		assertEquals(5, bets.get(1).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(2).getBetStatus());
		assertEquals(BetType.B, bets.get(2).getBetType());
		assertEquals(2, bets.get(2).getSize(),0);
		assertEquals(4, bets.get(2).getPrice(),0);	
		
		assertEquals(BetStatus.M, bets.get(3).getBetStatus());
		assertEquals(BetType.L, bets.get(3).getBetType());
		assertEquals(2, bets.get(3).getSize(),0);
		assertEquals(4, bets.get(3).getPrice(),0);	
	}
	
	@Test 
	public void testMatchingBackBetPartiallyMatchedWithLayBet() {
		betex.placeBet(1, BetType.L, 2, 5);
		betex.placeBet(1, BetType.B,6, 3);
		
		List<Bet> bets = betex.getMUBets(1);
		assertEquals(3, bets.size());
		
		assertEquals(BetStatus.U, bets.get(0).getBetStatus());
		assertEquals(BetType.B, bets.get(0).getBetType());
		assertEquals(4, bets.get(0).getSize(),0);
		assertEquals(3, bets.get(0).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(1).getBetStatus());
		assertEquals(BetType.B, bets.get(1).getBetType());
		assertEquals(2, bets.get(1).getSize(),0);
		assertEquals(5, bets.get(1).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(2).getBetStatus());
		assertEquals(BetType.L, bets.get(2).getBetType());
		assertEquals(2, bets.get(2).getSize(),0);
		assertEquals(5, bets.get(2).getPrice(),0);
		
	}	
	
	@Test 
	public void testMatchingBackBetPartiallyMatchedWithLayBetVersion2() {
		betex.placeBet(1, BetType.L, 6, 5);
		betex.placeBet(1, BetType.B,2, 3);
		
		List<Bet> bets = betex.getMUBets(1);
		assertEquals(3, bets.size());
		
		assertEquals(BetStatus.U, bets.get(0).getBetStatus());
		assertEquals(BetType.L, bets.get(0).getBetType());
		assertEquals(4, bets.get(0).getSize(),0);
		assertEquals(5, bets.get(0).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(1).getBetStatus());
		assertEquals(BetType.B, bets.get(1).getBetType());
		assertEquals(2, bets.get(1).getSize(),0);
		assertEquals(5, bets.get(1).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(2).getBetStatus());
		assertEquals(BetType.L, bets.get(2).getBetType());
		assertEquals(2, bets.get(2).getSize(),0);
		assertEquals(5, bets.get(2).getPrice(),0);
		
	}	
	
	@Test 
	public void testMatchingLayBetPartiallyMatchedWithBackBet() {
		betex.placeBet(1, BetType.B,2, 8);
		betex.placeBet(1, BetType.L, 6, 11);
				
		List<Bet> bets = betex.getMUBets(1);
		assertEquals(3, bets.size());
		
		assertEquals(BetStatus.U, bets.get(0).getBetStatus());
		assertEquals(BetType.L, bets.get(0).getBetType());
		assertEquals(4, bets.get(0).getSize(),0);
		assertEquals(11, bets.get(0).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(1).getBetStatus());
		assertEquals(BetType.L, bets.get(1).getBetType());
		assertEquals(2, bets.get(1).getSize(),0);
		assertEquals(8, bets.get(1).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(2).getBetStatus());
		assertEquals(BetType.B, bets.get(2).getBetType());
		assertEquals(2, bets.get(2).getSize(),0);
		assertEquals(8, bets.get(2).getPrice(),0);
		
	}	
	
	@Test 
	public void testMatchingLayBetPartiallyMatchedWithBackBetVersion2() {
		betex.placeBet(1, BetType.B,6, 8);
		betex.placeBet(1, BetType.L, 2, 11);
				
		List<Bet> bets = betex.getMUBets(1);
		assertEquals(3, bets.size());
		
		assertEquals(BetStatus.U, bets.get(0).getBetStatus());
		assertEquals(BetType.B, bets.get(0).getBetType());
		assertEquals(4, bets.get(0).getSize(),0);
		assertEquals(8, bets.get(0).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(1).getBetStatus());
		assertEquals(BetType.L, bets.get(1).getBetType());
		assertEquals(2, bets.get(1).getSize(),0);
		assertEquals(8, bets.get(1).getPrice(),0);
		
		assertEquals(BetStatus.M, bets.get(2).getBetStatus());
		assertEquals(BetType.B, bets.get(2).getBetType());
		assertEquals(2, bets.get(2).getSize(),0);
		assertEquals(8, bets.get(2).getPrice(),0);
		
	}	
}
