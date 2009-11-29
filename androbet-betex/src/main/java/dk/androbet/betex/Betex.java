package dk.androbet.betex;

import java.util.List;

import dk.androbet.betex.model.Bet;
import dk.androbet.betex.model.BetType;
import dk.androbet.betex.model.RunnerPrice;

/**
 * Simplified Betting Exchange for one runner only.
 * 
 * @author korzekwad
 * 
 */
public interface Betex {

	public List<RunnerPrice> getRunnerPrices();
	
	/** Get all unmatched/matched bets. */
	public List<Bet> getMUBets(int userId);

	/**
	 * @return betId
	 */
	public long placeBet(int userId, BetType betType, double size, double price);
	
	/** Total value of all matched bets. For two bets matched (back and lay) on size 3 the total matched is 6*/
	public double getTotalMatched();
	
}
